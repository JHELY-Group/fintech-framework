package org.jhely.money.base.service.payments;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.jhely.money.base.api.payments.BridgeProperties;
import org.jhely.money.base.api.payments.WebhookSettings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import org.jhely.money.sdk.bridge.api.WebhooksApi;
import org.jhely.money.sdk.bridge.client.ApiClient;
import org.jhely.money.sdk.bridge.model.CreateWebhook;
import org.jhely.money.sdk.bridge.model.PutWebhookPayload;
import org.jhely.money.sdk.bridge.model.Webhook;
import org.jhely.money.sdk.bridge.model.WebhookEventCategory;

@Component
public class BridgeWebhookProvisioner {
    private static final Logger log = LoggerFactory.getLogger(BridgeWebhookProvisioner.class);

    private final BridgeProperties bridgeProps;
    private final WebhookSettings webhookSettings;
    private final BridgeWebhookKeyProvider keyProvider;

    public BridgeWebhookProvisioner(BridgeProperties bridgeProps,
                                    WebhookSettings webhookSettings,
                                    BridgeWebhookKeyProvider keyProvider) {
        this.bridgeProps = bridgeProps;
        this.webhookSettings = webhookSettings;
        this.keyProvider = keyProvider;
    }

    @EventListener(ApplicationReadyEvent.class)
    public void ensureWebhook() {
        if (!webhookSettings.isAutoProvision()) {
            // Allow manual configuration; still expose configured public key if provided
            if (StringUtils.hasText(webhookSettings.getPublicKey())) {
                keyProvider.setPublicKeyPem(webhookSettings.getPublicKey());
            }
            return;
        }

        try {
            var env = switch (bridgeProps.getMode().toLowerCase()) {
                case "live", "prod", "production" -> bridgeProps.getLive();
                case "sandbox", "sbx", "test", "" -> bridgeProps.getSandbox();
                default -> throw new IllegalArgumentException("Unknown bridge.mode=" + bridgeProps.getMode());
            };
            String baseUrl = Objects.requireNonNull(env.getBaseUrl(), "Bridge baseUrl");
            String apiKey  = Objects.requireNonNull(env.getApiKey(), "Bridge apiKey");

            String targetUrl = determineWebhookUrl();
            if (!StringUtils.hasText(targetUrl)) {
                log.warn("Bridge webhook auto-provision enabled but no URL determined. Set bridge.webhook.url or bridge.webhook.app-public-base-url.");
                return;
            }

            WebhooksApi api = webhooksApi(baseUrl, apiKey);

            var existingList = api.webhooksGet();
            Webhook existing = null;
            if (existingList != null && existingList.getData() != null) {
                for (Webhook w : existingList.getData()) {
                    if (targetUrl.equalsIgnoreCase(w.getUrl())) { existing = w; break; }
                }
            }

            List<WebhookEventCategory> desiredCats = desiredCategories(webhookSettings.getEventCategories());

            if (existing == null) {
                // Create disabled then enable via PUT
                CreateWebhook body = new CreateWebhook()
                        .url(targetUrl)
                        .eventEpoch(CreateWebhook.EventEpochEnum.WEBHOOK_CREATION)
                        .eventCategories(desiredCats);
                Webhook created = api.webhooksPost(UUID.randomUUID().toString(), body);
                log.info("Created Bridge webhook id={} url={}", created.getId(), created.getUrl());
                // PUT: activate
                PutWebhookPayload put = new PutWebhookPayload()
                        .status(PutWebhookPayload.StatusEnum.ACTIVE)
                        .eventCategories(desiredCats);
                Webhook activated = api.webhooksWebhookIDPut(created.getId(), put);
                // publish public key and id
                webhookSettings.setId(activated.getId());
                keyProvider.setPublicKeyPem(activated.getPublicKey());
                if (StringUtils.hasText(webhookSettings.getPublicKey())) {
                    // Allow explicit override if configured
                    keyProvider.setPublicKeyPem(webhookSettings.getPublicKey());
                }
                log.info("Activated Bridge webhook id={} status={} cats={}", activated.getId(), activated.getStatus(), activated.getEventCategories());
                // Disable any other previously active webhook(s)
                disableOtherActiveWebhooks(api, activated.getId(), targetUrl);
            } else {
                boolean needsPut = existing.getStatus() != Webhook.StatusEnum.ACTIVE || !hasAll(existing.getEventCategories(), desiredCats);
                if (needsPut) {
                    PutWebhookPayload put = new PutWebhookPayload()
                            .status(PutWebhookPayload.StatusEnum.ACTIVE)
                            .eventCategories(desiredCats);
                    existing = api.webhooksWebhookIDPut(existing.getId(), put);
                    log.info("Updated Bridge webhook id={} to ACTIVE with categories={}"
                            , existing.getId(), existing.getEventCategories());
                }
                // publish public key and id
                webhookSettings.setId(existing.getId());
                keyProvider.setPublicKeyPem(existing.getPublicKey());
                if (StringUtils.hasText(webhookSettings.getPublicKey())) {
                    keyProvider.setPublicKeyPem(webhookSettings.getPublicKey());
                }
                // Disable any other previously active webhook(s)
                disableOtherActiveWebhooks(api, existing.getId(), targetUrl);
            }
        } catch (Exception e) {
            log.warn("Bridge webhook auto-provision failed: {}", e.getMessage(), e);
        }
    }

    private WebhooksApi webhooksApi(String baseUrl, String apiKey) {
        ApiClient gen = new ApiClient();
        String base = baseUrl.endsWith("/v0") ? baseUrl : (baseUrl + "/v0");
        gen.setBasePath(base);
        gen.setApiKey(apiKey);
        return new WebhooksApi(gen);
    }

    private String determineWebhookUrl() {
        if (StringUtils.hasText(webhookSettings.getUrl())) return webhookSettings.getUrl();
        if (StringUtils.hasText(webhookSettings.getAppPublicBaseUrl())) {
            String base = webhookSettings.getAppPublicBaseUrl();
            if (base.endsWith("/")) base = base.substring(0, base.length()-1);
            return base + "/api/bridge/webhook";
        }
        return null;
    }

    private static boolean hasAll(List<WebhookEventCategory> current, List<WebhookEventCategory> desired) {
        if (desired == null || desired.isEmpty()) return true;
        if (current == null) return false;
        var set = new java.util.HashSet<>(current);
        return set.containsAll(desired);
    }

    private static List<WebhookEventCategory> desiredCategories(List<String> configured) {
        List<WebhookEventCategory> defaults = List.of(WebhookEventCategory.CUSTOMER, WebhookEventCategory.KYC_LINK);
        if (configured == null || configured.isEmpty()) return defaults;
        List<WebhookEventCategory> out = new ArrayList<>();
        for (String s : configured) {
            try {
                out.add(WebhookEventCategory.fromValue(s));
            } catch (Exception ignore) {}
        }
        return out.isEmpty() ? defaults : out;
    }

    private void disableOtherActiveWebhooks(WebhooksApi api, String activeId, String activeUrl) {
        try {
            var list = api.webhooksGet();
            if (list == null || list.getData() == null) return;
            for (Webhook w : list.getData()) {
                if (!w.getId().equals(activeId) && w.getStatus() == Webhook.StatusEnum.ACTIVE) {
                    try {
                        PutWebhookPayload put = new PutWebhookPayload()
                                .status(PutWebhookPayload.StatusEnum.DISABLED)
                                .eventCategories(w.getEventCategories());
                        api.webhooksWebhookIDPut(w.getId(), put);
                        log.info("Disabled previous active Bridge webhook id={} url={}", w.getId(), w.getUrl());
                    } catch (Exception ex) {
                        log.warn("Failed disabling webhook id={}: {}", w.getId(), ex.getMessage());
                    }
                }
            }
        } catch (Exception e) {
            log.warn("Listing webhooks failed while attempting to disable others: {}", e.getMessage());
        }
    }
}
