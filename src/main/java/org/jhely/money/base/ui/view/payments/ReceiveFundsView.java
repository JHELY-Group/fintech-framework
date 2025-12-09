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
import org.jhely.money.base.ui.view.MainLayout;
import org.jhely.money.sdk.bridge.model.VirtualAccountActivationStatus;
import org.jhely.money.sdk.bridge.model.VirtualAccountEvent;
import org.jhely.money.sdk.bridge.model.VirtualAccountHistory;
import org.jhely.money.sdk.bridge.model.VirtualAccountResponse;
import org.jhely.money.sdk.bridge.model.VirtualAccountSourceDepositInstructions;
import org.jhely.money.sdk.bridge.model.EuroInclusiveFiatCurrency;
import org.jhely.money.sdk.bridge.model.OfframpChain;
import org.jhely.money.sdk.bridge.model.CryptoCurrency;

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

    private BridgeCustomer bridgeCustomer;
    private Div virtualAccountsContainer;

    public ReceiveFundsView(VirtualAccountService virtualAccountService,
            AuthenticatedUser auth,
            BridgeOnboardingService onboarding) {
        this.virtualAccountService = virtualAccountService;
        this.auth = auth;
        this.onboarding = onboarding;

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

        // Virtual Accounts section
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
                detailsForm.add(copyableField("Account Number", instructions.getBankAccountNumber()));
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
        var copyBtn = new Button("Copy Details", new Icon(VaadinIcon.COPY), e -> copyAccountDetails(account));

        actions.add(viewActivityBtn, copyBtn);

        if (isActive) {
            var deactivateBtn = new Button("Deactivate", e -> deactivateAccount(account));
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
        sourceCurrency.setHelperText("The fiat currency you'll receive");

        // Destination currency
        var destCurrency = new Select<String>();
        destCurrency.setLabel("Convert To");
        destCurrency.setItems("USDC", "EURC");
        destCurrency.setValue("USDC");
        destCurrency.setHelperText("The stablecoin you'll receive");

        // Destination chain
        var destChain = new Select<String>();
        destChain.setLabel("Blockchain");
        destChain.setItems("Base", "Ethereum", "Polygon", "Solana", "Arbitrum");
        destChain.setValue("Base");
        destChain.setHelperText("Where to deliver the stablecoins");

        // Destination address
        var destAddress = new TextField("Wallet Address");
        destAddress.setPlaceholder("0x... or Solana address");
        destAddress.setHelperText("Your crypto wallet address");
        destAddress.setRequired(true);
        destAddress.setWidthFull();

        form.add(sourceCurrency, destCurrency, destChain, destAddress);

        var createBtn = new Button("Create", e -> {
            if (destAddress.isEmpty()) {
                Notification.show("Please enter a wallet address", 3000, Notification.Position.MIDDLE)
                        .addThemeVariants(NotificationVariant.LUMO_ERROR);
                return;
            }

            try {
                String source = sourceCurrency.getValue().startsWith("USD") ? "usd" : "eur";
                String dest = destCurrency.getValue().toLowerCase();
                String chain = destChain.getValue().toLowerCase();

                VirtualAccountResponse created;
                if ("usd".equals(source)) {
                    created = virtualAccountService.createUsdAccount(
                            bridgeCustomer.getBridgeCustomerId(),
                            destAddress.getValue(),
                            chain,
                            null // No developer fee
                    );
                } else {
                    created = virtualAccountService.createEurAccount(
                            bridgeCustomer.getBridgeCustomerId(),
                            dest,
                            destAddress.getValue(),
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
}
