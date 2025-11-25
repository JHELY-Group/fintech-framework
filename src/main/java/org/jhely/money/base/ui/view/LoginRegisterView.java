package org.jhely.money.base.ui.view;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.context.HttpSessionSecurityContextRepository;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.VaadinRequest;
import com.vaadin.flow.server.VaadinResponse;
import com.vaadin.flow.server.VaadinService;
import com.vaadin.flow.server.VaadinServletRequest;
import com.vaadin.flow.server.VaadinServletResponse;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import org.jhely.money.base.security.AuthenticatedUser;
import org.jhely.money.base.service.AccountBalanceService;
import org.jhely.money.base.service.OtpService;
import org.jhely.money.base.service.UserService;

@Route(value = "login", layout = MainLayout.class)
@PageTitle("Log in")
@AnonymousAllowed
public class LoginRegisterView extends VerticalLayout {
  private final OtpService otpService;
  private final UserService userService;
  private final AccountBalanceService balanceService;
  private final AuthenticatedUser authenticatedUser;
  private final String demoLoginEmail;

  private final EmailField email = new EmailField("Email");
  private final TextField code = new TextField("One-time code");
  private final Button sendCode = new Button("Send code");
  private final Button verify = new Button("Verify & Continue");
  private final Button demoLogin = new Button("Demo Login");

  public LoginRegisterView(OtpService otpService, UserService userService,
                           AccountBalanceService balanceService, AuthenticatedUser authenticatedUser,
                           @Value("${demo.login.email:}") String demoLoginEmail) {
    this.otpService = otpService;
    this.userService = userService;
	this.balanceService = balanceService;
    this.authenticatedUser = authenticatedUser;
    this.demoLoginEmail = demoLoginEmail;

    setMaxWidth("420px");
    setAlignItems(Alignment.STRETCH);
    setSpacing(true);

    add(new H2("Log in"), new Paragraph("Enter your email; we will send you a one-time code."));

    email.setPlaceholder("you@example.com");
    email.setClearButtonVisible(true);

    code.setVisible(false);
    verify.setVisible(false);

  sendCode.addClickListener(e -> onSendCode());
  verify.addClickListener(e -> onVerify());
  demoLogin.addClickListener(e -> onDemoLogin());

    var form = new FormLayout(email, code);

    // Buttons inline; verify hidden until first code is sent.
    HorizontalLayout actions = new HorizontalLayout(sendCode, verify);
    actions.setSpacing(true);
    actions.setAlignItems(Alignment.BASELINE);

    // Show Demo Login only when property is set
    if (demoLoginEmail != null && !demoLoginEmail.isBlank()) {
      demoLogin.getElement().setProperty("title", "Log in instantly as demo user");
      actions.add(demoLogin);
    }

    add(form, actions);
  }

  private void onDemoLogin() {
    try {
      performLoginFlow(demoLoginEmail);
      Notification.show("Logged in as demo user").addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    } catch (Exception ex) {
      Notification.show("Demo login failed: " + (ex.getMessage() != null ? ex.getMessage() : "Unknown error"))
          .addThemeVariants(NotificationVariant.LUMO_ERROR);
    }
  }

  private void onSendCode() {
    if (email.isInvalid() || email.getValue().isBlank()) {
      email.setInvalid(true);
      email.setErrorMessage("Please enter a valid email");
      return;
    }
    otpService.sendOtp(email.getValue());
    code.setVisible(true);
    verify.setVisible(true);
    // Rename the button after first (and subsequent) sends
    sendCode.setText("Send code again");
    Notification.show("We sent a 6-digit code to your email").addThemeVariants(NotificationVariant.LUMO_PRIMARY);
  }

  private void onVerify() {
    var mail = email.getValue();
    var c = code.getValue();
    if (c == null || c.isBlank()) {
      code.setInvalid(true);
      code.setErrorMessage("Enter the code from your email");
      return;
    }
    boolean ok = otpService.verifyOtp(mail, c.trim());
    if (!ok) {
      Notification.show("Invalid or expired code").addThemeVariants(NotificationVariant.LUMO_ERROR);
      return;
    }

  // Keep profile name empty
  var user = userService.upsertForEmail(mail, "");

	// One-time welcome credit: 0.10 USD if the user has no USD row yet
    boolean granted = balanceService.grantWelcomeUsdIfAbsent(user.getEmail());
    if (granted) {
      Notification.show("Welcome! We've credited your account with $0.10 USD.")
          .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }

    performSecurityLogin(user.getEmail());
    UI.getCurrent().navigate(HomeView.class);
  }

  private void performLoginFlow(String emailAddress) {
    // Create/ensure user exists
    var user = userService.upsertForEmail(emailAddress, "");
    // One-time welcome credit: 0.10 USD if the user has no USD row yet
    boolean granted = balanceService.grantWelcomeUsdIfAbsent(user.getEmail());
    if (granted) {
      Notification.show("Welcome! We've credited your account with $0.10 USD.")
          .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
    }
    performSecurityLogin(user.getEmail());
    UI.getCurrent().navigate(HomeView.class);
  }

  private void performSecurityLogin(String principalEmail) {
    var authorities = java.util.List.<GrantedAuthority>of(new SimpleGrantedAuthority("ROLE_USER"));
    var authentication = new UsernamePasswordAuthenticationToken(principalEmail, null, authorities);

    VaadinService.reinitializeSession(VaadinRequest.getCurrent());

    SecurityContext context = SecurityContextHolder.createEmptyContext();
    context.setAuthentication(authentication);
    SecurityContextHolder.setContext(context);

    var req = ((VaadinServletRequest) VaadinRequest.getCurrent()).getHttpServletRequest();
    var res = ((VaadinServletResponse) VaadinResponse.getCurrent()).getHttpServletResponse();
    new HttpSessionSecurityContextRepository().saveContext(context, req, res);

    authenticatedUser.set(principalEmail);
  }
}
