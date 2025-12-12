package org.jhely.money.base.ui.view;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.FlexComponent.Alignment;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.sidenav.SideNav;
import com.vaadin.flow.component.sidenav.SideNavItem;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;

import jakarta.annotation.security.PermitAll;
import org.jhely.money.base.security.AuthenticatedUser;
import org.jhely.money.base.service.AccountBalanceService;
import org.jhely.money.base.ui.view.components.UserBadge;
import org.jhely.money.base.ui.view.events.BalanceChangedEvent;
import org.jhely.money.base.ui.view.HomeView;
import org.jhely.money.base.ui.view.LoginRegisterView;
import org.jhely.money.base.ui.view.ReceiptsView;
import org.jhely.money.base.ui.view.WalletView;
import org.jhely.money.base.ui.view.payments.AccountsOverviewView;
import org.jhely.money.base.ui.view.payments.ReceiveFundsView;
import org.jhely.money.base.ui.view.payments.SendFundsView;
import org.jhely.money.base.ui.view.payments.TransactionsView;
import org.jhely.money.base.ui.view.x402.X402DashboardView;
import org.jhely.money.base.ui.view.x402.X402FacilitatorView;
import org.springframework.beans.factory.annotation.Value;

@PermitAll
@PageTitle("App")
public class MainLayout extends AppLayout implements AfterNavigationObserver {
  private final AuthenticatedUser auth;
  private final AccountBalanceService balanceService;
  private final HorizontalLayout right = new HorizontalLayout();
  private final boolean isSandboxMode;
  
  // Branding configuration (whitelabel support)
  private final String brandName;
  private final String shortName;
  private final String logoUrl;
  private final String companyName;

  public MainLayout(AuthenticatedUser auth, AccountBalanceService balanceService,
                    @Value("${bridge.mode:sandbox}") String bridgeMode,
                    @Value("${app.branding.name:JHELY FinTech Framework}") String brandName,
                    @Value("${app.branding.short-name:FinTech}") String shortName,
                    @Value("${app.branding.logo-url:https://jhely.com/icons/jhelyLogoYellow.svg}") String logoUrl,
                    @Value("${app.branding.company-name}") String companyName) {
    this.auth = auth;
    this.balanceService = balanceService;
    this.isSandboxMode = "sandbox".equalsIgnoreCase(bridgeMode);
    this.brandName = brandName;
    this.shortName = shortName;
    this.logoUrl = logoUrl;
    this.companyName = companyName;

    setPrimarySection(Section.DRAWER);
    setDrawerOpened(true);

    createHeader();
    createDrawer();
    createFixedBottomBar();
    addContentBottomPaddingCSS();
    updateHeader();

    // 🔔 Listen for balance changes coming from anywhere in this UI (e.g., WalletView)
    ComponentUtil.addListener(UI.getCurrent(), BalanceChangedEvent.class, e -> updateHeader());
 
  }

  private void createDrawer() {
    SideNav nav = new SideNav();
    nav.addItem(new SideNavItem("Home", HomeView.class, VaadinIcon.HOME.create()));
    nav.addItem(new SideNavItem("Receipts", ReceiptsView.class, VaadinIcon.DASHBOARD.create()));

    SideNavItem finance = new SideNavItem("Finance", AccountsOverviewView.class, VaadinIcon.WALLET.create());

    SideNavItem overview = new SideNavItem("Overview", AccountsOverviewView.class);

    SideNavItem receive = new SideNavItem("Receive", ReceiveFundsView.class);

    SideNavItem send = new SideNavItem("Send", SendFundsView.class);

    SideNavItem tx = new SideNavItem("Transactions", TransactionsView.class);

    finance.addItem(overview, receive, send, tx);
    nav.addItem(finance);

    // x402 for machine-to-machine payments
    SideNavItem x402 = new SideNavItem("x402", X402DashboardView.class, VaadinIcon.CONNECT.create());
    x402.addItem(new SideNavItem("Dashboard", X402DashboardView.class));
    x402.addItem(new SideNavItem("Configuration", X402FacilitatorView.class));
    nav.addItem(x402);

    addToDrawer(nav);

    // Add sandbox mode indicator if in sandbox mode
    if (isSandboxMode) {
      Div sandboxBadge = new Div();
      sandboxBadge.getStyle()
          .set("margin", "16px")
          .set("padding", "8px 12px")
          .set("background", "linear-gradient(135deg, #ff9800 0%, #f57c00 100%)")
          .set("color", "white")
          .set("border-radius", "8px")
          .set("font-size", "12px")
          .set("font-weight", "bold")
          .set("text-align", "center")
          .set("text-transform", "uppercase")
          .set("letter-spacing", "1px")
          .set("box-shadow", "0 2px 8px rgba(255, 152, 0, 0.4)");
      
      Span icon = new Span("⚠ ");
      Span text = new Span("SANDBOX MODE");
      sandboxBadge.add(icon, text);
      sandboxBadge.getElement().setAttribute("title", "Bridge API is running in sandbox/test mode. No real money transactions.");
      
      addToDrawer(sandboxBadge);
    }
  }

  private void createHeader() {
    DrawerToggle toggle = new DrawerToggle();
    toggle.getElement().setProperty("aria-label", "Menu");

    Image logo = new Image(logoUrl, brandName + " logo");
    logo.setHeight("28px");
    logo.getStyle().set("filter",
        "invert(48%) sepia(72%) saturate(636%) hue-rotate(180deg) brightness(95%) contrast(95%)");

    var title = new H1(shortName);
    title.getStyle().set("font-size", "1.25rem");
    title.getStyle().set("margin", "0");

    var brand = new HorizontalLayout(logo, title);
    brand.setAlignItems(Alignment.CENTER);
    brand.setSpacing(true);

    right.setAlignItems(Alignment.CENTER);
    right.setSpacing(true);

    var header = new HorizontalLayout(toggle, brand, right);
    header.setWidthFull();
    header.setAlignItems(Alignment.CENTER);
    header.expand(brand);
    addToNavbar(header);
  }

  private void createFixedBottomBar() {
    Anchor privacy = new Anchor("https://example.com/privacy.pdf", "Privacy");
    privacy.setTarget("_blank");
    privacy.getElement().setAttribute("rel", "noopener");

    Anchor terms = new Anchor("https://example.com/terms.pdf", "Terms & Conditions");
    terms.setTarget("_blank");
    terms.getElement().setAttribute("rel", "noopener");

    Span copyright = new Span("© " + companyName);

    // Disclaimer using the configured app name
    Span disclaimer = new Span(brandName + " is not a bank. Bank accounts and payment services are provided by regulated financial institutions we partner with.");
    disclaimer.getStyle()
        .set("color", "var(--lumo-secondary-text-color)")
        .set("font-size", "var(--lumo-font-size-xs)")
        .set("text-align", "center");

    HorizontalLayout linksRow = new HorizontalLayout(privacy, terms, copyright);
    linksRow.setSpacing(true);
    linksRow.setAlignItems(Alignment.CENTER);
    linksRow.setJustifyContentMode(JustifyContentMode.CENTER);

    VerticalLayout bar = new VerticalLayout(linksRow, disclaimer);
    bar.setWidthFull();
    bar.setPadding(true);
    bar.setSpacing(false);
    bar.getStyle().set("gap", "4px");
    bar.setAlignItems(Alignment.CENTER);
    bar.setJustifyContentMode(JustifyContentMode.CENTER);
    bar.getStyle()
        .set("position", "fixed")
        .set("left", "0")
        .set("right", "0")
        .set("bottom", "0")
        .set("z-index", "1000")
        .set("background", "var(--lumo-base-color)")
        .set("border-top", "1px solid var(--lumo-contrast-10pct)")
        .set("box-shadow", "0 -2px 6px var(--lumo-shade-5pct)")
        .set("font-size", "var(--lumo-font-size-s)");

    UI.getCurrent().add(bar);
  }

  private void addContentBottomPaddingCSS() {
  // Reserve more space so the fixed bar never overlaps content
  // MainLayout.addContentBottomPaddingCSS()
    UI.getCurrent().getPage().executeJs(
      "let s=document.getElementById('app-bottom-padding');" +
      "if(!s){s=document.createElement('style');s.id='app-bottom-padding';document.head.appendChild(s);}"+
      ":root{--app-bottom-bar-height:64px}"
    );

  // UI.getCurrent().getPage().executeJs(
  //   "let s=document.getElementById('app-bottom-padding');" +
  //   "if(!s){s=document.createElement('style');s.id='app-bottom-padding';document.head.appendChild(s);}"+
  //   "s.textContent='vaadin-app-layout::part(content){padding-bottom:96px;}';"
  // );
}

  @Override
  public void afterNavigation(AfterNavigationEvent event) {
    updateHeader();
  }

  private void updateHeader() {
    right.removeAll();

    auth.get().ifPresentOrElse(user -> {
      BigDecimal usd = balanceService.getUsdBalanceFor(user.getEmail());
      Component usdBadge = balanceBadge(usd); // clickable

      var logout = new Button("Logout", VaadinIcon.SIGN_OUT.create());
      logout.addThemeVariants(ButtonVariant.LUMO_TERTIARY, ButtonVariant.LUMO_SMALL);
      logout.addClickListener(e -> {
        org.springframework.security.core.context.SecurityContextHolder.clearContext();
        auth.logout();
  UI.getCurrent().getPage().setLocation("/login");
      });

      right.add(usdBadge,
                new UserBadge(user, e -> {
                  org.springframework.security.core.context.SecurityContextHolder.clearContext();
                  auth.logout();
                  UI.getCurrent().getPage().setLocation("/login");
                }),
                logout);

    }, () -> {
      var cta = new Button("Log in / Register", VaadinIcon.SIGN_IN.create());
      cta.addClickListener(e -> UI.getCurrent().navigate(LoginRegisterView.class));
      right.add(cta);
    });
  }

  private Component balanceBadge(BigDecimal amount) {
    NumberFormat fmt = NumberFormat.getCurrencyInstance(Locale.US);
    Span badge = new Span(fmt.format(amount) + " USD");
    badge.getElement().getThemeList().add("badge small primary");
    badge.getStyle().set("cursor", "pointer").set("margin-inline-end", "0.5rem");
    badge.getElement().setProperty("title", "Open Wallet");
    badge.addClickListener(e -> UI.getCurrent().navigate(WalletView.class));
    return badge;
  }
}
