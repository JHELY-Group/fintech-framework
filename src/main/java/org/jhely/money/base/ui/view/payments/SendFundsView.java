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
    private List<BridgeWalletBalance> walletBalances = new ArrayList<>();
    private List<ExternalAccountResponse> savedAccounts = new ArrayList<>();
    private String bridgeWalletId;

    // UI Components
    private Div contentArea;
    private Select<BalanceOption> sourceBalanceSelect;
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
            
            // Use first wallet's ID for transfers (they share the same customer)
            bridgeWalletId = walletsWithBalances.get(0).getId();
            
            // Aggregate balances across ALL wallets (user may have Base + Solana wallets)
            walletBalances = aggregateBalances(walletsWithBalances);
            
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

        // Balance Summary
        card.add(createBalanceSummary());

        // Form
        FormLayout form = new FormLayout();
        form.setResponsiveSteps(
            new FormLayout.ResponsiveStep("0", 1),
            new FormLayout.ResponsiveStep("600px", 2)
        );

        // Source balance selection
        sourceBalanceSelect = new Select<>();
        sourceBalanceSelect.setLabel("Send from");
        List<BalanceOption> balanceOptions = createBalanceOptions();
        sourceBalanceSelect.setItems(balanceOptions);
        sourceBalanceSelect.setItemLabelGenerator(BalanceOption::getLabel);
        if (!balanceOptions.isEmpty()) {
            sourceBalanceSelect.setValue(balanceOptions.get(0));
        }

        // Amount
        amountField = new BigDecimalField("Amount");
        amountField.setPlaceholder("0.00");
        amountField.setClearButtonVisible(true);
        amountField.setHelperText("Amount in source currency");
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

        form.add(sourceBalanceSelect, amountField, destinationType, memoField);
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
        if (sourceBalanceSelect.getValue() == null) {
            showError("Please select a source balance");
            return;
        }
        if (amountField.getValue() == null || amountField.getValue().compareTo(BigDecimal.ZERO) <= 0) {
            showError("Please enter a valid amount");
            return;
        }

        BalanceOption source = sourceBalanceSelect.getValue();
        BigDecimal amount = amountField.getValue();

        // Check sufficient balance
        if (amount.compareTo(source.balance) > 0) {
            showError("Insufficient balance. Available: " + source.balance + " " + source.currency.getValue());
            return;
        }

        String destType = destinationType.getValue();
        try {
            switch (destType) {
                case "Saved Bank Account" -> handleSendToSavedAccount(source, amount);
                case "Crypto Wallet" -> handleSendToCrypto(source, amount);
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

        // Create transfer
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

        showSuccess("Transfer initiated! ID: " + response.getId());
        UI.getCurrent().navigate("/finance/transactions");
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
        TextField euBenef = new TextField("Beneficiary Name");
        euBenef.setWidthFull();
        TextField euIban = new TextField("IBAN");
        euIban.setWidthFull();
        TextField euBic = new TextField("BIC/SWIFT (optional)");
        euBic.setWidthFull();
        euLayout.add(euBenef, euIban, euBic);

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
                        euBenef.getValue(),
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
        if (account.getBankName() != null) {
            sb.append(account.getBankName()).append(" - ");
        }
        if (account.getLast4() != null) {
            sb.append("••••").append(account.getLast4());
        } else if (account.getAccountOwnerName() != null) {
            sb.append(account.getAccountOwnerName());
        }
        
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
