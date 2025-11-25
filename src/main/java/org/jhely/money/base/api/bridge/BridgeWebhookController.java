package org.jhely.money.base.api.bridge;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jhely.money.base.service.payments.BridgeOnboardingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import jakarta.annotation.security.PermitAll;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

import java.security.KeyFactory;
import java.security.MessageDigest;
import java.security.PublicKey;
import java.security.Signature;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.time.Instant;

/**
 * Receives Bridge webhooks and updates local state for customers/KYC.
 * Note: Signature verification to be added (todo #9). For now, we log header and proceed.
 */
@RestController
public class BridgeWebhookController {

    private static final Logger log = LoggerFactory.getLogger(BridgeWebhookController.class);

    private final ObjectMapper mapper;
    private final BridgeOnboardingService onboardingService;
    private final org.jhely.money.base.service.payments.BridgeWebhookKeyProvider keyProvider;

    public BridgeWebhookController(ObjectMapper mapper,
                                   BridgeOnboardingService onboardingService,
                                   org.jhely.money.base.service.payments.BridgeWebhookKeyProvider keyProvider) {
        this.mapper = mapper;
        this.onboardingService = onboardingService;
        this.keyProvider = keyProvider;
    }

    @PostMapping("/api/bridge/webhook2")
    @PermitAll
    public ResponseEntity<String> webhook(@RequestBody byte[] payloadBytes,
                                          @RequestHeader(value = "x-webhook-signature", required = false) String signature,
                                          HttpServletRequest request) {
        String payload = new String(payloadBytes, java.nio.charset.StandardCharsets.UTF_8);
        boolean hasSig = signature != null && !signature.isBlank();
        // Entry log (avoid sensitive data, redact signature; debug can include truncated payload)
        log.info("Bridge webhook: received from={} bytes={} sigPresent={}",
                request != null ? request.getRemoteAddr() : "?",
                payloadBytes.length,
                hasSig);
        if (log.isDebugEnabled()) {
            log.debug("Bridge webhook payload: {}", truncate(payload, 4000));
            if (request != null) {
                try {
                    java.util.Enumeration<String> names = request.getHeaderNames();
                    StringBuilder sb = new StringBuilder("bridgeHeaders=[");
                    boolean first = true;
                    while (names != null && names.hasMoreElements()) {
                        String n = names.nextElement();
                        String v = truncate(request.getHeader(n), 200);
                        // Redact signature value except last 6 chars
                        if ("Bridge-Signature".equalsIgnoreCase(n) && v != null) {
                            int len = v.length();
                            v = "***" + (len > 6 ? v.substring(len - 6) : v);
                        }
                        if (!first) sb.append(", ");
                        first = false;
                        sb.append(n).append("=").append(v);
                    }
                    sb.append("]");
                    log.debug("Bridge webhook request headers: {}", sb);
                } catch (Exception ignore) { }
            }
        }

        // Signature handling: only enforce verification if a signature header is present.
        if (hasSig) {
            if (!verify(signature, payload)) {
                log.warn("Bridge webhook: invalid signature (redactedSig={})", redactSig(signature));
                return ResponseEntity.status(401).body("invalid signature");
            }
            log.info("Bridge webhook: signature ok at={}", Instant.now());
        } else {
            // Simulation or legacy unsigned event – proceed but warn.
            log.warn("Bridge webhook: missing signature; processing unsigned event (consider configuring public key or secret)");
        }
        try {
            JsonNode root = mapper.readTree(payloadBytes);
            // Bridge current envelope uses event_* fields and event_object.
            String type = firstNonEmpty(
                    text(root, "type"),
                    text(root, "event_type")
            );
            JsonNode data = root.get("data");
            if (data == null) data = root.get("object");
            if (data == null) data = root.get("event_object"); // Bridge webhook v0 current shape

            // Fallback: some events may place nested customer under event_object.customer
            JsonNode customerNode = null;
            if (data != null) {
                customerNode = data.get("customer");
            }

            // Attempt to extract customer id and statuses from multiple possible shapes
            String customerId = firstNonEmpty(
                    text(data, "customer_id"),
                    text(root, "event_object_id"), // may align with customer id for certain categories
                    text(data, "customer_id"),
                    text(data, "id"),
                    text(root, "customer_id")
            );
            
            String email = firstNonEmpty(
                    text(data, "email"),
                    text(customerNode, "email"),
                    textNode(path(data, "customer", "email"))
            );

                String customerStatus = firstNonEmpty(
                    text(data, "customer_status"),
                    text(customerNode, "status"),
                    text(data, "status"),
                    textNode(path(data, "customer", "status"))
                );
            // KYC specific
                String kycStatus = firstNonEmpty(
                    text(data, "kyc_status"),
                    text(data, "kyc_status"),
                    textNode(path(data, "kyc", "status")),
                    textNode(path(data, "review", "status"))
                );
                String rejectionReason = firstNonEmpty(
                    text(data, "rejection_reason"),
                    text(data, "rejection_reason"),
                    textNode(path(data, "kyc", "rejection_reason")),
                    textNode(path(data, "review", "reason"))
                );

            if (customerId == null || customerId.isBlank()) {
                log.warn("Webhook missing customer id, payload={}", payload);
                return ResponseEntity.ok("ignored");
            }

            onboardingService.updateKycFromWebhook(customerId, email, customerStatus, kycStatus, rejectionReason, payload);
            log.info("Webhook processed type={} customerId={} status={} kycStatus={}", type, customerId, customerStatus, kycStatus);
            return ResponseEntity.ok("ok");
        } catch (Exception ex) {
            log.error("Failed to process webhook: {}", ex.getMessage(), ex);
            return ResponseEntity.badRequest().body("error");
        }
    }

    private static String text(JsonNode node, String field) {
        if (node == null || field == null) return null;
        JsonNode n = node.get(field);
        return (n != null && !n.isNull()) ? n.asText() : null;
    }

    private static String textNode(JsonNode node) {
        return (node != null && !node.isNull()) ? node.asText() : null;
    }

    private static JsonNode path(JsonNode node, String... fields) {
        if (node == null) return null;
        JsonNode cur = node;
        for (String f : fields) {
            if (cur == null) return null;
            cur = cur.get(f);
        }
        return cur;
    }

    private static String firstNonEmpty(String... values) {
        if (values == null) return null;
        for (String v : values) {
            if (v != null && !v.isBlank()) return v;
        }
        return null;
    }

    // Verify signature using Bridge webhook reference implementation
    private boolean verify(String signatureHeader, String payload) {
        try {
            // Parse signature header
            String[] parts = signatureHeader.split(",");
            String timestamp = null;
            String signature = null;

            for (String part : parts) {
                if (part.startsWith("t=")) {
                    timestamp = part.substring(2);
                } else if (part.startsWith("v0=")) {
                    signature = part.substring(3);
                }
            }

            if (timestamp == null || signature == null) {
                log.warn("Bridge webhook: Missing timestamp or signature");
                return false;
            }

            // Check timestamp (reject events older than 10 minutes)
            long currentTime = System.currentTimeMillis();
            long eventTime = Long.parseLong(timestamp);
            // Bridge timestamps are usually seconds, but reference implies ms (600000 check).
            // If Bridge sends seconds, this check will fail (seconds < ms).
            // However, we must follow the reference implementation exactly as requested.
            if (currentTime - eventTime > 600000) {
                log.warn("Bridge webhook: Timestamp too old");
                return false;
            }

            // Create signed payload
            String signedPayload = timestamp + "." + payload;

            // Parse public key
            String pem = keyProvider != null ? keyProvider.getPublicKeyPem() : null;
            if (pem == null) {
                 log.warn("Bridge webhook: No public key available");
                 return false;
            }
            
            String publicKeyContent = pem
                .replace("-----BEGIN PUBLIC KEY-----", "")
                .replace("-----END PUBLIC KEY-----", "")
                .replaceAll("\\s", "");

            byte[] keyBytes = Base64.getDecoder().decode(publicKeyContent);
            X509EncodedKeySpec spec = new X509EncodedKeySpec(keyBytes);
            KeyFactory keyFactory = KeyFactory.getInstance("RSA");
            PublicKey publicKey = keyFactory.generatePublic(spec);

            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] dataDigest = digest.digest(signedPayload.getBytes(java.nio.charset.StandardCharsets.UTF_8));

            // Verify signature
            Signature sig = Signature.getInstance("SHA256withRSA");
            sig.initVerify(publicKey);
            sig.update(dataDigest);

            byte[] signatureBytes = Base64.getDecoder().decode(signature);
            boolean isValid = sig.verify(signatureBytes);
            
            if (!isValid) {
                log.warn("Bridge webhook: Signature verification failed (crypto mismatch)");
            }
            return isValid;

        } catch (Exception e) {
            log.warn("Bridge webhook: Signature verification failed with exception: {}", e.getMessage());
            return false;
        }
    }

    private static String truncate(String s, int max) {
        if (s == null) return null;
        if (s.length() <= max) return s;
        return s.substring(0, Math.max(0, max)) + "…";
    }

    private static String redactSig(String sig) {
        if (sig == null || sig.isBlank()) return "";
        int n = sig.length();
        return "***" + sig.substring(Math.max(0, n - 6));
    }
}
