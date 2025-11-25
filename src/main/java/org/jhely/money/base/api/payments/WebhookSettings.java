package org.jhely.money.base.api.payments;

import java.util.List;
import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bridge.webhook")
public class WebhookSettings {
    /** If true, app will auto-create/ensure a Bridge webhook at startup. */
    private boolean autoProvision = false;
    /** Optional explicit webhook URL; when blank, uses appPublicBaseUrl + "/api/bridge/webhook". */
    private String url;
    /** Optional explicit webhook ID; if set, admin endpoints will use this. */
    private String id;
    /** Optional explicit public key PEM for verification fallback. */
    private String publicKey;
    /** Event categories to subscribe to; defaults to customer + kyc_link. */
    private List<String> eventCategories;
    /** Optional application public base URL (e.g., https://app.example.com). */
    private String appPublicBaseUrl;

    public boolean isAutoProvision() { return autoProvision; }
    public void setAutoProvision(boolean autoProvision) { this.autoProvision = autoProvision; }
    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
    public String getId() { return id; }
    public void setId(String id) { this.id = id; }
    public String getPublicKey() { return publicKey; }
    public void setPublicKey(String publicKey) { this.publicKey = publicKey; }
    public List<String> getEventCategories() { return eventCategories; }
    public void setEventCategories(List<String> eventCategories) { this.eventCategories = eventCategories; }
    public String getAppPublicBaseUrl() { return appPublicBaseUrl; }
    public void setAppPublicBaseUrl(String appPublicBaseUrl) { this.appPublicBaseUrl = appPublicBaseUrl; }
}
