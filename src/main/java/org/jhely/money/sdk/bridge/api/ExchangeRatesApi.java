package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.ExchangeRatesGet200Response;

import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Arrays;
import java.util.stream.Collectors;

import org.springframework.core.io.FileSystemResource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestClient.ResponseSpec;
import org.springframework.web.client.RestClientResponseException;

@jakarta.annotation.Generated(value = "org.openapitools.codegen.languages.JavaClientCodegen", comments = "Generator version: 7.17.0")
public class ExchangeRatesApi {
    private ApiClient apiClient;

    public ExchangeRatesApi() {
        this(new ApiClient());
    }

    public ExchangeRatesApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get current exchange rate between two currencies.
     * Returns the current exchange rate from the \&quot;from\&quot; currency to the \&quot;to\&quot; currency. The exchange rate is updated roughly every 30s. Note that as of this writing, Bridge does not offer a \&quot;quote\&quot; by which a user can lock in a rate for a given amount of time. This is provided only as a courtesy to estimate what you are likely to get in a subsequent transfer request that involves currency exchange.  As of October 2025, we support:   - USD - EUR   - USD - MXN   - USD - BRL   - BRL - USD   - BTC - USD   - ETH - USD   - EUR - USD   - MXN - USD   - SOL - USD   - USDT - USD 
     * <p><b>200</b> - The exchange rate information.
     * @param from The currency code to convert from.
     * @param to The currency code to convert to.
     * @return ExchangeRatesGet200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec exchangeRatesGetRequestCreation(@jakarta.annotation.Nonnull String from, @jakarta.annotation.Nonnull String to) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'from' is set
        if (from == null) {
            throw new RestClientResponseException("Missing the required parameter 'from' when calling exchangeRatesGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'to' is set
        if (to == null) {
            throw new RestClientResponseException("Missing the required parameter 'to' when calling exchangeRatesGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "from", from));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "to", to));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<ExchangeRatesGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/exchange_rates", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get current exchange rate between two currencies.
     * Returns the current exchange rate from the \&quot;from\&quot; currency to the \&quot;to\&quot; currency. The exchange rate is updated roughly every 30s. Note that as of this writing, Bridge does not offer a \&quot;quote\&quot; by which a user can lock in a rate for a given amount of time. This is provided only as a courtesy to estimate what you are likely to get in a subsequent transfer request that involves currency exchange.  As of October 2025, we support:   - USD - EUR   - USD - MXN   - USD - BRL   - BRL - USD   - BTC - USD   - ETH - USD   - EUR - USD   - MXN - USD   - SOL - USD   - USDT - USD 
     * <p><b>200</b> - The exchange rate information.
     * @param from The currency code to convert from.
     * @param to The currency code to convert to.
     * @return ExchangeRatesGet200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ExchangeRatesGet200Response exchangeRatesGet(@jakarta.annotation.Nonnull String from, @jakarta.annotation.Nonnull String to) throws RestClientResponseException {
        ParameterizedTypeReference<ExchangeRatesGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return exchangeRatesGetRequestCreation(from, to).body(localVarReturnType);
    }

    /**
     * Get current exchange rate between two currencies.
     * Returns the current exchange rate from the \&quot;from\&quot; currency to the \&quot;to\&quot; currency. The exchange rate is updated roughly every 30s. Note that as of this writing, Bridge does not offer a \&quot;quote\&quot; by which a user can lock in a rate for a given amount of time. This is provided only as a courtesy to estimate what you are likely to get in a subsequent transfer request that involves currency exchange.  As of October 2025, we support:   - USD - EUR   - USD - MXN   - USD - BRL   - BRL - USD   - BTC - USD   - ETH - USD   - EUR - USD   - MXN - USD   - SOL - USD   - USDT - USD 
     * <p><b>200</b> - The exchange rate information.
     * @param from The currency code to convert from.
     * @param to The currency code to convert to.
     * @return ResponseEntity&lt;ExchangeRatesGet200Response&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ExchangeRatesGet200Response> exchangeRatesGetWithHttpInfo(@jakarta.annotation.Nonnull String from, @jakarta.annotation.Nonnull String to) throws RestClientResponseException {
        ParameterizedTypeReference<ExchangeRatesGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return exchangeRatesGetRequestCreation(from, to).toEntity(localVarReturnType);
    }

    /**
     * Get current exchange rate between two currencies.
     * Returns the current exchange rate from the \&quot;from\&quot; currency to the \&quot;to\&quot; currency. The exchange rate is updated roughly every 30s. Note that as of this writing, Bridge does not offer a \&quot;quote\&quot; by which a user can lock in a rate for a given amount of time. This is provided only as a courtesy to estimate what you are likely to get in a subsequent transfer request that involves currency exchange.  As of October 2025, we support:   - USD - EUR   - USD - MXN   - USD - BRL   - BRL - USD   - BTC - USD   - ETH - USD   - EUR - USD   - MXN - USD   - SOL - USD   - USDT - USD 
     * <p><b>200</b> - The exchange rate information.
     * @param from The currency code to convert from.
     * @param to The currency code to convert to.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec exchangeRatesGetWithResponseSpec(@jakarta.annotation.Nonnull String from, @jakarta.annotation.Nonnull String to) throws RestClientResponseException {
        return exchangeRatesGetRequestCreation(from, to);
    }
}
