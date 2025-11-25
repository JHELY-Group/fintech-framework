package org.jhely.money.base.ui.view;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Locale;

import jakarta.annotation.security.RolesAllowed;

 

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;

import org.jhely.money.base.domain.AccountTransaction;
import org.jhely.money.base.domain.UserAccount;

import org.jhely.money.base.repository.AccountTransactionRepository;
import org.jhely.money.base.repository.UserAccountRepository;
import org.jhely.money.base.security.AuthenticatedUser;
import org.jhely.money.base.service.AccountBalanceService;
import org.jhely.money.base.service.CouponService;
import org.jhely.money.base.ui.view.MainLayout;
import org.jhely.money.base.ui.view.events.BalanceChangedEvent;
import com.vaadin.flow.server.VaadinService;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RolesAllowed("USER")
@PageTitle("Wallet")
@Route(value = "wallet", layout = MainLayout.class)
public class WalletView extends VerticalLayout {

  private final AuthenticatedUser auth;
  private final AccountBalanceService balances;
  private final CouponService coupons;
  private final UserAccountRepository users;
  private final AccountTransactionRepository txRepo;

  private final Paragraph balanceText = new Paragraph();
  private final Grid<AccountTransaction> grid = new Grid<>(AccountTransaction.class, false);
  private final TextField couponField = new TextField("TOPUP COUPON");
  private final Button redeemBtn = new Button("Redeem");
  private final BigDecimalField amountField = new BigDecimalField("Top-up amount (USD)");
  private final Button stripeBtn = new Button("Top up with Stripe");
  private static final Logger log = LoggerFactory.getLogger(WalletView.class);

  private final NumberFormat moneyFmt = NumberFormat.getCurrencyInstance(Locale.US);
  private final DateTimeFormatter timeFmt =
      DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm").withZone(ZoneId.systemDefault());
  public WalletView(AuthenticatedUser auth,
                    AccountBalanceService balances,
                    CouponService coupons,
                    UserAccountRepository users,
                    AccountTransactionRepository txRepo,
                    org.jhely.money.base.service.StripeService stripeService) {
    this.auth = auth;
    this.balances = balances;
    this.coupons = coupons;
    this.users = users;
    this.txRepo = txRepo;
    this.stripeService = stripeService;

    setPadding(true);
    setSpacing(true);
    setWidthFull();
    getStyle().set("padding-bottom", "96px"); // avoid overlap with fixed bottom bar

    add(new H2("Wallet"));

    // Balance line
    balanceText.getStyle().set("font-weight", "600");
    add(balanceText);

  // Coupon form (above grid)
    couponField.setPlaceholder("Enter coupon code");
    couponField.setClearButtonVisible(true);
    couponField.setWidth("260px");

    redeemBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
    redeemBtn.addClickListener(e -> onRedeem());

  // Min validation handled in click handler
  amountField.setPlaceholder("min $5");
  amountField.setClearButtonVisible(true);
  amountField.setWidth("160px");

  stripeBtn.addThemeVariants(ButtonVariant.LUMO_SUCCESS);
  stripeBtn.addClickListener(e -> onStripeTopup());

  HorizontalLayout form = new HorizontalLayout(couponField, redeemBtn, amountField, stripeBtn);
  form.setAlignItems(Alignment.BASELINE);
  add(form);

    // Transactions grid
    setupGrid();
    add(grid);

    refresh();
  }

  private void setupGrid() {
    grid.addColumn(tx -> timeFmt.format(tx.getCreatedAt())).setHeader("Time").setAutoWidth(true);
    grid.addColumn(AccountTransaction::getType).setHeader("Type").setAutoWidth(true);
    grid.addColumn(AccountTransaction::getCurrency).setHeader("CCY").setAutoWidth(true);
    grid.addColumn(tx -> moneyFmt.format(tx.getAmount())).setHeader("Amount").setAutoWidth(true);
    grid.addColumn(AccountTransaction::getNote).setHeader("Note").setFlexGrow(1);
    grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
    grid.setAllRowsVisible(true);
  }

  private void refresh() {
    auth.get().ifPresentOrElse(user -> {
      String email = user.getEmail();
      BigDecimal usd = balances.getUsdBalanceFor(email);
      balanceText.setText("USD Balance: " + moneyFmt.format(usd));

      UserAccount ua = users.findByEmail(email).orElse(null);
      List<AccountTransaction> txs = (ua == null)
          ? List.of()
          : txRepo.findByUserOrderByCreatedAtDesc(ua);
      grid.setItems(txs);

    }, () -> UI.getCurrent().navigate(LoginRegisterView.class));
  }

  private void onRedeem() {
    String code = couponField.getValue();
    if (code == null || code.trim().isEmpty()) {
      Notification.show("Please enter a coupon code").addThemeVariants(NotificationVariant.LUMO_ERROR);
      return;
    }
    try {
      String email = auth.get().map(UserAccount::getEmail).orElseThrow();
      coupons.redeemCoupon(email, code.trim());
      Notification.show("Coupon applied!").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
      couponField.clear();
      refresh();

      // 🔔 Tell MainLayout to refresh the top-right balance badge
      ComponentUtil.fireEvent(UI.getCurrent(), new BalanceChangedEvent(UI.getCurrent(), false));

    } catch (IllegalArgumentException | IllegalStateException ex) {
      Notification.show(ex.getMessage()).addThemeVariants(NotificationVariant.LUMO_ERROR);
    } catch (Exception ex) {
      Notification.show("Unexpected error redeeming coupon").addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

  private void onStripeTopup() {
    var amt = amountField.getValue();
    if (amt == null || amt.compareTo(new java.math.BigDecimal("5")) < 0) {
      Notification.show("Minimum top-up is $5").addThemeVariants(NotificationVariant.LUMO_ERROR);
      return;
    }
    try {
      // Create Checkout Session directly via service (avoids auth/cookie issues on server->server HTTP)
      String email = auth.get().map(UserAccount::getEmail).orElseThrow();
      long cents = amt.movePointRight(2).setScale(0, java.math.RoundingMode.HALF_UP).longValueExact();

      HttpServletRequest srvReq = (HttpServletRequest) VaadinService.getCurrentRequest();
      String origin = srvReq.getScheme() + "://" + srvReq.getServerName()
          + ((srvReq.getServerPort() == 80 || srvReq.getServerPort() == 443) ? "" : ":" + srvReq.getServerPort());
      String successUrl = origin + "/wallet";
      String cancelUrl = origin + "/wallet";

      String url = stripeService.createCheckoutSession(email, cents, successUrl, cancelUrl);
      log.info("Stripe Checkout initiated from WalletView: email={}, amountUsd={}, sessionUrlPresent={}",
          email, amt, (url != null && !url.isBlank()));
      UI.getCurrent().getPage().setLocation(url);
    } catch (Exception ex) {
      Notification.show("Failed to start Stripe checkout: " + ex.getMessage())
          .addThemeVariants(NotificationVariant.LUMO_ERROR);
      log.error("Stripe checkout initiation failed in WalletView", ex);
    }
  }
  
  // Injected Stripe service
  private final org.jhely.money.base.service.StripeService stripeService;
}
