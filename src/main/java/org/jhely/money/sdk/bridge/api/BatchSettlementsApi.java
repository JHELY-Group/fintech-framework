package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.BatchSettlementSchedule;
import org.jhely.money.sdk.bridge.model.CreateBatchSettlementSchedule;
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
public class BatchSettlementsApi {
    private ApiClient apiClient;

    public BatchSettlementsApi() {
        this(new ApiClient());
    }

    public BatchSettlementsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Create a Batch Settlement Schedule
     * Creates a Batch Settlement Schedule that can be used as the destination of a liquidation address.
     * <p><b>200</b> - Batch Settlement Schedule object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createBatchSettlementSchedule The createBatchSettlementSchedule parameter
     * @return BatchSettlementSchedule
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDBatchSettlementSchedulesPostRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateBatchSettlementSchedule createBatchSettlementSchedule) throws RestClientResponseException {
        Object postBody = createBatchSettlementSchedule;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDBatchSettlementSchedulesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDBatchSettlementSchedulesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'createBatchSettlementSchedule' is set
        if (createBatchSettlementSchedule == null) {
            throw new RestClientResponseException("Missing the required parameter 'createBatchSettlementSchedule' when calling customersCustomerIDBatchSettlementSchedulesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<BatchSettlementSchedule> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/batch_settlement_schedules", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a Batch Settlement Schedule
     * Creates a Batch Settlement Schedule that can be used as the destination of a liquidation address.
     * <p><b>200</b> - Batch Settlement Schedule object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createBatchSettlementSchedule The createBatchSettlementSchedule parameter
     * @return BatchSettlementSchedule
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public BatchSettlementSchedule customersCustomerIDBatchSettlementSchedulesPost(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateBatchSettlementSchedule createBatchSettlementSchedule) throws RestClientResponseException {
        ParameterizedTypeReference<BatchSettlementSchedule> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDBatchSettlementSchedulesPostRequestCreation(customerID, idempotencyKey, createBatchSettlementSchedule).body(localVarReturnType);
    }

    /**
     * Create a Batch Settlement Schedule
     * Creates a Batch Settlement Schedule that can be used as the destination of a liquidation address.
     * <p><b>200</b> - Batch Settlement Schedule object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createBatchSettlementSchedule The createBatchSettlementSchedule parameter
     * @return ResponseEntity&lt;BatchSettlementSchedule&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<BatchSettlementSchedule> customersCustomerIDBatchSettlementSchedulesPostWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateBatchSettlementSchedule createBatchSettlementSchedule) throws RestClientResponseException {
        ParameterizedTypeReference<BatchSettlementSchedule> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDBatchSettlementSchedulesPostRequestCreation(customerID, idempotencyKey, createBatchSettlementSchedule).toEntity(localVarReturnType);
    }

    /**
     * Create a Batch Settlement Schedule
     * Creates a Batch Settlement Schedule that can be used as the destination of a liquidation address.
     * <p><b>200</b> - Batch Settlement Schedule object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createBatchSettlementSchedule The createBatchSettlementSchedule parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDBatchSettlementSchedulesPostWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateBatchSettlementSchedule createBatchSettlementSchedule) throws RestClientResponseException {
        return customersCustomerIDBatchSettlementSchedulesPostRequestCreation(customerID, idempotencyKey, createBatchSettlementSchedule);
    }
}
