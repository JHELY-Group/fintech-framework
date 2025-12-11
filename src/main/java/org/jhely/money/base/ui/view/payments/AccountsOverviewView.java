package org.jhely.money.base.ui.view.payments;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.service.payments.mock.MockStablecoinAccountsService;
import org.jhely.money.base.service.payments.mock.PaymentModels.*;
import org.jhely.money.base.service.payments.BridgeOnboardingService;
import org.jhely.money.base.domain.BridgeCustomer;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.servlet.http.HttpServletRequest;

import org.jhely.money.base.service.CustomerService;
import org.jhely.money.base.service.payments.BridgeAgreementService;
import org.jhely.money.base.domain.BridgeAgreement;
import org.jhely.money.base.ui.view.MainLayout;
import org.jhely.money.sdk.bridge.model.Customer;
import org.jhely.money.sdk.bridge.model.CustomerCapabilities;
import org.jhely.money.sdk.bridge.model.CustomerCapabilityState;
import org.jhely.money.sdk.bridge.model.CustomerStatus;
import org.jhely.money.sdk.bridge.model.Endorsement;
import org.jhely.money.sdk.bridge.model.EndorsementType;
import org.jhely.money.sdk.bridge.model.RejectionReason;
import org.jhely.money.sdk.bridge.model.ExternalAccountResponse;
import org.jhely.money.base.service.payments.ExternalAccountService;
import org.jhely.money.base.service.BridgeWalletService;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.formlayout.FormLayout;

@RolesAllowed("USER")
@Route(value = "finance", layout = MainLayout.class)
@PageTitle("Finance · Overview")
public class AccountsOverviewView extends VerticalLayout {

    private static final long serialVersionUID = -2727514825789008957L;
    private static final Logger log = LoggerFactory.getLogger(AccountsOverviewView.class);

    private final MockStablecoinAccountsService svc;
    private final BridgeOnboardingService onboarding;
    private final CustomerService customers;
    private final BridgeAgreementService agreements;
    private final org.jhely.money.base.service.payments.BridgeAgreementBroadcaster broadcaster;
    private final org.jhely.money.base.service.payments.KycStatusBroadcaster kycBroadcaster;
    private final ExternalAccountService externalAccountService;
    private final BridgeWalletService bridgeWalletService;

    public AccountsOverviewView(MockStablecoinAccountsService svc,
            BridgeOnboardingService onboarding,
            CustomerService customers,
            BridgeAgreementService agreements,
            org.jhely.money.base.service.payments.BridgeAgreementBroadcaster broadcaster,
            org.jhely.money.base.service.payments.KycStatusBroadcaster kycBroadcaster,
            ExternalAccountService externalAccountService,
            BridgeWalletService bridgeWalletService) {
        this.svc = svc;
        this.onboarding = onboarding;
        this.customers = customers;
        this.agreements = agreements;
        this.broadcaster = broadcaster;
        this.kycBroadcaster = kycBroadcaster;
        this.externalAccountService = externalAccountService;
        this.bridgeWalletService = bridgeWalletService;

        setSizeFull();
        setPadding(true);
        setSpacing(false);
        setAlignItems(Alignment.STRETCH); // make children take full width

        add(new PaymentsSubnav("/finance"));
        add(buildBridgeSection());
        add(header());
        add(balancesRow());
        add(accountsTable());
    }

    private Component buildBridgeSection() {
        var user = currentUser();
        
        // First, try to sync from Bridge API if we don't have complete KYC data locally.
        // This handles the case where the same user completed KYC on another app instance
        // sharing the same Bridge API key.
        Optional<BridgeCustomer> existing = onboarding.syncFromBridgeIfNeeded(user.id(), user.email());

        if (existing.isPresent()) {
            return onboardedCard(existing.get());
        } else {
            return onboardingCard(user.id(), user.email(), user.displayName());
        }
    }

    private Component onboardedCard(BridgeCustomer bc) {
        var card = new Div();
        card.getStyle()
                .set("marginBottom", "12px")
                .set("padding", "16px 20px")
                .set("borderRadius", "12px")
                .set("background", "var(--lumo-base-color)")
                .set("boxShadow", "0 2px 12px rgba(0,0,0,0.06)");

        // Fetch full customer details from Bridge API
        Customer bridgeCustomer = null;
        try {
            bridgeCustomer = customers.getCustomer(bc.getBridgeCustomerId());
        } catch (Exception e) {
            log.warn("Failed to fetch Bridge customer details for {}: {}", bc.getBridgeCustomerId(), e.getMessage());
        }

        // Customer details panel
        var detailsPanel = buildCustomerDetailsPanel(bc, bridgeCustomer);

        // Create content container first so we can pass it to header builder
        var content = new VerticalLayout();
        content.setPadding(false);
        content.setSpacing(true);

        // detailsHolder for dynamic updates from refresh button
        final Component[] detailsHolder = new Component[] { detailsPanel };

        // Header row with status - pass content and detailsHolder for dynamic updates
        var headerRow = buildCustomerHeader(bc, bridgeCustomer, content, detailsHolder);

        content.add(headerRow, detailsPanel);
        card.add(content);

        // Register for KYC status updates to live-refresh
        var ui = UI.getCurrent();
        final Customer[] customerHolder = new Customer[] { bridgeCustomer };
        org.jhely.money.base.service.payments.KycStatusBroadcaster.Listener listener = updated -> {
            if (!bc.getUserId().equals(updated.getUserId()))
                return;
            ui.access(() -> {
                // Refresh customer data from Bridge
                try {
                    Customer refreshed = customers.getCustomer(bc.getBridgeCustomerId());
                    customerHolder[0] = refreshed;
                    Component newPanel = buildCustomerDetailsPanel(updated, refreshed);
                    content.replace(detailsHolder[0], newPanel);
                    detailsHolder[0] = newPanel;
                } catch (Exception e) {
                    log.warn("Failed to refresh Bridge customer: {}", e.getMessage());
                }
            });
        };
        kycBroadcaster.register(bc.getUserId(), listener);
        card.addDetachListener(ev -> kycBroadcaster.unregister(bc.getUserId(), listener));
        return card;
    }

    private Component buildCustomerHeader(BridgeCustomer bc, Customer bridgeCustomer,
            VerticalLayout contentContainer, Component[] detailsHolder) {
        var row = new HorizontalLayout();
        row.setAlignItems(Alignment.CENTER);
        row.setWidthFull();
        row.setSpacing(true);
        row.setPadding(false);

        // Status icon
        CustomerStatus status = bridgeCustomer != null ? bridgeCustomer.getStatus() : null;
        Icon icon;
        String iconColor;
        if (status == CustomerStatus.ACTIVE) {
            icon = new Icon(VaadinIcon.CHECK_CIRCLE);
            iconColor = "var(--lumo-success-color)";
        } else if (status == CustomerStatus.REJECTED || status == CustomerStatus.OFFBOARDED) {
            icon = new Icon(VaadinIcon.CLOSE_CIRCLE);
            iconColor = "var(--lumo-error-color)";
        } else if (status == CustomerStatus.UNDER_REVIEW || status == CustomerStatus.PAUSED) {
            icon = new Icon(VaadinIcon.CLOCK);
            iconColor = "var(--lumo-primary-color)";
        } else {
            icon = new Icon(VaadinIcon.INFO_CIRCLE);
            iconColor = "var(--lumo-contrast-60pct)";
        }
        icon.setColor(iconColor);
        icon.setSize("24px");

        // Title and subtitle
        var title = new H4("Bridge Account");
        title.getStyle().set("margin", "0");

        String statusText = status != null ? formatStatus(status.getValue()) : bc.getStatus();
        String nameText = "";
        if (bridgeCustomer != null && StringUtils.hasText(bridgeCustomer.getFirstName())) {
            nameText = bridgeCustomer.getFirstName();
            if (StringUtils.hasText(bridgeCustomer.getLastName())) {
                nameText += " " + bridgeCustomer.getLastName();
            }
            nameText += " · ";
        }
        var subtitle = new Span(nameText + "Status: " + statusText);
        subtitle.getStyle().set("color", "var(--lumo-secondary-text-color)").set("fontSize", "var(--lumo-font-size-s)");

        var textBlock = new Div(title, subtitle);
        textBlock.getStyle().set("display", "flex").set("flexDirection", "column").set("gap", "2px");

        // Refresh button - now does background refresh instead of page reload
        var refreshBtn = new Button(new Icon(VaadinIcon.REFRESH));
        refreshBtn.getElement().setAttribute("title", "Refresh status from Bridge");
        refreshBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);

        // Store references for dynamic update
        final Icon[] iconHolder = new Icon[] { icon };
        final Span[] subtitleHolder = new Span[] { subtitle };
        final HorizontalLayout headerRow = row;

        refreshBtn.addClickListener(e -> {
            log.info("Refresh button clicked for customer {}", bc.getBridgeCustomerId());
            refreshBtn.setEnabled(false);
            var ui = UI.getCurrent();

            // Run the Bridge API call in background thread
            new Thread(() -> {
                log.info("Background thread started for refreshing customer {}", bc.getBridgeCustomerId());
                try {
                    log.info("Calling Bridge API to get customer {}", bc.getBridgeCustomerId());
                    Customer refreshed = customers.getCustomer(bc.getBridgeCustomerId());
                    log.info("Bridge API call completed, status: {}",
                            refreshed != null ? refreshed.getStatus() : "null");
                    ui.access(() -> {
                        log.info("UI access callback executing for customer {}", bc.getBridgeCustomerId());
                        // Update status icon
                        CustomerStatus newStatus = refreshed != null ? refreshed.getStatus() : null;
                        Icon newIcon;
                        String newColor;
                        if (newStatus == CustomerStatus.ACTIVE) {
                            newIcon = new Icon(VaadinIcon.CHECK_CIRCLE);
                            newColor = "var(--lumo-success-color)";
                        } else if (newStatus == CustomerStatus.REJECTED || newStatus == CustomerStatus.OFFBOARDED) {
                            newIcon = new Icon(VaadinIcon.CLOSE_CIRCLE);
                            newColor = "var(--lumo-error-color)";
                        } else if (newStatus == CustomerStatus.UNDER_REVIEW || newStatus == CustomerStatus.PAUSED) {
                            newIcon = new Icon(VaadinIcon.CLOCK);
                            newColor = "var(--lumo-primary-color)";
                        } else {
                            newIcon = new Icon(VaadinIcon.INFO_CIRCLE);
                            newColor = "var(--lumo-contrast-60pct)";
                        }
                        newIcon.setColor(newColor);
                        newIcon.setSize("24px");
                        headerRow.replace(iconHolder[0], newIcon);
                        iconHolder[0] = newIcon;

                        // Update subtitle
                        String newStatusText = newStatus != null ? formatStatus(newStatus.getValue()) : bc.getStatus();
                        String newNameText = "";
                        if (refreshed != null && StringUtils.hasText(refreshed.getFirstName())) {
                            newNameText = refreshed.getFirstName();
                            if (StringUtils.hasText(refreshed.getLastName())) {
                                newNameText += " " + refreshed.getLastName();
                            }
                            newNameText += " · ";
                        }
                        subtitleHolder[0].setText(newNameText + "Status: " + newStatusText);

                        // Update details panel
                        Component newPanel = buildCustomerDetailsPanel(bc, refreshed);
                        contentContainer.replace(detailsHolder[0], newPanel);
                        detailsHolder[0] = newPanel;

                        refreshBtn.setEnabled(true);
                        Notification.show("Status refreshed", 2000, Notification.Position.BOTTOM_START);
                        log.info("UI update completed for customer {}", bc.getBridgeCustomerId());
                    });
                } catch (Exception ex) {
                    log.warn("Failed to refresh Bridge customer {}: {}", bc.getBridgeCustomerId(), ex.getMessage(), ex);
                    ui.access(() -> {
                        refreshBtn.setEnabled(true);
                        Notification.show("Failed to refresh status", 3000, Notification.Position.MIDDLE);
                    });
                }
            }).start();
        });

        row.add(icon, textBlock, refreshBtn);
        row.expand(textBlock);
        return row;
    }

    private Component buildCustomerDetailsPanel(BridgeCustomer bc, Customer bridgeCustomer) {
        var panel = new VerticalLayout();
        panel.setPadding(false);
        panel.setSpacing(true);
        panel.setWidthFull();

        if (bridgeCustomer == null) {
            // Fallback to local KYC status
            panel.add(buildLegacyKycStatusPanel(bc));
            return panel;
        }

        // Capabilities section
        CustomerCapabilities caps = bridgeCustomer.getCapabilities();
        if (caps != null) {
            panel.add(buildCapabilitiesSection(caps));
        }

        // Requirements due
        var reqsDue = bridgeCustomer.getRequirementsDue();
        if (reqsDue != null && !reqsDue.isEmpty()) {
            panel.add(buildRequirementsSection(reqsDue));
        }

        // Rejection reasons (if any)
        var rejections = bridgeCustomer.getRejectionReasons();
        if (rejections != null && !rejections.isEmpty()) {
            panel.add(buildRejectionsSection(rejections));
        }

        // Endorsements summary
        var endorsements = bridgeCustomer.getEndorsements();
        if (endorsements != null && !endorsements.isEmpty()) {
            panel.add(buildEndorsementsSection(endorsements));
        }

        // External accounts (linked bank accounts)
        try {
            var externalAccounts = externalAccountService.listAccounts(bc.getBridgeCustomerId());
            if (externalAccounts != null && !externalAccounts.isEmpty()) {
                panel.add(buildExternalAccountsSection(externalAccounts));
            }
        } catch (Exception e) {
            log.debug("Could not fetch external accounts: {}", e.getMessage());
        }

        // Actions row
        panel.add(buildActionsRow(bc, bridgeCustomer));

        return panel;
    }

    private Component buildCapabilitiesSection(CustomerCapabilities caps) {
        var section = new Div();
        section.getStyle()
                .set("padding", "12px 16px")
                .set("borderRadius", "8px")
                .set("background", "var(--lumo-contrast-5pct)");

        var header = new Span("Capabilities");
        header.getStyle().set("fontWeight", "600").set("display", "block").set("marginBottom", "8px");

        var grid = new Div();
        grid.getStyle()
                .set("display", "grid")
                .set("gridTemplateColumns", "repeat(2, 1fr)")
                .set("gap", "8px");

        grid.add(capabilityBadge("Crypto Payin", caps.getPayinCrypto()));
        grid.add(capabilityBadge("Crypto Payout", caps.getPayoutCrypto()));
        grid.add(capabilityBadge("Fiat Payin", caps.getPayinFiat()));
        grid.add(capabilityBadge("Fiat Payout", caps.getPayoutFiat()));

        section.add(header, grid);
        return section;
    }

    private Component capabilityBadge(String label, CustomerCapabilityState state) {
        var badge = new Div();
        badge.getStyle()
                .set("padding", "6px 10px")
                .set("borderRadius", "6px")
                .set("display", "flex")
                .set("alignItems", "center")
                .set("gap", "6px")
                .set("fontSize", "var(--lumo-font-size-s)");

        Icon icon;
        String bg, color;
        String stateText = state != null ? state.getValue() : "unknown";

        if (state == CustomerCapabilityState.ACTIVE) {
            icon = new Icon(VaadinIcon.CHECK);
            bg = "var(--lumo-success-color-10pct)";
            color = "var(--lumo-success-text-color)";
        } else if (state == CustomerCapabilityState.PENDING) {
            icon = new Icon(VaadinIcon.CLOCK);
            bg = "var(--lumo-primary-color-10pct)";
            color = "var(--lumo-primary-text-color)";
        } else if (state == CustomerCapabilityState.REJECTED || state == CustomerCapabilityState.INACTIVE) {
            icon = new Icon(VaadinIcon.CLOSE_SMALL);
            bg = "var(--lumo-error-color-10pct)";
            color = "var(--lumo-error-text-color)";
        } else {
            icon = new Icon(VaadinIcon.QUESTION);
            bg = "var(--lumo-contrast-10pct)";
            color = "var(--lumo-secondary-text-color)";
        }
        icon.setSize("14px");
        icon.setColor(color);

        badge.getStyle().set("background", bg).set("color", color);
        badge.add(icon, new Span(label + ": " + stateText));
        return badge;
    }

    private Component buildRequirementsSection(List<Customer.RequirementsDueEnum> reqs) {
        var section = new Div();
        section.getStyle()
                .set("padding", "12px 16px")
                .set("borderRadius", "8px")
                .set("background", "var(--lumo-primary-color-10pct)");

        var icon = new Icon(VaadinIcon.EXCLAMATION_CIRCLE);
        icon.setColor("var(--lumo-primary-color)");
        icon.setSize("16px");

        var header = new HorizontalLayout(icon, new Span("Requirements Due"));
        header.setAlignItems(Alignment.CENTER);
        header.setSpacing(true);
        header.setPadding(false);
        header.getStyle().set("marginBottom", "8px");

        var list = new UnorderedList();
        list.getStyle().set("margin", "0").set("paddingLeft", "20px");
        for (var req : reqs) {
            list.add(new ListItem(formatRequirement(req.getValue())));
        }

        section.add(header, list);
        return section;
    }

    private Component buildRejectionsSection(List<RejectionReason> rejections) {
        var section = new Div();
        section.getStyle()
                .set("padding", "12px 16px")
                .set("borderRadius", "8px")
                .set("background", "var(--lumo-error-color-10pct)");

        var icon = new Icon(VaadinIcon.WARNING);
        icon.setColor("var(--lumo-error-color)");
        icon.setSize("16px");

        var header = new HorizontalLayout(icon, new Span("Rejection Reasons"));
        header.setAlignItems(Alignment.CENTER);
        header.setSpacing(true);
        header.setPadding(false);
        header.getStyle().set("marginBottom", "8px");

        var list = new UnorderedList();
        list.getStyle().set("margin", "0").set("paddingLeft", "20px").set("color", "var(--lumo-error-text-color)");
        for (var rej : rejections) {
            String text = rej.getReason();
            if (!StringUtils.hasText(text))
                text = rej.getDeveloperReason();
            if (StringUtils.hasText(text)) {
                list.add(new ListItem(text));
            }
        }

        section.add(header, list);
        return section;
    }

    private Component buildEndorsementsSection(List<Endorsement> endorsements) {
        var section = new Div();
        section.getStyle()
                .set("padding", "12px 16px")
                .set("borderRadius", "8px")
                .set("background", "var(--lumo-contrast-5pct)");

        var header = new Span("Endorsements");
        header.getStyle().set("fontWeight", "600").set("display", "block").set("marginBottom", "8px");

        var badges = new HorizontalLayout();
        badges.setSpacing(true);
        badges.setPadding(false);
        badges.getStyle().set("flexWrap", "wrap").set("gap", "6px");

        for (var e : endorsements) {
            var badge = new Span(formatEndorsementName(e.getName().getValue()) + ": " + e.getStatus().getValue());
            String bg, color;
            if (e.getStatus() == Endorsement.StatusEnum.APPROVED) {
                bg = "var(--lumo-success-color-10pct)";
                color = "var(--lumo-success-text-color)";
            } else if (e.getStatus() == Endorsement.StatusEnum.REVOKED) {
                bg = "var(--lumo-error-color-10pct)";
                color = "var(--lumo-error-text-color)";
            } else {
                bg = "var(--lumo-contrast-10pct)";
                color = "var(--lumo-secondary-text-color)";
            }
            badge.getStyle()
                    .set("padding", "4px 10px")
                    .set("borderRadius", "12px")
                    .set("fontSize", "var(--lumo-font-size-xs)")
                    .set("background", bg)
                    .set("color", color);
            badges.add(badge);
        }

        section.add(header, badges);
        return section;
    }

    private Component buildExternalAccountsSection(List<ExternalAccountResponse> accounts) {
        var section = new Div();
        section.getStyle()
                .set("padding", "12px 16px")
                .set("borderRadius", "8px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("marginTop", "8px");

        var header = new Span("Linked Bank Accounts");
        header.getStyle().set("fontWeight", "600").set("display", "block").set("marginBottom", "8px");

        var accountList = new VerticalLayout();
        accountList.setPadding(false);
        accountList.setSpacing(true);

        for (var account : accounts) {
            var row = new HorizontalLayout();
            row.setWidthFull();
            row.setAlignItems(FlexComponent.Alignment.CENTER);
            row.getStyle().set("gap", "12px");

            // Bank icon
            var icon = new Icon(VaadinIcon.INSTITUTION);
            icon.setSize("20px");
            icon.getStyle().set("color", "var(--lumo-secondary-text-color)");

            // Account details
            var details = new VerticalLayout();
            details.setPadding(false);
            details.setSpacing(false);

            String accountType = account.getAccountType() != null
                    ? account.getAccountType().toString().toUpperCase()
                    : "BANK";
            String currency = account.getCurrency() != null
                    ? account.getCurrency().toString().toUpperCase()
                    : "";
            String last4 = account.getLast4() != null ? account.getLast4() : "****";
            String bankName = account.getBankName() != null ? account.getBankName() : "";

            var accountName = new Span(accountType + " •••• " + last4);
            accountName.getStyle().set("fontWeight", "500");

            var bankInfo = new Span((bankName.isEmpty() ? "" : bankName + " • ") + currency);
            bankInfo.getStyle()
                    .set("fontSize", "var(--lumo-font-size-s)")
                    .set("color", "var(--lumo-secondary-text-color)");

            details.add(accountName, bankInfo);

            // Status badge - always show as active since account exists
            var statusBadge = new Span("active");
            statusBadge.getStyle()
                    .set("padding", "2px 8px")
                    .set("borderRadius", "8px")
                    .set("fontSize", "var(--lumo-font-size-xs)")
                    .set("background", "var(--lumo-success-color-10pct)")
                    .set("color", "var(--lumo-success-text-color)");

            row.add(icon, details, statusBadge);
            row.expand(details);
            accountList.add(row);
        }

        section.add(header, accountList);
        return section;
    }

    private Component buildActionsRow(BridgeCustomer bc, Customer bridgeCustomer) {
        var actions = new HorizontalLayout();
        actions.setSpacing(true);
        actions.setPadding(false);
        actions.getStyle().set("marginTop", "8px").set("flexWrap", "wrap").set("gap", "8px");

        CustomerStatus status = bridgeCustomer != null ? bridgeCustomer.getStatus() : null;
        boolean needsKyc = status == null
                || status == CustomerStatus.NOT_STARTED
                || status == CustomerStatus.INCOMPLETE
                || status == CustomerStatus.REJECTED
                || status == CustomerStatus.AWAITING_QUESTIONNAIRE;

        // Check endorsement-specific needs
        var endorsements = bridgeCustomer != null ? bridgeCustomer.getEndorsements() : null;
        boolean baseIncomplete = false;
        boolean sepaIncomplete = false;

        if (endorsements != null) {
            for (var e : endorsements) {
                if (e.getName() == EndorsementType.BASE && e.getStatus() != Endorsement.StatusEnum.APPROVED) {
                    baseIncomplete = true;
                }
                if (e.getName() == EndorsementType.SEPA && e.getStatus() != Endorsement.StatusEnum.APPROVED) {
                    sepaIncomplete = true;
                }
            }
        }

        // Show endorsement-specific KYC buttons when rejected or incomplete
        if (needsKyc) {
            if (baseIncomplete) {
                var baseKycBtn = new Button("Complete Base KYC", new Icon(VaadinIcon.USER_CHECK));
                baseKycBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                baseKycBtn.addClickListener(e -> openKycLink(bc, "base"));
                actions.add(baseKycBtn);
            }

            if (sepaIncomplete) {
                var sepaKycBtn = new Button("Complete SEPA KYC", new Icon(VaadinIcon.INSTITUTION));
                sepaKycBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
                sepaKycBtn.addClickListener(e -> openKycLink(bc, "sepa"));
                actions.add(sepaKycBtn);
            }

            // Fallback button if no specific endorsements detected
            if (!baseIncomplete && !sepaIncomplete) {
                var kycBtn = new Button("Complete KYC", new Icon(VaadinIcon.USER_CHECK));
                kycBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
                kycBtn.addClickListener(e -> openKycLink(bc, null));
                actions.add(kycBtn);
            }
        }

        // Check if requirements_due includes external_account
        var reqsDue = bridgeCustomer != null ? bridgeCustomer.getRequirementsDue() : null;
        boolean needsExternalAccount = reqsDue != null && reqsDue.stream()
                .anyMatch(r -> r == Customer.RequirementsDueEnum.EXTERNAL_ACCOUNT);
        if (needsExternalAccount) {
            var linkAccountBtn = new Button("Link Bank Account", new Icon(VaadinIcon.INSTITUTION));
            linkAccountBtn.addClickListener(e -> openLinkBankAccountDialog(bc, bridgeCustomer));
            actions.add(linkAccountBtn);
        }

        // Re-open ToS if needed
        Boolean tosAccepted = bridgeCustomer != null ? bridgeCustomer.getHasAcceptedTermsOfService() : null;
        if (tosAccepted == null || !tosAccepted) {
            var tosBtn = new Button("Accept Terms", new Icon(VaadinIcon.FILE_TEXT_O));
            tosBtn.addClickListener(e -> openTosLink(bc));
            actions.add(tosBtn);
        }

        return actions;
    }

    private void openKycLink(BridgeCustomer bc) {
        openKycLink(bc, null);
    }

    private void openKycLink(BridgeCustomer bc, String endorsement) {
        try {
            String redirect = buildAbsoluteUrl("/api/bridge/kyc-callback");
            var resp = customers.getKycLink(bc.getBridgeCustomerId(),
                    Optional.ofNullable(endorsement),
                    Optional.of(redirect));
            String url = resp.getUrl();
            if (StringUtils.hasText(url)) {
                UI.getCurrent().getPage().open(url);
            } else {
                Notification.show("KYC link unavailable", 3000, Notification.Position.MIDDLE);
            }
        } catch (Exception ex) {
            log.error("Open KYC failed: {}", ex.getMessage(), ex);
            Notification.show("Failed to open KYC", 4000, Notification.Position.MIDDLE);
        }
    }

    private void openTosLink(BridgeCustomer bc) {
        try {
            var resp = customers.getTosAcceptanceLink(bc.getBridgeCustomerId());
            String url = resp.getUrl();
            if (StringUtils.hasText(url)) {
                String redirect = buildAbsoluteUrl("/api/bridge/tos-callback");
                String encoded = URLEncoder.encode(redirect, StandardCharsets.UTF_8);
                url = url + (url.contains("?") ? "&" : "?") + "redirect_uri=" + encoded;
                UI.getCurrent().getPage().open(url);
            } else {
                Notification.show("ToS link unavailable", 3000, Notification.Position.MIDDLE);
            }
        } catch (Exception ex) {
            log.error("Open ToS failed: {}", ex.getMessage(), ex);
            Notification.show("Failed to open Terms of Service", 4000, Notification.Position.MIDDLE);
        }
    }

    private void openLinkBankAccountDialog(BridgeCustomer bc, Customer bridgeCustomer) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Link Bank Account (SEPA/IBAN)");
        dialog.setWidth("500px");

        FormLayout form = new FormLayout();
        form.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1));

        // Pre-populate with customer name if available
        String defaultName = "";
        if (bridgeCustomer != null) {
            String fn = bridgeCustomer.getFirstName();
            String ln = bridgeCustomer.getLastName();
            if (fn != null && ln != null) {
                defaultName = fn + " " + ln;
            }
        }

        TextField firstNameField = new TextField("First Name");
        firstNameField.setRequired(true);
        firstNameField.setWidthFull();

        TextField lastNameField = new TextField("Last Name");
        lastNameField.setRequired(true);
        lastNameField.setWidthFull();
        
        // Pre-populate if we have a default name
        if (defaultName != null && !defaultName.isBlank()) {
            String[] parts = defaultName.trim().split("\\s+", 2);
            firstNameField.setValue(parts[0]);
            if (parts.length > 1) {
                lastNameField.setValue(parts[1]);
            }
        }

        TextField ibanField = new TextField("IBAN");
        ibanField.setPlaceholder("ES12 3456 7890 1234 5678 9012");
        ibanField.setRequired(true);
        ibanField.setWidthFull();
        ibanField.setHelperText("International Bank Account Number");

        // Auto-detect country display
        Span countrySpan = new Span("Country: (auto-detected from IBAN)");
        countrySpan.getStyle().set("color", "var(--lumo-secondary-text-color)");

        ibanField.addValueChangeListener(e -> {
            String iban = e.getValue();
            if (iban != null && iban.length() >= 2) {
                try {
                    String countryName = externalAccountService.getCountryNameFromIban(iban);
                    countrySpan.setText("Country: " + countryName);
                    countrySpan.getStyle().set("color", "var(--lumo-success-text-color)");
                } catch (Exception ex) {
                    countrySpan.setText("Country: Unknown");
                    countrySpan.getStyle().set("color", "var(--lumo-error-text-color)");
                }
            } else {
                countrySpan.setText("Country: (enter IBAN)");
                countrySpan.getStyle().set("color", "var(--lumo-secondary-text-color)");
            }
        });

        TextField bicField = new TextField("BIC/SWIFT Code (Optional)");
        bicField.setPlaceholder("ABCDESMMXXX");
        bicField.setWidthFull();
        bicField.setHelperText("Bank Identifier Code - usually optional for SEPA");

        form.add(firstNameField, lastNameField, ibanField, countrySpan, bicField);

        // Submit button
        Button submitBtn = new Button("Link Account", new Icon(VaadinIcon.CHECK));
        submitBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        submitBtn.addClickListener(e -> {
            String firstName = firstNameField.getValue();
            String lastName = lastNameField.getValue();
            String iban = ibanField.getValue();
            String bic = bicField.getValue();

            if (firstName == null || firstName.isBlank()) {
                Notification.show("Please enter the first name", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (lastName == null || lastName.isBlank()) {
                Notification.show("Please enter the last name", 3000, Notification.Position.MIDDLE);
                return;
            }
            if (iban == null || iban.length() < 15) {
                Notification.show("Please enter a valid IBAN", 3000, Notification.Position.MIDDLE);
                return;
            }

            submitBtn.setEnabled(false);
            submitBtn.setText("Linking...");

            try {
                externalAccountService.createEuAccount(
                        bc.getBridgeCustomerId(),
                        firstName,
                        lastName,
                        iban,
                        bic,
                        null); // country extracted from IBAN

                dialog.close();
                Notification.show("Bank account linked successfully!", 4000, Notification.Position.BOTTOM_START);

                // Refresh the page to show updated status
                UI.getCurrent().getPage().reload();
            } catch (Exception ex) {
                log.error("Failed to link bank account: {}", ex.getMessage(), ex);
                String errorMsg = ex.getMessage();
                if (errorMsg != null && errorMsg.contains("400")) {
                    Notification.show("Invalid bank account details. Please check your IBAN.", 5000,
                            Notification.Position.MIDDLE);
                } else {
                    Notification.show("Failed to link bank account: " + errorMsg, 5000, Notification.Position.MIDDLE);
                }
                submitBtn.setEnabled(true);
                submitBtn.setText("Link Account");
            }
        });

        Button cancelBtn = new Button("Cancel", e -> dialog.close());
        cancelBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY);

        dialog.add(form);
        dialog.getFooter().add(cancelBtn, submitBtn);
        dialog.open();
    }

    private String formatStatus(String status) {
        if (status == null)
            return "Unknown";
        return status.replace("_", " ").substring(0, 1).toUpperCase()
                + status.replace("_", " ").substring(1).toLowerCase();
    }

    private String formatRequirement(String req) {
        if (req == null)
            return "";
        return req.replace("_", " ").substring(0, 1).toUpperCase()
                + req.replace("_", " ").substring(1).toLowerCase();
    }

    private String formatEndorsementName(String name) {
        if (name == null)
            return "";
        return name.toUpperCase();
    }

    /** Legacy KYC status panel for fallback when Bridge API is unavailable */
    private Component buildLegacyKycStatusPanel(BridgeCustomer bc) {
        String kyc = bc.getKycStatus();
        String reason = bc.getKycRejectionReason();
        var wrap = new Div();
        wrap.getStyle()
                .set("width", "100%")
                .set("padding", "12px 16px")
                .set("borderRadius", "12px")
                .set("margin", "8px 0 0 0");

        String text;
        String bg;
        String color;
        if (kyc == null || kyc.isBlank()) {
            text = "KYC not started. Start KYC to enable transfers.";
            bg = "var(--lumo-primary-color-10pct)";
            color = "var(--lumo-primary-text-color)";
        } else if (kyc.equalsIgnoreCase("approved") || kyc.equalsIgnoreCase("verified")
                || kyc.equalsIgnoreCase("passed")) {
            text = "KYC approved. You're ready to use payments.";
            bg = "var(--lumo-success-color-10pct)";
            color = "var(--lumo-success-text-color)";
        } else if (kyc.equalsIgnoreCase("in_review") || kyc.equalsIgnoreCase("pending")
                || kyc.equalsIgnoreCase("processing")) {
            text = "KYC in review. We'll notify you when it's done.";
            bg = "var(--lumo-tint-10pct)";
            color = "var(--lumo-body-text-color)";
        } else if (kyc.equalsIgnoreCase("requires_resubmission") || kyc.equalsIgnoreCase("rejected")
                || kyc.equalsIgnoreCase("failed")) {
            text = "KYC requires attention" + (reason != null && !reason.isBlank() ? (": " + reason) : ".");
            bg = "var(--lumo-error-color-10pct)";
            color = "var(--lumo-error-text-color)";
        } else {
            text = "KYC status: " + kyc;
            bg = "var(--lumo-contrast-10pct)";
            color = "var(--lumo-secondary-text-color)";
        }

        wrap.getStyle().set("background", bg).set("color", color);
        var p = new Paragraph(text);
        p.getStyle().set("margin", "0");

        var actions = new HorizontalLayout();
        actions.setSpacing(true);
        actions.setPadding(false);
        actions.getStyle().set("flexWrap", "wrap").set("gap", "8px").set("marginTop", "8px");

        // Check if rejected or needs resubmission - show endorsement-specific buttons
        boolean needsResubmission = kyc == null || kyc.isBlank()
                || kyc.equalsIgnoreCase("requires_resubmission")
                || kyc.equalsIgnoreCase("rejected")
                || kyc.equalsIgnoreCase("failed");

        if (needsResubmission) {
            // Show Base KYC button
            var baseKycBtn = new Button("Complete Base KYC", new Icon(VaadinIcon.USER_CHECK));
            baseKycBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            baseKycBtn.addClickListener(e -> openKycLink(bc, "base"));
            actions.add(baseKycBtn);

            // Check stored status for SEPA needs - if rejected, likely needs SEPA
            // completion
            String storedStatus = bc.getStatus();
            if ("rejected".equalsIgnoreCase(storedStatus) || "incomplete".equalsIgnoreCase(kyc)) {
                var sepaKycBtn = new Button("Complete SEPA KYC", new Icon(VaadinIcon.INSTITUTION));
                sepaKycBtn.addThemeVariants(ButtonVariant.LUMO_CONTRAST);
                sepaKycBtn.addClickListener(e -> openKycLink(bc, "sepa"));
                actions.add(sepaKycBtn);
            }
        } else {
            // Simple fallback button for other states
            var openBtn = new Button("Open KYC");
            openBtn.addClickListener(e -> openKycLink(bc));
            actions.add(openBtn);
        }

        wrap.add(p, actions);
        return wrap;
    }

    private Component onboardingCard(String userId, String email, String name) {
        var card = new Div();
        card.getStyle()
                .set("marginBottom", "12px")
                .set("padding", "12px 16px")
                .set("borderRadius", "12px")
                .set("background", "var(--lumo-base-color)")
                .set("boxShadow", "0 2px 12px rgba(0,0,0,0.06)");

        // Agreement status / timestamp display
        Optional<BridgeAgreement> existingAgreement = agreements.findForUser(userId, email);
        var agreementInfo = new Paragraph(existingAgreement.map(ag -> {
            String ts = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'")
                    .withZone(ZoneOffset.UTC)
                    .format(ag.getCreatedAt());
            return "Terms accepted " + ts;
        }).orElse("Terms not accepted yet."));
        agreementInfo.getStyle().set("color", "var(--lumo-secondary-text-color").set("fontSize",
                "var(--lumo-font-size-s)");

        // --- Hosted flow buttons -------------------------------------------------
        var existing = onboarding.findForUser(userId, email);
        boolean hasAgreement = existingAgreement.isPresent();
        var tosBtn = new Button(hasAgreement ? "Re-open ToS" : "Accept Terms of Service");
        tosBtn.addClickListener(e -> {
            try {
                String url;
                if (existing.isPresent()) {
                    var resp = customers.getTosAcceptanceLink(existing.get().getBridgeCustomerId());
                    url = resp.getUrl();
                } else {
                    var resp = customers.requestTosLink(UUID.randomUUID().toString());
                    url = resp.getUrl();
                }
                if (StringUtils.hasText(url)) {
                    // Append redirect target for callback capture (absolute URL)
                    String redirect = buildAbsoluteUrl("/api/bridge/tos-callback");
                    String encoded = URLEncoder.encode(redirect, StandardCharsets.UTF_8);
                    if (url.contains("?")) {
                        url = url + "&redirect_uri=" + encoded;
                    } else {
                        url = url + "?redirect_uri=" + encoded;
                    }
                    UI.getCurrent().getPage().open(url);
                } else {
                    Notification.show("ToS link unavailable", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                log.error("requestTosLink failed: {}", ex.getMessage(), ex);
                Notification.show("Failed to open ToS link", 4000, Notification.Position.MIDDLE);
            }
        });

        // --- Alternative onboarding: hosted KYC that creates the customer ---
        var hostedKycBtn = new Button("Initiate KYC");
        hostedKycBtn.addClickListener(ev -> {
            try {
                String redirect = buildAbsoluteUrl("/api/bridge/kyc-callback");
                var user = currentUser();
                String kycUrl = onboarding.requestHostedKycLinkForNewCustomer(user.email(), user.displayName(),
                        redirect);
                if (StringUtils.hasText(kycUrl)) {
                    // Optionally append a state param (user id) if Bridge preserves it; harmless if
                    // ignored
                    String userState = currentUser().id();
                    if (kycUrl.contains("?")) {
                        kycUrl = kycUrl + "&state=" + URLEncoder.encode(userState, StandardCharsets.UTF_8);
                    } else {
                        kycUrl = kycUrl + "?state=" + URLEncoder.encode(userState, StandardCharsets.UTF_8);
                    }
                    UI.getCurrent().getPage().open(kycUrl);
                } else {
                    Notification.show("Hosted KYC link unavailable", 3000, Notification.Position.MIDDLE);
                }
            } catch (Exception ex) {
                log.error("Hosted KYC link failed: {}", ex.getMessage(), ex);
                Notification.show("Failed to open hosted KYC link", 4000, Notification.Position.MIDDLE);
            }
        });

        var actions = new HorizontalLayout(tosBtn, hostedKycBtn);
        actions.setSpacing(true);

        var content = new VerticalLayout(agreementInfo, actions);
        content.setPadding(false);
        content.setSpacing(false);
        card.add(content);

        // --- Push-based update: register listener if agreement not yet captured ---
        if (!hasAgreement) {
            var ui = UI.getCurrent();
            org.jhely.money.base.service.payments.BridgeAgreementBroadcaster.Listener listener = ag -> {
                // Ensure UI thread access
                ui.access(() -> {
                    String ts = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'")
                            .withZone(ZoneOffset.UTC)
                            .format(ag.getCreatedAt());
                    agreementInfo.setText("Terms accepted " + ts);
                    tosBtn.setText("Re-open ToS");
                    Notification.show("Terms accepted. You can continue with hosted KYC.", 4000,
                            Notification.Position.TOP_CENTER);
                });
            };
            broadcaster.register(userId, listener);
            // Clean up on detach to prevent leaks
            card.addDetachListener(ev -> broadcaster.unregister(userId, listener));
        }
        return card;
    }

    private record CurrentUser(String id, String email, String displayName) {
    }

    private CurrentUser currentUser() {
        Authentication a = SecurityContextHolder.getContext() != null
                ? SecurityContextHolder.getContext().getAuthentication()
                : null;
        String userId = (a != null && a.getName() != null) ? a.getName() : "user-unknown";
        String email = (a != null && a.getName() != null) ? a.getName() : "unknown@example.com";
        String name = (a != null) ? a.getName() : "User";
        return new CurrentUser(userId, email, name);
    }

    private static String buildAbsoluteUrl(String path) {
        var req = VaadinService.getCurrentRequest();
        if (req instanceof VaadinServletRequest vsr) {
            HttpServletRequest hsr = vsr.getHttpServletRequest();
            String scheme = headerOrDefault(hsr, "X-Forwarded-Proto", hsr.getScheme());
            String hostHeader = headerOrDefault(hsr, "X-Forwarded-Host", hsr.getHeader("Host"));
            String host;
            if (StringUtils.hasText(hostHeader)) {
                host = hostHeader;
            } else {
                int port = hsr.getServerPort();
                boolean standard = ("http".equalsIgnoreCase(scheme) && port == 80)
                        || ("https".equalsIgnoreCase(scheme) && port == 443);
                host = hsr.getServerName() + (standard ? "" : (":" + port));
            }
            String context = hsr.getContextPath();
            if (!StringUtils.hasText(context))
                context = "";
            String cleanPath = path.startsWith("/") ? path : ("/" + path);
            return scheme + "://" + host + context + cleanPath;
        }
        // Fallback to relative if request not available
        return path;
    }

    private static String headerOrDefault(HttpServletRequest hsr, String name, String def) {
        String v = hsr.getHeader(name);
        return StringUtils.hasText(v) ? v : def;
    }

    private Component header() {
        var h = new HorizontalLayout();
        h.setWidthFull();
        h.setAlignItems(Alignment.CENTER);
        var title = new H2("Stablecoin Financial Accounts");
        var subtitle = new Paragraph("Receive and send with USDC/EURC and more. Sandbox with mock data.");
        subtitle.getStyle().set("color", "var(--lumo-secondary-text-color)");
        var left = new VerticalLayout(title, subtitle);
        left.setPadding(false);
        left.setSpacing(false);
        var receive = new Button("Receive", new Icon(VaadinIcon.DOWNLOAD_ALT));
        receive.addClickListener(e -> UI.getCurrent().navigate("/finance/receive"));
        var send = new Button("Send", new Icon(VaadinIcon.UPLOAD_ALT));
        send.addClickListener(e -> UI.getCurrent().navigate("/finance/send"));
        h.add(left, new Div(), receive, send);
        h.expand(left);
        return h;
    }

    private Component balancesRow() {
        var row = new HorizontalLayout();
        row.setWidthFull();
        row.setSpacing(true);
        row.setWrap(false);

        // Fetch real balances from Bridge wallets if customer exists
        java.math.BigDecimal usdc = java.math.BigDecimal.ZERO;
        java.math.BigDecimal eurc = java.math.BigDecimal.ZERO;
        java.math.BigDecimal usdt = java.math.BigDecimal.ZERO;

        try {
            var user = currentUser();
            var bridgeCustomer = onboarding.findForUser(user.id(), user.email());
            if (bridgeCustomer.isPresent()) {
                var balances = bridgeWalletService.getAggregatedBalances(
                        bridgeCustomer.get().getBridgeCustomerId());
                usdc = balances.getOrDefault("usdc", java.math.BigDecimal.ZERO);
                eurc = balances.getOrDefault("eurc", java.math.BigDecimal.ZERO);
                usdt = balances.getOrDefault("usdt", java.math.BigDecimal.ZERO);
            }
        } catch (Exception e) {
            log.warn("Failed to fetch Bridge wallet balances: {}", e.getMessage());
        }

        row.add(balanceCard("USDC Available", usdc, "usd-circle"));
        row.add(balanceCard("EURC Available", eurc, "euro"));
        row.add(balanceCard("USDT Available", usdt, "wallet"));

        return row;
    }

    private Component balanceCard(String label, BigDecimal amt, String icon) {
        var card = new Div();
        card.getStyle().set("flex", "1")
                .set("padding", "18px")
                .set("borderRadius", "16px")
                .set("background", "var(--lumo-base-color)")
                .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)");
        var h = new H3(label);
        h.getStyle().set("margin", "0 0 8px 0");
        var v = new H1(amt.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString());
        v.getStyle().set("margin", "0");
        card.add(h, v);
        return card;
    }

    private Component accountsTable() {
        var card = new Div();
        card.getStyle()
                .set("marginTop", "18px")
                .set("padding", "12px")
                .set("borderRadius", "16px")
                .set("background", "var(--lumo-base-color)")
                .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)");
        card.setWidthFull();

        // Try to fetch Bridge wallets with balances
        java.util.List<org.jhely.money.sdk.bridge.model.BridgeWalletWithBalances> wallets = new java.util.ArrayList<>();
        try {
            var user = currentUser();
            var bridgeCustomer = onboarding.findForUser(user.id(), user.email());
            if (bridgeCustomer.isPresent()) {
                String customerId = bridgeCustomer.get().getBridgeCustomerId();
                var walletList = bridgeWalletService.listWalletsForCustomer(customerId);
                for (var wallet : walletList) {
                    var detailed = bridgeWalletService.getWalletWithBalances(customerId, wallet.getId());
                    if (detailed != null) {
                        wallets.add(detailed);
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Failed to fetch Bridge wallets: {}", e.getMessage());
        }

        Grid<org.jhely.money.sdk.bridge.model.BridgeWalletWithBalances> grid = new Grid<>();
        grid.setWidthFull();

        grid.addColumn(w -> w.getChain() != null ? w.getChain().getValue() : "Unknown")
                .setHeader("Chain")
                .setAutoWidth(false)
                .setFlexGrow(1);

        grid.addColumn(w -> truncateAddress(w.getAddress()))
                .setHeader("Wallet Address")
                .setAutoWidth(false)
                .setFlexGrow(2);

        grid.addColumn(w -> getBalanceForCurrency(w, "usdc"))
                .setHeader("USDC")
                .setAutoWidth(false)
                .setFlexGrow(1);

        grid.addColumn(w -> getBalanceForCurrency(w, "eurc"))
                .setHeader("EURC")
                .setAutoWidth(false)
                .setFlexGrow(1);

        grid.addColumn(w -> getBalanceForCurrency(w, "usdt"))
                .setHeader("USDT")
                .setAutoWidth(false)
                .setFlexGrow(1);

        grid.setItems(wallets);

        if (wallets.isEmpty()) {
            var noWallets = new Span(
                    "No Bridge wallets found. Wallets are created automatically when you receive stablecoins.");
            noWallets.getStyle().set("color", "var(--lumo-secondary-text-color)");
            card.add(new H3("Bridge Wallets"), noWallets);
        } else {
            card.add(new H3("Bridge Wallets"), grid);
        }
        return card;
    }

    private String truncateAddress(String address) {
        if (address == null || address.length() <= 12) {
            return address != null ? address : "";
        }
        return address.substring(0, 6) + "..." + address.substring(address.length() - 4);
    }

    private String getBalanceForCurrency(org.jhely.money.sdk.bridge.model.BridgeWalletWithBalances wallet,
            String currency) {
        if (wallet.getBalances() == null) {
            return "0.00";
        }
        return wallet.getBalances().stream()
                .filter(b -> b.getCurrency() != null && currency.equalsIgnoreCase(b.getCurrency().getValue()))
                .map(b -> {
                    String balance = b.getBalance() != null ? b.getBalance() : "0";
                    try {
                        return new java.math.BigDecimal(balance)
                                .setScale(2, java.math.RoundingMode.HALF_UP)
                                .toPlainString();
                    } catch (NumberFormatException e) {
                        return "0.00";
                    }
                })
                .findFirst()
                .orElse("0.00");
    }

    private BigDecimal amount(FinancialAccount acc, Asset a) {
        return acc.balances.stream().filter(b -> b.asset == a)
                .map(b -> b.available).findFirst().orElse(BigDecimal.ZERO);
    }

    private String balance(FinancialAccount a, Asset asset) {
        return a.balances.stream().filter(b -> b.asset == asset)
                .map(b -> b.available.setScale(2, java.math.RoundingMode.HALF_UP).toPlainString())
                .findFirst().orElse("0.00");
    }
}
