package org.jhely.money.base.ui.view.payments;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.domain.BridgeCustomer;
import org.jhely.money.base.security.AuthenticatedUser;
import org.jhely.money.base.service.payments.VirtualAccountService;
import org.jhely.money.base.service.payments.VirtualAccountService.VirtualAccountException;
import org.jhely.money.base.service.payments.BridgeOnboardingService;
import org.jhely.money.base.service.BridgeWalletService;
import org.jhely.money.base.service.BridgeWalletService.WalletCreationException;
import org.jhely.money.base.ui.view.MainLayout;
import org.jhely.money.sdk.bridge.model.VirtualAccountActivationStatus;
import org.jhely.money.sdk.bridge.model.VirtualAccountEvent;
import org.jhely.money.sdk.bridge.model.VirtualAccountHistory;
import org.jhely.money.sdk.bridge.model.VirtualAccountResponse;
import org.jhely.money.sdk.bridge.model.VirtualAccountSourceDepositInstructions;
import org.jhely.money.sdk.bridge.model.EuroInclusiveFiatCurrency;
import org.jhely.money.sdk.bridge.model.OfframpChain;
import org.jhely.money.sdk.bridge.model.CryptoCurrency;
import org.jhely.money.sdk.bridge.model.BridgeWalletChain;
import org.jhely.money.sdk.bridge.model.BridgeWalletWithBalances;
import org.jhely.money.sdk.bridge.model.BridgeWalletBalance;

import java.math.BigDecimal;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * View for receiving funds via Bridge Virtual Accounts.
 * 
 * Virtual Accounts allow receiving fiat (USD via ACH/Wire, EUR via SEPA) and
 * automatically converting to stablecoins (USDC/EURC) delivered to a crypto
 * address.
 */
@RolesAllowed("USER")
@Route(value = "finance/receive", layout = MainLayout.class)
@PageTitle("Finance · Receive")
public class ReceiveFundsView extends VerticalLayout {

    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
            .withZone(ZoneId.systemDefault());

    private final VirtualAccountService virtualAccountService;
    private final AuthenticatedUser auth;
    private final BridgeOnboardingService onboarding;
    private final BridgeWalletService bridgeWalletService;

    private BridgeCustomer bridgeCustomer;
    private Div virtualAccountsContainer;
    private Div cryptoWalletsContainer;

    public ReceiveFundsView(VirtualAccountService virtualAccountService,
            AuthenticatedUser auth,
            BridgeOnboardingService onboarding,
            BridgeWalletService bridgeWalletService) {
        this.virtualAccountService = virtualAccountService;
        this.auth = auth;
        this.onboarding = onboarding;
        this.bridgeWalletService = bridgeWalletService;

        setSizeFull();
        setPadding(true);
        setSpacing(false);

        add(new PaymentsSubnav("/finance/receive"));

        // Load Bridge customer
        loadBridgeCustomer();

        add(buildPage());
    }

    private void loadBridgeCustomer() {
        var user = auth.get().orElse(null);
        if (user != null) {
            // Use same lookup as AccountsOverviewView - by userId AND email
            bridgeCustomer = onboarding.findForUser(
                    String.valueOf(user.getId()), user.getEmail()).orElse(null);
        }
    }

    private Component buildPage() {
        var wrapper = new VerticalLayout();
        wrapper.setPadding(false);
        wrapper.setSpacing(true);
        wrapper.setWidthFull();

        // Header
        wrapper.add(buildHeader());

        // Check prerequisites
        if (bridgeCustomer == null) {
            wrapper.add(buildKycRequiredCard());
            return wrapper;
        }

        if (!virtualAccountService.isKycVerified(bridgeCustomer)) {
            wrapper.add(buildKycPendingCard());
            return wrapper;
        }

        // Crypto Wallets section - for receiving crypto directly
        wrapper.add(buildCryptoWalletsSection());

        // Virtual Accounts section - for receiving fiat
        wrapper.add(buildVirtualAccountsSection());

        return wrapper;
    }

    private Component buildHeader() {
        var header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        var left = new VerticalLayout();
        left.setPadding(false);
        left.setSpacing(false);

        var title = new H2("Receive Funds");
        var subtitle = new Paragraph("Receive fiat payments (USD, EUR) and automatically convert to stablecoins");
        subtitle.getStyle().set("color", "var(--lumo-secondary-text-color)");

        left.add(title, subtitle);
        header.add(left);

        return header;
    }

    private Component buildKycRequiredCard() {
        var card = new Div();
        card.getStyle()
                .set("padding", "40px")
                .set("borderRadius", "16px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("textAlign", "center");

        var icon = new Icon(VaadinIcon.USER_CHECK);
        icon.setSize("48px");
        icon.getStyle().set("color", "var(--lumo-primary-color)");

        var message = new H3("Complete KYC Verification");
        var description = new Paragraph(
                "To receive funds via bank transfer, you need to complete identity verification first. " +
                        "This enables us to create virtual bank accounts in your name.");

        var kycLink = new RouterLink("finance", AccountsOverviewView.class);
        var kycBtn = new Button("Start Verification", new Icon(VaadinIcon.ARROW_RIGHT));
        kycBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        kycLink.removeAll();
        kycLink.add(kycBtn);

        card.add(icon, message, description, kycLink);
        return card;
    }

    private Component buildKycPendingCard() {
        var card = new Div();
        card.getStyle()
                .set("padding", "40px")
                .set("borderRadius", "16px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("textAlign", "center");

        var icon = new Icon(VaadinIcon.HOURGLASS);
        icon.setSize("48px");
        icon.getStyle().set("color", "var(--lumo-warning-color)");

        var message = new H3("KYC Verification In Progress");
        var description = new Paragraph(
                "Your identity verification is being processed. " +
                        "Once approved, you'll be able to create virtual bank accounts to receive funds.");

        String status = bridgeCustomer != null ? bridgeCustomer.getKycStatus() : "unknown";
        var statusBadge = new Span("Status: " + status);
        statusBadge.getStyle()
                .set("padding", "8px 16px")
                .set("borderRadius", "8px")
                .set("background", "var(--lumo-warning-color-10pct)")
                .set("color", "var(--lumo-warning-text-color)")
                .set("fontWeight", "bold");

        card.add(icon, message, description, statusBadge);
        return card;
    }

    private Component buildVirtualAccountsSection() {
        var card = new Div();
        card.getStyle()
                .set("padding", "20px")
                .set("borderRadius", "16px")
                .set("background", "var(--lumo-base-color)")
                .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)");
        card.setWidthFull();

        var headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setAlignItems(Alignment.CENTER);
        headerRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

        var titleSection = new VerticalLayout();
        titleSection.setPadding(false);
        titleSection.setSpacing(false);
        titleSection.add(new H3("Virtual Bank Accounts"));
        titleSection.add(new Paragraph("Receive USD (ACH/Wire) or EUR (SEPA) and auto-convert to USDC/EURC"));

        var createBtn = new Button("Create Virtual Account", new Icon(VaadinIcon.PLUS));
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createBtn.addClickListener(e -> openCreateVirtualAccountDialog());

        headerRow.add(titleSection, createBtn);
        card.add(headerRow);

        // Container for virtual accounts list
        virtualAccountsContainer = new Div();
        virtualAccountsContainer.setWidthFull();
        virtualAccountsContainer.getStyle().set("marginTop", "16px");

        refreshVirtualAccounts();

        card.add(virtualAccountsContainer);
        return card;
    }

    private void refreshVirtualAccounts() {
        virtualAccountsContainer.removeAll();

        if (bridgeCustomer == null) {
            virtualAccountsContainer.add(new Paragraph("No Bridge customer found."));
            return;
        }

        try {
            List<VirtualAccountResponse> accounts = virtualAccountService
                    .listForCustomer(bridgeCustomer.getBridgeCustomerId());

            if (accounts.isEmpty()) {
                var emptyState = new Div();
                emptyState.getStyle()
                        .set("padding", "40px")
                        .set("textAlign", "center")
                        .set("background", "var(--lumo-contrast-5pct)")
                        .set("borderRadius", "12px");

                var icon = new Icon(VaadinIcon.WALLET);
                icon.setSize("32px");
                icon.getStyle().set("color", "var(--lumo-secondary-text-color)");

                var message = new Paragraph("No virtual accounts yet. Create one to start receiving funds.");
                message.getStyle().set("color", "var(--lumo-secondary-text-color)");

                emptyState.add(icon, message);
                virtualAccountsContainer.add(emptyState);
                return;
            }

            // Display each virtual account as a card
            for (VirtualAccountResponse account : accounts) {
                virtualAccountsContainer.add(buildVirtualAccountCard(account));
            }

        } catch (VirtualAccountException e) {
            var error = new Paragraph("Failed to load virtual accounts: " + e.getMessage());
            error.getStyle().set("color", "var(--lumo-error-color)");
            virtualAccountsContainer.add(error);
        }
    }

    private Component buildVirtualAccountCard(VirtualAccountResponse account) {
        var card = new Div();
        card.getStyle()
                .set("padding", "16px")
                .set("borderRadius", "12px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("marginBottom", "12px");

        // Header with status
        var headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setAlignItems(Alignment.CENTER);
        headerRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

        VirtualAccountSourceDepositInstructions instructions = account.getSourceDepositInstructions();
        String currency = instructions != null && instructions.getCurrency() != null
                ? instructions.getCurrency().getValue().toUpperCase()
                : "USD";

        var title = new H4(currency + " Virtual Account");
        title.getStyle().set("margin", "0");

        var statusBadge = new Span(account.getStatus() != null ? account.getStatus().getValue() : "unknown");
        boolean isActive = account.getStatus() == VirtualAccountActivationStatus.ACTIVATED;
        statusBadge.getStyle()
                .set("padding", "4px 8px")
                .set("borderRadius", "4px")
                .set("fontSize", "12px")
                .set("fontWeight", "bold")
                .set("background", isActive ? "var(--lumo-success-color-10pct)" : "var(--lumo-contrast-10pct)")
                .set("color", isActive ? "var(--lumo-success-color)" : "var(--lumo-secondary-text-color)");

        headerRow.add(title, statusBadge);
        card.add(headerRow);

        // Warning for deactivated accounts
        if (!isActive) {
            var warning = new Div();
            warning.getStyle()
                    .set("padding", "8px 12px")
                    .set("margin", "12px 0")
                    .set("borderRadius", "8px")
                    .set("background", "var(--lumo-error-color-10pct)")
                    .set("color", "var(--lumo-error-color)")
                    .set("fontSize", "13px");
            warning.add(new Span("⚠️ This account is deactivated and can no longer receive deposits."));
            card.add(warning);
        }

        // Bank details
        if (instructions != null) {
            var detailsForm = new FormLayout();
            detailsForm.setResponsiveSteps(
                    new FormLayout.ResponsiveStep("0", 1),
                    new FormLayout.ResponsiveStep("600px", 2));
            detailsForm.getStyle().set("marginTop", "12px");

            if (instructions.getBankName() != null) {
                detailsForm.add(readOnlyField("Bank Name", instructions.getBankName()));
            }
            if (instructions.getBankBeneficiaryName() != null) {
                detailsForm.add(readOnlyField("Beneficiary Name", instructions.getBankBeneficiaryName()));
            }
            if (instructions.getBankAccountNumber() != null) {
                // Only show copy button for active accounts
                if (isActive) {
                    detailsForm.add(copyableField("Account Number", instructions.getBankAccountNumber()));
                } else {
                    detailsForm.add(readOnlyField("Account Number", instructions.getBankAccountNumber()));
                }
            }
            if (instructions.getBankRoutingNumber() != null) {
                detailsForm.add(readOnlyField("Routing Number", instructions.getBankRoutingNumber()));
            }
            if (instructions.getBankAddress() != null) {
                detailsForm.add(readOnlyField("Bank Address", instructions.getBankAddress()));
            }
            if (instructions.getPaymentRails() != null && !instructions.getPaymentRails().isEmpty()) {
                String rails = instructions.getPaymentRails().stream()
                        .map(r -> r.getValue())
                        .reduce((a, b) -> a + ", " + b)
                        .orElse("");
                detailsForm.add(readOnlyField("Payment Rails", rails.toUpperCase()));
            }

            card.add(detailsForm);
        }

        // Destination info
        if (account.getDestination() != null) {
            var destSection = new Div();
            destSection.getStyle()
                    .set("marginTop", "12px")
                    .set("padding", "12px")
                    .set("borderRadius", "8px")
                    .set("background", "var(--lumo-contrast-5pct)");

            var destTitle = new Span("→ Converts to: ");
            destTitle.getStyle().set("fontWeight", "bold");

            String destCurrency = account.getDestination().getCurrency() != null
                    ? account.getDestination().getCurrency().getValue().toUpperCase()
                    : "USDC";
            String destChain = account.getDestination().getPaymentRail() != null
                    ? account.getDestination().getPaymentRail().getValue()
                    : "unknown";
            String destAddress = account.getDestination().getAddress();

            var destInfo = new Span(destCurrency + " on " + destChain);
            var addressSpan = new Span(" → " + (destAddress != null ? truncate(destAddress, 20) : ""));
            addressSpan.getStyle().set("fontFamily", "monospace").set("fontSize", "12px");

            destSection.add(destTitle, destInfo, addressSpan);
            card.add(destSection);
        }

        // Actions
        var actions = new HorizontalLayout();
        actions.getStyle().set("marginTop", "12px");

        var viewActivityBtn = new Button("View Activity", e -> openActivityDialog(account));
        actions.add(viewActivityBtn);

        // Only show copy button for active accounts
        if (isActive) {
            var copyBtn = new Button("Copy Details", new Icon(VaadinIcon.COPY), e -> copyAccountDetails(account));
            actions.add(copyBtn);
        }

        if (isActive) {
            var deactivateBtn = new Button("Deactivate", e -> {
                // Show confirmation dialog
                Dialog confirmDialog = new Dialog();
                confirmDialog.setHeaderTitle("Deactivate Virtual Account?");

                var message = new Paragraph("Are you sure you want to deactivate this bank account? " +
                        "Once deactivated, it will no longer accept new deposits.");
                message.getStyle().set("color", "var(--lumo-secondary-text-color)");

                var cancelBtn = new Button("Cancel", ev -> confirmDialog.close());
                var confirmBtn = new Button("Deactivate", ev -> {
                    confirmDialog.close();
                    deactivateAccount(account);
                });
                confirmBtn.getStyle().set("background", "var(--lumo-error-color)").set("color", "white");

                confirmDialog.add(message);
                confirmDialog.getFooter().add(cancelBtn, confirmBtn);
                confirmDialog.open();
            });
            deactivateBtn.getStyle().set("color", "var(--lumo-error-color)");
            actions.add(deactivateBtn);
        }

        card.add(actions);
        return card;
    }

    private TextField readOnlyField(String label, String value) {
        var field = new TextField(label);
        field.setValue(value != null ? value : "");
        field.setReadOnly(true);
        field.setWidthFull();
        return field;
    }

    private Component copyableField(String label, String value) {
        var layout = new HorizontalLayout();
        layout.setAlignItems(Alignment.END);
        layout.setWidthFull();

        var field = new TextField(label);
        field.setValue(value != null ? value : "");
        field.setReadOnly(true);
        field.setWidthFull();

        var copyBtn = new Button(new Icon(VaadinIcon.COPY));
        copyBtn.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", value);
            Notification.show("Copied!", 1500, Notification.Position.BOTTOM_START);
        });

        layout.add(field, copyBtn);
        layout.expand(field);
        return layout;
    }

    private void openCreateVirtualAccountDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Create Virtual Account");
        dialog.setWidth("500px");

        var form = new FormLayout();

        // Source currency
        var sourceCurrency = new Select<String>();
        sourceCurrency.setLabel("Receive Currency");
        sourceCurrency.setItems("USD (ACH/Wire)", "EUR (SEPA)");
        sourceCurrency.setValue("USD (ACH/Wire)");
        sourceCurrency.setHelperText("The fiat currency you'll receive via bank transfer");

        // Destination currency
        var destCurrency = new Select<String>();
        destCurrency.setLabel("Convert To");
        destCurrency.setItems("USDC", "EURC");
        destCurrency.setValue("USDC");
        destCurrency.setHelperText("The stablecoin you'll receive");

        // Destination wallet - single dropdown with existing Bridge wallets
        var destWallet = new Select<BridgeWalletWithBalances>();
        destWallet.setLabel("Destination Wallet");
        destWallet.setHelperText("Where to deliver the stablecoins");
        destWallet.setWidthFull();

        // Load existing wallets
        List<BridgeWalletWithBalances> wallets = bridgeWalletService
                .getWalletsWithBalances(bridgeCustomer.getBridgeCustomerId());

        if (wallets.isEmpty()) {
            // No wallets - show message
            var noWalletsMsg = new Paragraph(
                    "No wallets found. Please create a crypto wallet first using the 'Create Wallet' button above.");
            noWalletsMsg.getStyle().set("color", "var(--lumo-error-color)");
            form.add(noWalletsMsg);

            var closeBtn = new Button("Close", e -> dialog.close());
            dialog.add(form);
            dialog.getFooter().add(closeBtn);
            dialog.open();
            return;
        }

        destWallet.setItems(wallets);
        destWallet.setValue(wallets.get(0)); // Default to first wallet

        // Custom renderer to show "CHAIN - address"
        destWallet.setItemLabelGenerator(wallet -> {
            String chain = wallet.getChain() != null
                    ? wallet.getChain().getValue().toUpperCase()
                    : "Unknown";
            String address = wallet.getAddress();
            String truncatedAddr = address != null && address.length() > 20
                    ? address.substring(0, 10) + "..." + address.substring(address.length() - 8)
                    : address;
            return chain + " — " + truncatedAddr;
        });

        form.add(sourceCurrency, destCurrency, destWallet);

        var createBtn = new Button("Create", e -> {
            BridgeWalletWithBalances selectedWallet = destWallet.getValue();
            if (selectedWallet == null) {
                Notification.show("Please select a destination wallet", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                String source = sourceCurrency.getValue().startsWith("USD") ? "usd" : "eur";
                String dest = destCurrency.getValue().toLowerCase();
                String chain = selectedWallet.getChain() != null
                        ? selectedWallet.getChain().getValue()
                        : "base";
                String address = selectedWallet.getAddress();

                System.out.println(">>> Creating VA with wallet: id=" + selectedWallet.getId()
                        + ", chain=" + chain + ", address=" + address);

                VirtualAccountResponse created;
                if ("usd".equals(source)) {
                    created = virtualAccountService.createUsdAccount(
                            bridgeCustomer.getBridgeCustomerId(),
                            address,
                            chain,
                            null // No developer fee
                    );
                } else {
                    created = virtualAccountService.createEurAccount(
                            bridgeCustomer.getBridgeCustomerId(),
                            dest,
                            address,
                            chain,
                            null);
                }

                Notification.show("Virtual account created!", 3000, Notification.Position.BOTTOM_START)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                dialog.close();
                refreshVirtualAccounts();

            } catch (VirtualAccountException ex) {
                Notification.show("Error: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.add(form);
        dialog.getFooter().add(cancelBtn, createBtn);
        dialog.open();
    }

    private void openActivityDialog(VirtualAccountResponse account) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Account Activity");
        dialog.setWidth("800px");
        dialog.setHeight("600px");

        try {
            VirtualAccountHistory history = virtualAccountService.getActivity(
                    bridgeCustomer.getBridgeCustomerId(),
                    account.getId(),
                    50);

            if (history.getData() == null || history.getData().isEmpty()) {
                dialog.add(new Paragraph("No activity yet for this account."));
            } else {
                Grid<VirtualAccountEvent> grid = new Grid<>();
                grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
                grid.setHeight("400px");

                grid.addColumn(e -> e.getCreatedAt() != null
                        ? DATE_FORMATTER.format(e.getCreatedAt().toInstant())
                        : "-")
                        .setHeader("Date");
                grid.addColumn(e -> e.getType() != null ? e.getType().getValue() : "-")
                        .setHeader("Event");
                grid.addColumn(e -> e.getAmount() != null ? e.getAmount() : "-")
                        .setHeader("Amount");
                grid.addColumn(e -> e.getCurrency() != null ? e.getCurrency().getValue().toUpperCase() : "-")
                        .setHeader("Currency");

                grid.setItems(history.getData());
                dialog.add(grid);
            }

        } catch (VirtualAccountException e) {
            dialog.add(new Paragraph("Error loading activity: " + e.getMessage()));
        }

        var closeBtn = new Button("Close", e -> dialog.close());
        dialog.getFooter().add(closeBtn);
        dialog.open();
    }

    private void copyAccountDetails(VirtualAccountResponse account) {
        VirtualAccountSourceDepositInstructions instructions = account.getSourceDepositInstructions();
        if (instructions == null) {
            Notification.show("No bank details available", 2000, Notification.Position.BOTTOM_START);
            return;
        }

        StringBuilder details = new StringBuilder();
        if (instructions.getBankName() != null)
            details.append("Bank: ").append(instructions.getBankName()).append("\n");
        if (instructions.getBankBeneficiaryName() != null)
            details.append("Beneficiary: ").append(instructions.getBankBeneficiaryName()).append("\n");
        if (instructions.getBankAccountNumber() != null)
            details.append("Account: ").append(instructions.getBankAccountNumber()).append("\n");
        if (instructions.getBankRoutingNumber() != null)
            details.append("Routing: ").append(instructions.getBankRoutingNumber()).append("\n");
        if (instructions.getBankAddress() != null)
            details.append("Address: ").append(instructions.getBankAddress()).append("\n");

        UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", details.toString());
        Notification.show("Bank details copied to clipboard", 2000, Notification.Position.BOTTOM_START);
    }

    private void deactivateAccount(VirtualAccountResponse account) {
        try {
            virtualAccountService.deactivate(bridgeCustomer.getBridgeCustomerId(), account.getId());
            Notification.show("Account deactivated", 2000, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
            refreshVirtualAccounts();
        } catch (VirtualAccountException e) {
            Notification.show("Error: " + e.getMessage(), 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private String truncate(String str, int maxLen) {
        if (str == null)
            return "";
        if (str.length() <= maxLen)
            return str;
        return str.substring(0, maxLen - 3) + "...";
    }

    // ==================== CRYPTO WALLETS SECTION ====================

    private Component buildCryptoWalletsSection() {
        var card = new Div();
        card.getStyle()
                .set("padding", "20px")
                .set("borderRadius", "16px")
                .set("background", "var(--lumo-base-color)")
                .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)")
                .set("marginBottom", "16px");
        card.setWidthFull();

        var headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setAlignItems(Alignment.CENTER);
        headerRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

        var titleSection = new VerticalLayout();
        titleSection.setPadding(false);
        titleSection.setSpacing(false);

        var titleRow = new HorizontalLayout();
        titleRow.setAlignItems(Alignment.CENTER);
        var cryptoIcon = new Icon(VaadinIcon.COIN_PILES);
        cryptoIcon.setSize("24px");
        cryptoIcon.getStyle().set("color", "var(--lumo-primary-color)");
        titleRow.add(cryptoIcon, new H3("Receive Crypto"));

        titleSection.add(titleRow);
        titleSection.add(new Paragraph("Deposit stablecoins (USDC, EURC, USDT) directly to your wallet addresses"));

        var createBtn = new Button("Create Wallet", new Icon(VaadinIcon.PLUS));
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        createBtn.addClickListener(e -> openCreateCryptoWalletDialog());

        headerRow.add(titleSection, createBtn);
        card.add(headerRow);

        // Container for crypto wallets list
        cryptoWalletsContainer = new Div();
        cryptoWalletsContainer.setWidthFull();
        cryptoWalletsContainer.getStyle().set("marginTop", "16px");

        refreshCryptoWallets();

        card.add(cryptoWalletsContainer);
        return card;
    }

    private void refreshCryptoWallets() {
        cryptoWalletsContainer.removeAll();

        if (bridgeCustomer == null) {
            cryptoWalletsContainer.add(new Paragraph("No Bridge customer found."));
            return;
        }

        try {
            List<BridgeWalletWithBalances> wallets = bridgeWalletService
                    .getWalletsWithBalances(bridgeCustomer.getBridgeCustomerId());

            if (wallets.isEmpty()) {
                var emptyState = new Div();
                emptyState.getStyle()
                        .set("padding", "40px")
                        .set("textAlign", "center")
                        .set("background", "var(--lumo-contrast-5pct)")
                        .set("borderRadius", "12px");

                var icon = new Icon(VaadinIcon.COIN_PILES);
                icon.setSize("32px");
                icon.getStyle().set("color", "var(--lumo-secondary-text-color)");

                var message = new Paragraph(
                        "No crypto wallets yet. Create one to receive stablecoins directly on Base, Ethereum, or Solana.");
                message.getStyle().set("color", "var(--lumo-secondary-text-color)");

                emptyState.add(icon, message);
                cryptoWalletsContainer.add(emptyState);
                return;
            }

            // Display each wallet as a card
            for (BridgeWalletWithBalances wallet : wallets) {
                cryptoWalletsContainer.add(buildCryptoWalletCard(wallet));
            }

        } catch (Exception e) {
            var error = new Paragraph("Failed to load crypto wallets: " + e.getMessage());
            error.getStyle().set("color", "var(--lumo-error-color)");
            cryptoWalletsContainer.add(error);
        }
    }

    private Component buildCryptoWalletCard(BridgeWalletWithBalances wallet) {
        var card = new Div();
        card.getStyle()
                .set("padding", "16px")
                .set("borderRadius", "12px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("marginBottom", "12px");

        // Header with chain name
        var headerRow = new HorizontalLayout();
        headerRow.setWidthFull();
        headerRow.setAlignItems(Alignment.CENTER);
        headerRow.setJustifyContentMode(JustifyContentMode.BETWEEN);

        String chainName = wallet.getChain() != null
                ? wallet.getChain().getValue().toUpperCase()
                : "Unknown";

        // Chain icon
        Icon chainIcon;
        String chainColor;
        switch (chainName.toLowerCase()) {
            case "base":
                chainIcon = new Icon(VaadinIcon.CIRCLE);
                chainColor = "#0052FF"; // Base blue
                break;
            case "ethereum":
                chainIcon = new Icon(VaadinIcon.DIAMOND_O);
                chainColor = "#627EEA"; // Ethereum purple
                break;
            case "solana":
                chainIcon = new Icon(VaadinIcon.BOLT);
                chainColor = "#00FFA3"; // Solana green
                break;
            default:
                chainIcon = new Icon(VaadinIcon.COIN_PILES);
                chainColor = "var(--lumo-primary-color)";
        }
        chainIcon.setSize("20px");
        chainIcon.getStyle().set("color", chainColor);

        var titleLayout = new HorizontalLayout();
        titleLayout.setAlignItems(Alignment.CENTER);
        titleLayout.setSpacing(true);
        var title = new H4(chainName + " Wallet");
        title.getStyle().set("margin", "0");
        titleLayout.add(chainIcon, title);

        headerRow.add(titleLayout);
        card.add(headerRow);

        // Wallet address with copy button
        var addressSection = new Div();
        addressSection.getStyle()
                .set("marginTop", "12px")
                .set("padding", "12px")
                .set("borderRadius", "8px")
                .set("background", "var(--lumo-base-color)");

        var addressLabel = new Span("Deposit Address:");
        addressLabel.getStyle().set("fontWeight", "bold").set("fontSize", "12px");

        var addressRow = new HorizontalLayout();
        addressRow.setWidthFull();
        addressRow.setAlignItems(Alignment.CENTER);

        String address = wallet.getAddress();
        var addressText = new Span(address != null ? address : "");
        addressText.getStyle()
                .set("fontFamily", "monospace")
                .set("fontSize", "13px")
                .set("wordBreak", "break-all")
                .set("flex", "1");

        var copyBtn = new Button(new Icon(VaadinIcon.COPY));
        copyBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        copyBtn.getElement().setAttribute("title", "Copy address");
        copyBtn.addClickListener(e -> {
            UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", address);
            Notification.show("Address copied!", 1500, Notification.Position.BOTTOM_START)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        addressRow.add(addressText, copyBtn);
        addressSection.add(addressLabel, addressRow);
        card.add(addressSection);

        // Balances
        if (wallet.getBalances() != null && !wallet.getBalances().isEmpty()) {
            var balancesSection = new Div();
            balancesSection.getStyle()
                    .set("marginTop", "12px")
                    .set("display", "flex")
                    .set("gap", "16px")
                    .set("flexWrap", "wrap");

            for (BridgeWalletBalance balance : wallet.getBalances()) {
                // Skip unknown currencies - only show recognized stablecoins
                if (balance.getCurrency() == null) {
                    continue;
                }
                String currency = balance.getCurrency().getValue().toUpperCase();

                // Filter to only known stablecoins (USDC, EURC, USDT, USDB)
                if (!currency.equals("USDC") && !currency.equals("EURC")
                        && !currency.equals("USDT") && !currency.equals("USDB")) {
                    continue;
                }

                String amount = balance.getBalance() != null ? balance.getBalance() : "0";

                // Format balance with 2 decimal places
                try {
                    BigDecimal bal = new BigDecimal(amount);
                    amount = bal.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
                } catch (NumberFormatException ignored) {
                }

                var balanceChip = new Span(amount + " " + currency);
                balanceChip.getStyle()
                        .set("padding", "4px 8px")
                        .set("borderRadius", "4px")
                        .set("background", "var(--lumo-success-color-10pct)")
                        .set("color", "var(--lumo-success-color)")
                        .set("fontWeight", "500")
                        .set("fontSize", "13px");
                balancesSection.add(balanceChip);
            }

            card.add(balancesSection);
        }

        return card;
    }

    private void openCreateCryptoWalletDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Create Crypto Wallet");
        dialog.setWidth("400px");

        var form = new FormLayout();
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        // Chain selection
        var chainSelect = new Select<String>();
        chainSelect.setLabel("Blockchain Network");
        chainSelect.setItems("Base", "Ethereum", "Solana");
        chainSelect.setValue("Base");
        chainSelect.setHelperText("Select the blockchain where you want to receive crypto");

        // Info text about supported currencies
        var infoSection = new Div();
        infoSection.getStyle()
                .set("padding", "12px")
                .set("borderRadius", "8px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("marginTop", "8px");

        var infoTitle = new Span("Supported Currencies:");
        infoTitle.getStyle().set("fontWeight", "bold").set("display", "block").set("marginBottom", "4px");
        var infoText = new Span("USDC, EURC, USDT");
        infoText.getStyle().set("color", "var(--lumo-secondary-text-color)");
        infoSection.add(infoTitle, infoText);

        form.add(chainSelect, infoSection);

        var createBtn = new Button("Create Wallet", e -> {
            String selectedChain = chainSelect.getValue().toLowerCase();
            BridgeWalletChain chain;
            switch (selectedChain) {
                case "base":
                    chain = BridgeWalletChain.BASE;
                    break;
                case "ethereum":
                    chain = BridgeWalletChain.ETHEREUM;
                    break;
                case "solana":
                    chain = BridgeWalletChain.SOLANA;
                    break;
                default:
                    Notification.show("Invalid chain selected", 3000, Notification.Position.MIDDLE)
                            .addThemeVariants(NotificationVariant.LUMO_ERROR);
                    return;
            }

            try {
                bridgeWalletService.createWallet(bridgeCustomer.getBridgeCustomerId(), chain);
                Notification.show("Wallet created on " + selectedChain.toUpperCase() + "!",
                        3000, Notification.Position.BOTTOM_START)
                        .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
                dialog.close();
                refreshCryptoWallets();
            } catch (WalletCreationException ex) {
                Notification.show("Error: " + ex.getMessage(), 5000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
            }
        });
        createBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        var cancelBtn = new Button("Cancel", e -> dialog.close());

        dialog.add(form);
        dialog.getFooter().add(cancelBtn, createBtn);
        dialog.open();
    }
}
