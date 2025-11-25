package org.jhely.money.base.api.bridge;

import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.api.payments.BridgeProperties;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Objects;

@RestController
@RequestMapping("/api/bridge/admin/webhooks")
@RolesAllowed("USER")
public class BridgeWebhooksAdminListController {
    private static final Logger log = LoggerFactory.getLogger(BridgeWebhooksAdminListController.class);

    private final BridgeProperties bridgeProps;

    public BridgeWebhooksAdminListController(BridgeProperties bridgeProps) {
        this.bridgeProps = bridgeProps;
    }

    // List all webhook endpoints (raw JSON passthrough to avoid schema/enum drift)
    @GetMapping(produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> listAll() {
        try {
            String json = fetchRaw("/webhooks");
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            log.warn("Fetching webhook endpoints failed: {}", e.getMessage());
            return ResponseEntity.status(502)
                    .body("{\"error\":\"bridge_webhooks_fetch_failed\",\"message\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    // Create a new webhook endpoint (raw passthrough). Body is forwarded as-is.
    @org.springframework.web.bind.annotation.PostMapping(path = "/create", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> create(@org.springframework.web.bind.annotation.RequestBody String body) {
        try {
            String json = requestRaw("POST", "/webhooks", body);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            log.warn("Create webhook failed: {}", e.getMessage());
            return ResponseEntity.status(502)
                    .body("{\"error\":\"bridge_webhook_create_failed\",\"message\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    // Update existing webhook endpoint
    @org.springframework.web.bind.annotation.PutMapping(path = "/{webhookId}", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> update(@org.springframework.web.bind.annotation.PathVariable String webhookId,
                                         @org.springframework.web.bind.annotation.RequestBody String body) {
        try {
            String json = requestRaw("PUT", "/webhooks/" + webhookId, body);
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            log.warn("Update webhook {} failed: {}", webhookId, e.getMessage());
            return ResponseEntity.status(502)
                    .body("{\"error\":\"bridge_webhook_update_failed\",\"message\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    private String fetchRaw(String path) throws Exception {
        var env = switch (Objects.requireNonNull(bridgeProps.getMode(), "bridge.mode").toLowerCase()) {
            case "live", "prod", "production" -> bridgeProps.getLive();
            case "sandbox", "sbx", "test", "" -> bridgeProps.getSandbox();
            default -> throw new IllegalArgumentException("Unknown bridge.mode=" + bridgeProps.getMode());
        };
        String baseUrl = Objects.requireNonNull(env.getBaseUrl(), "Bridge baseUrl");
        String apiKey = Objects.requireNonNull(env.getApiKey(), "Bridge apiKey");
        String url = (baseUrl.endsWith("/v0") ? baseUrl : baseUrl + "/v0") + path;
        okhttp3.Request req = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .addHeader("Api-Key", apiKey)
                .build();
        try (okhttp3.Response resp = new okhttp3.OkHttpClient().newCall(req).execute()) {
            if (!resp.isSuccessful()) {
                throw new IllegalStateException("Bridge API " + resp.code() + " for " + path + ": " + (resp.body() != null ? resp.body().string() : ""));
            }
            return resp.body() != null ? resp.body().string() : "{}";
        }
    }

    private String requestRaw(String method, String path, String body) throws Exception {
        var env = switch (Objects.requireNonNull(bridgeProps.getMode(), "bridge.mode").toLowerCase()) {
            case "live", "prod", "production" -> bridgeProps.getLive();
            case "sandbox", "sbx", "test", "" -> bridgeProps.getSandbox();
            default -> throw new IllegalArgumentException("Unknown bridge.mode=" + bridgeProps.getMode());
        };
        String baseUrl = Objects.requireNonNull(env.getBaseUrl(), "Bridge baseUrl");
        String apiKey = Objects.requireNonNull(env.getApiKey(), "Bridge apiKey");
        String url = (baseUrl.endsWith("/v0") ? baseUrl : baseUrl + "/v0") + path;
        okhttp3.MediaType json = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody reqBody = body == null ? okhttp3.RequestBody.create(new byte[0]) : okhttp3.RequestBody.create(body, json);
        okhttp3.Request.Builder b = new okhttp3.Request.Builder().url(url).addHeader("Api-Key", apiKey);
        // Bridge requires Idempotency-Key for POST requests
        if ("POST".equalsIgnoreCase(method)) {
            b.addHeader("Idempotency-Key", java.util.UUID.randomUUID().toString());
        }
        switch (method) {
            case "POST" -> b.post(reqBody);
            case "PUT" -> b.put(reqBody);
            default -> throw new IllegalArgumentException("Unsupported method " + method);
        }
        try (okhttp3.Response resp = new okhttp3.OkHttpClient().newCall(b.build()).execute()) {
            if (!resp.isSuccessful()) {
                throw new IllegalStateException("Bridge API " + resp.code() + " for " + path + ": " + (resp.body() != null ? resp.body().string() : ""));
            }
            return resp.body() != null ? resp.body().string() : "{}";
        }
    }

    private static String escape(String in) { return in == null ? "" : in.replace("\"", "\\\""); }
}
