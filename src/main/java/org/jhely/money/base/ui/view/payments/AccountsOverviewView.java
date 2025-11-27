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
import org.jhely.money.sdk.bridge.model.RejectionReason;

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

    public AccountsOverviewView(MockStablecoinAccountsService svc, 
                                BridgeOnboardingService onboarding,
                                CustomerService customers,
                                BridgeAgreementService agreements,
                                org.jhely.money.base.service.payments.BridgeAgreementBroadcaster broadcaster,
                                org.jhely.money.base.service.payments.KycStatusBroadcaster kycBroadcaster) {
        this.svc = svc;
        this.onboarding = onboarding;
        this.customers = customers;
        this.agreements = agreements;
        this.broadcaster = broadcaster;
        this.kycBroadcaster = kycBroadcaster;

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
        Optional<BridgeCustomer> existing = onboarding.findForUser(user.id(), user.email());

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

        // Header row with status
        var headerRow = buildCustomerHeader(bc, bridgeCustomer);
        
        // Customer details panel
        var detailsPanel = buildCustomerDetailsPanel(bc, bridgeCustomer);
        
        var content = new VerticalLayout(headerRow, detailsPanel);
        content.setPadding(false);
        content.setSpacing(true);
        card.add(content);

        // Register for KYC status updates to live-refresh
        var ui = UI.getCurrent();
        final Customer[] customerHolder = new Customer[]{bridgeCustomer};
        final Component[] detailsHolder = new Component[]{detailsPanel};
        org.jhely.money.base.service.payments.KycStatusBroadcaster.Listener listener = updated -> {
            if (!bc.getUserId().equals(updated.getUserId())) return;
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

    private Component buildCustomerHeader(BridgeCustomer bc, Customer bridgeCustomer) {
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

        // Refresh button
        var refreshBtn = new Button(new Icon(VaadinIcon.REFRESH));
        refreshBtn.getElement().setAttribute("title", "Refresh status from Bridge");
        refreshBtn.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
        refreshBtn.addClickListener(e -> UI.getCurrent().getPage().reload());

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
            if (!StringUtils.hasText(text)) text = rej.getDeveloperReason();
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

    private Component buildActionsRow(BridgeCustomer bc, Customer bridgeCustomer) {
        var actions = new HorizontalLayout();
        actions.setSpacing(true);
        actions.setPadding(false);
        actions.getStyle().set("marginTop", "8px");

        CustomerStatus status = bridgeCustomer != null ? bridgeCustomer.getStatus() : null;
        boolean needsKyc = status == null 
            || status == CustomerStatus.NOT_STARTED 
            || status == CustomerStatus.INCOMPLETE 
            || status == CustomerStatus.REJECTED
            || status == CustomerStatus.AWAITING_QUESTIONNAIRE;

        if (needsKyc) {
            var kycBtn = new Button("Complete KYC", new Icon(VaadinIcon.USER_CHECK));
            kycBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
            kycBtn.addClickListener(e -> openKycLink(bc));
            actions.add(kycBtn);
        }

        // Check if requirements_due includes external_account
        var reqsDue = bridgeCustomer != null ? bridgeCustomer.getRequirementsDue() : null;
        boolean needsExternalAccount = reqsDue != null && reqsDue.stream()
            .anyMatch(r -> r == Customer.RequirementsDueEnum.EXTERNAL_ACCOUNT);
        if (needsExternalAccount) {
            var linkAccountBtn = new Button("Link Bank Account", new Icon(VaadinIcon.INSTITUTION));
            linkAccountBtn.addClickListener(e -> Notification.show("Bank account linking coming soon", 3000, Notification.Position.MIDDLE));
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
        try {
            String redirect = buildAbsoluteUrl("/api/bridge/kyc-callback");
            var resp = customers.getKycLink(bc.getBridgeCustomerId(), Optional.empty(), Optional.of(redirect));
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

    private String formatStatus(String status) {
        if (status == null) return "Unknown";
        return status.replace("_", " ").substring(0, 1).toUpperCase() 
            + status.replace("_", " ").substring(1).toLowerCase();
    }

    private String formatRequirement(String req) {
        if (req == null) return "";
        return req.replace("_", " ").substring(0, 1).toUpperCase() 
            + req.replace("_", " ").substring(1).toLowerCase();
    }

    private String formatEndorsementName(String name) {
        if (name == null) return "";
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
        } else if (kyc.equalsIgnoreCase("approved") || kyc.equalsIgnoreCase("verified") || kyc.equalsIgnoreCase("passed")) {
            text = "KYC approved. You're ready to use payments.";
            bg = "var(--lumo-success-color-10pct)";
            color = "var(--lumo-success-text-color)";
        } else if (kyc.equalsIgnoreCase("in_review") || kyc.equalsIgnoreCase("pending") || kyc.equalsIgnoreCase("processing")) {
            text = "KYC in review. We'll notify you when it's done.";
            bg = "var(--lumo-tint-10pct)";
            color = "var(--lumo-body-text-color)";
        } else if (kyc.equalsIgnoreCase("requires_resubmission") || kyc.equalsIgnoreCase("rejected") || kyc.equalsIgnoreCase("failed")) {
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

        var startOrAgain = new Button((kyc == null || kyc.isBlank() || kyc.equalsIgnoreCase("requires_resubmission") || kyc.equalsIgnoreCase("rejected"))
                ? "Start/Resume KYC" : "Open KYC");
        startOrAgain.addClickListener(e -> openKycLink(bc));

        actions.add(startOrAgain);
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
        agreementInfo.getStyle().set("color", "var(--lumo-secondary-text-color").set("fontSize", "var(--lumo-font-size-s)");

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
                String kycUrl = onboarding.requestHostedKycLinkForNewCustomer(user.email(), user.displayName(), redirect);
                if (StringUtils.hasText(kycUrl)) {
                    // Optionally append a state param (user id) if Bridge preserves it; harmless if ignored
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
                    Notification.show("Terms accepted. You can continue with hosted KYC.", 4000, Notification.Position.TOP_CENTER);
                });
            };
            broadcaster.register(userId, listener);
            // Clean up on detach to prevent leaks
            card.addDetachListener(ev -> broadcaster.unregister(userId, listener));
        }
        return card;
    }

    private record CurrentUser(String id, String email, String displayName) {}

    private CurrentUser currentUser() {
        Authentication a = SecurityContextHolder.getContext() != null
                ? SecurityContextHolder.getContext().getAuthentication()
                : null;
        String userId = (a != null && a.getName() != null) ? a.getName() : "user-unknown";
        String email  = (a != null && a.getName() != null) ? a.getName() : "unknown@example.com";
        String name   = (a != null) ? a.getName() : "User";
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
                boolean standard = ("http".equalsIgnoreCase(scheme) && port == 80) || ("https".equalsIgnoreCase(scheme) && port == 443);
                host = hsr.getServerName() + (standard ? "" : (":" + port));
            }
            String context = hsr.getContextPath();
            if (!StringUtils.hasText(context)) context = "";
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
        left.setPadding(false); left.setSpacing(false);
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

        FinancialAccount acc = svc.getDefaultAccount();

        row.add(balanceCard("USDC Available", amount(acc, Asset.USDC), "usd-circle"));
        row.add(balanceCard("EURC Available", amount(acc, Asset.EURC), "euro"));
        row.add(balanceCard("USDT Available", amount(acc, Asset.USDT), "wallet"));

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
        var v = new H1(amt.toPlainString());
        v.getStyle().set("margin", "0");
        card.add(h, v);
        return card;
    }

    private BigDecimal amount(FinancialAccount acc, Asset a) {
        return acc.balances.stream().filter(b -> b.asset == a)
                .map(b -> b.available).findFirst().orElse(BigDecimal.ZERO);
    }

    private Component accountsTable() {
        var card = new Div();
        card.getStyle()
            .set("marginTop", "18px")
            .set("padding", "12px")
            .set("borderRadius", "16px")
            .set("background", "var(--lumo-base-color)")
            .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)");
        card.setWidthFull(); // <-- full width

        Grid<FinancialAccount> grid = new Grid<>(FinancialAccount.class, false);
        grid.setWidthFull();           // <-- full width
//        grid.setHeightByRows(true);    // nice compact height (optional)
        // grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES); // optional styling

        grid.addColumn(a -> a.name)
            .setHeader("Account")
            .setAutoWidth(false)
            .setFlexGrow(2);

        grid.addColumn(a -> a.status)
            .setHeader("Status")
            .setAutoWidth(false)
            .setFlexGrow(1);

        grid.addColumn(a -> balance(a, Asset.USDC))
            .setHeader("USDC")
            .setAutoWidth(false)
            .setFlexGrow(1);

        grid.addColumn(a -> balance(a, Asset.EURC))
            .setHeader("EURC")
            .setAutoWidth(false)
            .setFlexGrow(1);

        grid.addColumn(a -> balance(a, Asset.USDT))
            .setHeader("USDT")
            .setAutoWidth(false)
            .setFlexGrow(1);

        grid.setItems(svc.listAccounts());
        grid.addItemClickListener(ev -> UI.getCurrent().navigate("/finance/transactions"));

        card.add(new H3("Accounts"), grid);
        return card;
    }


    private String balance(FinancialAccount a, Asset asset) {
        return a.balances.stream().filter(b -> b.asset == asset)
                .map(b -> b.available.toPlainString())
                .findFirst().orElse("0.00");
    }
}

