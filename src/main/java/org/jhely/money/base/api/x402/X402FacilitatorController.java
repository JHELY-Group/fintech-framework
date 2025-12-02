package org.jhely.money.base.api.x402;

import org.jhely.money.base.domain.X402FacilitatorConfig;
import org.jhely.money.base.service.x402.X402FacilitatorService;
import org.jhely.money.base.service.x402.X402Models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

/**
 * REST controller exposing x402 protocol endpoints.
 * Compatible with x402-express, x402-next, and other x402 clients.
 * 
 * <p>
 * Endpoints per x402 spec:
 * <ul>
 * <li>GET /api/x402/ - Root endpoint (returns facilitator info)</li>
 * <li>GET /api/x402/supported - Get supported networks and capabilities (kinds)</li>
 * <li>POST /api/x402/verify - Verify a payment payload</li>
 * <li>POST /api/x402/settle - Settle (submit) a verified payment</li>
 * <li>GET /api/x402/transaction/{txHash} - Get transaction status</li>
 * <li>GET /api/x402/health - Health check</li>
 * </ul>
 * 
 * @see <a href="https://x402.org">x402 Protocol</a>
 * @see <a href="https://www.npmjs.com/package/x402-express">x402-express</a>
 * @see <a href="https://www.npmjs.com/package/x402-next">x402-next</a>
 */
@RestController
@RequestMapping("/api/x402")
public class X402FacilitatorController {

    private static final Logger log = LoggerFactory.getLogger(X402FacilitatorController.class);
    private static final String FACILITATOR_VERSION = "1.0.0";

    private final X402FacilitatorService facilitatorService;

    public X402FacilitatorController(X402FacilitatorService facilitatorService) {
        this.facilitatorService = facilitatorService;
    }

    /**
     * Root endpoint - returns facilitator info (no auth required).
     */
    @GetMapping({"", "/"})
    public ResponseEntity<?> root() {
        return ResponseEntity.ok(Map.of(
                "name", "x402-facilitator",
                "version", FACILITATOR_VERSION,
                "protocol", "x402",
                "x402Version", 1
        ));
    }

    /**
     * Verify a payment payload against requirements.
     * Called by resource servers before granting access.
     * 
     * Request body per x402 spec:
     * {
     *   "x402Version": 1,
     *   "paymentHeader": "base64-encoded-X-PAYMENT-header",
     *   "paymentRequirements": { ... }
     * }
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @RequestBody VerifyRequest request) {

        var configOpt = facilitatorService.validateApiKey(apiKey);
        if (configOpt.isEmpty()) {
            log.warn("x402 verify: Invalid or missing API key");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("isValid", false, "invalidReason", "Invalid or missing API key"));
        }

        X402FacilitatorConfig config = configOpt.get();

        try {
            VerifyResponse response = facilitatorService.verify(config, request);
            // Always return 200 for verify - use isValid to indicate result
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Verification error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("isValid", false, "invalidReason", "Internal error: " + e.getMessage()));
        }
    }

    /**
     * Settle (submit) a verified payment to Solana.
     * Called by resource servers after successful verification.
     * 
     * Request body per x402 spec (same as verify):
     * {
     *   "x402Version": 1,
     *   "paymentHeader": "...",
     *   "paymentRequirements": { ... }
     * }
     * 
     * Response per x402 spec:
     * {
     *   "success": true,
     *   "error": null,
     *   "txHash": "5a...solana_sig",
     *   "networkId": "solana-devnet"
     * }
     */
    @PostMapping("/settle")
    public ResponseEntity<?> settle(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @RequestBody SettleRequest request) {

        var configOpt = facilitatorService.validateApiKey(apiKey);
        if (configOpt.isEmpty()) {
            log.warn("x402 settle: Invalid or missing API key");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("success", false, "error", "Invalid or missing API key"));
        }

        X402FacilitatorConfig config = configOpt.get();

        try {
            SettleResponse response = facilitatorService.settle(config, request);
            // Always return 200 for settle - use success to indicate result
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            log.error("Settlement error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("success", false, "error", "Internal error: " + e.getMessage()));
        }
    }

    /**
     * Get supported networks and capabilities for this facilitator.
     * Called by clients to discover what this facilitator supports.
     * 
     * Response per x402 spec:
     * {
     *   "kinds": [
     *     { "scheme": "exact", "network": "solana-devnet" },
     *     { "scheme": "exact", "network": "solana" }
     *   ]
     * }
     */
    @GetMapping("/supported")
    public ResponseEntity<?> supported(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey) {

        var configOpt = facilitatorService.validateApiKey(apiKey);
        if (configOpt.isEmpty()) {
            log.warn("x402 supported: Invalid or missing API key");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or missing API key"));
        }

        X402FacilitatorConfig config = configOpt.get();
        SupportResponse response = facilitatorService.getSupportedCapabilities(config);

        return ResponseEntity.ok(response);
    }

    /**
     * Get transaction status by hash.
     * Helper endpoint for checking settlement status.
     */
    @GetMapping("/transaction/{txHash}")
    public ResponseEntity<?> getTransaction(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @PathVariable String txHash) {

        var configOpt = facilitatorService.validateApiKey(apiKey);
        if (configOpt.isEmpty()) {
            log.warn("x402 transaction: Invalid or missing API key");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or missing API key"));
        }

        // For now, return a placeholder - could be enhanced to query Solana RPC
        // for transaction status
        return ResponseEntity.ok(Map.of(
                "txHash", txHash,
                "status", "unknown",
                "message", "Transaction lookup not yet implemented. Use Solana explorer to check status."
        ));
    }

    /**
     * Health check endpoint (no auth required).
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "healthy",
                "version", FACILITATOR_VERSION,
                "protocol", "x402",
                "x402Version", 1
        ));
    }
}
