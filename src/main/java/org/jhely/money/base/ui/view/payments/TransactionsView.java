package org.jhely.money.base.ui.view.payments;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.PermitAll;
import org.springframework.web.client.RestClientResponseException;
import org.jhely.money.base.domain.BridgeCustomer;
import org.jhely.money.base.service.BridgeApiClientFactory;
import org.jhely.money.base.service.payments.BridgeOnboardingService;
import org.jhely.money.base.ui.view.MainLayout;
import org.jhely.money.sdk.bridge.model.BridgeWalletHistory;
import org.jhely.money.sdk.bridge.model.BridgeWalletHistoryDataInner;

import org.jhely.money.sdk.bridge.model.VirtualAccountEvent;
import org.jhely.money.sdk.bridge.model.VirtualAccountHistory;
import org.springframework.security.core.context.SecurityContextHolder;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@PermitAll
@Route(value = "finance/transactions", layout = MainLayout.class)
@PageTitle("Finance · Transactions")
public class TransactionsView extends VerticalLayout {

    private final BridgeApiClientFactory bridgeFactory;
    private final BridgeOnboardingService bridgeOnboardingService;
    private BridgeCustomer bridgeCustomer;
    private Grid<UnifiedTransaction> grid;

    // Unified transaction record that can represent both VA events and wallet
    // history
    public record UnifiedTransaction(
            String id,
            OffsetDateTime createdAt,
            String type,
            String source, // "Virtual Account" or "Wallet (chain)"
            String currency,
            String amount,
            String network,
            String txHash,
            String details // Additional details for the dialog
    ) {
    }

    public TransactionsView(BridgeApiClientFactory bridgeFactory,
            BridgeOnboardingService bridgeOnboardingService) {
        this.bridgeFactory = bridgeFactory;
        this.bridgeOnboardingService = bridgeOnboardingService;

        setSizeFull();
        setPadding(true);
        setSpacing(false);
        setAlignItems(Alignment.STRETCH);

        // Find the current user's Bridge customer
        String userEmail = SecurityContextHolder.getContext().getAuthentication().getName();
        this.bridgeCustomer = bridgeOnboardingService.findForUser(null, userEmail).orElse(null);

        add(new PaymentsSubnav("/finance/transactions"));
        add(buildTransactionsCard());
    }

    private Div buildTransactionsCard() {
        var card = new Div();
        card.getStyle()
                .set("padding", "16px")
                .set("borderRadius", "16px")
                .set("background", "var(--lumo-base-color)")
                .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)");
        card.setWidthFull();

        grid = new Grid<>(UnifiedTransaction.class, false);
        grid.setWidthFull();
        grid.setHeight("600px");

        var fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault());

        grid.addColumn(e -> e.createdAt() != null ? fmt.format(e.createdAt()) : "")
                .setHeader("Date")
                .setAutoWidth(false)
                .setFlexGrow(1);

        grid.addColumn(UnifiedTransaction::type)
                .setHeader("Type")
                .setAutoWidth(false)
                .setFlexGrow(1);

        grid.addColumn(UnifiedTransaction::source)
                .setHeader("Source")
                .setAutoWidth(false)
                .setFlexGrow(1);

        grid.addColumn(e -> e.currency() != null ? e.currency().toUpperCase() : "")
                .setHeader("Currency")
                .setAutoWidth(false)
                .setFlexGrow(1);

        grid.addColumn(e -> formatAmount(e.amount()))
                .setHeader("Amount")
                .setAutoWidth(false)
                .setFlexGrow(1);

        grid.addColumn(e -> e.network() != null ? e.network().toUpperCase() : "")
                .setHeader("Network")
                .setAutoWidth(false)
                .setFlexGrow(1);

        grid.addColumn(e -> truncateHash(e.txHash()))
                .setHeader("Tx Hash")
                .setAutoWidth(false)
                .setFlexGrow(2);

        grid.addItemClickListener(ev -> openDetails(ev.getItem()));

        // Load transactions
        loadTransactions();

        card.add(new H3("All Transactions"), grid);
        return card;
    }

    private void loadTransactions() {
        List<UnifiedTransaction> transactions = new ArrayList<>();

        if (bridgeCustomer == null) {
            grid.setItems(transactions);
            return;
        }

        String customerId = bridgeCustomer.getBridgeCustomerId();

        // 1. Load Virtual Account events (bank deposits -> crypto)
        loadVirtualAccountEvents(transactions, customerId);

        // 2. Load Bridge Wallet history (direct crypto deposits via history API - often
        // empty)
        loadWalletHistory(transactions, customerId);

        // 3. Load Transfers (main source for crypto deposits)
        loadTransfers(transactions, customerId);

        // Sort all transactions by date (newest first)
        transactions.sort(Comparator.comparing(
                UnifiedTransaction::createdAt,
                Comparator.nullsLast(Comparator.reverseOrder())));

        grid.setItems(transactions);
    }

    private void loadVirtualAccountEvents(List<UnifiedTransaction> transactions, String customerId) {
        try {
            // Fetch virtual account history
            // Parameters: depositId, depositIds, txHash, limit, startingAfter,
            // endingBefore, eventType, updatedAfterMs, updatedBeforeMs
            VirtualAccountHistory history = bridgeFactory.virtualAccounts()
                    .virtualAccountsHistoryGet(null, null, null, 100, null, null, null, null, null);

            if (history != null && history.getData() != null) {
                for (VirtualAccountEvent event : history.getData()) {
                    if (customerId.equals(event.getCustomerId())) {
                        transactions.add(new UnifiedTransaction(
                                event.getId(),
                                event.getCreatedAt(),
                                formatVaEventType(event.getType()),
                                "Virtual Account",
                                event.getCurrency() != null ? event.getCurrency().getValue() : null,
                                formatAmount(event.getAmount()),
                                event.getDestinationPaymentRail(),
                                event.getDestinationTxHash(),
                                buildVaEventDetails(event)));
                    }
                }
            }
        } catch (RestClientResponseException e) {
            System.err.println("Failed to load virtual account events: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Failed to load virtual account events: " + e.getMessage());
        }
    }

    private void loadWalletHistory(List<UnifiedTransaction> transactions, String customerId) {
        try {
            System.out.println("DEBUG: Loading wallet history for customer: " + customerId);
            // First, get all wallets for this customer
            var walletsList = bridgeFactory.wallets()
                    .customersCustomerIDWalletsGet(customerId, null, null, null);

            if (walletsList != null && walletsList.getData() != null) {
                System.out.println("DEBUG: Found " + walletsList.getData().size() + " wallets");
                for (org.jhely.money.sdk.bridge.model.BridgeWallet wallet : walletsList.getData()) {
                    System.out.println("DEBUG: Checking wallet: " + wallet.getId() + " ("
                            + (wallet.getChain() != null ? wallet.getChain().getValue() : "unknown") + ")");
                    // For each wallet, get its transaction history
                    try {
                        BridgeWalletHistory history = bridgeFactory.wallets()
                                .walletsBridgeWalletIDHistoryGet(wallet.getId(), 100, null, null);

                        if (history != null && history.getData() != null) {
                            System.out.println("DEBUG: Wallet " + wallet.getId() + " has " + history.getData().size()
                                    + " transactions");
                            String chainName = wallet.getChain() != null
                                    ? wallet.getChain().getValue()
                                    : "unknown";

                            for (BridgeWalletHistoryDataInner item : history.getData()) {
                                System.out.println("DEBUG: Processing transaction: " + item.toString());
                                transactions.add(new UnifiedTransaction(
                                        wallet.getId() + "-"
                                                + (item.getCreatedAt() != null ? item.getCreatedAt().toEpochSecond()
                                                        : 0),
                                        item.getCreatedAt(),
                                        "Crypto Deposit",
                                        "Wallet (" + chainName + ")",
                                        item.getSource() != null && item.getSource().getCurrency() != null
                                                ? item.getSource().getCurrency().getValue()
                                                : null,
                                        formatAmount(item.getAmount()),
                                        chainName,
                                        null, // wallet history doesn't have tx hash in this model
                                        buildWalletHistoryDetails(item, wallet)));
                            }
                        } else {
                            System.out.println("DEBUG: Wallet " + wallet.getId() + " has no history (null)");
                        }
                    } catch (RestClientResponseException e) {
                        System.err.println("Failed to load wallet history for " + wallet.getId() + ": "
                                + e.getResponseBodyAsString());
                    }
                }
            } else {
                System.out.println("DEBUG: No wallets found for customer");
            }
        } catch (RestClientResponseException e) {
            System.err.println("Failed to load wallets: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Failed to load wallets: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void loadTransfers(List<UnifiedTransaction> transactions, String customerId) {
        try {
            System.out.println("DEBUG: Loading transfers for customer: " + customerId);
            // Fetch transfers with a reasonable limit
            org.jhely.money.sdk.bridge.model.Transfers transfersList = bridgeFactory.transfers()
                    .transfersGet(100, null, null, null, null, null, null, null);

            if (transfersList != null && transfersList.getData() != null) {
                System.out.println("DEBUG: Found " + transfersList.getData().size() + " total transfers");
                for (org.jhely.money.sdk.bridge.model.TransferResponse transfer : transfersList.getData()) {
                    // Filter by customer ID (on_behalf_of)
                    if (customerId.equals(transfer.getOnBehalfOf())) {
                        System.out.println("DEBUG: Found matching transfer: " + transfer.getId());

                        String sourceStr = "Transfer";
                        String currencyStr = "Unknown";
                        String networkStr = "Unknown";

                        if (transfer.getSource() != null) {
                            if (transfer.getSource().getCurrency() != null) {
                                currencyStr = transfer.getSource().getCurrency().getValue();
                            }
                            if (transfer.getSource().getPaymentRail() != null) {
                                networkStr = transfer.getSource().getPaymentRail().getValue();
                            }
                        }

                        // Try to determine if it's a deposit
                        // For now we treat all matching transfers as transactions to show

                        // Extract hash from receipt if available
                        String txHash = null;
                        if (transfer.getReceipt() != null) {
                            if (transfer.getReceipt().getDestinationTxHash() != null) {
                                txHash = transfer.getReceipt().getDestinationTxHash().toString();
                            } else if (transfer.getReceipt().getSourceTxHash() != null) {
                                txHash = transfer.getReceipt().getSourceTxHash().toString();
                            }
                        }

                        transactions.add(new UnifiedTransaction(
                                transfer.getId(),
                                transfer.getCreatedAt(),
                                "Crypto Transfer", // Label as transfer
                                "Wallet (" + networkStr + ")",
                                currencyStr,
                                formatAmount(transfer.getAmount()),
                                networkStr,
                                txHash,
                                buildTransferDetails(transfer)));
                    }
                }
            } else {
                System.out.println("DEBUG: No transfers found");
            }
        } catch (RestClientResponseException e) {
            System.err.println("Failed to load transfers: " + e.getResponseBodyAsString());
        } catch (Exception e) {
            System.err.println("Failed to load transfers: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private String buildTransferDetails(org.jhely.money.sdk.bridge.model.TransferResponse transfer) {
        StringBuilder sb = new StringBuilder();
        sb.append("ID: ").append(transfer.getId()).append("\n");
        sb.append("State: ").append(transfer.getState()).append("\n");
        if (transfer.getDeveloperFee() != null) {
            sb.append("Developer Fee: ").append(transfer.getDeveloperFee()).append("\n");
        }
        if (transfer.getSource() != null) {
            if (transfer.getSource().getPaymentRail() != null) {
                sb.append("Source Rail: ").append(transfer.getSource().getPaymentRail().getValue()).append("\n");
            }
            if (transfer.getSource().getCurrency() != null) {
                sb.append("Source Currency: ").append(transfer.getSource().getCurrency().getValue()).append("\n");
            }
        }
        if (transfer.getDestination() != null) {
            if (transfer.getDestination().getPaymentRail() != null) {
                sb.append("Dest Rail: ").append(transfer.getDestination().getPaymentRail().getValue()).append("\n");
            }
            if (transfer.getDestination().getCurrency() != null) {
                sb.append("Dest Currency: ").append(transfer.getDestination().getCurrency().getValue()).append("\n");
            }
        }
        if (transfer.getReceipt() != null) {
            if (transfer.getReceipt().getInitialAmount() != null) {
                sb.append("Initial Amount: ").append(transfer.getReceipt().getInitialAmount()).append("\n");
            }
            if (transfer.getReceipt().getSubtotalAmount() != null) {
                sb.append("Subtotal: ").append(transfer.getReceipt().getSubtotalAmount()).append("\n");
            }
            if (transfer.getReceipt().getGasFee() != null) {
                sb.append("Gas Fee: ").append(transfer.getReceipt().getGasFee()).append("\n");
            }
        }
        return sb.toString();

    }

    private String formatVaEventType(VirtualAccountEvent.TypeEnum type) {
        if (type == null)
            return "";
        return switch (type) {
            case FUNDS_SCHEDULED -> "Scheduled";
            case FUNDS_RECEIVED -> "Received";
            case PAYMENT_SUBMITTED -> "Submitted";
            case PAYMENT_PROCESSED -> "Processed";
            case IN_REVIEW -> "In Review";
            case REFUND -> "Refund";
            case MICRODEPOSIT -> "Microdeposit";
            case ACCOUNT_UPDATE -> "Account Update";
            case DEACTIVATION -> "Deactivation";
            case ACTIVATION -> "Activation";
        };
    }

    private String buildVaEventDetails(VirtualAccountEvent event) {
        StringBuilder sb = new StringBuilder();
        if (event.getSubtotalAmount() != null)
            sb.append("Subtotal: ").append(event.getSubtotalAmount()).append("\n");
        if (event.getExchangeFeeAmount() != null)
            sb.append("Exchange Fee: ").append(event.getExchangeFeeAmount()).append("\n");
        if (event.getDeveloperFeeAmount() != null)
            sb.append("Developer Fee: ").append(event.getDeveloperFeeAmount()).append("\n");
        if (event.getGasFee() != null)
            sb.append("Gas Fee: ").append(event.getGasFee()).append("\n");
        if (event.getDepositId() != null)
            sb.append("Deposit ID: ").append(event.getDepositId()).append("\n");
        if (event.getVirtualAccountId() != null)
            sb.append("Virtual Account ID: ").append(event.getVirtualAccountId()).append("\n");
        return sb.toString();
    }

    private String buildWalletHistoryDetails(BridgeWalletHistoryDataInner item,
            org.jhely.money.sdk.bridge.model.BridgeWallet wallet) {
        StringBuilder sb = new StringBuilder();
        sb.append("Wallet Address: ").append(wallet.getAddress()).append("\n");
        sb.append("Chain: ").append(wallet.getChain() != null ? wallet.getChain().getValue() : "").append("\n");
        if (item.getDeveloperFee() != null)
            sb.append("Developer Fee: ").append(item.getDeveloperFee()).append("\n");
        if (item.getSource() != null) {
            if (item.getSource().getPaymentRail() != null) {
                sb.append("Source Rail: ").append(item.getSource().getPaymentRail().getValue()).append("\n");
            }
            if (item.getSource().getCurrency() != null) {
                sb.append("Source Currency: ").append(item.getSource().getCurrency().getValue()).append("\n");
            }
        }
        if (item.getDestination() != null) {
            if (item.getDestination().getPaymentRail() != null) {
                sb.append("Dest Rail: ").append(item.getDestination().getPaymentRail().getValue()).append("\n");
            }
            if (item.getDestination().getCurrency() != null) {
                sb.append("Dest Currency: ").append(item.getDestination().getCurrency().getValue()).append("\n");
            }
        }
        return sb.toString();
    }

    private String truncateHash(String hash) {
        if (hash == null || hash.length() < 12)
            return hash != null ? hash : "";
        return hash.substring(0, 6) + "..." + hash.substring(hash.length() - 4);
    }

    /**
     * Format amount string to always show 2 decimal places.
     */
    private String formatAmount(String amount) {
        if (amount == null || amount.isBlank()) {
            return "0.00";
        }
        try {
            BigDecimal value = new BigDecimal(amount);
            return value.setScale(2, RoundingMode.HALF_UP).toPlainString();
        } catch (NumberFormatException e) {
            return amount;
        }
    }

    private void openDetails(UnifiedTransaction tx) {
        Dialog d = new Dialog();
        d.setHeaderTitle("Transaction Details");
        d.setWidth("500px");

        var content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);

        addDetailRow(content, "ID", tx.id());
        addDetailRow(content, "Type", tx.type());
        addDetailRow(content, "Source", tx.source());
        addDetailRow(content, "Amount", tx.amount());
        addDetailRow(content, "Currency", tx.currency() != null ? tx.currency().toUpperCase() : "");
        addDetailRow(content, "Network", tx.network());
        addDetailRow(content, "Tx Hash", tx.txHash());
        addDetailRow(content, "Created At", tx.createdAt() != null ? tx.createdAt().toString() : "");

        // Add extra details if available
        if (tx.details() != null && !tx.details().isBlank()) {
            for (String line : tx.details().split("\n")) {
                if (line.contains(": ")) {
                    String[] parts = line.split(": ", 2);
                    addDetailRow(content, parts[0], parts[1]);
                }
            }
        }

        d.add(content);
        d.open();
    }

    private void addDetailRow(VerticalLayout container, String label, String value) {
        if (value == null || value.isBlank())
            return;

        var row = new Div();
        row.getStyle()
                .set("display", "flex")
                .set("justifyContent", "space-between")
                .set("padding", "4px 0")
                .set("borderBottom", "1px solid var(--lumo-contrast-10pct)");

        var labelSpan = new Span(label);
        labelSpan.getStyle()
                .set("fontWeight", "500")
                .set("color", "var(--lumo-secondary-text-color)");

        var valueSpan = new Span(value);
        valueSpan.getStyle()
                .set("fontFamily",
                        label.contains("Hash") || label.contains("ID") || label.contains("Address") ? "monospace"
                                : "inherit")
                .set("fontSize", "14px");

        row.add(labelSpan, valueSpan);
        container.add(row);
    }
}
