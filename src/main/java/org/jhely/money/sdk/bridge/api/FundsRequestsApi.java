package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.ListOfFundsRequests;

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
public class FundsRequestsApi {
    private ApiClient apiClient;

    public FundsRequestsApi() {
        this(new ApiClient());
    }

    public FundsRequestsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get all funds requests
     * Retrieve a list of all funds requests submitted by partnered banks and financial institutions
     * <p><b>200</b> - List of funds requests
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a funds request id. If this is specified, the next page that starts with a funds request right AFTER the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that funds request older than the specified funds request id will be returned (shouldn&#39;t be set if &#x60;ending_before&#x60; is set)
     * @param endingBefore This is a funds request id. If this is specified, the previous page that ends with a funds request right BEFORE the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that funds request newer than the specified funds request id will be returned (shouldn&#39;t be set if &#x60;starting_after&#x60; is set)
     * @param customerId If specified, results will be filtered to the given customer_id only
     * @param fraud If this is specified as either \&quot;true\&quot; or \&quot;false\&quot;, then only fraudulent or non-fraudulent entries will be returned, respectively
     * @param noticeDateStartingOn A date in the format YYYY-MM-DD. If specified, then only entries with a notice date on or after the provided date, inclusive, will be returned
     * @param noticeDateEndingOn A date in the format YYYY-MM-DD. If specified, then only entries with a notice date on or before the provided date, inclusive, will be returned
     * @return ListOfFundsRequests
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec fundsRequestsGetRequestCreation(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String customerId, @jakarta.annotation.Nullable Boolean fraud, @jakarta.annotation.Nullable String noticeDateStartingOn, @jakarta.annotation.Nullable String noticeDateEndingOn) throws RestClientResponseException {
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
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customer_id", customerId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "fraud", fraud));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "notice_date_starting_on", noticeDateStartingOn));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "notice_date_ending_on", noticeDateEndingOn));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<ListOfFundsRequests> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/funds_requests", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all funds requests
     * Retrieve a list of all funds requests submitted by partnered banks and financial institutions
     * <p><b>200</b> - List of funds requests
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a funds request id. If this is specified, the next page that starts with a funds request right AFTER the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that funds request older than the specified funds request id will be returned (shouldn&#39;t be set if &#x60;ending_before&#x60; is set)
     * @param endingBefore This is a funds request id. If this is specified, the previous page that ends with a funds request right BEFORE the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that funds request newer than the specified funds request id will be returned (shouldn&#39;t be set if &#x60;starting_after&#x60; is set)
     * @param customerId If specified, results will be filtered to the given customer_id only
     * @param fraud If this is specified as either \&quot;true\&quot; or \&quot;false\&quot;, then only fraudulent or non-fraudulent entries will be returned, respectively
     * @param noticeDateStartingOn A date in the format YYYY-MM-DD. If specified, then only entries with a notice date on or after the provided date, inclusive, will be returned
     * @param noticeDateEndingOn A date in the format YYYY-MM-DD. If specified, then only entries with a notice date on or before the provided date, inclusive, will be returned
     * @return ListOfFundsRequests
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ListOfFundsRequests fundsRequestsGet(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String customerId, @jakarta.annotation.Nullable Boolean fraud, @jakarta.annotation.Nullable String noticeDateStartingOn, @jakarta.annotation.Nullable String noticeDateEndingOn) throws RestClientResponseException {
        ParameterizedTypeReference<ListOfFundsRequests> localVarReturnType = new ParameterizedTypeReference<>() {};
        return fundsRequestsGetRequestCreation(limit, startingAfter, endingBefore, customerId, fraud, noticeDateStartingOn, noticeDateEndingOn).body(localVarReturnType);
    }

    /**
     * Get all funds requests
     * Retrieve a list of all funds requests submitted by partnered banks and financial institutions
     * <p><b>200</b> - List of funds requests
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a funds request id. If this is specified, the next page that starts with a funds request right AFTER the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that funds request older than the specified funds request id will be returned (shouldn&#39;t be set if &#x60;ending_before&#x60; is set)
     * @param endingBefore This is a funds request id. If this is specified, the previous page that ends with a funds request right BEFORE the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that funds request newer than the specified funds request id will be returned (shouldn&#39;t be set if &#x60;starting_after&#x60; is set)
     * @param customerId If specified, results will be filtered to the given customer_id only
     * @param fraud If this is specified as either \&quot;true\&quot; or \&quot;false\&quot;, then only fraudulent or non-fraudulent entries will be returned, respectively
     * @param noticeDateStartingOn A date in the format YYYY-MM-DD. If specified, then only entries with a notice date on or after the provided date, inclusive, will be returned
     * @param noticeDateEndingOn A date in the format YYYY-MM-DD. If specified, then only entries with a notice date on or before the provided date, inclusive, will be returned
     * @return ResponseEntity&lt;ListOfFundsRequests&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListOfFundsRequests> fundsRequestsGetWithHttpInfo(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String customerId, @jakarta.annotation.Nullable Boolean fraud, @jakarta.annotation.Nullable String noticeDateStartingOn, @jakarta.annotation.Nullable String noticeDateEndingOn) throws RestClientResponseException {
        ParameterizedTypeReference<ListOfFundsRequests> localVarReturnType = new ParameterizedTypeReference<>() {};
        return fundsRequestsGetRequestCreation(limit, startingAfter, endingBefore, customerId, fraud, noticeDateStartingOn, noticeDateEndingOn).toEntity(localVarReturnType);
    }

    /**
     * Get all funds requests
     * Retrieve a list of all funds requests submitted by partnered banks and financial institutions
     * <p><b>200</b> - List of funds requests
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a funds request id. If this is specified, the next page that starts with a funds request right AFTER the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that funds request older than the specified funds request id will be returned (shouldn&#39;t be set if &#x60;ending_before&#x60; is set)
     * @param endingBefore This is a funds request id. If this is specified, the previous page that ends with a funds request right BEFORE the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that funds request newer than the specified funds request id will be returned (shouldn&#39;t be set if &#x60;starting_after&#x60; is set)
     * @param customerId If specified, results will be filtered to the given customer_id only
     * @param fraud If this is specified as either \&quot;true\&quot; or \&quot;false\&quot;, then only fraudulent or non-fraudulent entries will be returned, respectively
     * @param noticeDateStartingOn A date in the format YYYY-MM-DD. If specified, then only entries with a notice date on or after the provided date, inclusive, will be returned
     * @param noticeDateEndingOn A date in the format YYYY-MM-DD. If specified, then only entries with a notice date on or before the provided date, inclusive, will be returned
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec fundsRequestsGetWithResponseSpec(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String customerId, @jakarta.annotation.Nullable Boolean fraud, @jakarta.annotation.Nullable String noticeDateStartingOn, @jakarta.annotation.Nullable String noticeDateEndingOn) throws RestClientResponseException {
        return fundsRequestsGetRequestCreation(limit, startingAfter, endingBefore, customerId, fraud, noticeDateStartingOn, noticeDateEndingOn);
    }
}
