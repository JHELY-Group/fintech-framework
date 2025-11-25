package org.jhely.money.base.api.bridge;

import java.util.Objects;
import java.util.UUID;

import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.api.payments.BridgeProperties;
import org.jhely.money.base.api.payments.WebhookSettings;
import org.jhely.money.sdk.bridge.api.WebhooksApi;
import org.jhely.money.sdk.bridge.client.ApiClient;
import org.jhely.money.sdk.bridge.model.SendWebhookPayload;
import org.jhely.money.sdk.bridge.model.WebhookEventSent;
import org.jhely.money.sdk.bridge.model.Webhook;
import org.jhely.money.sdk.bridge.model.Webhooks;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/bridge/admin/webhook")
@RolesAllowed("USER")
public class BridgeWebhookAdminController {
    private static final Logger log = LoggerFactory.getLogger(BridgeWebhookAdminController.class);

    private final BridgeProperties bridgeProps;
    private final WebhookSettings webhookSettings;

    public BridgeWebhookAdminController(BridgeProperties bridgeProps, WebhookSettings webhookSettings) {
        this.bridgeProps = bridgeProps;
        this.webhookSettings = webhookSettings;
    }

    // 1) View recent delivery logs (raw JSON pass-through to avoid enum breakage)
    @GetMapping(value = "/logs", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> logs(@RequestParam(name = "webhookId", required = false) String webhookId) {
        String id = resolveWebhookId(webhookId);
        try {
            String json = fetchRaw("/webhooks/" + id + "/logs");
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            log.warn("Fetching webhook logs failed: {}", e.getMessage());
            return ResponseEntity.status(502).body("{\"error\":\"bridge_logs_fetch_failed\",\"message\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    // 2) List upcoming events (next 10) as raw JSON to tolerate new enum values
    @GetMapping(value = "/events", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<String> events(@RequestParam(name = "webhookId", required = false) String webhookId) {
        String id = resolveWebhookId(webhookId);
        try {
            String json = fetchRaw("/webhooks/" + id + "/events");
            return ResponseEntity.ok(json);
        } catch (Exception e) {
            log.warn("Fetching webhook events failed: {}", e.getMessage());
            return ResponseEntity.status(502).body("{\"error\":\"bridge_events_fetch_failed\",\"message\":\"" + escape(e.getMessage()) + "\"}");
        }
    }

    // 3) Simulate sending a specific event to the webhook (by event_id)
    @GetMapping("/send")
    public ResponseEntity<WebhookEventSent> send(@RequestParam(name = "eventId") String eventId,
                                                 @RequestParam(name = "webhookId", required = false) String webhookId) {
        if (!StringUtils.hasText(eventId)) {
            return ResponseEntity.badRequest().build();
        }
        String id = resolveWebhookId(webhookId);
        WebhooksApi api = webhooksApi();
        var payload = new SendWebhookPayload().eventId(eventId);
        var sent = api.webhooksWebhookIDSendPost(id, UUID.randomUUID().toString(), payload);
        return ResponseEntity.ok(sent);
    }

    private WebhooksApi webhooksApi() {
        var env = switch (Objects.requireNonNull(bridgeProps.getMode(), "bridge.mode").toLowerCase()) {
            case "live", "prod", "production" -> bridgeProps.getLive();
            case "sandbox", "sbx", "test", "" -> bridgeProps.getSandbox();
            default -> throw new IllegalArgumentException("Unknown bridge.mode=" + bridgeProps.getMode());
        };
        String baseUrl = Objects.requireNonNull(env.getBaseUrl(), "Bridge baseUrl");
        String apiKey = Objects.requireNonNull(env.getApiKey(), "Bridge apiKey");
        ApiClient gen = new ApiClient();
        String base = baseUrl.endsWith("/v0") ? baseUrl : (baseUrl + "/v0");
        gen.setBasePath(base);
        gen.setApiKey(apiKey);
        return new WebhooksApi(gen);
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

    private static String escape(String in) { return in == null ? "" : in.replace("\"", "\\\""); }

    private String resolveWebhookId(String override) {
        if (StringUtils.hasText(override)) return override;
        if (StringUtils.hasText(webhookSettings.getId())) return webhookSettings.getId();

        // Try to infer by matching the configured URL (if present) against Bridge's list
        String targetUrl = determineWebhookUrlFromSettings();
        if (StringUtils.hasText(targetUrl)) {
            try {
                Webhooks list = webhooksApi().webhooksGet();
                if (list != null && list.getData() != null) {
                    for (Webhook w : list.getData()) {
                        if (targetUrl.equalsIgnoreCase(w.getUrl())) {
                            return w.getId();
                        }
                    }
                }
            } catch (Exception e) {
                log.warn("Failed to list webhooks to resolve id: {}", e.getMessage());
            }
        }
        throw new IllegalArgumentException("Webhook ID not set. Provide ?webhookId=... or set bridge.webhook.id or bridge.webhook.(url|app-public-base-url)");
    }

    private String determineWebhookUrlFromSettings() {
        if (StringUtils.hasText(webhookSettings.getUrl())) return webhookSettings.getUrl();
        if (StringUtils.hasText(webhookSettings.getAppPublicBaseUrl())) {
            String base = webhookSettings.getAppPublicBaseUrl();
            if (base.endsWith("/")) base = base.substring(0, base.length()-1);
            return base + "/api/bridge/webhook";
        }
        return null;
    }
}
