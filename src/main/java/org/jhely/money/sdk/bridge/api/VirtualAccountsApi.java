package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.CreateVirtualAccount;
import org.jhely.money.sdk.bridge.model.CustomersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost200Response;
import org.jhely.money.sdk.bridge.model.UpdateVirtualAccount;
import org.jhely.money.sdk.bridge.model.VirtualAccountActivationStatus;
import org.jhely.money.sdk.bridge.model.VirtualAccountHistory;
import org.jhely.money.sdk.bridge.model.VirtualAccountResponse;
import org.jhely.money.sdk.bridge.model.VirtualAccounts;

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
public class VirtualAccountsApi {
    private ApiClient apiClient;

    public VirtualAccountsApi() {
        this(new ApiClient());
    }

    public VirtualAccountsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * List Virtual Accounts by Customer
     * List all Virtual Account objects for a customer
     * <p><b>200</b> - List of Virtual Accounts
     * @param customerID The customerID parameter
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a virtual account id. If this is specified, the next page that starts with a virtual account right AFTER the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account older than the specified virtual account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a virtual account id. If this is specified, the previous page that ends with a virtual account right BEFORE the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account newer than the specified virtual account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return VirtualAccounts
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDVirtualAccountsGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDVirtualAccountsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "status", status));
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

        ParameterizedTypeReference<VirtualAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/virtual_accounts", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * List Virtual Accounts by Customer
     * List all Virtual Account objects for a customer
     * <p><b>200</b> - List of Virtual Accounts
     * @param customerID The customerID parameter
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a virtual account id. If this is specified, the next page that starts with a virtual account right AFTER the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account older than the specified virtual account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a virtual account id. If this is specified, the previous page that ends with a virtual account right BEFORE the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account newer than the specified virtual account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return VirtualAccounts
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public VirtualAccounts customersCustomerIDVirtualAccountsGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsGetRequestCreation(customerID, status, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * List Virtual Accounts by Customer
     * List all Virtual Account objects for a customer
     * <p><b>200</b> - List of Virtual Accounts
     * @param customerID The customerID parameter
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a virtual account id. If this is specified, the next page that starts with a virtual account right AFTER the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account older than the specified virtual account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a virtual account id. If this is specified, the previous page that ends with a virtual account right BEFORE the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account newer than the specified virtual account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;VirtualAccounts&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<VirtualAccounts> customersCustomerIDVirtualAccountsGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsGetRequestCreation(customerID, status, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * List Virtual Accounts by Customer
     * List all Virtual Account objects for a customer
     * <p><b>200</b> - List of Virtual Accounts
     * @param customerID The customerID parameter
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a virtual account id. If this is specified, the next page that starts with a virtual account right AFTER the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account older than the specified virtual account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a virtual account id. If this is specified, the previous page that ends with a virtual account right BEFORE the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account newer than the specified virtual account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDVirtualAccountsGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return customersCustomerIDVirtualAccountsGetRequestCreation(customerID, status, limit, startingAfter, endingBefore);
    }

    /**
     * Create a Virtual Account
     * Create a Virtual Account or Virtual IBAN for the specified customer
     * <p><b>200</b> - Virtual Account object created
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param createVirtualAccount Virtual Account object to be created
     * @return VirtualAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDVirtualAccountsPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CreateVirtualAccount createVirtualAccount) throws RestClientResponseException {
        Object postBody = createVirtualAccount;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDVirtualAccountsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDVirtualAccountsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'createVirtualAccount' is set
        if (createVirtualAccount == null) {
            throw new RestClientResponseException("Missing the required parameter 'createVirtualAccount' when calling customersCustomerIDVirtualAccountsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/virtual_accounts", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a Virtual Account
     * Create a Virtual Account or Virtual IBAN for the specified customer
     * <p><b>200</b> - Virtual Account object created
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param createVirtualAccount Virtual Account object to be created
     * @return VirtualAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public VirtualAccountResponse customersCustomerIDVirtualAccountsPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CreateVirtualAccount createVirtualAccount) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsPostRequestCreation(idempotencyKey, customerID, createVirtualAccount).body(localVarReturnType);
    }

    /**
     * Create a Virtual Account
     * Create a Virtual Account or Virtual IBAN for the specified customer
     * <p><b>200</b> - Virtual Account object created
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param createVirtualAccount Virtual Account object to be created
     * @return ResponseEntity&lt;VirtualAccountResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<VirtualAccountResponse> customersCustomerIDVirtualAccountsPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CreateVirtualAccount createVirtualAccount) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsPostRequestCreation(idempotencyKey, customerID, createVirtualAccount).toEntity(localVarReturnType);
    }

    /**
     * Create a Virtual Account
     * Create a Virtual Account or Virtual IBAN for the specified customer
     * <p><b>200</b> - Virtual Account object created
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param createVirtualAccount Virtual Account object to be created
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDVirtualAccountsPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CreateVirtualAccount createVirtualAccount) throws RestClientResponseException {
        return customersCustomerIDVirtualAccountsPostRequestCreation(idempotencyKey, customerID, createVirtualAccount);
    }

    /**
     * Deactivate a Virtual Account
     * Deactivate a Virtual Account to prevent it from acceping new incoming transactions
     * <p><b>200</b> - Deactivated Virtual Account object
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return CustomersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'virtualAccountID' is set
        if (virtualAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'virtualAccountID' when calling customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("virtualAccountID", virtualAccountID);

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
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<CustomersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/virtual_accounts/{virtualAccountID}/deactivate", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Deactivate a Virtual Account
     * Deactivate a Virtual Account to prevent it from acceping new incoming transactions
     * <p><b>200</b> - Deactivated Virtual Account object
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return CustomersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CustomersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost200Response customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePostRequestCreation(idempotencyKey, customerID, virtualAccountID).body(localVarReturnType);
    }

    /**
     * Deactivate a Virtual Account
     * Deactivate a Virtual Account to prevent it from acceping new incoming transactions
     * <p><b>200</b> - Deactivated Virtual Account object
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return ResponseEntity&lt;CustomersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost200Response&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CustomersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost200Response> customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePostRequestCreation(idempotencyKey, customerID, virtualAccountID).toEntity(localVarReturnType);
    }

    /**
     * Deactivate a Virtual Account
     * Deactivate a Virtual Account to prevent it from acceping new incoming transactions
     * <p><b>200</b> - Deactivated Virtual Account object
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        return customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePostRequestCreation(idempotencyKey, customerID, virtualAccountID);
    }

    /**
     * Get a Virtual Account
     * Retrieve the Virtual Account object from the passed ID
     * <p><b>200</b> - Successful Virtual Account object response
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return VirtualAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDVirtualAccountsVirtualAccountIDGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDVirtualAccountsVirtualAccountIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'virtualAccountID' is set
        if (virtualAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'virtualAccountID' when calling customersCustomerIDVirtualAccountsVirtualAccountIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("virtualAccountID", virtualAccountID);

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

        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/virtual_accounts/{virtualAccountID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a Virtual Account
     * Retrieve the Virtual Account object from the passed ID
     * <p><b>200</b> - Successful Virtual Account object response
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return VirtualAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public VirtualAccountResponse customersCustomerIDVirtualAccountsVirtualAccountIDGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsVirtualAccountIDGetRequestCreation(customerID, virtualAccountID).body(localVarReturnType);
    }

    /**
     * Get a Virtual Account
     * Retrieve the Virtual Account object from the passed ID
     * <p><b>200</b> - Successful Virtual Account object response
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return ResponseEntity&lt;VirtualAccountResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<VirtualAccountResponse> customersCustomerIDVirtualAccountsVirtualAccountIDGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsVirtualAccountIDGetRequestCreation(customerID, virtualAccountID).toEntity(localVarReturnType);
    }

    /**
     * Get a Virtual Account
     * Retrieve the Virtual Account object from the passed ID
     * <p><b>200</b> - Successful Virtual Account object response
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDVirtualAccountsVirtualAccountIDGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        return customersCustomerIDVirtualAccountsVirtualAccountIDGetRequestCreation(customerID, virtualAccountID);
    }

    /**
     * Virtual Account Activity
     * History of activity for a Virtual Account
     * <p><b>200</b> - List of Virtual Account events
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @return VirtualAccountHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDVirtualAccountsVirtualAccountIDHistoryGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID, @jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDVirtualAccountsVirtualAccountIDHistoryGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'virtualAccountID' is set
        if (virtualAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'virtualAccountID' when calling customersCustomerIDVirtualAccountsVirtualAccountIDHistoryGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("virtualAccountID", virtualAccountID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "deposit_id", depositId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("multi".toUpperCase(Locale.ROOT)), "deposit_ids", depositIds));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "tx_hash", txHash));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "starting_after", startingAfter));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ending_before", endingBefore));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "event_type", eventType));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<VirtualAccountHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/virtual_accounts/{virtualAccountID}/history", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Virtual Account Activity
     * History of activity for a Virtual Account
     * <p><b>200</b> - List of Virtual Account events
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @return VirtualAccountHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public VirtualAccountHistory customersCustomerIDVirtualAccountsVirtualAccountIDHistoryGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID, @jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsVirtualAccountIDHistoryGetRequestCreation(customerID, virtualAccountID, depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType).body(localVarReturnType);
    }

    /**
     * Virtual Account Activity
     * History of activity for a Virtual Account
     * <p><b>200</b> - List of Virtual Account events
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @return ResponseEntity&lt;VirtualAccountHistory&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<VirtualAccountHistory> customersCustomerIDVirtualAccountsVirtualAccountIDHistoryGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID, @jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsVirtualAccountIDHistoryGetRequestCreation(customerID, virtualAccountID, depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType).toEntity(localVarReturnType);
    }

    /**
     * Virtual Account Activity
     * History of activity for a Virtual Account
     * <p><b>200</b> - List of Virtual Account events
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDVirtualAccountsVirtualAccountIDHistoryGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID, @jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        return customersCustomerIDVirtualAccountsVirtualAccountIDHistoryGetRequestCreation(customerID, virtualAccountID, depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType);
    }

    /**
     * Update a Virtual Account
     * Update instructions for an existing Virtual Account
     * <p><b>200</b> - Updated Virtual Account object
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @param updateVirtualAccount The Virtual Account details to be updated
     * @return VirtualAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDVirtualAccountsVirtualAccountIDPutRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID, @jakarta.annotation.Nonnull UpdateVirtualAccount updateVirtualAccount) throws RestClientResponseException {
        Object postBody = updateVirtualAccount;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDVirtualAccountsVirtualAccountIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'virtualAccountID' is set
        if (virtualAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'virtualAccountID' when calling customersCustomerIDVirtualAccountsVirtualAccountIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'updateVirtualAccount' is set
        if (updateVirtualAccount == null) {
            throw new RestClientResponseException("Missing the required parameter 'updateVirtualAccount' when calling customersCustomerIDVirtualAccountsVirtualAccountIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("virtualAccountID", virtualAccountID);

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

        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/virtual_accounts/{virtualAccountID}", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update a Virtual Account
     * Update instructions for an existing Virtual Account
     * <p><b>200</b> - Updated Virtual Account object
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @param updateVirtualAccount The Virtual Account details to be updated
     * @return VirtualAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public VirtualAccountResponse customersCustomerIDVirtualAccountsVirtualAccountIDPut(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID, @jakarta.annotation.Nonnull UpdateVirtualAccount updateVirtualAccount) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsVirtualAccountIDPutRequestCreation(customerID, virtualAccountID, updateVirtualAccount).body(localVarReturnType);
    }

    /**
     * Update a Virtual Account
     * Update instructions for an existing Virtual Account
     * <p><b>200</b> - Updated Virtual Account object
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @param updateVirtualAccount The Virtual Account details to be updated
     * @return ResponseEntity&lt;VirtualAccountResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<VirtualAccountResponse> customersCustomerIDVirtualAccountsVirtualAccountIDPutWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID, @jakarta.annotation.Nonnull UpdateVirtualAccount updateVirtualAccount) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsVirtualAccountIDPutRequestCreation(customerID, virtualAccountID, updateVirtualAccount).toEntity(localVarReturnType);
    }

    /**
     * Update a Virtual Account
     * Update instructions for an existing Virtual Account
     * <p><b>200</b> - Updated Virtual Account object
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @param updateVirtualAccount The Virtual Account details to be updated
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDVirtualAccountsVirtualAccountIDPutWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID, @jakarta.annotation.Nonnull UpdateVirtualAccount updateVirtualAccount) throws RestClientResponseException {
        return customersCustomerIDVirtualAccountsVirtualAccountIDPutRequestCreation(customerID, virtualAccountID, updateVirtualAccount);
    }

    /**
     * Reactivate a Virtual Account
     * Reactivate a previously deactivated Virtual Account
     * <p><b>200</b> - Reactivated Virtual Account object
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return VirtualAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'virtualAccountID' is set
        if (virtualAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'virtualAccountID' when calling customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("virtualAccountID", virtualAccountID);

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
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/virtual_accounts/{virtualAccountID}/reactivate", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Reactivate a Virtual Account
     * Reactivate a previously deactivated Virtual Account
     * <p><b>200</b> - Reactivated Virtual Account object
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return VirtualAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public VirtualAccountResponse customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePostRequestCreation(idempotencyKey, customerID, virtualAccountID).body(localVarReturnType);
    }

    /**
     * Reactivate a Virtual Account
     * Reactivate a previously deactivated Virtual Account
     * <p><b>200</b> - Reactivated Virtual Account object
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return ResponseEntity&lt;VirtualAccountResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<VirtualAccountResponse> customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePostRequestCreation(idempotencyKey, customerID, virtualAccountID).toEntity(localVarReturnType);
    }

    /**
     * Reactivate a Virtual Account
     * Reactivate a previously deactivated Virtual Account
     * <p><b>200</b> - Reactivated Virtual Account object
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param virtualAccountID The virtualAccountID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String virtualAccountID) throws RestClientResponseException {
        return customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePostRequestCreation(idempotencyKey, customerID, virtualAccountID);
    }

    /**
     * List Virtual Accounts
     * List all Virtual Account objects
     * <p><b>200</b> - List of Virtual Accounts
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a virtual account id. If this is specified, the next page that starts with a virtual account right AFTER the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account older than the specified virtual account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a virtual account id. If this is specified, the previous page that ends with a virtual account right BEFORE the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account newer than the specified virtual account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return VirtualAccounts
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec virtualAccountsGetRequestCreation(@jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "status", status));
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

        ParameterizedTypeReference<VirtualAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/virtual_accounts", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * List Virtual Accounts
     * List all Virtual Account objects
     * <p><b>200</b> - List of Virtual Accounts
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a virtual account id. If this is specified, the next page that starts with a virtual account right AFTER the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account older than the specified virtual account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a virtual account id. If this is specified, the previous page that ends with a virtual account right BEFORE the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account newer than the specified virtual account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return VirtualAccounts
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public VirtualAccounts virtualAccountsGet(@jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return virtualAccountsGetRequestCreation(status, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * List Virtual Accounts
     * List all Virtual Account objects
     * <p><b>200</b> - List of Virtual Accounts
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a virtual account id. If this is specified, the next page that starts with a virtual account right AFTER the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account older than the specified virtual account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a virtual account id. If this is specified, the previous page that ends with a virtual account right BEFORE the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account newer than the specified virtual account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;VirtualAccounts&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<VirtualAccounts> virtualAccountsGetWithHttpInfo(@jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return virtualAccountsGetRequestCreation(status, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * List Virtual Accounts
     * List all Virtual Account objects
     * <p><b>200</b> - List of Virtual Accounts
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a virtual account id. If this is specified, the next page that starts with a virtual account right AFTER the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account older than the specified virtual account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a virtual account id. If this is specified, the previous page that ends with a virtual account right BEFORE the specified virtual account id on the virtual account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that virtual account newer than the specified virtual account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec virtualAccountsGetWithResponseSpec(@jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return virtualAccountsGetRequestCreation(status, limit, startingAfter, endingBefore);
    }

    /**
     * Virtual Account Activity Across All Customers
     * History of activity across all customers and Virtual Accounts
     * <p><b>200</b> - List of Virtual Account events
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return VirtualAccountHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec virtualAccountsHistoryGetRequestCreation(@jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "deposit_id", depositId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("multi".toUpperCase(Locale.ROOT)), "deposit_ids", depositIds));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "tx_hash", txHash));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "starting_after", startingAfter));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ending_before", endingBefore));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "event_type", eventType));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_after_ms", updatedAfterMs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_before_ms", updatedBeforeMs));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<VirtualAccountHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/virtual_accounts/history", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Virtual Account Activity Across All Customers
     * History of activity across all customers and Virtual Accounts
     * <p><b>200</b> - List of Virtual Account events
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return VirtualAccountHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public VirtualAccountHistory virtualAccountsHistoryGet(@jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return virtualAccountsHistoryGetRequestCreation(depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType, updatedAfterMs, updatedBeforeMs).body(localVarReturnType);
    }

    /**
     * Virtual Account Activity Across All Customers
     * History of activity across all customers and Virtual Accounts
     * <p><b>200</b> - List of Virtual Account events
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return ResponseEntity&lt;VirtualAccountHistory&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<VirtualAccountHistory> virtualAccountsHistoryGetWithHttpInfo(@jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        ParameterizedTypeReference<VirtualAccountHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return virtualAccountsHistoryGetRequestCreation(depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType, updatedAfterMs, updatedBeforeMs).toEntity(localVarReturnType);
    }

    /**
     * Virtual Account Activity Across All Customers
     * History of activity across all customers and Virtual Accounts
     * <p><b>200</b> - List of Virtual Account events
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec virtualAccountsHistoryGetWithResponseSpec(@jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs) throws RestClientResponseException {
        return virtualAccountsHistoryGetRequestCreation(depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType, updatedAfterMs, updatedBeforeMs);
    }
}
