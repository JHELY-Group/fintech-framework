package org.jhely.money.base.ui.view.payments;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
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
            .set("padding", "12px 16px")
            .set("borderRadius", "12px")
            .set("background", "var(--lumo-base-color)")
            .set("boxShadow", "0 2px 12px rgba(0,0,0,0.06)");

        var check = new Icon(VaadinIcon.CHECK_CIRCLE);
        check.setColor("var(--lumo-success-color)");
        check.setSize("20px");

        var title = new H4("Connected to Bridge");
        title.getStyle().set("margin", "0");
        
        var info = new Paragraph("Customer ID: " + bc.getBridgeCustomerId() + " · Status: " + bc.getStatus());
        info.getStyle().set("color", "var(--lumo-secondary-text-color)").set("margin", "0");

        var row = new HorizontalLayout(check, new Div(title, info));
        row.setAlignItems(Alignment.CENTER);
        row.setSpacing(true);
        row.setPadding(false);
        
        // KYC status panel
        var kycPanel = buildKycStatusPanel(bc);
        
        var content = new VerticalLayout(row, kycPanel);
        content.setPadding(false);
        content.setSpacing(false);
        card.add(content);

        // Register for KYC status updates to live-refresh the panel
        var ui = UI.getCurrent();
        final Component[] panelHolder = new Component[]{kycPanel};
        org.jhely.money.base.service.payments.KycStatusBroadcaster.Listener listener = updated -> {
            if (!bc.getUserId().equals(updated.getUserId())) return;
            ui.access(() -> {
                Component newPanel = buildKycStatusPanel(updated);
                content.replace(panelHolder[0], newPanel);
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

