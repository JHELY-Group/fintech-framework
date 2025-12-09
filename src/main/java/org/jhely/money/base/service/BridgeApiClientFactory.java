package org.jhely.money.base.service;

import org.jhely.money.sdk.bridge.client.ApiClient;
import org.jhely.money.sdk.bridge.api.BridgeWalletsApi;
import org.jhely.money.sdk.bridge.api.CustomersApi;
import org.jhely.money.sdk.bridge.api.ExchangeRatesApi;
import org.jhely.money.sdk.bridge.api.ExternalAccountsApi;
import org.jhely.money.sdk.bridge.api.VirtualAccountsApi;
import org.jhely.money.sdk.bridge.api.WebhooksApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Factory bean that provisions a configured OpenAPI generated Bridge ApiClient
 * and exposes typed API helpers.
 * Keeps generated code decoupled from the rest of the application and
 * centralizes auth/base URL wiring.
 */
@Component
public class BridgeApiClientFactory {

    private final ApiClient apiClient;

    private final BridgeWalletsApi walletsApi;
    private final CustomersApi customersApi;
    private final ExchangeRatesApi exchangeRatesApi;
    private final ExternalAccountsApi externalAccountsApi;
    private final VirtualAccountsApi virtualAccountsApi;
    private final WebhooksApi webhooksApi;
    private final org.jhely.money.sdk.bridge.api.TransfersApi transfersApi;

    public BridgeApiClientFactory(
            @Value("${bridge.mode:sandbox}") String mode,
            @Value("${bridge.sandbox.base-url}") String sandboxBaseUrl,
            @Value("${bridge.live.base-url}") String liveBaseUrl,
            @Value("${bridge.sandbox.api-key}") String sandboxApiKey,
            @Value("${bridge.live.api-key}") String liveApiKey) {

        this.apiClient = new ApiClient();
        // Choose base URL & API key based on mode
        System.out.println(">>> Bridge API mode: " + mode);
        if ("live".equalsIgnoreCase(mode)) {
            System.out.println(">>> Using LIVE Bridge API: " + liveBaseUrl);
            apiClient.setBasePath(ensureVersionSegment(liveBaseUrl));
            apiClient.addDefaultHeader("Api-Key", liveApiKey);
        } else { // default sandbox
            System.out.println(">>> Using SANDBOX Bridge API: " + sandboxBaseUrl);
            apiClient.setBasePath(ensureVersionSegment(sandboxBaseUrl));
            apiClient.addDefaultHeader("Api-Key", sandboxApiKey);
        }

        apiClient.setUserAgent("jhely-money-sdk/1.0");

        // Instantiate typed APIs
        this.walletsApi = new BridgeWalletsApi(apiClient);
        this.customersApi = new CustomersApi(apiClient);
        this.exchangeRatesApi = new ExchangeRatesApi(apiClient);
        this.externalAccountsApi = new ExternalAccountsApi(apiClient);
        this.virtualAccountsApi = new VirtualAccountsApi(apiClient);
        this.webhooksApi = new WebhooksApi(apiClient);
        this.transfersApi = new org.jhely.money.sdk.bridge.api.TransfersApi(apiClient);
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public BridgeWalletsApi wallets() {
        return walletsApi;
    }

    public CustomersApi customers() {
        return customersApi;
    }

    public ExchangeRatesApi exchangeRates() {
        return exchangeRatesApi;
    }

    public ExternalAccountsApi externalAccounts() {
        return externalAccountsApi;
    }

    public VirtualAccountsApi virtualAccounts() {
        return virtualAccountsApi;
    }

    public WebhooksApi webhooks() {
        return webhooksApi;
    }

    public org.jhely.money.sdk.bridge.api.TransfersApi transfers() {
        return transfersApi;
    }

    /**
     * Ensure the Bridge API version segment (/v0) is present. Official spec servers
     * include /v0.
     * Accept both base URLs that already contain it and those without. Trailing
     * slashes are normalized.
     */
    private String ensureVersionSegment(String baseUrl) {
        if (baseUrl == null || baseUrl.isBlank())
            return baseUrl;
        String trimmed = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length() - 1) : baseUrl;
        if (trimmed.matches(".*/v\\d+$")) {
            return trimmed; // already versioned
        }
        // Default to v0 for current spec version
        return trimmed + "/v0";
    }
}
