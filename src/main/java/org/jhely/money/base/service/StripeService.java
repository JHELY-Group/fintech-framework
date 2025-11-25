package org.jhely.money.base.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.stripe.Stripe;
import com.stripe.exception.SignatureVerificationException;
import com.stripe.model.Event;
import com.stripe.model.EventDataObjectDeserializer;
import com.stripe.model.checkout.Session;
import com.stripe.net.Webhook;
import com.stripe.param.checkout.SessionCreateParams;

import org.jhely.money.base.domain.AccountTransaction;
import org.jhely.money.base.domain.AccountTransactionType;
import org.jhely.money.base.domain.UserAccount;
import org.jhely.money.base.repository.AccountTransactionRepository;
import org.jhely.money.base.repository.UserAccountRepository;

@Service
public class StripeService {

  private final AccountBalanceService balances;
  private final UserAccountRepository users;
  private final AccountTransactionRepository txRepo;

  private final String webhookSecret;

  public StripeService(
      AccountBalanceService balances,
      UserAccountRepository users,
      AccountTransactionRepository txRepo,
      @Value("${stripe.api-key:}") String apiKey,
      @Value("${stripe.webhook-secret:}") String webhookSecret
  ) {
    this.balances = balances;
    this.users = users;
    this.txRepo = txRepo;
    this.webhookSecret = webhookSecret;
    if (apiKey != null && !apiKey.isBlank()) {
      Stripe.apiKey = apiKey;
    }
  }

  /**
   * Create a Stripe Checkout Session for a top-up.
   * @param email user email (client_reference_id)
   * @param amountCents minimal 500 (=$5)
   * @param successUrl absolute URL to redirect after success
   * @param cancelUrl absolute URL to redirect if canceled
   * @return the hosted session URL
   */
  public String createCheckoutSession(String email, long amountCents, String successUrl, String cancelUrl) {
    if (amountCents < 500) {
      throw new IllegalArgumentException("Minimum top-up is $5");
    }
    SessionCreateParams params = SessionCreateParams.builder()
        .setMode(SessionCreateParams.Mode.PAYMENT)
        .setSuccessUrl(successUrl)
        .setCancelUrl(cancelUrl)
        .setClientReferenceId(email.toLowerCase())
        .addLineItem(
            SessionCreateParams.LineItem.builder()
                .setQuantity(1L)
                .setPriceData(SessionCreateParams.LineItem.PriceData.builder()
                    .setCurrency("usd")
                    .setUnitAmount(amountCents)
                    .setProductData(
                        SessionCreateParams.LineItem.PriceData.ProductData.builder()
                            .setName("Account top-up")
                            .build())
                    .build())
                .build())
        .putAllMetadata(metadata("user", email))
        .build();
    try {
      Session session = Session.create(params);
      return session.getUrl();
    } catch (Exception e) {
      throw new RuntimeException("Failed to create Stripe session", e);
    }
  }

  private static Map<String, String> metadata(String k, String v) {
    HashMap<String, String> m = new HashMap<>();
    m.put(k, v);
    return m;
  }

  /**
   * Handle webhook events and credit user balance on successful payment.
   */
  @Transactional
  public void handleWebhook(String payload, String sigHeader) {
    if (webhookSecret == null || webhookSecret.isBlank()) {
      throw new IllegalStateException("Stripe webhook secret not configured");
    }
    Event event;
    try {
      event = Webhook.constructEvent(payload, sigHeader, webhookSecret);
    } catch (SignatureVerificationException e) {
      throw new IllegalArgumentException("Invalid Stripe signature", e);
    }

    // We primarily process checkout.session.completed
    if ("checkout.session.completed".equals(event.getType())) {
      EventDataObjectDeserializer deserializer = event.getDataObjectDeserializer();
      Session session = (Session) deserializer.getObject().orElse(null);
      if (session == null) return;

      String email = session.getClientReferenceId();
      Long total = session.getAmountTotal(); // amount paid in cents
      String sessionId = session.getId();
      if (email == null || total == null || total <= 0) return;

      // Idempotency: avoid double-crediting by note
      String note = "stripe: " + sessionId;
      if (txRepo.existsByNote(note)) return;

      // Credit wallet and record transaction
      BigDecimal amount = new BigDecimal(total).movePointLeft(2); // cents -> dollars
      balances.creditUsd(email, amount);

      UserAccount user = users.findByEmail(email).orElseThrow();
      AccountTransaction tx = new AccountTransaction(
          user,
          "USD",
          AccountTransactionType.TOPUP_STRIPE,
          amount,
          note
      );
      txRepo.save(tx);
    }
  }
}
