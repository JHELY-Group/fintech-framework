package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.CreateStaticMemo;
import org.jhely.money.sdk.bridge.model.StaticMemoHistory;
import org.jhely.money.sdk.bridge.model.StaticMemoResponse;
import org.jhely.money.sdk.bridge.model.StaticMemos;
import org.jhely.money.sdk.bridge.model.UpdateStaticMemo;
import org.jhely.money.sdk.bridge.model.VirtualAccountActivationStatus;

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
public class StaticMemosApi {
    private ApiClient apiClient;

    public StaticMemosApi() {
        this(new ApiClient());
    }

    public StaticMemosApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * List Static Memos for Customer
     * List all Static Memo objects for a customer
     * <p><b>200</b> - List of Static Memos
     * @param customerID The customerID parameter
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a static memo id. If this is specified, the next page that starts with a static memo right AFTER the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo older than the specified static memo id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a static memo id. If this is specified, the previous page that ends with a static memo right BEFORE the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo newer than the specified static memo id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return StaticMemos
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDStaticMemosGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDStaticMemosGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<StaticMemos> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/static_memos", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * List Static Memos for Customer
     * List all Static Memo objects for a customer
     * <p><b>200</b> - List of Static Memos
     * @param customerID The customerID parameter
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a static memo id. If this is specified, the next page that starts with a static memo right AFTER the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo older than the specified static memo id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a static memo id. If this is specified, the previous page that ends with a static memo right BEFORE the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo newer than the specified static memo id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return StaticMemos
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public StaticMemos customersCustomerIDStaticMemosGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemos> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticMemosGetRequestCreation(customerID, status, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * List Static Memos for Customer
     * List all Static Memo objects for a customer
     * <p><b>200</b> - List of Static Memos
     * @param customerID The customerID parameter
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a static memo id. If this is specified, the next page that starts with a static memo right AFTER the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo older than the specified static memo id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a static memo id. If this is specified, the previous page that ends with a static memo right BEFORE the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo newer than the specified static memo id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;StaticMemos&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StaticMemos> customersCustomerIDStaticMemosGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemos> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticMemosGetRequestCreation(customerID, status, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * List Static Memos for Customer
     * List all Static Memo objects for a customer
     * <p><b>200</b> - List of Static Memos
     * @param customerID The customerID parameter
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a static memo id. If this is specified, the next page that starts with a static memo right AFTER the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo older than the specified static memo id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a static memo id. If this is specified, the previous page that ends with a static memo right BEFORE the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo newer than the specified static memo id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDStaticMemosGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return customersCustomerIDStaticMemosGetRequestCreation(customerID, status, limit, startingAfter, endingBefore);
    }

    /**
     * Create a Static Memo
     * Create a Static Memo for the specified customer
     * <p><b>200</b> - Static Memo object created
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param createStaticMemo Static Memo object to be created
     * @return StaticMemoResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDStaticMemosPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CreateStaticMemo createStaticMemo) throws RestClientResponseException {
        Object postBody = createStaticMemo;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDStaticMemosPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDStaticMemosPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'createStaticMemo' is set
        if (createStaticMemo == null) {
            throw new RestClientResponseException("Missing the required parameter 'createStaticMemo' when calling customersCustomerIDStaticMemosPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<StaticMemoResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/static_memos", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a Static Memo
     * Create a Static Memo for the specified customer
     * <p><b>200</b> - Static Memo object created
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param createStaticMemo Static Memo object to be created
     * @return StaticMemoResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public StaticMemoResponse customersCustomerIDStaticMemosPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CreateStaticMemo createStaticMemo) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemoResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticMemosPostRequestCreation(idempotencyKey, customerID, createStaticMemo).body(localVarReturnType);
    }

    /**
     * Create a Static Memo
     * Create a Static Memo for the specified customer
     * <p><b>200</b> - Static Memo object created
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param createStaticMemo Static Memo object to be created
     * @return ResponseEntity&lt;StaticMemoResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StaticMemoResponse> customersCustomerIDStaticMemosPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CreateStaticMemo createStaticMemo) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemoResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticMemosPostRequestCreation(idempotencyKey, customerID, createStaticMemo).toEntity(localVarReturnType);
    }

    /**
     * Create a Static Memo
     * Create a Static Memo for the specified customer
     * <p><b>200</b> - Static Memo object created
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param createStaticMemo Static Memo object to be created
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDStaticMemosPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CreateStaticMemo createStaticMemo) throws RestClientResponseException {
        return customersCustomerIDStaticMemosPostRequestCreation(idempotencyKey, customerID, createStaticMemo);
    }

    /**
     * Get a Static Memo
     * Retrieve the Static Memo object from the passed ID
     * <p><b>200</b> - Successful static memo object response
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @return StaticMemoResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDStaticMemosStaticMemoIDGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDStaticMemosStaticMemoIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'staticMemoID' is set
        if (staticMemoID == null) {
            throw new RestClientResponseException("Missing the required parameter 'staticMemoID' when calling customersCustomerIDStaticMemosStaticMemoIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("staticMemoID", staticMemoID);

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

        ParameterizedTypeReference<StaticMemoResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/static_memos/{staticMemoID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a Static Memo
     * Retrieve the Static Memo object from the passed ID
     * <p><b>200</b> - Successful static memo object response
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @return StaticMemoResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public StaticMemoResponse customersCustomerIDStaticMemosStaticMemoIDGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemoResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticMemosStaticMemoIDGetRequestCreation(customerID, staticMemoID).body(localVarReturnType);
    }

    /**
     * Get a Static Memo
     * Retrieve the Static Memo object from the passed ID
     * <p><b>200</b> - Successful static memo object response
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @return ResponseEntity&lt;StaticMemoResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StaticMemoResponse> customersCustomerIDStaticMemosStaticMemoIDGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemoResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticMemosStaticMemoIDGetRequestCreation(customerID, staticMemoID).toEntity(localVarReturnType);
    }

    /**
     * Get a Static Memo
     * Retrieve the Static Memo object from the passed ID
     * <p><b>200</b> - Successful static memo object response
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDStaticMemosStaticMemoIDGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID) throws RestClientResponseException {
        return customersCustomerIDStaticMemosStaticMemoIDGetRequestCreation(customerID, staticMemoID);
    }

    /**
     * Static Memo Activity
     * History of activity for a Static Memo
     * <p><b>200</b> - List of Static Memo events
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @return StaticMemoHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDStaticMemosStaticMemoIDHistoryGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID, @jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDStaticMemosStaticMemoIDHistoryGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'staticMemoID' is set
        if (staticMemoID == null) {
            throw new RestClientResponseException("Missing the required parameter 'staticMemoID' when calling customersCustomerIDStaticMemosStaticMemoIDHistoryGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("staticMemoID", staticMemoID);

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

        ParameterizedTypeReference<StaticMemoHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/static_memos/{staticMemoID}/history", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Static Memo Activity
     * History of activity for a Static Memo
     * <p><b>200</b> - List of Static Memo events
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @return StaticMemoHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public StaticMemoHistory customersCustomerIDStaticMemosStaticMemoIDHistoryGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID, @jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemoHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticMemosStaticMemoIDHistoryGetRequestCreation(customerID, staticMemoID, depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType).body(localVarReturnType);
    }

    /**
     * Static Memo Activity
     * History of activity for a Static Memo
     * <p><b>200</b> - List of Static Memo events
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @return ResponseEntity&lt;StaticMemoHistory&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StaticMemoHistory> customersCustomerIDStaticMemosStaticMemoIDHistoryGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID, @jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemoHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticMemosStaticMemoIDHistoryGetRequestCreation(customerID, staticMemoID, depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType).toEntity(localVarReturnType);
    }

    /**
     * Static Memo Activity
     * History of activity for a Static Memo
     * <p><b>200</b> - List of Static Memo events
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
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
    public ResponseSpec customersCustomerIDStaticMemosStaticMemoIDHistoryGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID, @jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        return customersCustomerIDStaticMemosStaticMemoIDHistoryGetRequestCreation(customerID, staticMemoID, depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType);
    }

    /**
     * Update a Static Memo
     * Update instructions for an existing Static Memo
     * <p><b>200</b> - Updated Static Memo object
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @param updateStaticMemo The Static Memo details to be updated
     * @return StaticMemoResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDStaticMemosStaticMemoIDPutRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID, @jakarta.annotation.Nonnull UpdateStaticMemo updateStaticMemo) throws RestClientResponseException {
        Object postBody = updateStaticMemo;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDStaticMemosStaticMemoIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'staticMemoID' is set
        if (staticMemoID == null) {
            throw new RestClientResponseException("Missing the required parameter 'staticMemoID' when calling customersCustomerIDStaticMemosStaticMemoIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'updateStaticMemo' is set
        if (updateStaticMemo == null) {
            throw new RestClientResponseException("Missing the required parameter 'updateStaticMemo' when calling customersCustomerIDStaticMemosStaticMemoIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("staticMemoID", staticMemoID);

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

        ParameterizedTypeReference<StaticMemoResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/static_memos/{staticMemoID}", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update a Static Memo
     * Update instructions for an existing Static Memo
     * <p><b>200</b> - Updated Static Memo object
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @param updateStaticMemo The Static Memo details to be updated
     * @return StaticMemoResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public StaticMemoResponse customersCustomerIDStaticMemosStaticMemoIDPut(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID, @jakarta.annotation.Nonnull UpdateStaticMemo updateStaticMemo) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemoResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticMemosStaticMemoIDPutRequestCreation(customerID, staticMemoID, updateStaticMemo).body(localVarReturnType);
    }

    /**
     * Update a Static Memo
     * Update instructions for an existing Static Memo
     * <p><b>200</b> - Updated Static Memo object
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @param updateStaticMemo The Static Memo details to be updated
     * @return ResponseEntity&lt;StaticMemoResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StaticMemoResponse> customersCustomerIDStaticMemosStaticMemoIDPutWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID, @jakarta.annotation.Nonnull UpdateStaticMemo updateStaticMemo) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemoResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticMemosStaticMemoIDPutRequestCreation(customerID, staticMemoID, updateStaticMemo).toEntity(localVarReturnType);
    }

    /**
     * Update a Static Memo
     * Update instructions for an existing Static Memo
     * <p><b>200</b> - Updated Static Memo object
     * @param customerID The customerID parameter
     * @param staticMemoID The staticMemoID parameter
     * @param updateStaticMemo The Static Memo details to be updated
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDStaticMemosStaticMemoIDPutWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String staticMemoID, @jakarta.annotation.Nonnull UpdateStaticMemo updateStaticMemo) throws RestClientResponseException {
        return customersCustomerIDStaticMemosStaticMemoIDPutRequestCreation(customerID, staticMemoID, updateStaticMemo);
    }

    /**
     * List Static Memos
     * List all Static Memo objects
     * <p><b>200</b> - List of Static Memos
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a static memo id. If this is specified, the next page that starts with a static memo right AFTER the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo older than the specified static memo id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a static memo id. If this is specified, the previous page that ends with a static memo right BEFORE the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo newer than the specified static memo id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return StaticMemos
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec staticMemosGetRequestCreation(@jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
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

        ParameterizedTypeReference<StaticMemos> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/static_memos", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * List Static Memos
     * List all Static Memo objects
     * <p><b>200</b> - List of Static Memos
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a static memo id. If this is specified, the next page that starts with a static memo right AFTER the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo older than the specified static memo id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a static memo id. If this is specified, the previous page that ends with a static memo right BEFORE the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo newer than the specified static memo id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return StaticMemos
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public StaticMemos staticMemosGet(@jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemos> localVarReturnType = new ParameterizedTypeReference<>() {};
        return staticMemosGetRequestCreation(status, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * List Static Memos
     * List all Static Memo objects
     * <p><b>200</b> - List of Static Memos
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a static memo id. If this is specified, the next page that starts with a static memo right AFTER the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo older than the specified static memo id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a static memo id. If this is specified, the previous page that ends with a static memo right BEFORE the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo newer than the specified static memo id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;StaticMemos&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StaticMemos> staticMemosGetWithHttpInfo(@jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemos> localVarReturnType = new ParameterizedTypeReference<>() {};
        return staticMemosGetRequestCreation(status, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * List Static Memos
     * List all Static Memo objects
     * <p><b>200</b> - List of Static Memos
     * @param status Limit results to those with the given activation status
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a static memo id. If this is specified, the next page that starts with a static memo right AFTER the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo older than the specified static memo id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a static memo id. If this is specified, the previous page that ends with a static memo right BEFORE the specified static memo id on the static memo timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that static memo newer than the specified static memo id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec staticMemosGetWithResponseSpec(@jakarta.annotation.Nullable VirtualAccountActivationStatus status, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return staticMemosGetRequestCreation(status, limit, startingAfter, endingBefore);
    }

    /**
     * Static Memo Activity Across All Customers
     * History of activity across all customers and Virtual Accounts
     * <p><b>200</b> - List of Static Memo events
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @return StaticMemoHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec staticMemosHistoryGetRequestCreation(@jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
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

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<StaticMemoHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/static_memos/history", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Static Memo Activity Across All Customers
     * History of activity across all customers and Virtual Accounts
     * <p><b>200</b> - List of Static Memo events
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @return StaticMemoHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public StaticMemoHistory staticMemosHistoryGet(@jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemoHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return staticMemosHistoryGetRequestCreation(depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType).body(localVarReturnType);
    }

    /**
     * Static Memo Activity Across All Customers
     * History of activity across all customers and Virtual Accounts
     * <p><b>200</b> - List of Static Memo events
     * @param depositId The deposit id associated with the events. Cannot be passed if deposit_ids is also passed
     * @param depositIds The deposit ids associated with the events. Pass a list of deposit ids like \&quot;deposit_ids[]&#x3D;id1&amp;deposit_ids[]&#x3D;id2\&quot;. Cannot be passed if deposit_id is also passed
     * @param txHash The hash of the transaction
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param eventType Filter history by event type
     * @return ResponseEntity&lt;StaticMemoHistory&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StaticMemoHistory> staticMemosHistoryGetWithHttpInfo(@jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        ParameterizedTypeReference<StaticMemoHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return staticMemosHistoryGetRequestCreation(depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType).toEntity(localVarReturnType);
    }

    /**
     * Static Memo Activity Across All Customers
     * History of activity across all customers and Virtual Accounts
     * <p><b>200</b> - List of Static Memo events
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
    public ResponseSpec staticMemosHistoryGetWithResponseSpec(@jakarta.annotation.Nullable String depositId, @jakarta.annotation.Nullable List<String> depositIds, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String eventType) throws RestClientResponseException {
        return staticMemosHistoryGetRequestCreation(depositId, depositIds, txHash, limit, startingAfter, endingBefore, eventType);
    }
}
