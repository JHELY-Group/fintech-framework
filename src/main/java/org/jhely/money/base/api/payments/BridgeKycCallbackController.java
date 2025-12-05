package org.jhely.money.base.api.payments;

import org.jhely.money.base.service.payments.BridgeOnboardingService;
import org.jhely.money.base.service.CustomerService;
import org.jhely.money.base.domain.BridgeCustomer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import java.net.URI;
import java.util.Map;

import org.springframework.web.client.RestClientResponseException;

/**
 * Callback endpoint for hosted KYC flow that creates a new Bridge customer.
 * Redirect URL configured as /api/bridge/kyc-callback. Bridge should append query params such as customer_id.
 * We fetch the created customer from Bridge and persist it locally if not already present.
 */
@RestController
@RequestMapping("/api/bridge")
public class BridgeKycCallbackController {

    private static final Logger log = LoggerFactory.getLogger(BridgeKycCallbackController.class);

    private final BridgeOnboardingService onboarding;
    private final CustomerService customerService;

    public BridgeKycCallbackController(BridgeOnboardingService onboarding, CustomerService customerService) {
        this.onboarding = onboarding;
        this.customerService = customerService;
    }

    @GetMapping("/kyc-callback")
    public ResponseEntity<String> kycCallback(@RequestParam Map<String,String> params) {
        // Bridge/Persona redirect may NOT include customer_id; hosted flow often passes reference-id (same as customer id) instead
        String customerId = firstNonBlank(
                params.get("customer_id"),
                params.get("id"),
                params.get("reference-id"),
                params.get("reference_id")
        );
        // status currently unused; could be leveraged later for UI messaging
        String error = params.get("error");

        Authentication auth = SecurityContextHolder.getContext() != null ? SecurityContextHolder.getContext().getAuthentication() : null;
        String userId = auth != null ? auth.getName() : "anonymous";
        String email = auth != null ? auth.getName() : "anonymous@example.com";

        if (error != null) {
            log.warn("KYC callback error userId={} error={} params={}", userId, error, params);
            return redirectWithMessage("/finance", "KYC flow error: " + error);
        }
        if (customerId == null || customerId.isBlank()) {
            // We cannot fetch yet; rely on upcoming webhook events to create the record
            log.info("KYC callback without customer id yet (userId={}) params={}, will await webhook", userId, params);
            return redirectWithMessage("/finance", "Verification submitted. Awaiting Bridge to finalize (webhook pending). Refresh shortly.");
        }
        try {
            var remote = customerService.getCustomer(customerId);
            BridgeCustomer bc = onboarding.persistFetched(userId, email, remote);
            log.info("KYC callback persisted customer userId={} bridgeId={} status={}", userId, bc.getBridgeCustomerId(), bc.getStatus());
            return redirectWithMessage("/finance", "Bridge customer created via hosted KYC.");
        } catch (RestClientResponseException rex) {
            // If Bridge has not yet materialized the customer (race with webhook), treat as pending rather than error
            int sc = rex.getStatusCode().value();
            if (sc == 404 || sc == 400) {
                log.info("KYC callback customer not yet available (status={}) customerId={} userId={} will await webhook", sc, customerId, userId);
                return redirectWithMessage("/finance", "KYC verified. Customer provisioning in progress (webhook). Refresh soon.");
            }
            log.error("KYC callback fetch failed userId={} customerId={} status={} body={}", userId, customerId, sc, rex.getResponseBodyAsString());
            return redirectWithMessage("/finance", "Failed to fetch Bridge customer (" + sc + ").");
        } catch (Exception ex) {
            log.error("KYC callback failed userId={} customerId={} msg={}", userId, customerId, ex.getMessage(), ex);
            return redirectWithMessage("/finance", "Unexpected error after KYC.");
        }
    }

    private ResponseEntity<String> redirectWithMessage(String path, String msg) {
        // Log the transient message so it’s available after redirect
        log.info("KYC callback redirect path={} msg={}", path, msg);
        // Lightweight HTML response with meta refresh to maintain simplicity (Vaadin view will show status after refresh)
        String html = "<html><head><meta http-equiv='refresh' content='0;url=" + path + "'/></head><body>" + msg + "</body></html>";
        return ResponseEntity.status(HttpStatus.OK).location(URI.create(path)).body(html);
    }

    private String firstNonBlank(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }
}
