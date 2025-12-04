package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.CreateLiquidationAddress;
import org.jhely.money.sdk.bridge.model.CreateLiquidationAddressResponse;
import org.jhely.money.sdk.bridge.model.CustomersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet200Response;
import org.jhely.money.sdk.bridge.model.CustomersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet200Response;
import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.LiquidationAddress;
import org.jhely.money.sdk.bridge.model.LiquidationAddressHistory;
import org.jhely.money.sdk.bridge.model.LiquidationAddresses;
import org.jhely.money.sdk.bridge.model.UpdateLiquidationAddress;

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
public class LiquidationAddressesApi {
    private ApiClient apiClient;

    public LiquidationAddressesApi() {
        this(new ApiClient());
    }

    public LiquidationAddressesApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get all Liquidation Addresses for a customer
     * Get Liquidation Addresses
     * <p><b>200</b> - List of Liquidation Addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a liquidation address id. If this is specified, the next page that starts with a liquidation address right AFTER the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address older than the specified liquidation address id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a liquidation address id. If this is specified, the previous page that ends with a liquidation address right BEFORE the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address newer than the specified liquidation address id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return LiquidationAddresses
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDLiquidationAddressesGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDLiquidationAddressesGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<LiquidationAddresses> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/liquidation_addresses", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all Liquidation Addresses for a customer
     * Get Liquidation Addresses
     * <p><b>200</b> - List of Liquidation Addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a liquidation address id. If this is specified, the next page that starts with a liquidation address right AFTER the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address older than the specified liquidation address id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a liquidation address id. If this is specified, the previous page that ends with a liquidation address right BEFORE the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address newer than the specified liquidation address id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return LiquidationAddresses
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public LiquidationAddresses customersCustomerIDLiquidationAddressesGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<LiquidationAddresses> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesGetRequestCreation(customerID, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Get all Liquidation Addresses for a customer
     * Get Liquidation Addresses
     * <p><b>200</b> - List of Liquidation Addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a liquidation address id. If this is specified, the next page that starts with a liquidation address right AFTER the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address older than the specified liquidation address id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a liquidation address id. If this is specified, the previous page that ends with a liquidation address right BEFORE the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address newer than the specified liquidation address id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;LiquidationAddresses&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<LiquidationAddresses> customersCustomerIDLiquidationAddressesGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<LiquidationAddresses> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesGetRequestCreation(customerID, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Get all Liquidation Addresses for a customer
     * Get Liquidation Addresses
     * <p><b>200</b> - List of Liquidation Addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a liquidation address id. If this is specified, the next page that starts with a liquidation address right AFTER the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address older than the specified liquidation address id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a liquidation address id. If this is specified, the previous page that ends with a liquidation address right BEFORE the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address newer than the specified liquidation address id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDLiquidationAddressesGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return customersCustomerIDLiquidationAddressesGetRequestCreation(customerID, limit, startingAfter, endingBefore);
    }

    /**
     * Get the balance of a Liquidation Address (deprecated)
     * Get the balance of a Liquidation Address. Note that most Liquidation Addresses no longer hold a balance. To check recent activity on a Liquidation Address, use the &#x60;/customers/{customerID}/liquidation_addresses/{liquidationAddressID}/drains&#x60; endpoint.
     * <p><b>200</b> - Get the current balance of a Liquidation Address
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @return CustomersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     * @deprecated
     */
    @Deprecated
    private ResponseSpec customersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'liquidationAddressID' is set
        if (liquidationAddressID == null) {
            throw new RestClientResponseException("Missing the required parameter 'liquidationAddressID' when calling customersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("liquidationAddressID", liquidationAddressID);

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

        ParameterizedTypeReference<CustomersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/liquidation_addresses/{liquidationAddressID}/balances", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get the balance of a Liquidation Address (deprecated)
     * Get the balance of a Liquidation Address. Note that most Liquidation Addresses no longer hold a balance. To check recent activity on a Liquidation Address, use the &#x60;/customers/{customerID}/liquidation_addresses/{liquidationAddressID}/drains&#x60; endpoint.
     * <p><b>200</b> - Get the current balance of a Liquidation Address
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @return CustomersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CustomersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet200Response customersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGetRequestCreation(customerID, liquidationAddressID).body(localVarReturnType);
    }

    /**
     * Get the balance of a Liquidation Address (deprecated)
     * Get the balance of a Liquidation Address. Note that most Liquidation Addresses no longer hold a balance. To check recent activity on a Liquidation Address, use the &#x60;/customers/{customerID}/liquidation_addresses/{liquidationAddressID}/drains&#x60; endpoint.
     * <p><b>200</b> - Get the current balance of a Liquidation Address
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @return ResponseEntity&lt;CustomersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet200Response&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CustomersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet200Response> customersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGetRequestCreation(customerID, liquidationAddressID).toEntity(localVarReturnType);
    }

    /**
     * Get the balance of a Liquidation Address (deprecated)
     * Get the balance of a Liquidation Address. Note that most Liquidation Addresses no longer hold a balance. To check recent activity on a Liquidation Address, use the &#x60;/customers/{customerID}/liquidation_addresses/{liquidationAddressID}/drains&#x60; endpoint.
     * <p><b>200</b> - Get the current balance of a Liquidation Address
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID) throws RestClientResponseException {
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDBalancesGetRequestCreation(customerID, liquidationAddressID);
    }

    /**
     * Get drain history of a Liquidation Address
     * Get drain history of a Liquidation Address
     * <p><b>200</b> - List of drains (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a drain id. If this is specified, the next page that starts with a drain right AFTER the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains older than the specified drain id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a drain id. If this is specified, the previous page that ends with a drain right BEFORE the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains newer than the specified drain id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return CustomersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'liquidationAddressID' is set
        if (liquidationAddressID == null) {
            throw new RestClientResponseException("Missing the required parameter 'liquidationAddressID' when calling customersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("liquidationAddressID", liquidationAddressID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "starting_after", startingAfter));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ending_before", endingBefore));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_after_ms", updatedAfterMs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_before_ms", updatedBeforeMs));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<CustomersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/liquidation_addresses/{liquidationAddressID}/drains", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get drain history of a Liquidation Address
     * Get drain history of a Liquidation Address
     * <p><b>200</b> - List of drains (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a drain id. If this is specified, the next page that starts with a drain right AFTER the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains older than the specified drain id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a drain id. If this is specified, the previous page that ends with a drain right BEFORE the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains newer than the specified drain id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return CustomersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CustomersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet200Response customersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGetRequestCreation(customerID, liquidationAddressID, limit, startingAfter, endingBefore, updatedAfterMs, updatedBeforeMs).body(localVarReturnType);
    }

    /**
     * Get drain history of a Liquidation Address
     * Get drain history of a Liquidation Address
     * <p><b>200</b> - List of drains (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a drain id. If this is specified, the next page that starts with a drain right AFTER the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains older than the specified drain id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a drain id. If this is specified, the previous page that ends with a drain right BEFORE the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains newer than the specified drain id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return ResponseEntity&lt;CustomersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet200Response&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CustomersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet200Response> customersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGetRequestCreation(customerID, liquidationAddressID, limit, startingAfter, endingBefore, updatedAfterMs, updatedBeforeMs).toEntity(localVarReturnType);
    }

    /**
     * Get drain history of a Liquidation Address
     * Get drain history of a Liquidation Address
     * <p><b>200</b> - List of drains (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a drain id. If this is specified, the next page that starts with a drain right AFTER the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains older than the specified drain id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a drain id. If this is specified, the previous page that ends with a drain right BEFORE the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains newer than the specified drain id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDDrainsGetRequestCreation(customerID, liquidationAddressID, limit, startingAfter, endingBefore, updatedAfterMs, updatedBeforeMs);
    }

    /**
     * Get a Liquidation Address
     * Retrieve a Liquidation Address for the specified Liquidation Address ID
     * <p><b>200</b> - A Liquidation Address object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @return LiquidationAddress
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDLiquidationAddressesLiquidationAddressIDGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDLiquidationAddressesLiquidationAddressIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'liquidationAddressID' is set
        if (liquidationAddressID == null) {
            throw new RestClientResponseException("Missing the required parameter 'liquidationAddressID' when calling customersCustomerIDLiquidationAddressesLiquidationAddressIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("liquidationAddressID", liquidationAddressID);

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

        ParameterizedTypeReference<LiquidationAddress> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/liquidation_addresses/{liquidationAddressID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a Liquidation Address
     * Retrieve a Liquidation Address for the specified Liquidation Address ID
     * <p><b>200</b> - A Liquidation Address object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @return LiquidationAddress
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public LiquidationAddress customersCustomerIDLiquidationAddressesLiquidationAddressIDGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID) throws RestClientResponseException {
        ParameterizedTypeReference<LiquidationAddress> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDGetRequestCreation(customerID, liquidationAddressID).body(localVarReturnType);
    }

    /**
     * Get a Liquidation Address
     * Retrieve a Liquidation Address for the specified Liquidation Address ID
     * <p><b>200</b> - A Liquidation Address object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @return ResponseEntity&lt;LiquidationAddress&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<LiquidationAddress> customersCustomerIDLiquidationAddressesLiquidationAddressIDGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID) throws RestClientResponseException {
        ParameterizedTypeReference<LiquidationAddress> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDGetRequestCreation(customerID, liquidationAddressID).toEntity(localVarReturnType);
    }

    /**
     * Get a Liquidation Address
     * Retrieve a Liquidation Address for the specified Liquidation Address ID
     * <p><b>200</b> - A Liquidation Address object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDLiquidationAddressesLiquidationAddressIDGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID) throws RestClientResponseException {
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDGetRequestCreation(customerID, liquidationAddressID);
    }

    /**
     * Update a Liquidation Address
     * Update a Liquidation Address for the specified liquidation address ID. Note that &#x60;external_account_id&#x60; and &#x60;custom_developer_fee_percent&#x60; can be updated independently and are not both required.
     * <p><b>200</b> - Updated Liquidation Address object
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @param updateLiquidationAddress Liquidation Address details to be updated
     * @return LiquidationAddress
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDLiquidationAddressesLiquidationAddressIDPutRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID, @jakarta.annotation.Nonnull UpdateLiquidationAddress updateLiquidationAddress) throws RestClientResponseException {
        Object postBody = updateLiquidationAddress;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDLiquidationAddressesLiquidationAddressIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'liquidationAddressID' is set
        if (liquidationAddressID == null) {
            throw new RestClientResponseException("Missing the required parameter 'liquidationAddressID' when calling customersCustomerIDLiquidationAddressesLiquidationAddressIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'updateLiquidationAddress' is set
        if (updateLiquidationAddress == null) {
            throw new RestClientResponseException("Missing the required parameter 'updateLiquidationAddress' when calling customersCustomerIDLiquidationAddressesLiquidationAddressIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("liquidationAddressID", liquidationAddressID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<LiquidationAddress> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/liquidation_addresses/{liquidationAddressID}", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update a Liquidation Address
     * Update a Liquidation Address for the specified liquidation address ID. Note that &#x60;external_account_id&#x60; and &#x60;custom_developer_fee_percent&#x60; can be updated independently and are not both required.
     * <p><b>200</b> - Updated Liquidation Address object
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @param updateLiquidationAddress Liquidation Address details to be updated
     * @return LiquidationAddress
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public LiquidationAddress customersCustomerIDLiquidationAddressesLiquidationAddressIDPut(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID, @jakarta.annotation.Nonnull UpdateLiquidationAddress updateLiquidationAddress) throws RestClientResponseException {
        ParameterizedTypeReference<LiquidationAddress> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDPutRequestCreation(customerID, liquidationAddressID, updateLiquidationAddress).body(localVarReturnType);
    }

    /**
     * Update a Liquidation Address
     * Update a Liquidation Address for the specified liquidation address ID. Note that &#x60;external_account_id&#x60; and &#x60;custom_developer_fee_percent&#x60; can be updated independently and are not both required.
     * <p><b>200</b> - Updated Liquidation Address object
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @param updateLiquidationAddress Liquidation Address details to be updated
     * @return ResponseEntity&lt;LiquidationAddress&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<LiquidationAddress> customersCustomerIDLiquidationAddressesLiquidationAddressIDPutWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID, @jakarta.annotation.Nonnull UpdateLiquidationAddress updateLiquidationAddress) throws RestClientResponseException {
        ParameterizedTypeReference<LiquidationAddress> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDPutRequestCreation(customerID, liquidationAddressID, updateLiquidationAddress).toEntity(localVarReturnType);
    }

    /**
     * Update a Liquidation Address
     * Update a Liquidation Address for the specified liquidation address ID. Note that &#x60;external_account_id&#x60; and &#x60;custom_developer_fee_percent&#x60; can be updated independently and are not both required.
     * <p><b>200</b> - Updated Liquidation Address object
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param liquidationAddressID The liquidationAddressID parameter
     * @param updateLiquidationAddress Liquidation Address details to be updated
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDLiquidationAddressesLiquidationAddressIDPutWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String liquidationAddressID, @jakarta.annotation.Nonnull UpdateLiquidationAddress updateLiquidationAddress) throws RestClientResponseException {
        return customersCustomerIDLiquidationAddressesLiquidationAddressIDPutRequestCreation(customerID, liquidationAddressID, updateLiquidationAddress);
    }

    /**
     * Create a Liquidation Address
     * 
     * <p><b>201</b> - Liquidation Address object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createLiquidationAddress Liquidation Address object to be created
     * @return CreateLiquidationAddressResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDLiquidationAddressesPostRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateLiquidationAddress createLiquidationAddress) throws RestClientResponseException {
        Object postBody = createLiquidationAddress;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDLiquidationAddressesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDLiquidationAddressesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'createLiquidationAddress' is set
        if (createLiquidationAddress == null) {
            throw new RestClientResponseException("Missing the required parameter 'createLiquidationAddress' when calling customersCustomerIDLiquidationAddressesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<CreateLiquidationAddressResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/liquidation_addresses", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a Liquidation Address
     * 
     * <p><b>201</b> - Liquidation Address object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createLiquidationAddress Liquidation Address object to be created
     * @return CreateLiquidationAddressResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CreateLiquidationAddressResponse customersCustomerIDLiquidationAddressesPost(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateLiquidationAddress createLiquidationAddress) throws RestClientResponseException {
        ParameterizedTypeReference<CreateLiquidationAddressResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesPostRequestCreation(customerID, idempotencyKey, createLiquidationAddress).body(localVarReturnType);
    }

    /**
     * Create a Liquidation Address
     * 
     * <p><b>201</b> - Liquidation Address object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createLiquidationAddress Liquidation Address object to be created
     * @return ResponseEntity&lt;CreateLiquidationAddressResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CreateLiquidationAddressResponse> customersCustomerIDLiquidationAddressesPostWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateLiquidationAddress createLiquidationAddress) throws RestClientResponseException {
        ParameterizedTypeReference<CreateLiquidationAddressResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDLiquidationAddressesPostRequestCreation(customerID, idempotencyKey, createLiquidationAddress).toEntity(localVarReturnType);
    }

    /**
     * Create a Liquidation Address
     * 
     * <p><b>201</b> - Liquidation Address object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createLiquidationAddress Liquidation Address object to be created
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDLiquidationAddressesPostWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateLiquidationAddress createLiquidationAddress) throws RestClientResponseException {
        return customersCustomerIDLiquidationAddressesPostRequestCreation(customerID, idempotencyKey, createLiquidationAddress);
    }

    /**
     * Liquidation Address Activity Across All Customers
     * History of activity across all customers and Liquidation Addresses
     * <p><b>200</b> - List of drains for all liquidation addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a drain id. If this is specified, the next page that starts with a drain right AFTER the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains older than the specified drain id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a drain id. If this is specified, the previous page that ends with a drain right BEFORE the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains newer than the specified drain id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return LiquidationAddressHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec liquidationAddressesDrainsGetRequestCreation(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
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
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_after_ms", updatedAfterMs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_before_ms", updatedBeforeMs));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<LiquidationAddressHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/liquidation_addresses/drains", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Liquidation Address Activity Across All Customers
     * History of activity across all customers and Liquidation Addresses
     * <p><b>200</b> - List of drains for all liquidation addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a drain id. If this is specified, the next page that starts with a drain right AFTER the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains older than the specified drain id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a drain id. If this is specified, the previous page that ends with a drain right BEFORE the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains newer than the specified drain id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return LiquidationAddressHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public LiquidationAddressHistory liquidationAddressesDrainsGet(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        ParameterizedTypeReference<LiquidationAddressHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return liquidationAddressesDrainsGetRequestCreation(limit, startingAfter, endingBefore, updatedAfterMs, updatedBeforeMs).body(localVarReturnType);
    }

    /**
     * Liquidation Address Activity Across All Customers
     * History of activity across all customers and Liquidation Addresses
     * <p><b>200</b> - List of drains for all liquidation addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a drain id. If this is specified, the next page that starts with a drain right AFTER the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains older than the specified drain id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a drain id. If this is specified, the previous page that ends with a drain right BEFORE the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains newer than the specified drain id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return ResponseEntity&lt;LiquidationAddressHistory&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<LiquidationAddressHistory> liquidationAddressesDrainsGetWithHttpInfo(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        ParameterizedTypeReference<LiquidationAddressHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return liquidationAddressesDrainsGetRequestCreation(limit, startingAfter, endingBefore, updatedAfterMs, updatedBeforeMs).toEntity(localVarReturnType);
    }

    /**
     * Liquidation Address Activity Across All Customers
     * History of activity across all customers and Liquidation Addresses
     * <p><b>200</b> - List of drains for all liquidation addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a drain id. If this is specified, the next page that starts with a drain right AFTER the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains older than the specified drain id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a drain id. If this is specified, the previous page that ends with a drain right BEFORE the specified drain id on the drain timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that drains newer than the specified drain id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec liquidationAddressesDrainsGetWithResponseSpec(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        return liquidationAddressesDrainsGetRequestCreation(limit, startingAfter, endingBefore, updatedAfterMs, updatedBeforeMs);
    }

    /**
     * Get all Liquidation Addresses
     * Get Liquidation Addresses
     * <p><b>200</b> - List of Liquidation Addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a liquidation address id. If this is specified, the next page that starts with a liquidation address right AFTER the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address older than the specified liquidation address id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a liquidation address id. If this is specified, the previous page that ends with a liquidation address right BEFORE the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address newer than the specified liquidation address id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return LiquidationAddresses
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec liquidationAddressesGetRequestCreation(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
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

        ParameterizedTypeReference<LiquidationAddresses> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/liquidation_addresses", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all Liquidation Addresses
     * Get Liquidation Addresses
     * <p><b>200</b> - List of Liquidation Addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a liquidation address id. If this is specified, the next page that starts with a liquidation address right AFTER the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address older than the specified liquidation address id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a liquidation address id. If this is specified, the previous page that ends with a liquidation address right BEFORE the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address newer than the specified liquidation address id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return LiquidationAddresses
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public LiquidationAddresses liquidationAddressesGet(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<LiquidationAddresses> localVarReturnType = new ParameterizedTypeReference<>() {};
        return liquidationAddressesGetRequestCreation(limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Get all Liquidation Addresses
     * Get Liquidation Addresses
     * <p><b>200</b> - List of Liquidation Addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a liquidation address id. If this is specified, the next page that starts with a liquidation address right AFTER the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address older than the specified liquidation address id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a liquidation address id. If this is specified, the previous page that ends with a liquidation address right BEFORE the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address newer than the specified liquidation address id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;LiquidationAddresses&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<LiquidationAddresses> liquidationAddressesGetWithHttpInfo(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<LiquidationAddresses> localVarReturnType = new ParameterizedTypeReference<>() {};
        return liquidationAddressesGetRequestCreation(limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Get all Liquidation Addresses
     * Get Liquidation Addresses
     * <p><b>200</b> - List of Liquidation Addresses (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a liquidation address id. If this is specified, the next page that starts with a liquidation address right AFTER the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address older than the specified liquidation address id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a liquidation address id. If this is specified, the previous page that ends with a liquidation address right BEFORE the specified liquidation address id on the liquidation address timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that liquidation address newer than the specified liquidation address id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec liquidationAddressesGetWithResponseSpec(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return liquidationAddressesGetRequestCreation(limit, startingAfter, endingBefore);
    }
}
