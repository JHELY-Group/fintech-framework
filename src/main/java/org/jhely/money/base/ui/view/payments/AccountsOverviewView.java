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

import org.jhely.money.base.ui.view.MainLayout;

@RolesAllowed("USER")
@Route(value = "finance", layout = MainLayout.class)
@PageTitle("Finance · Overview")
public class AccountsOverviewView extends VerticalLayout {

    private static final long serialVersionUID = -2727514825789008957L;

    private final MockStablecoinAccountsService svc;
    private final BridgeOnboardingService onboarding;

    public AccountsOverviewView(MockStablecoinAccountsService svc, BridgeOnboardingService onboarding) {
        this.svc = svc;
        this.onboarding = onboarding;
        setSizeFull();
        setPadding(true);
        setSpacing(false);
        setAlignItems(Alignment.STRETCH); // make children take full width

        add(new PaymentsSubnav("/finance"));
        add(bridgeStatusBanner());
        add(header());
        add(balancesRow());
        add(accountsTable());
    }

    private Component bridgeStatusBanner() {
        var user = currentUser();
        var maybe = onboarding.findForUser(user.id(), user.email());

        var card = new Div();
        card.getStyle()
            .set("marginBottom", "12px")
            .set("padding", "12px 16px")
            .set("borderRadius", "12px")
            .set("background", "var(--lumo-base-color)")
            .set("boxShadow", "0 2px 12px rgba(0,0,0,0.06)");

        if (maybe.isPresent()) {
            BridgeCustomer bc = maybe.get();
            var icon = new Icon(VaadinIcon.CHECK_CIRCLE);
            icon.setColor("var(--lumo-success-color)");
            icon.setSize("20px");
            var title = new H4("Connected to Bridge");
            title.getStyle().set("margin", "0");
            var info = new Paragraph("Customer ID: " + bc.getBridgeCustomerId() + " · Status: " + bc.getStatus());
            info.getStyle().set("color", "var(--lumo-secondary-text-color)").set("margin", "0");
            var row = new HorizontalLayout(icon, new Div(title, info));
            row.setAlignItems(Alignment.CENTER);
            row.setSpacing(true);
            row.setPadding(false);
            card.add(row);
        } else {
            var icon = new Icon(VaadinIcon.WARNING);
            icon.setColor("var(--lumo-warning-color)");
            icon.setSize("20px");
            var title = new H4("Not connected to Bridge");
            title.getStyle().set("margin", "0");
            var info = new Paragraph("Connect your account to Bridge to receive and send money.");
            info.getStyle().set("color", "var(--lumo-secondary-text-color)").set("margin", "0");
            var cta = new Button("Open Payments", e -> UI.getCurrent().navigate(PaymentsView.class));
            var left = new Div(title, info);
            var row = new HorizontalLayout(icon, left, new Div(), cta);
            row.expand(left);
            row.setAlignItems(Alignment.CENTER);
            row.setSpacing(true);
            row.setPadding(false);
            card.add(row);
        }
        return card;
    }

    private record CurrentUser(String id, String email) {}

    private CurrentUser currentUser() {
        Authentication a = SecurityContextHolder.getContext() != null
                ? SecurityContextHolder.getContext().getAuthentication()
                : null;
        String userId = (a != null && a.getName() != null) ? a.getName() : "user-unknown";
        String email  = (a != null && a.getName() != null) ? a.getName() : "unknown@example.com";
        return new CurrentUser(userId, email);
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

