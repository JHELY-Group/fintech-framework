package org.jhely.money.base.api;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.annotation.security.PermitAll;
import org.jhely.money.base.security.AuthenticatedUser;
import org.jhely.money.base.service.StripeService;

@RestController
@RequestMapping("/api/stripe")
public class StripeController {

  private final StripeService stripe;
  private final AuthenticatedUser auth;
  private static final Logger log = LoggerFactory.getLogger(StripeController.class);

  public StripeController(StripeService stripe, AuthenticatedUser auth) {
    this.stripe = stripe;
    this.auth = auth;
  }

  @PostMapping("/checkout-session")
  public ResponseEntity<String> createSession(@RequestParam("amountUsd") String amountUsd,
                                              HttpServletRequest request) {
    var user = auth.get().orElseThrow(() -> new IllegalStateException("Not authenticated"));
    String email = user.getEmail();
    // parse amount; ensure >= 5 USD
    long cents;
    try {
      var normalized = amountUsd == null ? null : amountUsd.replace(",", "").trim();
      java.math.BigDecimal bd = new java.math.BigDecimal(normalized);
      if (bd.compareTo(new java.math.BigDecimal("5")) < 0) {
        log.warn("Stripe checkout rejected: amount too low. email={}, amountUsd={}", email, amountUsd);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Minimum top-up is $5");
      }
      cents = bd.movePointRight(2).setScale(0, java.math.RoundingMode.HALF_UP).longValueExact();
    } catch (Exception e) {
      log.warn("Stripe checkout rejected: invalid amount. email={}, amountUsd={}", email, amountUsd);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Invalid amount");
    }

    String origin = request.getScheme() + "://" + request.getServerName()
        + ((request.getServerPort() == 80 || request.getServerPort() == 443) ? "" : (":" + request.getServerPort()));
    String successUrl = origin + "/wallet";
    String cancelUrl = origin + "/wallet";

    try {
      String url = stripe.createCheckoutSession(email, cents, successUrl, cancelUrl);
      log.info("Stripe checkout created: email={}, cents={}, successUrl={} -> sessionUrl returned", email, cents, successUrl);
      return ResponseEntity.ok(url);
    } catch (IllegalArgumentException ex) {
      log.warn("Stripe checkout failed: {} (email={}, cents={})", ex.getMessage(), email, cents);
      return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ex.getMessage());
    } catch (Exception ex) {
      log.error("Stripe checkout error for email={}, cents={}", email, cents, ex);
      return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Stripe error: " + ex.getMessage());
    }
  }

  @PermitAll
  @PostMapping("/webhook")
  public ResponseEntity<String> webhook(@RequestBody String payload,
                                        @RequestHeader(name = "Stripe-Signature", required = false) String sigHeader) {
    stripe.handleWebhook(payload, sigHeader);
    return ResponseEntity.ok("ok");
  }
}
