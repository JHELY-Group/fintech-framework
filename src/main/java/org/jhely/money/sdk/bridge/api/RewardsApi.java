package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.CustomerRewardHistory;
import org.jhely.money.sdk.bridge.model.CustomerRewardSummary;
import org.jhely.money.sdk.bridge.model.DeveloperRewardSummary;
import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.RewardRate;

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
public class RewardsApi {
    private ApiClient apiClient;

    public RewardsApi() {
        this(new ApiClient());
    }

    public RewardsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get a summary of a customer&#39;s rewards
     * Get a summary of a customer&#39;s rewards
     * <p><b>200</b> - The summary of a customer&#39;s rewards
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param customerID The customerID parameter
     * @return CustomerRewardSummary
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec rewardsCurrencyCustomerCustomerIDGetRequestCreation(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'currency' is set
        if (currency == null) {
            throw new RestClientResponseException("Missing the required parameter 'currency' when calling rewardsCurrencyCustomerCustomerIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling rewardsCurrencyCustomerCustomerIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("currency", currency);
        pathParams.put("customerID", customerID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<CustomerRewardSummary> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/rewards/{currency}/customer/{customerID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a summary of a customer&#39;s rewards
     * Get a summary of a customer&#39;s rewards
     * <p><b>200</b> - The summary of a customer&#39;s rewards
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param customerID The customerID parameter
     * @return CustomerRewardSummary
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CustomerRewardSummary rewardsCurrencyCustomerCustomerIDGet(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<CustomerRewardSummary> localVarReturnType = new ParameterizedTypeReference<>() {};
        return rewardsCurrencyCustomerCustomerIDGetRequestCreation(currency, customerID).body(localVarReturnType);
    }

    /**
     * Get a summary of a customer&#39;s rewards
     * Get a summary of a customer&#39;s rewards
     * <p><b>200</b> - The summary of a customer&#39;s rewards
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param customerID The customerID parameter
     * @return ResponseEntity&lt;CustomerRewardSummary&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CustomerRewardSummary> rewardsCurrencyCustomerCustomerIDGetWithHttpInfo(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<CustomerRewardSummary> localVarReturnType = new ParameterizedTypeReference<>() {};
        return rewardsCurrencyCustomerCustomerIDGetRequestCreation(currency, customerID).toEntity(localVarReturnType);
    }

    /**
     * Get a summary of a customer&#39;s rewards
     * Get a summary of a customer&#39;s rewards
     * <p><b>200</b> - The summary of a customer&#39;s rewards
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param customerID The customerID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec rewardsCurrencyCustomerCustomerIDGetWithResponseSpec(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        return rewardsCurrencyCustomerCustomerIDGetRequestCreation(currency, customerID);
    }

    /**
     * Get a history of a customer&#39;s rewards
     * Get a history of a customer&#39;s rewards
     * <p><b>200</b> - The history of a customer&#39;s rewards
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param customerID The customerID parameter
     * @return CustomerRewardHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec rewardsCurrencyCustomerCustomerIDHistoryGetRequestCreation(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'currency' is set
        if (currency == null) {
            throw new RestClientResponseException("Missing the required parameter 'currency' when calling rewardsCurrencyCustomerCustomerIDHistoryGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling rewardsCurrencyCustomerCustomerIDHistoryGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("currency", currency);
        pathParams.put("customerID", customerID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<CustomerRewardHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/rewards/{currency}/customer/{customerID}/history", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a history of a customer&#39;s rewards
     * Get a history of a customer&#39;s rewards
     * <p><b>200</b> - The history of a customer&#39;s rewards
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param customerID The customerID parameter
     * @return CustomerRewardHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CustomerRewardHistory rewardsCurrencyCustomerCustomerIDHistoryGet(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<CustomerRewardHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return rewardsCurrencyCustomerCustomerIDHistoryGetRequestCreation(currency, customerID).body(localVarReturnType);
    }

    /**
     * Get a history of a customer&#39;s rewards
     * Get a history of a customer&#39;s rewards
     * <p><b>200</b> - The history of a customer&#39;s rewards
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param customerID The customerID parameter
     * @return ResponseEntity&lt;CustomerRewardHistory&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CustomerRewardHistory> rewardsCurrencyCustomerCustomerIDHistoryGetWithHttpInfo(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<CustomerRewardHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return rewardsCurrencyCustomerCustomerIDHistoryGetRequestCreation(currency, customerID).toEntity(localVarReturnType);
    }

    /**
     * Get a history of a customer&#39;s rewards
     * Get a history of a customer&#39;s rewards
     * <p><b>200</b> - The history of a customer&#39;s rewards
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param customerID The customerID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec rewardsCurrencyCustomerCustomerIDHistoryGetWithResponseSpec(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        return rewardsCurrencyCustomerCustomerIDHistoryGetRequestCreation(currency, customerID);
    }

    /**
     * Get a summary of all rewards for a given stablecoin
     * Get a summary of all rewards for a given stablecoin
     * <p><b>200</b> - The summary of all rewards for a given stablecoin
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @return DeveloperRewardSummary
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec rewardsCurrencyGetRequestCreation(@jakarta.annotation.Nonnull String currency) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'currency' is set
        if (currency == null) {
            throw new RestClientResponseException("Missing the required parameter 'currency' when calling rewardsCurrencyGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("currency", currency);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<DeveloperRewardSummary> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/rewards/{currency}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a summary of all rewards for a given stablecoin
     * Get a summary of all rewards for a given stablecoin
     * <p><b>200</b> - The summary of all rewards for a given stablecoin
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @return DeveloperRewardSummary
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public DeveloperRewardSummary rewardsCurrencyGet(@jakarta.annotation.Nonnull String currency) throws RestClientResponseException {
        ParameterizedTypeReference<DeveloperRewardSummary> localVarReturnType = new ParameterizedTypeReference<>() {};
        return rewardsCurrencyGetRequestCreation(currency).body(localVarReturnType);
    }

    /**
     * Get a summary of all rewards for a given stablecoin
     * Get a summary of all rewards for a given stablecoin
     * <p><b>200</b> - The summary of all rewards for a given stablecoin
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @return ResponseEntity&lt;DeveloperRewardSummary&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DeveloperRewardSummary> rewardsCurrencyGetWithHttpInfo(@jakarta.annotation.Nonnull String currency) throws RestClientResponseException {
        ParameterizedTypeReference<DeveloperRewardSummary> localVarReturnType = new ParameterizedTypeReference<>() {};
        return rewardsCurrencyGetRequestCreation(currency).toEntity(localVarReturnType);
    }

    /**
     * Get a summary of all rewards for a given stablecoin
     * Get a summary of all rewards for a given stablecoin
     * <p><b>200</b> - The summary of all rewards for a given stablecoin
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec rewardsCurrencyGetWithResponseSpec(@jakarta.annotation.Nonnull String currency) throws RestClientResponseException {
        return rewardsCurrencyGetRequestCreation(currency);
    }

    /**
     * Get the currency reward rates for a given stablecoin
     * Get the currency rewazrd rates for a given stablecoin
     * <p><b>200</b> - The current reward rates
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param since The starting time in ISO8601 format
     * @return RewardRate
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec rewardsCurrencyRatesGetRequestCreation(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nullable String since) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'currency' is set
        if (currency == null) {
            throw new RestClientResponseException("Missing the required parameter 'currency' when calling rewardsCurrencyRatesGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("currency", currency);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "since", since));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<RewardRate> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/rewards/{currency}/rates", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get the currency reward rates for a given stablecoin
     * Get the currency rewazrd rates for a given stablecoin
     * <p><b>200</b> - The current reward rates
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param since The starting time in ISO8601 format
     * @return RewardRate
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public RewardRate rewardsCurrencyRatesGet(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nullable String since) throws RestClientResponseException {
        ParameterizedTypeReference<RewardRate> localVarReturnType = new ParameterizedTypeReference<>() {};
        return rewardsCurrencyRatesGetRequestCreation(currency, since).body(localVarReturnType);
    }

    /**
     * Get the currency reward rates for a given stablecoin
     * Get the currency rewazrd rates for a given stablecoin
     * <p><b>200</b> - The current reward rates
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param since The starting time in ISO8601 format
     * @return ResponseEntity&lt;RewardRate&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RewardRate> rewardsCurrencyRatesGetWithHttpInfo(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nullable String since) throws RestClientResponseException {
        ParameterizedTypeReference<RewardRate> localVarReturnType = new ParameterizedTypeReference<>() {};
        return rewardsCurrencyRatesGetRequestCreation(currency, since).toEntity(localVarReturnType);
    }

    /**
     * Get the currency reward rates for a given stablecoin
     * Get the currency rewazrd rates for a given stablecoin
     * <p><b>200</b> - The current reward rates
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param currency The stablecoin symbol
     * @param since The starting time in ISO8601 format
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec rewardsCurrencyRatesGetWithResponseSpec(@jakarta.annotation.Nonnull String currency, @jakarta.annotation.Nullable String since) throws RestClientResponseException {
        return rewardsCurrencyRatesGetRequestCreation(currency, since);
    }

    /**
     * Get the current reward rates
     * Get the current reward rates
     * <p><b>200</b> - The current reward rates
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param since The starting time in ISO8601 format
     * @return RewardRate
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec rewardsRatesGetRequestCreation(@jakarta.annotation.Nullable String since) throws RestClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "since", since));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<RewardRate> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/rewards/rates", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get the current reward rates
     * Get the current reward rates
     * <p><b>200</b> - The current reward rates
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param since The starting time in ISO8601 format
     * @return RewardRate
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public RewardRate rewardsRatesGet(@jakarta.annotation.Nullable String since) throws RestClientResponseException {
        ParameterizedTypeReference<RewardRate> localVarReturnType = new ParameterizedTypeReference<>() {};
        return rewardsRatesGetRequestCreation(since).body(localVarReturnType);
    }

    /**
     * Get the current reward rates
     * Get the current reward rates
     * <p><b>200</b> - The current reward rates
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param since The starting time in ISO8601 format
     * @return ResponseEntity&lt;RewardRate&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<RewardRate> rewardsRatesGetWithHttpInfo(@jakarta.annotation.Nullable String since) throws RestClientResponseException {
        ParameterizedTypeReference<RewardRate> localVarReturnType = new ParameterizedTypeReference<>() {};
        return rewardsRatesGetRequestCreation(since).toEntity(localVarReturnType);
    }

    /**
     * Get the current reward rates
     * Get the current reward rates
     * <p><b>200</b> - The current reward rates
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param since The starting time in ISO8601 format
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec rewardsRatesGetWithResponseSpec(@jakarta.annotation.Nullable String since) throws RestClientResponseException {
        return rewardsRatesGetRequestCreation(since);
    }
}
