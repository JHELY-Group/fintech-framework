package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.CreateKycLinks;
import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.IndividualKycLinkResponse;
import org.jhely.money.sdk.bridge.model.KycLinks;

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
public class KycLinksApi {
    private ApiClient apiClient;

    public KycLinksApi() {
        this(new ApiClient());
    }

    public KycLinksApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get all KYC links.
     * Retrieve the full list of kyc links.
     * <p><b>200</b> - Successful KYC links status response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerId If included, filters to KYC links for the customer with the given id
     * @param email If included, filters to KYC links for the customer with the given email
     * @param startingAfter This is a customer id. If this is specified, the next page that starts with a customer right AFTER the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers older than the specified customer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a customer id. If this is specified, the previous page that ends with a customer right BEFORE the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers newer than the specified customer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param limit The number of items to return (default of 10, max of 100)
     * @return KycLinks
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec kycLinksGetRequestCreation(@jakarta.annotation.Nullable String customerId, @jakarta.annotation.Nullable String email, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer limit) throws RestClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "customer_id", customerId));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "email", email));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "starting_after", startingAfter));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ending_before", endingBefore));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<KycLinks> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/kyc_links", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all KYC links.
     * Retrieve the full list of kyc links.
     * <p><b>200</b> - Successful KYC links status response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerId If included, filters to KYC links for the customer with the given id
     * @param email If included, filters to KYC links for the customer with the given email
     * @param startingAfter This is a customer id. If this is specified, the next page that starts with a customer right AFTER the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers older than the specified customer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a customer id. If this is specified, the previous page that ends with a customer right BEFORE the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers newer than the specified customer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param limit The number of items to return (default of 10, max of 100)
     * @return KycLinks
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public KycLinks kycLinksGet(@jakarta.annotation.Nullable String customerId, @jakarta.annotation.Nullable String email, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer limit) throws RestClientResponseException {
        ParameterizedTypeReference<KycLinks> localVarReturnType = new ParameterizedTypeReference<>() {};
        return kycLinksGetRequestCreation(customerId, email, startingAfter, endingBefore, limit).body(localVarReturnType);
    }

    /**
     * Get all KYC links.
     * Retrieve the full list of kyc links.
     * <p><b>200</b> - Successful KYC links status response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerId If included, filters to KYC links for the customer with the given id
     * @param email If included, filters to KYC links for the customer with the given email
     * @param startingAfter This is a customer id. If this is specified, the next page that starts with a customer right AFTER the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers older than the specified customer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a customer id. If this is specified, the previous page that ends with a customer right BEFORE the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers newer than the specified customer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param limit The number of items to return (default of 10, max of 100)
     * @return ResponseEntity&lt;KycLinks&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<KycLinks> kycLinksGetWithHttpInfo(@jakarta.annotation.Nullable String customerId, @jakarta.annotation.Nullable String email, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer limit) throws RestClientResponseException {
        ParameterizedTypeReference<KycLinks> localVarReturnType = new ParameterizedTypeReference<>() {};
        return kycLinksGetRequestCreation(customerId, email, startingAfter, endingBefore, limit).toEntity(localVarReturnType);
    }

    /**
     * Get all KYC links.
     * Retrieve the full list of kyc links.
     * <p><b>200</b> - Successful KYC links status response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerId If included, filters to KYC links for the customer with the given id
     * @param email If included, filters to KYC links for the customer with the given email
     * @param startingAfter This is a customer id. If this is specified, the next page that starts with a customer right AFTER the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers older than the specified customer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a customer id. If this is specified, the previous page that ends with a customer right BEFORE the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers newer than the specified customer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param limit The number of items to return (default of 10, max of 100)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec kycLinksGetWithResponseSpec(@jakarta.annotation.Nullable String customerId, @jakarta.annotation.Nullable String email, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer limit) throws RestClientResponseException {
        return kycLinksGetRequestCreation(customerId, email, startingAfter, endingBefore, limit);
    }

    /**
     * Check the status of a KYC link
     * Retrieve the status of a KYC request from the passed in KYC link id
     * <p><b>200</b> - Successful KYC link status response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param kycLinkID The kycLinkID parameter
     * @return IndividualKycLinkResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec kycLinksKycLinkIDGetRequestCreation(@jakarta.annotation.Nonnull String kycLinkID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'kycLinkID' is set
        if (kycLinkID == null) {
            throw new RestClientResponseException("Missing the required parameter 'kycLinkID' when calling kycLinksKycLinkIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("kycLinkID", kycLinkID);

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

        ParameterizedTypeReference<IndividualKycLinkResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/kyc_links/{kycLinkID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Check the status of a KYC link
     * Retrieve the status of a KYC request from the passed in KYC link id
     * <p><b>200</b> - Successful KYC link status response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param kycLinkID The kycLinkID parameter
     * @return IndividualKycLinkResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public IndividualKycLinkResponse kycLinksKycLinkIDGet(@jakarta.annotation.Nonnull String kycLinkID) throws RestClientResponseException {
        ParameterizedTypeReference<IndividualKycLinkResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return kycLinksKycLinkIDGetRequestCreation(kycLinkID).body(localVarReturnType);
    }

    /**
     * Check the status of a KYC link
     * Retrieve the status of a KYC request from the passed in KYC link id
     * <p><b>200</b> - Successful KYC link status response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param kycLinkID The kycLinkID parameter
     * @return ResponseEntity&lt;IndividualKycLinkResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<IndividualKycLinkResponse> kycLinksKycLinkIDGetWithHttpInfo(@jakarta.annotation.Nonnull String kycLinkID) throws RestClientResponseException {
        ParameterizedTypeReference<IndividualKycLinkResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return kycLinksKycLinkIDGetRequestCreation(kycLinkID).toEntity(localVarReturnType);
    }

    /**
     * Check the status of a KYC link
     * Retrieve the status of a KYC request from the passed in KYC link id
     * <p><b>200</b> - Successful KYC link status response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param kycLinkID The kycLinkID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec kycLinksKycLinkIDGetWithResponseSpec(@jakarta.annotation.Nonnull String kycLinkID) throws RestClientResponseException {
        return kycLinksKycLinkIDGetRequestCreation(kycLinkID);
    }

    /**
     * Generate the Links needs to complete KYC for an individual or business
     * 
     * <p><b>200</b> - KYC Links generated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createKycLinks Information about the customer to create KYC Links for
     * @return IndividualKycLinkResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec kycLinksPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateKycLinks createKycLinks) throws RestClientResponseException {
        Object postBody = createKycLinks;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling kycLinksPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'createKycLinks' is set
        if (createKycLinks == null) {
            throw new RestClientResponseException("Missing the required parameter 'createKycLinks' when calling kycLinksPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<IndividualKycLinkResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/kyc_links", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Generate the Links needs to complete KYC for an individual or business
     * 
     * <p><b>200</b> - KYC Links generated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createKycLinks Information about the customer to create KYC Links for
     * @return IndividualKycLinkResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public IndividualKycLinkResponse kycLinksPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateKycLinks createKycLinks) throws RestClientResponseException {
        ParameterizedTypeReference<IndividualKycLinkResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return kycLinksPostRequestCreation(idempotencyKey, createKycLinks).body(localVarReturnType);
    }

    /**
     * Generate the Links needs to complete KYC for an individual or business
     * 
     * <p><b>200</b> - KYC Links generated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createKycLinks Information about the customer to create KYC Links for
     * @return ResponseEntity&lt;IndividualKycLinkResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<IndividualKycLinkResponse> kycLinksPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateKycLinks createKycLinks) throws RestClientResponseException {
        ParameterizedTypeReference<IndividualKycLinkResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return kycLinksPostRequestCreation(idempotencyKey, createKycLinks).toEntity(localVarReturnType);
    }

    /**
     * Generate the Links needs to complete KYC for an individual or business
     * 
     * <p><b>200</b> - KYC Links generated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createKycLinks Information about the customer to create KYC Links for
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec kycLinksPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateKycLinks createKycLinks) throws RestClientResponseException {
        return kycLinksPostRequestCreation(idempotencyKey, createKycLinks);
    }
}
