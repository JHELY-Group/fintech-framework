package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.BridgeWalletHistory;
import org.jhely.money.sdk.bridge.model.BridgeWalletTotalBalance;
import org.jhely.money.sdk.bridge.model.BridgeWalletWithBalances;
import org.jhely.money.sdk.bridge.model.BridgeWalletsList;
import org.jhely.money.sdk.bridge.model.CreateBridgeWallet;
import org.jhely.money.sdk.bridge.model.CreateBridgeWalletResponse;
import org.jhely.money.sdk.bridge.model.Error;

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
public class BridgeWalletsApi {
    private ApiClient apiClient;

    public BridgeWalletsApi() {
        this(new ApiClient());
    }

    public BridgeWalletsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get a Bridge Wallet
     * Retrieve a Bridge Wallet for the specified Bridge Wallet ID
     * <p><b>200</b> - A Bridge Wallet object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param bridgeWalletID The bridgeWalletID parameter
     * @return BridgeWalletWithBalances
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDWalletsBridgeWalletIDGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String bridgeWalletID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDWalletsBridgeWalletIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'bridgeWalletID' is set
        if (bridgeWalletID == null) {
            throw new RestClientResponseException("Missing the required parameter 'bridgeWalletID' when calling customersCustomerIDWalletsBridgeWalletIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("bridgeWalletID", bridgeWalletID);

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

        ParameterizedTypeReference<BridgeWalletWithBalances> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/wallets/{bridgeWalletID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a Bridge Wallet
     * Retrieve a Bridge Wallet for the specified Bridge Wallet ID
     * <p><b>200</b> - A Bridge Wallet object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param bridgeWalletID The bridgeWalletID parameter
     * @return BridgeWalletWithBalances
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public BridgeWalletWithBalances customersCustomerIDWalletsBridgeWalletIDGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String bridgeWalletID) throws RestClientResponseException {
        ParameterizedTypeReference<BridgeWalletWithBalances> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDWalletsBridgeWalletIDGetRequestCreation(customerID, bridgeWalletID).body(localVarReturnType);
    }

    /**
     * Get a Bridge Wallet
     * Retrieve a Bridge Wallet for the specified Bridge Wallet ID
     * <p><b>200</b> - A Bridge Wallet object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param bridgeWalletID The bridgeWalletID parameter
     * @return ResponseEntity&lt;BridgeWalletWithBalances&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<BridgeWalletWithBalances> customersCustomerIDWalletsBridgeWalletIDGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String bridgeWalletID) throws RestClientResponseException {
        ParameterizedTypeReference<BridgeWalletWithBalances> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDWalletsBridgeWalletIDGetRequestCreation(customerID, bridgeWalletID).toEntity(localVarReturnType);
    }

    /**
     * Get a Bridge Wallet
     * Retrieve a Bridge Wallet for the specified Bridge Wallet ID
     * <p><b>200</b> - A Bridge Wallet object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param bridgeWalletID The bridgeWalletID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDWalletsBridgeWalletIDGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String bridgeWalletID) throws RestClientResponseException {
        return customersCustomerIDWalletsBridgeWalletIDGetRequestCreation(customerID, bridgeWalletID);
    }

    /**
     * Get all Bridge Wallets for a customer
     * List of Bridge Wallets for the specified Customer ID
     * <p><b>200</b> - List of Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a bridge wallet id. If this is specified, the next page that starts with a bridge wallet right AFTER the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets older than the specified bridge wallet id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a bridge wallet id. If this is specified, the previous page that ends with a bridge wallet right BEFORE the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets newer than the specified bridge wallet id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return BridgeWalletsList
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDWalletsGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDWalletsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "starting_after", startingAfter));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ending_before", endingBefore));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<BridgeWalletsList> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/wallets", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all Bridge Wallets for a customer
     * List of Bridge Wallets for the specified Customer ID
     * <p><b>200</b> - List of Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a bridge wallet id. If this is specified, the next page that starts with a bridge wallet right AFTER the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets older than the specified bridge wallet id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a bridge wallet id. If this is specified, the previous page that ends with a bridge wallet right BEFORE the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets newer than the specified bridge wallet id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return BridgeWalletsList
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public BridgeWalletsList customersCustomerIDWalletsGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<BridgeWalletsList> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDWalletsGetRequestCreation(customerID, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Get all Bridge Wallets for a customer
     * List of Bridge Wallets for the specified Customer ID
     * <p><b>200</b> - List of Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a bridge wallet id. If this is specified, the next page that starts with a bridge wallet right AFTER the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets older than the specified bridge wallet id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a bridge wallet id. If this is specified, the previous page that ends with a bridge wallet right BEFORE the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets newer than the specified bridge wallet id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;BridgeWalletsList&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<BridgeWalletsList> customersCustomerIDWalletsGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<BridgeWalletsList> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDWalletsGetRequestCreation(customerID, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Get all Bridge Wallets for a customer
     * List of Bridge Wallets for the specified Customer ID
     * <p><b>200</b> - List of Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a bridge wallet id. If this is specified, the next page that starts with a bridge wallet right AFTER the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets older than the specified bridge wallet id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a bridge wallet id. If this is specified, the previous page that ends with a bridge wallet right BEFORE the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets newer than the specified bridge wallet id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDWalletsGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return customersCustomerIDWalletsGetRequestCreation(customerID, limit, startingAfter, endingBefore);
    }

    /**
     * Create a Bridge Wallet
     * 
     * <p><b>201</b> - Bridge Wallet created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createBridgeWallet Bridge Wallet to be created
     * @return CreateBridgeWalletResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDWalletsPostRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateBridgeWallet createBridgeWallet) throws RestClientResponseException {
        Object postBody = createBridgeWallet;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDWalletsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDWalletsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'createBridgeWallet' is set
        if (createBridgeWallet == null) {
            throw new RestClientResponseException("Missing the required parameter 'createBridgeWallet' when calling customersCustomerIDWalletsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        if (idempotencyKey != null)
        headerParams.add("Idempotency-Key", apiClient.parameterToString(idempotencyKey));
        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<CreateBridgeWalletResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/wallets", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a Bridge Wallet
     * 
     * <p><b>201</b> - Bridge Wallet created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createBridgeWallet Bridge Wallet to be created
     * @return CreateBridgeWalletResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CreateBridgeWalletResponse customersCustomerIDWalletsPost(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateBridgeWallet createBridgeWallet) throws RestClientResponseException {
        ParameterizedTypeReference<CreateBridgeWalletResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDWalletsPostRequestCreation(customerID, idempotencyKey, createBridgeWallet).body(localVarReturnType);
    }

    /**
     * Create a Bridge Wallet
     * 
     * <p><b>201</b> - Bridge Wallet created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createBridgeWallet Bridge Wallet to be created
     * @return ResponseEntity&lt;CreateBridgeWalletResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CreateBridgeWalletResponse> customersCustomerIDWalletsPostWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateBridgeWallet createBridgeWallet) throws RestClientResponseException {
        ParameterizedTypeReference<CreateBridgeWalletResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDWalletsPostRequestCreation(customerID, idempotencyKey, createBridgeWallet).toEntity(localVarReturnType);
    }

    /**
     * Create a Bridge Wallet
     * 
     * <p><b>201</b> - Bridge Wallet created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createBridgeWallet Bridge Wallet to be created
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDWalletsPostWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateBridgeWallet createBridgeWallet) throws RestClientResponseException {
        return customersCustomerIDWalletsPostRequestCreation(customerID, idempotencyKey, createBridgeWallet);
    }

    /**
     * Get transaction history for a Bridge Wallet
     * Get the list of transactions involving this Bridge Wallet
     * <p><b>200</b> - List of transactions for this Bridge Wallet
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param bridgeWalletID The bridgeWalletID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return BridgeWalletHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec walletsBridgeWalletIDHistoryGetRequestCreation(@jakarta.annotation.Nonnull String bridgeWalletID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'bridgeWalletID' is set
        if (bridgeWalletID == null) {
            throw new RestClientResponseException("Missing the required parameter 'bridgeWalletID' when calling walletsBridgeWalletIDHistoryGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("bridgeWalletID", bridgeWalletID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_after_ms", updatedAfterMs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_before_ms", updatedBeforeMs));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<BridgeWalletHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/wallets/{bridgeWalletID}/history", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get transaction history for a Bridge Wallet
     * Get the list of transactions involving this Bridge Wallet
     * <p><b>200</b> - List of transactions for this Bridge Wallet
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param bridgeWalletID The bridgeWalletID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return BridgeWalletHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public BridgeWalletHistory walletsBridgeWalletIDHistoryGet(@jakarta.annotation.Nonnull String bridgeWalletID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        ParameterizedTypeReference<BridgeWalletHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return walletsBridgeWalletIDHistoryGetRequestCreation(bridgeWalletID, limit, updatedAfterMs, updatedBeforeMs).body(localVarReturnType);
    }

    /**
     * Get transaction history for a Bridge Wallet
     * Get the list of transactions involving this Bridge Wallet
     * <p><b>200</b> - List of transactions for this Bridge Wallet
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param bridgeWalletID The bridgeWalletID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return ResponseEntity&lt;BridgeWalletHistory&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<BridgeWalletHistory> walletsBridgeWalletIDHistoryGetWithHttpInfo(@jakarta.annotation.Nonnull String bridgeWalletID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        ParameterizedTypeReference<BridgeWalletHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return walletsBridgeWalletIDHistoryGetRequestCreation(bridgeWalletID, limit, updatedAfterMs, updatedBeforeMs).toEntity(localVarReturnType);
    }

    /**
     * Get transaction history for a Bridge Wallet
     * Get the list of transactions involving this Bridge Wallet
     * <p><b>200</b> - List of transactions for this Bridge Wallet
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param bridgeWalletID The bridgeWalletID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec walletsBridgeWalletIDHistoryGetWithResponseSpec(@jakarta.annotation.Nonnull String bridgeWalletID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        return walletsBridgeWalletIDHistoryGetRequestCreation(bridgeWalletID, limit, updatedAfterMs, updatedBeforeMs);
    }

    /**
     * Get all Bridge Wallets
     * List of Bridge Wallets
     * <p><b>200</b> - List of Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a bridge wallet id. If this is specified, the next page that starts with a bridge wallet right AFTER the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets older than the specified bridge wallet id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a bridge wallet id. If this is specified, the previous page that ends with a bridge wallet right BEFORE the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets newer than the specified bridge wallet id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return BridgeWalletsList
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec walletsGetRequestCreation(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "starting_after", startingAfter));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ending_before", endingBefore));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<BridgeWalletsList> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/wallets", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all Bridge Wallets
     * List of Bridge Wallets
     * <p><b>200</b> - List of Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a bridge wallet id. If this is specified, the next page that starts with a bridge wallet right AFTER the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets older than the specified bridge wallet id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a bridge wallet id. If this is specified, the previous page that ends with a bridge wallet right BEFORE the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets newer than the specified bridge wallet id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return BridgeWalletsList
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public BridgeWalletsList walletsGet(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<BridgeWalletsList> localVarReturnType = new ParameterizedTypeReference<>() {};
        return walletsGetRequestCreation(limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Get all Bridge Wallets
     * List of Bridge Wallets
     * <p><b>200</b> - List of Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a bridge wallet id. If this is specified, the next page that starts with a bridge wallet right AFTER the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets older than the specified bridge wallet id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a bridge wallet id. If this is specified, the previous page that ends with a bridge wallet right BEFORE the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets newer than the specified bridge wallet id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;BridgeWalletsList&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<BridgeWalletsList> walletsGetWithHttpInfo(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<BridgeWalletsList> localVarReturnType = new ParameterizedTypeReference<>() {};
        return walletsGetRequestCreation(limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Get all Bridge Wallets
     * List of Bridge Wallets
     * <p><b>200</b> - List of Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a bridge wallet id. If this is specified, the next page that starts with a bridge wallet right AFTER the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets older than the specified bridge wallet id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a bridge wallet id. If this is specified, the previous page that ends with a bridge wallet right BEFORE the specified bridge wallet id on the bridge wallet timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that bridge wallets newer than the specified bridge wallet id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec walletsGetWithResponseSpec(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return walletsGetRequestCreation(limit, startingAfter, endingBefore);
    }

    /**
     * Get total balances of all Bridge Wallets
     * Get the total balances of all Bridge Wallets
     * <p><b>200</b> - Total balances of all Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return List&lt;BridgeWalletTotalBalance&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec walletsTotalBalancesGetRequestCreation() throws RestClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

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

        ParameterizedTypeReference<List<BridgeWalletTotalBalance>> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/wallets/total_balances", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get total balances of all Bridge Wallets
     * Get the total balances of all Bridge Wallets
     * <p><b>200</b> - Total balances of all Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return List&lt;BridgeWalletTotalBalance&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public List<BridgeWalletTotalBalance> walletsTotalBalancesGet() throws RestClientResponseException {
        ParameterizedTypeReference<List<BridgeWalletTotalBalance>> localVarReturnType = new ParameterizedTypeReference<>() {};
        return walletsTotalBalancesGetRequestCreation().body(localVarReturnType);
    }

    /**
     * Get total balances of all Bridge Wallets
     * Get the total balances of all Bridge Wallets
     * <p><b>200</b> - Total balances of all Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseEntity&lt;List&lt;BridgeWalletTotalBalance&gt;&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<BridgeWalletTotalBalance>> walletsTotalBalancesGetWithHttpInfo() throws RestClientResponseException {
        ParameterizedTypeReference<List<BridgeWalletTotalBalance>> localVarReturnType = new ParameterizedTypeReference<>() {};
        return walletsTotalBalancesGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Get total balances of all Bridge Wallets
     * Get the total balances of all Bridge Wallets
     * <p><b>200</b> - Total balances of all Bridge Wallets
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec walletsTotalBalancesGetWithResponseSpec() throws RestClientResponseException {
        return walletsTotalBalancesGetRequestCreation();
    }
}
