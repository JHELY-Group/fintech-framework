package org.jhely.money.base.api.payments;

import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableConfigurationProperties({BridgeProperties.class, WebhookSettings.class})
public class BridgeConfig {

    @Bean
    public BridgeApiClient bridgeApiClient(BridgeProperties props) {
        var env = switch (props.getMode().toLowerCase()) {
            case "live", "prod", "production" -> props.getLive();
            case "sandbox", "sbx", "test", "" -> props.getSandbox();
            default -> throw new IllegalArgumentException("Unknown bridge.mode=" + props.getMode());
        };
        if (env.getBaseUrl() == null || env.getApiKey() == null) {
            throw new IllegalStateException("Bridge baseUrl/apiKey not set for mode=" + props.getMode());
        }
        return new BridgeApiClient(env.getBaseUrl(), env.getApiKey());
    }
}
