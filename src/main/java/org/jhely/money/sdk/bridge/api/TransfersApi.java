package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.StaticTemplates;
import org.jhely.money.sdk.bridge.model.TransferRequest;
import org.jhely.money.sdk.bridge.model.TransferResponse;
import org.jhely.money.sdk.bridge.model.TransferUpdateRequest;
import org.jhely.money.sdk.bridge.model.Transfers;
import org.jhely.money.sdk.bridge.model.TransfersPost201Response;

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
public class TransfersApi {
    private ApiClient apiClient;

    public TransfersApi() {
        this(new ApiClient());
    }

    public TransfersApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get all transfers
     * 
     * <p><b>200</b> - List of transfers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param txHash The hash of the transaction
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @param templateId This is a template id. If this is specified, the transfers with the specified template id will be returned.
     * @param state Filter transfers by their external state. See [Transfer States](https://apidocs.bridge.xyz/platform/orchestration/transfers/transfer-states) for more details.
     * @return Transfers
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec transfersGetRequestCreation(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs, @jakarta.annotation.Nullable String templateId, @jakarta.annotation.Nullable String state) throws RestClientResponseException {
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
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "tx_hash", txHash));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_after_ms", updatedAfterMs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_before_ms", updatedBeforeMs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "template_id", templateId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state", state));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<Transfers> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/transfers", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all transfers
     * 
     * <p><b>200</b> - List of transfers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param txHash The hash of the transaction
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @param templateId This is a template id. If this is specified, the transfers with the specified template id will be returned.
     * @param state Filter transfers by their external state. See [Transfer States](https://apidocs.bridge.xyz/platform/orchestration/transfers/transfer-states) for more details.
     * @return Transfers
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Transfers transfersGet(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs, @jakarta.annotation.Nullable String templateId, @jakarta.annotation.Nullable String state) throws RestClientResponseException {
        ParameterizedTypeReference<Transfers> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersGetRequestCreation(limit, startingAfter, endingBefore, txHash, updatedAfterMs, updatedBeforeMs, templateId, state).body(localVarReturnType);
    }

    /**
     * Get all transfers
     * 
     * <p><b>200</b> - List of transfers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param txHash The hash of the transaction
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @param templateId This is a template id. If this is specified, the transfers with the specified template id will be returned.
     * @param state Filter transfers by their external state. See [Transfer States](https://apidocs.bridge.xyz/platform/orchestration/transfers/transfer-states) for more details.
     * @return ResponseEntity&lt;Transfers&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Transfers> transfersGetWithHttpInfo(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs, @jakarta.annotation.Nullable String templateId, @jakarta.annotation.Nullable String state) throws RestClientResponseException {
        ParameterizedTypeReference<Transfers> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersGetRequestCreation(limit, startingAfter, endingBefore, txHash, updatedAfterMs, updatedBeforeMs, templateId, state).toEntity(localVarReturnType);
    }

    /**
     * Get all transfers
     * 
     * <p><b>200</b> - List of transfers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param txHash The hash of the transaction
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @param templateId This is a template id. If this is specified, the transfers with the specified template id will be returned.
     * @param state Filter transfers by their external state. See [Transfer States](https://apidocs.bridge.xyz/platform/orchestration/transfers/transfer-states) for more details.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec transfersGetWithResponseSpec(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs, @jakarta.annotation.Nullable String templateId, @jakarta.annotation.Nullable String state) throws RestClientResponseException {
        return transfersGetRequestCreation(limit, startingAfter, endingBefore, txHash, updatedAfterMs, updatedBeforeMs, templateId, state);
    }

    /**
     * Create a transfer
     * 
     * <p><b>201</b> - Transfer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>403</b> - The transfer has failed due to an AML violation (anti-money laundering).  Reach out to Bridge for more information
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param transferRequest Transfer object to be created
     * @return TransfersPost201Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec transfersPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull TransferRequest transferRequest) throws RestClientResponseException {
        Object postBody = transferRequest;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling transfersPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'transferRequest' is set
        if (transferRequest == null) {
            throw new RestClientResponseException("Missing the required parameter 'transferRequest' when calling transfersPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

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

        ParameterizedTypeReference<TransfersPost201Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/transfers", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a transfer
     * 
     * <p><b>201</b> - Transfer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>403</b> - The transfer has failed due to an AML violation (anti-money laundering).  Reach out to Bridge for more information
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param transferRequest Transfer object to be created
     * @return TransfersPost201Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public TransfersPost201Response transfersPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull TransferRequest transferRequest) throws RestClientResponseException {
        ParameterizedTypeReference<TransfersPost201Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersPostRequestCreation(idempotencyKey, transferRequest).body(localVarReturnType);
    }

    /**
     * Create a transfer
     * 
     * <p><b>201</b> - Transfer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>403</b> - The transfer has failed due to an AML violation (anti-money laundering).  Reach out to Bridge for more information
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param transferRequest Transfer object to be created
     * @return ResponseEntity&lt;TransfersPost201Response&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<TransfersPost201Response> transfersPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull TransferRequest transferRequest) throws RestClientResponseException {
        ParameterizedTypeReference<TransfersPost201Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersPostRequestCreation(idempotencyKey, transferRequest).toEntity(localVarReturnType);
    }

    /**
     * Create a transfer
     * 
     * <p><b>201</b> - Transfer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>403</b> - The transfer has failed due to an AML violation (anti-money laundering).  Reach out to Bridge for more information
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param transferRequest Transfer object to be created
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec transfersPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull TransferRequest transferRequest) throws RestClientResponseException {
        return transfersPostRequestCreation(idempotencyKey, transferRequest);
    }

    /**
     * Get all static templates
     * 
     * <p><b>200</b> - List of static templates (the returned list is empty if none found). Static templates are transfers that are used as templates for other transfers and can be created using the static_templates feature flag.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return StaticTemplates
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec transfersStaticTemplatesGetRequestCreation(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
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

        ParameterizedTypeReference<StaticTemplates> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/transfers/static_templates", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all static templates
     * 
     * <p><b>200</b> - List of static templates (the returned list is empty if none found). Static templates are transfers that are used as templates for other transfers and can be created using the static_templates feature flag.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return StaticTemplates
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public StaticTemplates transfersStaticTemplatesGet(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<StaticTemplates> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersStaticTemplatesGetRequestCreation(limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Get all static templates
     * 
     * <p><b>200</b> - List of static templates (the returned list is empty if none found). Static templates are transfers that are used as templates for other transfers and can be created using the static_templates feature flag.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;StaticTemplates&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StaticTemplates> transfersStaticTemplatesGetWithHttpInfo(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<StaticTemplates> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersStaticTemplatesGetRequestCreation(limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Get all static templates
     * 
     * <p><b>200</b> - List of static templates (the returned list is empty if none found). Static templates are transfers that are used as templates for other transfers and can be created using the static_templates feature flag.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec transfersStaticTemplatesGetWithResponseSpec(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return transfersStaticTemplatesGetRequestCreation(limit, startingAfter, endingBefore);
    }

    /**
     * Delete a transfer
     * Delete a transfer that was previously created. Must be in the awaiting_funds state.
     * <p><b>200</b> - Successful deletion of transfer
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec transfersTransferIDDeleteRequestCreation(@jakarta.annotation.Nonnull String transferID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'transferID' is set
        if (transferID == null) {
            throw new RestClientResponseException("Missing the required parameter 'transferID' when calling transfersTransferIDDelete", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("transferID", transferID);

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

        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/transfers/{transferID}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Delete a transfer
     * Delete a transfer that was previously created. Must be in the awaiting_funds state.
     * <p><b>200</b> - Successful deletion of transfer
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public void transfersTransferIDDelete(@jakarta.annotation.Nonnull String transferID) throws RestClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<>() {};
        transfersTransferIDDeleteRequestCreation(transferID).body(localVarReturnType);
    }

    /**
     * Delete a transfer
     * Delete a transfer that was previously created. Must be in the awaiting_funds state.
     * <p><b>200</b> - Successful deletion of transfer
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> transfersTransferIDDeleteWithHttpInfo(@jakarta.annotation.Nonnull String transferID) throws RestClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersTransferIDDeleteRequestCreation(transferID).toEntity(localVarReturnType);
    }

    /**
     * Delete a transfer
     * Delete a transfer that was previously created. Must be in the awaiting_funds state.
     * <p><b>200</b> - Successful deletion of transfer
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec transfersTransferIDDeleteWithResponseSpec(@jakarta.annotation.Nonnull String transferID) throws RestClientResponseException {
        return transfersTransferIDDeleteRequestCreation(transferID);
    }

    /**
     * Get a transfer
     * Retrieve a transfer object from the passed in transfer ID
     * <p><b>200</b> - Successful transfer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @return TransferResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec transfersTransferIDGetRequestCreation(@jakarta.annotation.Nonnull String transferID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'transferID' is set
        if (transferID == null) {
            throw new RestClientResponseException("Missing the required parameter 'transferID' when calling transfersTransferIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("transferID", transferID);

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

        ParameterizedTypeReference<TransferResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/transfers/{transferID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a transfer
     * Retrieve a transfer object from the passed in transfer ID
     * <p><b>200</b> - Successful transfer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @return TransferResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public TransferResponse transfersTransferIDGet(@jakarta.annotation.Nonnull String transferID) throws RestClientResponseException {
        ParameterizedTypeReference<TransferResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersTransferIDGetRequestCreation(transferID).body(localVarReturnType);
    }

    /**
     * Get a transfer
     * Retrieve a transfer object from the passed in transfer ID
     * <p><b>200</b> - Successful transfer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @return ResponseEntity&lt;TransferResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<TransferResponse> transfersTransferIDGetWithHttpInfo(@jakarta.annotation.Nonnull String transferID) throws RestClientResponseException {
        ParameterizedTypeReference<TransferResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersTransferIDGetRequestCreation(transferID).toEntity(localVarReturnType);
    }

    /**
     * Get a transfer
     * Retrieve a transfer object from the passed in transfer ID
     * <p><b>200</b> - Successful transfer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec transfersTransferIDGetWithResponseSpec(@jakarta.annotation.Nonnull String transferID) throws RestClientResponseException {
        return transfersTransferIDGetRequestCreation(transferID);
    }

    /**
     * Update a transfer
     * Update a transfer that was previously created. Must be in the awaiting_funds state.
     * <p><b>201</b> - Transfer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>403</b> - The transfer has failed due to an AML violation (anti-money laundering).  Reach out to Bridge for more information
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @param transferUpdateRequest Transfer object to be updated
     * @return TransferResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec transfersTransferIDPutRequestCreation(@jakarta.annotation.Nonnull String transferID, @jakarta.annotation.Nonnull TransferUpdateRequest transferUpdateRequest) throws RestClientResponseException {
        Object postBody = transferUpdateRequest;
        // verify the required parameter 'transferID' is set
        if (transferID == null) {
            throw new RestClientResponseException("Missing the required parameter 'transferID' when calling transfersTransferIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'transferUpdateRequest' is set
        if (transferUpdateRequest == null) {
            throw new RestClientResponseException("Missing the required parameter 'transferUpdateRequest' when calling transfersTransferIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("transferID", transferID);

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

        ParameterizedTypeReference<TransferResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/transfers/{transferID}", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update a transfer
     * Update a transfer that was previously created. Must be in the awaiting_funds state.
     * <p><b>201</b> - Transfer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>403</b> - The transfer has failed due to an AML violation (anti-money laundering).  Reach out to Bridge for more information
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @param transferUpdateRequest Transfer object to be updated
     * @return TransferResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public TransferResponse transfersTransferIDPut(@jakarta.annotation.Nonnull String transferID, @jakarta.annotation.Nonnull TransferUpdateRequest transferUpdateRequest) throws RestClientResponseException {
        ParameterizedTypeReference<TransferResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersTransferIDPutRequestCreation(transferID, transferUpdateRequest).body(localVarReturnType);
    }

    /**
     * Update a transfer
     * Update a transfer that was previously created. Must be in the awaiting_funds state.
     * <p><b>201</b> - Transfer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>403</b> - The transfer has failed due to an AML violation (anti-money laundering).  Reach out to Bridge for more information
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @param transferUpdateRequest Transfer object to be updated
     * @return ResponseEntity&lt;TransferResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<TransferResponse> transfersTransferIDPutWithHttpInfo(@jakarta.annotation.Nonnull String transferID, @jakarta.annotation.Nonnull TransferUpdateRequest transferUpdateRequest) throws RestClientResponseException {
        ParameterizedTypeReference<TransferResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return transfersTransferIDPutRequestCreation(transferID, transferUpdateRequest).toEntity(localVarReturnType);
    }

    /**
     * Update a transfer
     * Update a transfer that was previously created. Must be in the awaiting_funds state.
     * <p><b>201</b> - Transfer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>403</b> - The transfer has failed due to an AML violation (anti-money laundering).  Reach out to Bridge for more information
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param transferID The transferID parameter
     * @param transferUpdateRequest Transfer object to be updated
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec transfersTransferIDPutWithResponseSpec(@jakarta.annotation.Nonnull String transferID, @jakarta.annotation.Nonnull TransferUpdateRequest transferUpdateRequest) throws RestClientResponseException {
        return transfersTransferIDPutRequestCreation(transferID, transferUpdateRequest);
    }
}
