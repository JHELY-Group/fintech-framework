package org.jhely.money.base.ui.view.payments;

import java.util.Optional;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.util.StringUtils;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
 
import java.time.format.DateTimeFormatter;
import java.time.ZoneOffset;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
 
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.domain.BridgeCustomer;
import org.jhely.money.base.service.payments.BridgeOnboardingService;
import org.jhely.money.base.service.payments.BridgeAgreementService;
import org.jhely.money.base.domain.BridgeAgreement;
import org.jhely.money.base.service.CustomerService;
import org.jhely.money.base.ui.view.MainLayout;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import jakarta.servlet.http.HttpServletRequest;

@RolesAllowed("USER")
@Route(value = "payments", layout = MainLayout.class)
@PageTitle("Payments")
public class PaymentsView extends VerticalLayout {

    private static final Logger log = LoggerFactory.getLogger(PaymentsView.class);

    private final BridgeOnboardingService onboarding;
    private final CustomerService customers;
    private final BridgeAgreementService agreements;

    // (Photo ID upload fields removed with hosted KYC flow simplification)

    private final org.jhely.money.base.service.payments.BridgeAgreementBroadcaster broadcaster;
    private final org.jhely.money.base.service.payments.KycStatusBroadcaster kycBroadcaster;

    public PaymentsView(BridgeOnboardingService onboarding, CustomerService customers, BridgeAgreementService agreements, org.jhely.money.base.service.payments.BridgeAgreementBroadcaster broadcaster, org.jhely.money.base.service.payments.KycStatusBroadcaster kycBroadcaster) {
        this.onboarding = onboarding;
        this.customers = customers;
        this.agreements = agreements;
        this.broadcaster = broadcaster;
        this.kycBroadcaster = kycBroadcaster;
        setSizeFull();
        setPadding(true);
        setSpacing(true);
        setDefaultHorizontalComponentAlignment(Alignment.CENTER);
        // Add bottom padding to prevent footer overlap when forms are long
        getElement().getStyle().set("padding-bottom", "128px");

        add(buildHeader());
        add(buildBody());
    }

    /* ---------- UI ---------- */

    private Component buildHeader() {
        var header = new HorizontalLayout();
        header.setWidthFull();
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);
        header.setAlignItems(Alignment.CENTER);

        var title = new H2("Payments");
        var subtitle = new Paragraph("Connect your account to Bridge to receive and send money.");
        var left = new VerticalLayout(title, subtitle);
        left.setPadding(false);
        left.setSpacing(false);

        header.add(left);
        return header;
    }

    private Component buildBody() {
        var user = currentUser();
        String userId = user.id();
        String email  = user.email();
        String name   = user.displayName();

        Optional<BridgeCustomer> existing = onboarding.findForUser(userId, email);

        if (existing.isPresent()) {
            return onboardedCard(existing.get());
        } else {
            return onboardingCard(userId, email, name);
        }
    }

    private Component onboardedCard(BridgeCustomer bc) {
        var card = new Div();
        card.getStyle()
            .set("maxWidth", "680px")
            .set("width", "100%")
            .set("padding", "24px")
            .set("borderRadius", "16px")
            .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)")
            .set("background", "var(--lumo-base-color)");

        var check = new Icon(VaadinIcon.CHECK_CIRCLE);
        check.setColor("var(--lumo-success-color)");
        check.setSize("28px");

        var titleRow = new HorizontalLayout(check, new H3("You're connected to Bridge"));
        titleRow.setAlignItems(Alignment.CENTER);
        titleRow.setSpacing(true);
        titleRow.setPadding(false);

        var info = new Paragraph(
            "Customer ID: " + bc.getBridgeCustomerId() + " · Status: " + bc.getStatus()
        );
        info.getStyle().set("color", "var(--lumo-secondary-text-color)");

        // KYC status panel
        var kycPanel = buildKycStatusPanel(bc);

        var continueBtn = new Button("Continue to Payments");
        continueBtn.addClickListener(e -> UI.getCurrent().navigate("payments/home"));

        card.add(titleRow, info, kycPanel, continueBtn);

        // Register for KYC status updates to live-refresh the panel
        var ui = UI.getCurrent();
        final Component[] panelHolder = new Component[]{kycPanel};
        org.jhely.money.base.service.payments.KycStatusBroadcaster.Listener listener = updated -> {
            if (!bc.getUserId().equals(updated.getUserId())) return;
            ui.access(() -> {
                Component newPanel = buildKycStatusPanel(updated);
                card.replace(panelHolder[0], newPanel);
                panelHolder[0] = newPanel;
            });
        };
        kycBroadcaster.register(bc.getUserId(), listener);
        card.addDetachListener(ev -> kycBroadcaster.unregister(bc.getUserId(), listener));
        return card;
    }

    private Component buildKycStatusPanel(BridgeCustomer bc) {
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
        startOrAgain.addClickListener(e -> {
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
        });

        actions.add(startOrAgain);
        wrap.add(p, actions);
        return wrap;
    }

    private Component onboardingCard(String userId, String email, String name) {
        var card = new Div();
        card.getStyle()
            .set("maxWidth", "680px")
            .set("width", "100%")
            .set("padding", "24px")
            .set("borderRadius", "16px")
            .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)")
            .set("background", "var(--lumo-base-color)");

        var icon = new Icon(VaadinIcon.WALLET);
        icon.setColor("var(--lumo-primary-color)");
        icon.setSize("28px");

    var titleRow = new HorizontalLayout(icon, new H3("Connect to Bridge"));
        titleRow.setAlignItems(Alignment.CENTER);
        titleRow.setSpacing(true);
        titleRow.setPadding(false);

    var caption = new Paragraph("Use the hosted KYC flow to create your Bridge customer. Bridge will collect the required identity details and redirect you back here.");
        caption.getStyle().set("color", "var(--lumo-secondary-text-color)");

    // Agreement status / timestamp display
    Optional<BridgeAgreement> existingAgreement = agreements.findForUser(userId, email);
    var agreementInfo = new Paragraph(existingAgreement.map(ag -> {
        String ts = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss 'UTC'")
            .withZone(ZoneOffset.UTC)
            .format(ag.getCreatedAt());
        return "Terms accepted " + ts;
    }).orElse("Terms not accepted yet."));
    agreementInfo.getStyle().set("color", "var(--lumo-secondary-text-color").set("fontSize", "var(--lumo-font-size-s)");

        // Minimal fields — hosted KYC collects the rest
    var emailField = new EmailField("Email");
        emailField.setWidthFull();
        emailField.setValue(email != null ? email : "");
    emailField.setReadOnly(true);
        // Note: All other personal and address fields are collected by Bridge.

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

        var actions = new HorizontalLayout(tosBtn);
        actions.setSpacing(true);
        actions.setWidthFull();
        actions.getStyle()
            .set("margin-top", "24px")
            .set("padding", "8px 0 0 0")
            .set("border-top", "1px solid var(--lumo-contrast-10pct)");
        actions.setJustifyContentMode(JustifyContentMode.START);
        // Ensure card has enough bottom padding so footer does not overlap
        card.getStyle().set("padding-bottom", "160px");
        // Root layout safe bottom padding (in case of global footer height changes)
        getElement().getStyle().set("padding-bottom", "32px");

        // --- Alternative onboarding: hosted KYC that creates the customer ---
        var hostedKycBtn = new Button("Create via Hosted KYC (auto)");
        hostedKycBtn.getStyle().set("margin-left", "auto");
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

        // Explanatory hint
        var altHint = new Paragraph("Prefer a quicker path? Use the hosted KYC flow – Bridge collects all required identity & address details and creates the customer automatically. After completion you'll be redirected; refresh this page to see your status.");
        altHint.getStyle().set("fontSize", "var(--lumo-font-size-s)").set("color", "var(--lumo-secondary-text-color)");

        card.add(titleRow, caption,
                 agreementInfo,
                 emailField,
                 actions,
                 hostedKycBtn,
                 altHint);

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

    /* ---------- current user helpers (adapt to your security model) ---------- */

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

    // Small helper to derive ISO3 from ISO2 for your curated list
    // Removed ISO mapping helper as we no longer collect country/ID upfront.

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
}
