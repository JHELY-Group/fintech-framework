package org.jhely.money.base.ui.view.payments;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.domain.BridgeCustomer;
import org.jhely.money.base.security.AuthenticatedUser;
import org.jhely.money.base.service.BridgeWalletService;
import org.jhely.money.base.service.payments.BridgeOnboardingService;
import org.jhely.money.base.service.payments.ExternalAccountService;
import org.jhely.money.base.service.payments.TransferService;
import org.jhely.money.base.ui.view.MainLayout;
import org.jhely.money.sdk.bridge.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.*;

/**
 * Send Funds View - Redesigned for Bridge API integration.
 * 
 * Supports:
 * - Sending to saved external bank accounts (ACH, Wire, SEPA, SWIFT, SPEI)
 * - Sending to crypto wallet addresses on various chains
 * - Currency conversion (e.g., EURC balance → USD transfer)
 */
@RolesAllowed("USER")
@Route(value = "finance/send", layout = MainLayout.class)
@PageTitle("Finance · Send")
public class SendFundsView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(SendFundsView.class);

    private final AuthenticatedUser authenticatedUser;
    private final BridgeOnboardingService onboardingService;
    private final TransferService transferService;
    private final ExternalAccountService externalAccountService;
    private final BridgeWalletService bridgeWalletService;

    // User state
    private BridgeCustomer bridgeCustomer;
    private List<BridgeWalletWithBalances> allWallets = new ArrayList<>();
    private List<BridgeWalletBalance> walletBalances = new ArrayList<>();
    private List<ExternalAccountResponse> savedAccounts = new ArrayList<>();
    private String bridgeWalletId;

    // UI Components
    private Div contentArea;
    private ComboBox<BridgeWalletWithBalances> walletSelect;
    private Div balanceSummaryContainer;
    private BalanceOption selectedBalance; // Auto-selected best balance from wallet
    private BigDecimalField amountField;
    private RadioButtonGroup<String> destinationType;
    private ComboBox<ExternalAccountResponse> savedAccountSelect;
    private Div savedAccountSection;
    private Div newBankFields;
    private Div cryptoFields;
    private TextField memoField;

    public SendFundsView(
            AuthenticatedUser authenticatedUser,
            BridgeOnboardingService onboardingService,
            TransferService transferService,
            ExternalAccountService externalAccountService,
            BridgeWalletService bridgeWalletService) {
        this.authenticatedUser = authenticatedUser;
        this.onboardingService = onboardingService;
        this.transferService = transferService;
        this.externalAccountService = externalAccountService;
        this.bridgeWalletService = bridgeWalletService;

        setSizeFull();
        setPadding(true);
        setSpacing(false);

        add(new PaymentsSubnav("/finance/send"));
        
        contentArea = new Div();
        contentArea.setWidthFull();
        add(contentArea);
        
        loadUserData();
    }

    private void loadUserData() {
        var userOpt = authenticatedUser.get();
        if (userOpt.isEmpty()) {
            showNotKycCard("Please log in to continue.");
            return;
        }
        var user = userOpt.get();

        // Check for Bridge customer
        var bcOpt = onboardingService.findForUser(String.valueOf(user.getId()), user.getEmail());
        if (bcOpt.isEmpty()) {
            showNotKycCard("You need to complete KYC verification before sending funds.");
            return;
        }
        bridgeCustomer = bcOpt.get();
        
        // Check KYC status
        String status = bridgeCustomer.getKycStatus();
        if (!"approved".equalsIgnoreCase(status) && !"active".equalsIgnoreCase(status)) {
            showNotKycCard("Your KYC verification is pending. Status: " + status);
            return;
        }

        // Load Bridge Wallet and balances using BridgeWalletService
        try {
            List<BridgeWalletWithBalances> walletsWithBalances = 
                    bridgeWalletService.getWalletsWithBalances(bridgeCustomer.getBridgeCustomerId());
            
            if (walletsWithBalances.isEmpty()) {
                showNotKycCard("No Bridge Wallet found. Please contact support.");
                return;
            }
            
            // Store all wallets for selection
            allWallets = walletsWithBalances;
            
            // Log all wallets and their balances for debugging
            for (BridgeWalletWithBalances wallet : walletsWithBalances) {
                log.info("Wallet {} (chain: {}) balances: {}", 
                        wallet.getId(), 
                        wallet.getChain(),
                        wallet.getBalances() != null ? wallet.getBalances().stream()
                            .filter(b -> b.getCurrency() != null)
                            .map(b -> b.getCurrency().getValue() + "=" + b.getBalance())
                            .toList() : "none");
            }
            
            // Default to first wallet - filter out balances with null currency
            BridgeWalletWithBalances selectedWallet = walletsWithBalances.get(0);
            bridgeWalletId = selectedWallet.getId();
            walletBalances = filterValidBalances(selectedWallet.getBalances());
            log.info("Default selected wallet: {} (chain: {})", bridgeWalletId, selectedWallet.getChain());
            
            // Load saved external accounts
            savedAccounts = externalAccountService.listAccounts(bridgeCustomer.getBridgeCustomerId());
            
            // Show the send form
            showSendForm();
        } catch (Exception e) {
            log.error("Failed to load wallet data", e);
            showErrorCard("Failed to load wallet data: " + e.getMessage());
        }
    }

    /**
     * Aggregate balances across all wallets, summing by currency.
     * Only includes currencies supported by the Currency enum: USDC, USDT, USDB.
     * Note: EURC is not in Currency enum, so it's excluded.
     */
    private List<BridgeWalletBalance> aggregateBalances(List<BridgeWalletWithBalances> wallets) {
        Map<String, BigDecimal> totals = new java.util.LinkedHashMap<>();
        // Initialize supported currencies in display order (only those in Currency enum)
        totals.put("usdc", BigDecimal.ZERO);
        totals.put("usdt", BigDecimal.ZERO);
        totals.put("usdb", BigDecimal.ZERO);

        for (BridgeWalletWithBalances wallet : wallets) {
            if (wallet.getBalances() == null) continue;
            for (BridgeWalletBalance balance : wallet.getBalances()) {
                if (balance.getCurrency() == null) continue;
                String currency = balance.getCurrency().getValue().toLowerCase();
                // Only aggregate supported stablecoins
                if (totals.containsKey(currency)) {
                    BigDecimal amount = BigDecimal.ZERO;
                    try {
                        if (balance.getBalance() != null) {
                            amount = new BigDecimal(balance.getBalance());
                        }
                    } catch (NumberFormatException ignored) {}
                    totals.merge(currency, amount, BigDecimal::add);
                }
            }
        }

        // Convert to BridgeWalletBalance list for UI compatibility
        List<BridgeWalletBalance> result = new ArrayList<>();
        for (Map.Entry<String, BigDecimal> entry : totals.entrySet()) {
            BridgeWalletBalance bal = new BridgeWalletBalance();
            // Currency.fromValue() expects lowercase (e.g. "usdc")
            bal.setCurrency(org.jhely.money.sdk.bridge.model.Currency.fromValue(entry.getKey()));
            // Note: BridgeWalletBalance.balance is read-only, so we create synthetic entries
            // We'll use a wrapper or store the balance separately
            result.add(createSyntheticBalance(entry.getKey(), entry.getValue()));
        }
        return result;
    }

    /**
     * Create a synthetic balance entry for display purposes.
     */
    private BridgeWalletBalance createSyntheticBalance(String currency, BigDecimal amount) {
        // BridgeWalletBalance has readonly balance field, so we need to work around it
        // by using reflection or returning balance info differently
        // For now, we'll store the aggregated amounts in the BalanceOption directly
        BridgeWalletBalance bal = new BridgeWalletBalance(
            amount.toPlainString(),
            null // contractAddress
        );
        // Currency.fromValue() expects lowercase (e.g. "usdc")
        bal.setCurrency(org.jhely.money.sdk.bridge.model.Currency.fromValue(currency));
        return bal;
    }

    /**
     * Format wallet label for dropdown display.
     * Shows chain name and total balance.
     */
    private String formatWalletLabel(BridgeWalletWithBalances wallet) {
        String chain = wallet.getChain() != null ? wallet.getChain().getValue() : "Unknown";
        // Capitalize first letter
        chain = chain.substring(0, 1).toUpperCase() + chain.substring(1).toLowerCase();
        
        // Calculate total balance across all currencies
        BigDecimal total = BigDecimal.ZERO;
        if (wallet.getBalances() != null) {
            for (BridgeWalletBalance bal : wallet.getBalances()) {
                if (bal.getBalance() != null) {
                    try {
                        total = total.add(new BigDecimal(bal.getBalance()));
                    } catch (NumberFormatException ignored) {}
                }
            }
        }
        
        String totalFormatted = total.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
        return chain + " Wallet ($" + totalFormatted + " total)";
    }

    /**
     * Handle wallet selection change - update balances and UI.
     */
    private void onWalletSelected(BridgeWalletWithBalances wallet) {
        if (wallet == null) return;
        
        bridgeWalletId = wallet.getId();
        walletBalances = filterValidBalances(wallet.getBalances());
        
        log.info("Switched to wallet: {} (chain: {})", bridgeWalletId, wallet.getChain());
        
        // Update balance summary display
        if (balanceSummaryContainer != null) {
            balanceSummaryContainer.removeAll();
            balanceSummaryContainer.add(createBalanceSummary());
        }
        
        // Update selected balance and amount field helper
        updateSelectedBalance();
        updateAmountFieldHelper();
    }

    /**
     * Auto-select the best balance from the wallet (highest balance).
     */
    private void updateSelectedBalance() {
        List<BalanceOption> options = createBalanceOptions();
        if (!options.isEmpty()) {
            // Sort by balance descending and pick the highest
            selectedBalance = options.stream()
                    .max((a, b) -> a.balance.compareTo(b.balance))
                    .orElse(options.get(0));
        } else {
            selectedBalance = null;
        }
    }

    /**
     * Update the amount field helper text to show available balance.
     */
    private void updateAmountFieldHelper() {
        if (amountField != null && selectedBalance != null) {
            String currency = selectedBalance.currency.getValue().toUpperCase();
            String available = selectedBalance.balance.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
            amountField.setHelperText("Available: " + available + " " + currency);
        } else if (amountField != null) {
            amountField.setHelperText("No balance available");
        }
    }

    /**
     * Filter out balances with null currency.
     */
    private List<BridgeWalletBalance> filterValidBalances(List<BridgeWalletBalance> balances) {
        if (balances == null) return new ArrayList<>();
        return balances.stream()
                .filter(b -> b.getCurrency() != null)
                .toList();
    }

    private void showNotKycCard(String message) {
        contentArea.removeAll();
        var card = createCard();
        card.add(
            new H3("Send Funds"),
            new Paragraph(message),
            new Button("Go to Overview", e -> UI.getCurrent().navigate("/finance"))
        );
        contentArea.add(card);
    }

    private void showErrorCard(String message) {
        contentArea.removeAll();
        var card = createCard();
        card.add(
            new H3("Error"),
            new Paragraph(message),
            new Button("Retry", e -> loadUserData())
        );
        contentArea.add(card);
    }

    private void showSendForm() {
        contentArea.removeAll();
        var card = createCard();

        // Header
        card.add(new H3("Send Funds"));
        card.add(new Paragraph("Send stablecoins to bank accounts or crypto wallets. Bridge handles currency conversion automatically."));

        // Wallet selector (only show if multiple wallets)
        if (allWallets.size() > 1) {
            walletSelect = new ComboBox<>("Source Wallet");
            walletSelect.setItems(allWallets);
            walletSelect.setItemLabelGenerator(this::formatWalletLabel);
            walletSelect.setWidthFull();
            walletSelect.getStyle().set("max-width", "400px").set("margin-bottom", "16px");
            walletSelect.setValue(allWallets.get(0));
            walletSelect.addValueChangeListener(e -> onWalletSelected(e.getValue()));
            card.add(walletSelect);
        }

        // Balance Summary (wrapped in container for updates)
        balanceSummaryContainer = new Div();
        balanceSummaryContainer.add(createBalanceSummary());
        card.add(balanceSummaryContainer);

        // Auto-select the best balance (highest balance) from the wallet
        updateSelectedBalance();

        // Form
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("600px", 2)
        );

        // Amount field with currency indicator
        amountField = new BigDecimalField("Amount");
        amountField.setPlaceholder("0.00");
        amountField.setClearButtonVisible(true);
        updateAmountFieldHelper();
        amountField.addValueChangeListener(e -> {
            if (e.getValue() != null && e.getValue().signum() < 0) {
                amountField.setValue(e.getValue().abs());
            }
        });

        // Destination type
        destinationType = new RadioButtonGroup<>();
        destinationType.setLabel("Send to");
        destinationType.setItems("Saved Bank Account", "New Bank Account", "Crypto Wallet");
        destinationType.setValue("Saved Bank Account");
        destinationType.addValueChangeListener(e -> updateDestinationFields());

        // Memo
        memoField = new TextField("Reference/Memo (optional)");
        memoField.setPlaceholder("Payment reference");

        form.add(amountField, destinationType, memoField);
        card.add(form);

        // Dynamic destination fields container
        Div destinationContainer = new Div();
        destinationContainer.setWidthFull();
        destinationContainer.getStyle().set("marginTop", "16px");
        
        // Saved account selector
        savedAccountSection = new Div();
        savedAccountSelect = new ComboBox<>("Select Account");
        savedAccountSelect.setWidthFull();
        savedAccountSelect.setItems(savedAccounts);
        savedAccountSelect.setItemLabelGenerator(this::formatAccountLabel);
        savedAccountSelect.setPlaceholder("Choose a saved bank account");
        if (!savedAccounts.isEmpty()) {
            savedAccountSelect.setValue(savedAccounts.get(0));
        }
        
        Button addAccountBtn = new Button("Add New Account", VaadinIcon.PLUS.create());
        addAccountBtn.addClickListener(e -> showAddAccountDialog());
        
        savedAccountSection.add(savedAccountSelect, addAccountBtn);
        savedAccountSection.getStyle().set("display", "flex").set("gap", "12px").set("alignItems", "end");
        destinationContainer.add(savedAccountSection);

        // New bank account fields
        newBankFields = createNewBankFields();
        newBankFields.setVisible(false);
        destinationContainer.add(newBankFields);

        // Crypto wallet fields
        cryptoFields = createCryptoFields();
        cryptoFields.setVisible(false);
        destinationContainer.add(cryptoFields);

        card.add(destinationContainer);

        // Actions
        HorizontalLayout actions = new HorizontalLayout();
        actions.setSpacing(true);
        actions.getStyle().set("marginTop", "24px");

        Button cancelBtn = new Button("Cancel", e -> UI.getCurrent().navigate("/finance"));
        
        Button sendBtn = new Button("Send", VaadinIcon.PAPERPLANE.create());
        sendBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        sendBtn.addClickListener(e -> handleSend());

        actions.add(cancelBtn, sendBtn);
        card.add(actions);

        contentArea.add(card);
    }

    private void updateDestinationFields() {
        String selected = destinationType.getValue();
        
        // Hide all first
        savedAccountSection.setVisible(false);
        newBankFields.setVisible(false);
        cryptoFields.setVisible(false);
        
        // Show relevant section
        switch (selected) {
            case "Saved Bank Account" -> savedAccountSection.setVisible(true);
            case "New Bank Account" -> newBankFields.setVisible(true);
            case "Crypto Wallet" -> cryptoFields.setVisible(true);
        }
    }

    private Div createBalanceSummary() {
        Div container = new Div();
        container.getStyle()
            .set("display", "flex")
            .set("gap", "16px")
            .set("flexWrap", "wrap")
            .set("marginBottom", "20px");

        if (walletBalances.isEmpty()) {
            container.add(new Span("No balances available"));
            return container;
        }

        for (BridgeWalletBalance bal : walletBalances) {
            // Skip entries with null currency
            if (bal.getCurrency() == null) {
                continue;
            }
            
            Div balCard = new Div();
            balCard.getStyle()
                .set("padding", "12px 16px")
                .set("borderRadius", "8px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("minWidth", "120px");
            
            String currency = bal.getCurrency().getValue().toUpperCase();
            String balance = bal.getBalance() != null ? bal.getBalance() : "0.00";
            
            // Format balance with 2 decimal places
            try {
                BigDecimal balVal = new BigDecimal(balance);
                balance = balVal.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
            } catch (NumberFormatException ignored) {}
            
            Div currencyLabel = new Div();
            currencyLabel.setText(currency);
            currencyLabel.getStyle().set("fontWeight", "600").set("fontSize", "var(--lumo-font-size-s)");
            
            Div amountLabel = new Div();
            amountLabel.setText(balance);
            amountLabel.getStyle().set("fontSize", "var(--lumo-font-size-l)");
            
            balCard.add(currencyLabel, amountLabel);
            container.add(balCard);
        }

        return container;
    }

    private List<BalanceOption> createBalanceOptions() {
        List<BalanceOption> options = new ArrayList<>();
        for (BridgeWalletBalance bal : walletBalances) {
            if (bal.getCurrency() != null) {
                BigDecimal balance = bal.getBalance() != null ? new BigDecimal(bal.getBalance()) : BigDecimal.ZERO;
                if (balance.compareTo(BigDecimal.ZERO) > 0) {
                    options.add(new BalanceOption(bal.getCurrency(), balance));
                }
            }
        }
        // If no positive balances, still show all currencies for UI completeness
        if (options.isEmpty()) {
            for (BridgeWalletBalance bal : walletBalances) {
                if (bal.getCurrency() != null) {
                    options.add(new BalanceOption(bal.getCurrency(), BigDecimal.ZERO));
                }
            }
        }
        return options;
    }

    private Div createNewBankFields() {
        Div container = new Div();
        container.getStyle().set("marginTop", "16px");

        // Bank type selector
        Select<String> bankType = new Select<>();
        bankType.setLabel("Account Type");
        bankType.setItems("US (ACH)", "EU (SEPA)", "MX (SPEI)");
        bankType.setValue("US (ACH)");

        // US fields
        FormLayout usFields = new FormLayout();
        usFields.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("500px", 2));
        TextField usBeneficiary = new TextField("Beneficiary Name");
        TextField usRouting = new TextField("Routing Number");
        usRouting.setPattern("[0-9]{9}");
        usRouting.setHelperText("9-digit ABA routing number");
        TextField usAccount = new TextField("Account Number");
        Select<String> usType = new Select<>();
        usType.setLabel("Account Type");
        usType.setItems("checking", "savings");
        usType.setValue("checking");
        usFields.add(usBeneficiary, usRouting, usAccount, usType);

        // EU fields
        FormLayout euFields = new FormLayout();
        euFields.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("500px", 2));
        TextField euBeneficiary = new TextField("Beneficiary Name");
        TextField euIban = new TextField("IBAN");
        TextField euBic = new TextField("BIC/SWIFT (optional)");
        euFields.add(euBeneficiary, euIban, euBic);
        euFields.setVisible(false);

        // MX fields
        FormLayout mxFields = new FormLayout();
        TextField mxBeneficiary = new TextField("Beneficiary Name");
        TextField mxClabe = new TextField("CLABE");
        mxClabe.setPattern("[0-9]{18}");
        mxClabe.setHelperText("18-digit CLABE number");
        mxFields.add(mxBeneficiary, mxClabe);
        mxFields.setVisible(false);

        bankType.addValueChangeListener(e -> {
            usFields.setVisible("US (ACH)".equals(e.getValue()));
            euFields.setVisible("EU (SEPA)".equals(e.getValue()));
            mxFields.setVisible("MX (SPEI)".equals(e.getValue()));
        });

        container.add(bankType, usFields, euFields, mxFields);
        
        return container;
    }

    private Div createCryptoFields() {
        Div container = new Div();
        container.getStyle().set("marginTop", "16px");

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("500px", 2));

        TextField walletAddress = new TextField("Wallet Address");
        walletAddress.setPlaceholder("0x... or Solana address");
        walletAddress.setWidthFull();
        walletAddress.setId("crypto-wallet-address");

        Select<String> chain = new Select<>();
        chain.setLabel("Blockchain Network");
        // Bridge supports sending to all these chains (even though wallet creation is only Base/Ethereum/Solana)
        chain.setItems("base", "ethereum", "solana", "polygon", "arbitrum", "optimism", "avalanche_c_chain", "tron", "stellar");
        chain.setValue("base");
        chain.setHelperText("Select the destination blockchain");
        chain.setId("crypto-chain-select");

        // Memo field for Stellar and Tron
        TextField memoField = new TextField("Memo / Tag");
        memoField.setPlaceholder("Optional memo or destination tag");
        memoField.setWidthFull();
        memoField.setId("crypto-memo");
        memoField.setVisible(false); // Hidden by default
        memoField.setHelperText("Required by some exchanges, optional otherwise");

        // Show/hide memo field based on chain selection
        chain.addValueChangeListener(e -> {
            String selected = e.getValue();
            boolean needsMemo = "stellar".equals(selected) || "tron".equals(selected);
            memoField.setVisible(needsMemo);
            if (needsMemo) {
                memoField.setHelperText("stellar".equals(selected) 
                    ? "Stellar memo - required by most exchanges" 
                    : "Tron memo/tag - may be required by exchanges");
                walletAddress.setPlaceholder("stellar".equals(selected) 
                    ? "G... (Stellar address)" 
                    : "T... (Tron address)");
            } else {
                walletAddress.setPlaceholder("0x... or chain-specific address");
            }
        });

        form.add(walletAddress, chain, memoField);
        container.add(form);

        return container;
    }

    private void handleSend() {
        // Validate common fields
        if (selectedBalance == null) {
            showError("No balance available to send from");
            return;
        }
        if (amountField.getValue() == null || amountField.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            showError("Please enter a valid amount");
            return;
        }

        BigDecimal amount = amountField.getValue();

        // Check sufficient balance
        if (amount.compareTo(selectedBalance.balance) > 0) {
            showError("Insufficient balance. Available: " + selectedBalance.balance + " " + selectedBalance.currency.getValue().toUpperCase());
            return;
        }

        String destType = destinationType.getValue();
        try {
            switch (destType) {
                case "Saved Bank Account" -> handleSendToSavedAccount(selectedBalance, amount);
                case "Crypto Wallet" -> handleSendToCrypto(selectedBalance, amount);
                default -> showError("New bank account creation during transfer not yet implemented. Please save the account first.");
            }
        } catch (Exception e) {
            log.error("Transfer failed", e);
            showError("Transfer failed: " + e.getMessage());
        }
    }

    private void handleSendToSavedAccount(BalanceOption source, BigDecimal amount) {
        ExternalAccountResponse account = savedAccountSelect.getValue();
        if (account == null) {
            showError("Please select a bank account");
            return;
        }

        // Determine the payment rail and destination currency
        SepaSwiftInclusiveOfframpPaymentRail rail = externalAccountService.determinePaymentRail(account);
        EuroInclusiveCurrency destCurrency = determineDestCurrency(rail);
        EuroInclusiveCurrency srcCurrency = EuroInclusiveCurrency.fromValue(source.currency.getValue());
        String memo = memoField.getValue();

        // Show confirmation dialog before executing transfer
        showTransferConfirmationDialog(
            source, amount, account, rail, srcCurrency, destCurrency, memo,
            () -> executeTransferToSavedAccount(account, rail, srcCurrency, destCurrency, amount)
        );
    }

    private void showTransferConfirmationDialog(
            BalanceOption source,
            BigDecimal amount,
            ExternalAccountResponse account,
            SepaSwiftInclusiveOfframpPaymentRail rail,
            EuroInclusiveCurrency srcCurrency,
            EuroInclusiveCurrency destCurrency,
            String memo,
            Runnable onConfirm) {
        
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Confirm Transfer");
        dialog.setWidth("500px");
        dialog.setCloseOnEsc(true);
        dialog.setCloseOnOutsideClick(false);

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);

        // Transfer summary
        content.add(new H4("Transfer Details"));

        // Source
        Div sourceSection = createDetailRow("From", 
            source.currency.getValue().toUpperCase() + " Wallet");
        content.add(sourceSection);

        // Amount
        String formattedAmount = amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
        Div amountSection = createDetailRow("Amount", 
            formattedAmount + " " + srcCurrency.getValue().toUpperCase());
        content.add(amountSection);

        // Conversion info if currencies differ
        if (!srcCurrency.equals(destCurrency)) {
            Div conversionSection = createDetailRow("Converts to", 
                destCurrency.getValue().toUpperCase() + " (rate applied at execution)");
            conversionSection.getStyle().set("color", "var(--lumo-secondary-text-color)");
            content.add(conversionSection);
        }

        content.add(new Hr());
        content.add(new H4("Destination"));

        // Payment method
        String railLabel = formatRailLabel(rail);
        Div railSection = createDetailRow("Method", railLabel);
        content.add(railSection);

        // Bank account details
        String accountLabel = formatAccountLabel(account);
        Div accountSection = createDetailRow("Account", accountLabel);
        content.add(accountSection);

        // IBAN/Account number
        if (account.getIban() != null) {
            String ibanDisplay = formatIbanForDisplay(account);
            Div ibanSection = createDetailRow("IBAN", ibanDisplay);
            content.add(ibanSection);
        } else if (account.getAccountNumber() != null) {
            Div acctSection = createDetailRow("Account #", maskAccountNumber(String.valueOf(account.getAccountNumber())));
            content.add(acctSection);
        }

        // Reference/Memo
        if (memo != null && !memo.isBlank()) {
            Div memoSection = createDetailRow("Reference", memo);
            content.add(memoSection);
        }

        content.add(new Hr());

        // Warning
        Div warning = new Div();
        warning.setText("⚠️ Please verify all details. Transfers cannot be reversed once submitted.");
        warning.getStyle()
            .set("padding", "12px")
            .set("background", "var(--lumo-warning-color-10pct)")
            .set("borderRadius", "8px")
            .set("color", "var(--lumo-warning-text-color)")
            .set("fontSize", "var(--lumo-font-size-s)");
        content.add(warning);

        dialog.add(content);

        // Buttons
        Button cancelBtn = new Button("Cancel", e -> dialog.close());
        
        Button confirmBtn = new Button("Confirm & Send", VaadinIcon.CHECK.create());
        confirmBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        confirmBtn.addClickListener(e -> {
            dialog.close();
            onConfirm.run();
        });

        dialog.getFooter().add(cancelBtn, confirmBtn);
        dialog.open();
    }

    private Div createDetailRow(String label, String value) {
        Div row = new Div();
        row.getStyle()
            .set("display", "flex")
            .set("justifyContent", "space-between")
            .set("alignItems", "center")
            .set("gap", "16px")
            .set("padding", "8px 0")
            .set("borderBottom", "1px solid var(--lumo-contrast-10pct)");
        
        Span labelSpan = new Span(label);
        labelSpan.getStyle()
            .set("color", "var(--lumo-secondary-text-color)")
            .set("minWidth", "100px")
            .set("flexShrink", "0");
        
        Span valueSpan = new Span(value);
        valueSpan.getStyle()
            .set("fontWeight", "500")
            .set("textAlign", "right");
        
        row.add(labelSpan, valueSpan);
        return row;
    }

    private String formatRailLabel(SepaSwiftInclusiveOfframpPaymentRail rail) {
        return switch (rail) {
            case SEPA -> "SEPA Transfer (EU)";
            case SWIFT -> "SWIFT Wire Transfer";
            case ACH -> "ACH Transfer (US)";
            case ACH_SAME_DAY -> "ACH Same Day (US)";
            case ACH_PUSH -> "ACH Push (US)";
            case WIRE -> "Wire Transfer (US)";
            case SPEI -> "SPEI Transfer (Mexico)";
            default -> rail.getValue().toUpperCase();
        };
    }

    private String maskAccountNumber(String accountNumber) {
        if (accountNumber == null || accountNumber.length() <= 4) {
            return accountNumber != null ? accountNumber : "";
        }
        return "****" + accountNumber.substring(accountNumber.length() - 4);
    }

    private String formatIbanForDisplay(ExternalAccountResponse account) {
        // The IBAN object contains last_4, bic, and country
        // We need to format it nicely for display
        if (account.getLast4() != null) {
            String countryName = "";
            Object ibanObj = account.getIban();
            if (ibanObj != null) {
                String ibanStr = String.valueOf(ibanObj);
                // Try to extract country from the object representation
                if (ibanStr.contains("country=")) {
                    int start = ibanStr.indexOf("country=") + 8;
                    int end = ibanStr.indexOf(",", start);
                    if (end == -1) end = ibanStr.indexOf("}", start);
                    if (end > start) {
                        String countryCode = ibanStr.substring(start, end).trim();
                        // Convert alpha-3 back to country name
                        countryName = convertAlpha3ToCountryName(countryCode);
                    }
                }
            }
            if (!countryName.isEmpty()) {
                return countryName + " ••••" + account.getLast4();
            }
            return "••••" + account.getLast4();
        }
        return "IBAN on file";
    }

    private String convertAlpha3ToCountryName(String alpha3) {
        return switch (alpha3.toUpperCase()) {
            case "ESP" -> "Spain";
            case "DEU" -> "Germany";
            case "FRA" -> "France";
            case "ITA" -> "Italy";
            case "NLD" -> "Netherlands";
            case "BEL" -> "Belgium";
            case "PRT" -> "Portugal";
            case "AUT" -> "Austria";
            case "IRL" -> "Ireland";
            case "GBR" -> "United Kingdom";
            case "CHE" -> "Switzerland";
            case "POL" -> "Poland";
            case "GRC" -> "Greece";
            case "LUX" -> "Luxembourg";
            case "FIN" -> "Finland";
            case "SWE" -> "Sweden";
            case "DNK" -> "Denmark";
            case "NOR" -> "Norway";
            default -> alpha3;
        };
    }

    private void executeTransferToSavedAccount(
            ExternalAccountResponse account,
            SepaSwiftInclusiveOfframpPaymentRail rail,
            EuroInclusiveCurrency srcCurrency,
            EuroInclusiveCurrency destCurrency,
            BigDecimal amount) {
        
        try {
            TransfersPost201Response response = transferService.sendWithConversion(
                bridgeCustomer.getBridgeCustomerId(),
                bridgeWalletId,
                account.getId(),
                srcCurrency,
                destCurrency,
                rail,
                amount,
                null // No developer fee for now
            );

            showTransferSuccessDialog(response, amount, srcCurrency, destCurrency, rail, account);
        } catch (Exception e) {
            log.error("Transfer failed", e);
            showError(parseTransferError(e.getMessage()));
        }
    }

    private String parseTransferError(String errorMessage) {
        if (errorMessage == null) {
            return "Transfer failed. Please try again.";
        }
        
        // Parse common Bridge API errors into user-friendly messages
        if (errorMessage.contains("balance of the wallet") || errorMessage.contains("higher than the balance")) {
            return "Insufficient wallet balance. The transfer amount exceeds your available funds. " +
                   "Please check your balance and try a smaller amount.";
        }
        if (errorMessage.contains("invalid_parameters")) {
            // Try to extract specific field error
            if (errorMessage.contains("amount")) {
                return "Invalid transfer amount. Please verify the amount and try again.";
            }
            return "Invalid transfer parameters. Please verify all fields and try again.";
        }
        if (errorMessage.contains("external_account") || errorMessage.contains("destination")) {
            return "Invalid destination account. Please verify the bank account details.";
        }
        if (errorMessage.contains("kyc") || errorMessage.contains("KYC")) {
            return "KYC verification required. Please complete identity verification before transferring.";
        }
        if (errorMessage.contains("limit") || errorMessage.contains("exceeded")) {
            return "Transfer limit exceeded. Please try a smaller amount or contact support.";
        }
        
        // Fallback: clean up the raw error message
        return "Transfer failed: " + errorMessage.replaceAll("\\{.*\\}", "").trim();
    }

    private void showTransferSuccessDialog(
            TransfersPost201Response response,
            BigDecimal amount,
            EuroInclusiveCurrency srcCurrency,
            EuroInclusiveCurrency destCurrency,
            SepaSwiftInclusiveOfframpPaymentRail rail,
            ExternalAccountResponse account) {
        
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Transfer Submitted");
        dialog.setWidth("450px");
        dialog.setCloseOnEsc(true);

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);
        content.setAlignItems(FlexComponent.Alignment.CENTER);

        // Success icon
        Div iconDiv = new Div();
        iconDiv.getStyle()
            .set("width", "64px")
            .set("height", "64px")
            .set("borderRadius", "50%")
            .set("background", "var(--lumo-success-color-10pct)")
            .set("display", "flex")
            .set("alignItems", "center")
            .set("justifyContent", "center")
            .set("fontSize", "32px");
        iconDiv.setText("✓");
        iconDiv.getStyle().set("color", "var(--lumo-success-color)");
        content.add(iconDiv);

        // Amount
        String formattedAmount = amount.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
        H2 amountText = new H2(formattedAmount + " " + srcCurrency.getValue().toUpperCase());
        amountText.getStyle().set("margin", "8px 0");
        content.add(amountText);

        Span statusText = new Span("Transfer submitted successfully");
        statusText.getStyle().set("color", "var(--lumo-secondary-text-color)");
        content.add(statusText);

        content.add(new Hr());

        // Details
        VerticalLayout details = new VerticalLayout();
        details.setPadding(false);
        details.setSpacing(false);
        details.setWidthFull();

        details.add(createDetailRow("Transfer ID", response.getId()));
        details.add(createDetailRow("Status", response.getState() != null ? response.getState().getValue() : "Pending"));
        details.add(createDetailRow("Method", formatRailLabel(rail)));
        details.add(createDetailRow("To", formatAccountLabel(account)));

        content.add(details);

        dialog.add(content);

        Button viewTxBtn = new Button("View Transactions", e -> {
            dialog.close();
            UI.getCurrent().navigate("/finance/transactions");
        });
        viewTxBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button closeBtn = new Button("Close", e -> {
            dialog.close();
            UI.getCurrent().navigate("/finance");
        });

        dialog.getFooter().add(closeBtn, viewTxBtn);
        dialog.open();
    }

    @SuppressWarnings("unchecked")
    private void handleSendToCrypto(BalanceOption source, BigDecimal amount) {
        // Find crypto fields by ID
        TextField walletAddressField = null;
        Select<String> chainSelect = null;
        TextField memoField = null;
        
        for (var child : cryptoFields.getChildren().toList()) {
            if (child instanceof FormLayout fl) {
                for (var formChild : fl.getChildren().toList()) {
                    if (formChild instanceof TextField tf && "crypto-wallet-address".equals(tf.getId().orElse(""))) {
                        walletAddressField = tf;
                    } else if (formChild instanceof TextField tf && "crypto-memo".equals(tf.getId().orElse(""))) {
                        memoField = tf;
                    } else if (formChild instanceof Select<?> sel && "crypto-chain-select".equals(sel.getId().orElse(""))) {
                        chainSelect = (Select<String>) sel;
                    }
                }
            }
        }

        if (walletAddressField == null || chainSelect == null) {
            showError("Internal error: crypto fields not found");
            return;
        }

        String address = walletAddressField.getValue();
        String chain = chainSelect.getValue();
        String memo = memoField != null ? memoField.getValue() : null;

        if (address == null || address.isBlank()) {
            showError("Please enter a wallet address");
            return;
        }

        EuroInclusiveCurrency currency = EuroInclusiveCurrency.fromValue(source.currency.getValue());
        SepaSwiftInclusiveOfframpPaymentRail chainRail = SepaSwiftInclusiveOfframpPaymentRail.fromValue(chain);

        // Show confirmation dialog
        Dialog confirmDialog = new Dialog();
        confirmDialog.setHeaderTitle("Confirm Crypto Transfer");
        confirmDialog.setWidth("450px");

        VerticalLayout content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);

        // Summary info
        Div summaryLabel = new Div();
        summaryLabel.setText("Please review and confirm your transfer:");
        summaryLabel.getStyle().set("font-size", "14px").set("color", "var(--lumo-secondary-text-color)");
        content.add(summaryLabel);
        
        VerticalLayout details = new VerticalLayout();
        details.setPadding(true);
        details.getStyle()
            .set("background", "var(--lumo-contrast-5pct)")
            .set("border-radius", "var(--lumo-border-radius-m)");
        
        details.add(createDetailRow("From", source.currency.getValue().toUpperCase() + " Wallet"));
        details.add(createDetailRow("Amount", String.format("%.2f", amount) + " " + source.currency.getValue().toUpperCase()));
        details.add(createDetailRow("Network", chain));
        
        Div addressLabel = new Div();
        addressLabel.add(new Span("To Address:"));
        addressLabel.getStyle().set("font-weight", "500");
        details.add(addressLabel);
        
        Div addressValue = new Div();
        addressValue.setText(address);
        addressValue.getStyle()
            .set("font-family", "monospace")
            .set("font-size", "12px")
            .set("word-break", "break-all");
        details.add(addressValue);
        
        if (memo != null && !memo.isBlank()) {
            details.add(createDetailRow("Memo", memo));
        }
        
        content.add(details);
        
        // Warning
        Div warningDiv = new Div();
        warningDiv.setText("⚠️ Please verify the wallet address and network carefully. Crypto transfers cannot be reversed.");
        warningDiv.getStyle()
            .set("font-size", "13px")
            .set("color", "var(--lumo-error-text-color)");
        content.add(warningDiv);

        confirmDialog.add(content);

        Button cancelBtn = new Button("Cancel", e -> confirmDialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        Button confirmBtn = new Button("Confirm Transfer", e -> {
            confirmDialog.close();
            executeCryptoTransfer(source, amount, address, currency, chainRail, memo);
        });
        confirmBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY, ButtonVariant.LUMO_ERROR);

        confirmDialog.getFooter().add(cancelBtn, confirmBtn);
        confirmDialog.open();
    }

    private void executeCryptoTransfer(BalanceOption source, BigDecimal amount, String address,
                                       EuroInclusiveCurrency currency, SepaSwiftInclusiveOfframpPaymentRail chainRail,
                                       String memo) {
        try {
            TransfersPost201Response response = transferService.sendToCryptoWallet(
                bridgeCustomer.getBridgeCustomerId(),
                bridgeWalletId,
                address,
                currency,
                chainRail,
                amount,
                null,  // developerFee
                memo   // blockchainMemo for Stellar/Tron
            );

            showSuccess("Crypto transfer initiated! ID: " + response.getId());
            UI.getCurrent().navigate("/finance/transactions");
        } catch (Exception ex) {
            showError("Transfer failed: " + ex.getMessage());
        }
    }

    private EuroInclusiveCurrency determineDestCurrency(SepaSwiftInclusiveOfframpPaymentRail rail) {
        return switch (rail) {
            case SEPA, SWIFT -> EuroInclusiveCurrency.EUR;
            case ACH, ACH_PUSH, ACH_SAME_DAY, WIRE -> EuroInclusiveCurrency.USD;
            case SPEI -> EuroInclusiveCurrency.MXN;
            default -> EuroInclusiveCurrency.USD;
        };
    }

    private void showAddAccountDialog() {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Add Bank Account");
        dialog.setWidth("500px");

        // Type selector
        Select<String> typeSelect = new Select<>();
        typeSelect.setLabel("Account Region");
        typeSelect.setItems("US (ACH/Wire)", "EU (SEPA)", "MX (SPEI)");
        typeSelect.setValue("US (ACH/Wire)");
        typeSelect.setWidthFull();

        // Fields containers
        VerticalLayout usLayout = new VerticalLayout();
        usLayout.setPadding(false);
        TextField usBenef = new TextField("Beneficiary Name");
        usBenef.setWidthFull();
        TextField usRouting = new TextField("Routing Number");
        usRouting.setWidthFull();
        TextField usAcct = new TextField("Account Number");
        usAcct.setWidthFull();
        Select<String> usType = new Select<>();
        usType.setLabel("Account Type");
        usType.setItems("checking", "savings");
        usType.setValue("checking");
        usType.setWidthFull();
        usLayout.add(usBenef, usRouting, usAcct, usType);

        VerticalLayout euLayout = new VerticalLayout();
        euLayout.setPadding(false);
        euLayout.setVisible(false);
        TextField euFirstName = new TextField("First Name");
        euFirstName.setWidthFull();
        TextField euLastName = new TextField("Last Name");
        euLastName.setWidthFull();
        TextField euIban = new TextField("IBAN");
        euIban.setWidthFull();
        TextField euBic = new TextField("BIC/SWIFT (optional)");
        euBic.setWidthFull();
        euLayout.add(euFirstName, euLastName, euIban, euBic);

        VerticalLayout mxLayout = new VerticalLayout();
        mxLayout.setPadding(false);
        mxLayout.setVisible(false);
        TextField mxBenef = new TextField("Beneficiary Name");
        mxBenef.setWidthFull();
        TextField mxClabe = new TextField("CLABE (18 digits)");
        mxClabe.setWidthFull();
        mxLayout.add(mxBenef, mxClabe);

        typeSelect.addValueChangeListener(e -> {
            usLayout.setVisible("US (ACH/Wire)".equals(e.getValue()));
            euLayout.setVisible("EU (SEPA)".equals(e.getValue()));
            mxLayout.setVisible("MX (SPEI)".equals(e.getValue()));
        });

        VerticalLayout content = new VerticalLayout(typeSelect, usLayout, euLayout, mxLayout);
        content.setPadding(false);
        content.setSpacing(true);
        dialog.add(content);

        Button saveBtn = new Button("Save Account", ev -> {
            try {
                ExternalAccountResponse created;
                String type = typeSelect.getValue();
                
                if ("US (ACH/Wire)".equals(type)) {
                    created = externalAccountService.createUsAccount(
                        bridgeCustomer.getBridgeCustomerId(),
                        usBenef.getValue(),
                        usRouting.getValue(),
                        usAcct.getValue(),
                        usType.getValue()
                    );
                } else if ("EU (SEPA)".equals(type)) {
                    created = externalAccountService.createEuAccount(
                        bridgeCustomer.getBridgeCustomerId(),
                        euFirstName.getValue(),
                        euLastName.getValue(),
                        euIban.getValue(),
                        euBic.getValue(),
                        null // Extract from IBAN
                    );
                } else {
                    created = externalAccountService.createMxAccount(
                        bridgeCustomer.getBridgeCustomerId(),
                        mxBenef.getValue(),
                        mxClabe.getValue()
                    );
                }

                savedAccounts.add(created);
                savedAccountSelect.setItems(savedAccounts);
                savedAccountSelect.setValue(created);
                dialog.close();
                showSuccess("Bank account saved!");
            } catch (Exception ex) {
                showError("Failed to save account: " + ex.getMessage());
            }
        });
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);

        Button cancelBtn = new Button("Cancel", ev -> dialog.close());
        
        dialog.getFooter().add(cancelBtn, saveBtn);
        dialog.open();
    }

    private String formatAccountLabel(ExternalAccountResponse account) {
        if (account == null) return "";
        
        StringBuilder sb = new StringBuilder();
        
        // For IBAN accounts, show country and last 4 digits
        if (account.getIban() != null) {
            // Extract country and last4 from IBAN object
            String countryName = "";
            String last4 = account.getLast4(); // Try the direct last4 field first
            String ibanStr = String.valueOf(account.getIban());
            
            // Parse the IBAN object string to extract country and last_4
            if (ibanStr.contains("country=")) {
                int start = ibanStr.indexOf("country=") + 8;
                int end = ibanStr.indexOf(",", start);
                if (end == -1) end = ibanStr.indexOf("}", start);
                if (end > start) {
                    String countryCode = ibanStr.substring(start, end).trim();
                    countryName = convertAlpha3ToCountryName(countryCode);
                }
            }
            
            // If last4 is null, try to extract from IBAN object
            if (last4 == null && ibanStr.contains("last_4=")) {
                int start = ibanStr.indexOf("last_4=") + 7;
                int end = ibanStr.indexOf(",", start);
                if (end == -1) end = ibanStr.indexOf("}", start);
                if (end > start) {
                    last4 = ibanStr.substring(start, end).trim();
                }
            }
            
            if (!countryName.isEmpty()) {
                sb.append(countryName);
            } else {
                sb.append("SEPA");
            }
            if (last4 != null && !last4.isEmpty()) {
                sb.append(" ••••").append(last4);
            }
        } else if (account.getBankName() != null) {
            sb.append(account.getBankName());
            if (account.getLast4() != null) {
                sb.append(" ••••").append(account.getLast4());
            }
        } else if (account.getLast4() != null) {
            sb.append("••••").append(account.getLast4());
        } else if (account.getAccountOwnerName() != null) {
            sb.append(account.getAccountOwnerName());
        }
        
        // Add account type
        sb.append(" (").append(externalAccountService.getAccountType(account)).append(")");
        return sb.toString();
    }

    private void showError(String message) {
        Notification.show(message, 4000, Notification.Position.MIDDLE)
            .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }

    private void showSuccess(String message) {
        Notification.show(message, 3000, Notification.Position.BOTTOM_START)
            .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    private Div createCard() {
        Div card = new Div();
        card.getStyle()
            .set("padding", "24px")
            .set("borderRadius", "16px")
            .set("background", "var(--lumo-base-color)")
            .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)")
            .set("maxWidth", "800px");
        return card;
    }

    /**
     * Helper class to represent a selectable balance.
     */
    private static class BalanceOption {
        final org.jhely.money.sdk.bridge.model.Currency currency;
        final BigDecimal balance;

        BalanceOption(org.jhely.money.sdk.bridge.model.Currency currency, BigDecimal balance) {
            this.currency = currency;
            this.balance = balance;
        }

        String getLabel() {
            String formatted = balance.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString();
            return currency.getValue().toUpperCase() + " - " + formatted + " available";
        }
    }
}
