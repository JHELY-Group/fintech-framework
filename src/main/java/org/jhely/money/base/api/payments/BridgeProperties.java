package org.jhely.money.base.api.payments;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix = "bridge")
public class BridgeProperties {
    private String mode = "sandbox";
    private Env sandbox = new Env();
    private Env live = new Env();

    public static class Env {
        private String baseUrl;
        private String apiKey;
        public String getBaseUrl() { return baseUrl; }
        public void setBaseUrl(String baseUrl) { this.baseUrl = baseUrl; }
        public String getApiKey() { return apiKey; }
        public void setApiKey(String apiKey) { this.apiKey = apiKey; }
    }

    public String getMode() { return mode; }
    public void setMode(String mode) { this.mode = mode; }
    public Env getSandbox() { return sandbox; }
    public void setSandbox(Env sandbox) { this.sandbox = sandbox; }
    public Env getLive() { return live; }
    public void setLive(Env live) { this.live = live; }
}