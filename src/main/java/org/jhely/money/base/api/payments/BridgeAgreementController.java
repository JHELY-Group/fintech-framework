package org.jhely.money.base.api.payments;

import java.security.Principal;
import java.util.Map;

import jakarta.annotation.security.PermitAll;
import org.jhely.money.base.service.payments.BridgeAgreementService;
import org.jhely.money.base.service.payments.BridgeAgreementBroadcaster;
import org.jhely.money.base.domain.BridgeAgreement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bridge")
public class BridgeAgreementController {

    private static final Logger log = LoggerFactory.getLogger(BridgeAgreementController.class);
    private final BridgeAgreementService agreements;
    private final BridgeAgreementBroadcaster broadcaster;
    public BridgeAgreementController(BridgeAgreementService agreements, BridgeAgreementBroadcaster broadcaster) {
        this.agreements = agreements;
        this.broadcaster = broadcaster;
    }

    /** Callback endpoint that Bridge redirects to after ToS acceptance. */
    @PermitAll
    @GetMapping("/tos-callback")
    public ResponseEntity<String> tosCallback(@RequestParam Map<String,String> params, Principal principal) {
        String agreementId = params.getOrDefault("signed_agreement_id", params.get("agreement_id"));
        if (agreementId == null || agreementId.isBlank()) {
            log.warn("ToS callback missing agreement id. Params={}", params);
            return ResponseEntity.badRequest().body("Missing agreement id");
        }
        String userId = principal != null ? principal.getName() : "anonymous";
        String email = userId; // Assuming username=email in current security model
    BridgeAgreement saved = agreements.saveAgreement(userId, email, agreementId);
    log.info("Captured signed agreement id {} for user {}", agreementId, userId);
    // Broadcast to any active views for this user.
    broadcaster.broadcast(saved);
        // Simple HTML response guiding user back
        String html = "<html><body style='font-family:sans-serif'><h3>Terms accepted</h3>" +
                "<p>Your agreement has been recorded. You may now <a href='/payments'>return to Payments</a> to create your Bridge customer.</p></body></html>";
        return ResponseEntity.ok(html);
    }
}
