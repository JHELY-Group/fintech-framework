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
 * Endpoints:
 * <ul>
 * <li>POST /api/x402/verify - Verify a payment payload</li>
 * <li>POST /api/x402/settle - Settle (submit) a verified payment</li>
 * <li>GET /api/x402/support - Get supported networks and capabilities</li>
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

    private final X402FacilitatorService facilitatorService;

    public X402FacilitatorController(X402FacilitatorService facilitatorService) {
        this.facilitatorService = facilitatorService;
    }

    /**
     * Verify a payment payload against requirements.
     * Called by resource servers before granting access.
     */
    @PostMapping("/verify")
    public ResponseEntity<?> verify(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @RequestBody VerifyRequest request) {

        var configOpt = facilitatorService.validateApiKey(apiKey);
        if (configOpt.isEmpty()) {
            log.warn("x402 verify: Invalid or missing API key");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or missing API key", "errorCode", "UNAUTHORIZED"));
        }

        X402FacilitatorConfig config = configOpt.get();

        try {
            VerifyResponse response = facilitatorService.verify(config, request);

            if (response.isValid()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("Verification error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal error", "errorCode", "INTERNAL_ERROR"));
        }
    }

    /**
     * Settle (submit) a verified payment to Solana.
     * Called by resource servers after successful verification.
     */
    @PostMapping("/settle")
    public ResponseEntity<?> settle(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey,
            @RequestBody SettleRequest request) {

        var configOpt = facilitatorService.validateApiKey(apiKey);
        if (configOpt.isEmpty()) {
            log.warn("x402 settle: Invalid or missing API key");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or missing API key", "errorCode", "UNAUTHORIZED"));
        }

        X402FacilitatorConfig config = configOpt.get();

        try {
            SettleResponse response = facilitatorService.settle(config, request);

            if (response.isSuccess()) {
                return ResponseEntity.ok(response);
            } else {
                return ResponseEntity.badRequest().body(response);
            }
        } catch (Exception e) {
            log.error("Settlement error: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Internal error", "errorCode", "INTERNAL_ERROR"));
        }
    }

    /**
     * Get supported networks and capabilities for this facilitator.
     * Called by clients to discover what this facilitator supports.
     */
    @GetMapping("/support")
    public ResponseEntity<?> support(
            @RequestHeader(value = "X-API-Key", required = false) String apiKey) {

        var configOpt = facilitatorService.validateApiKey(apiKey);
        if (configOpt.isEmpty()) {
            log.warn("x402 support: Invalid or missing API key");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or missing API key", "errorCode", "UNAUTHORIZED"));
        }

        X402FacilitatorConfig config = configOpt.get();
        SupportResponse response = facilitatorService.getSupportedCapabilities(config);

        return ResponseEntity.ok(response);
    }

    /**
     * Health check endpoint (no auth required).
     */
    @GetMapping("/health")
    public ResponseEntity<?> health() {
        return ResponseEntity.ok(Map.of(
                "status", "healthy",
                "version", "1.0.0",
                "protocol", "x402",
                "networks", new String[] { "solana-mainnet", "solana-devnet" }));
    }
}
