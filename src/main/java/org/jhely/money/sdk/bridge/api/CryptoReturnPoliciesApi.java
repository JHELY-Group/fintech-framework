package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.CryptoReturnPolicy;
import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.PostCryptoReturnPoliciesInput;

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
public class CryptoReturnPoliciesApi {
    private ApiClient apiClient;

    public CryptoReturnPoliciesApi() {
        this(new ApiClient());
    }

    public CryptoReturnPoliciesApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get all crypto return policies
     * Retrieve all crypto return policies for the authenticated developer
     * <p><b>200</b> - List of crypto return policies
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return List&lt;CryptoReturnPolicy&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec cryptoReturnPoliciesGetRequestCreation() throws RestClientResponseException {
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

        ParameterizedTypeReference<List<CryptoReturnPolicy>> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/crypto_return_policies", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all crypto return policies
     * Retrieve all crypto return policies for the authenticated developer
     * <p><b>200</b> - List of crypto return policies
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return List&lt;CryptoReturnPolicy&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public List<CryptoReturnPolicy> cryptoReturnPoliciesGet() throws RestClientResponseException {
        ParameterizedTypeReference<List<CryptoReturnPolicy>> localVarReturnType = new ParameterizedTypeReference<>() {};
        return cryptoReturnPoliciesGetRequestCreation().body(localVarReturnType);
    }

    /**
     * Get all crypto return policies
     * Retrieve all crypto return policies for the authenticated developer
     * <p><b>200</b> - List of crypto return policies
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseEntity&lt;List&lt;CryptoReturnPolicy&gt;&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<CryptoReturnPolicy>> cryptoReturnPoliciesGetWithHttpInfo() throws RestClientResponseException {
        ParameterizedTypeReference<List<CryptoReturnPolicy>> localVarReturnType = new ParameterizedTypeReference<>() {};
        return cryptoReturnPoliciesGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Get all crypto return policies
     * Retrieve all crypto return policies for the authenticated developer
     * <p><b>200</b> - List of crypto return policies
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec cryptoReturnPoliciesGetWithResponseSpec() throws RestClientResponseException {
        return cryptoReturnPoliciesGetRequestCreation();
    }

    /**
     * Delete a single crypto return policy object
     * Delete a crypto return policy object from the passed in crypto return policy ID
     * <p><b>200</b> - Successfully deleted crypto return policy object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param policyID The policyID parameter
     * @return CryptoReturnPolicy
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec cryptoReturnPoliciesPolicyIDDeleteRequestCreation(@jakarta.annotation.Nonnull String policyID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'policyID' is set
        if (policyID == null) {
            throw new RestClientResponseException("Missing the required parameter 'policyID' when calling cryptoReturnPoliciesPolicyIDDelete", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("policyID", policyID);

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

        ParameterizedTypeReference<CryptoReturnPolicy> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/crypto_return_policies/{policyID}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Delete a single crypto return policy object
     * Delete a crypto return policy object from the passed in crypto return policy ID
     * <p><b>200</b> - Successfully deleted crypto return policy object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param policyID The policyID parameter
     * @return CryptoReturnPolicy
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CryptoReturnPolicy cryptoReturnPoliciesPolicyIDDelete(@jakarta.annotation.Nonnull String policyID) throws RestClientResponseException {
        ParameterizedTypeReference<CryptoReturnPolicy> localVarReturnType = new ParameterizedTypeReference<>() {};
        return cryptoReturnPoliciesPolicyIDDeleteRequestCreation(policyID).body(localVarReturnType);
    }

    /**
     * Delete a single crypto return policy object
     * Delete a crypto return policy object from the passed in crypto return policy ID
     * <p><b>200</b> - Successfully deleted crypto return policy object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param policyID The policyID parameter
     * @return ResponseEntity&lt;CryptoReturnPolicy&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CryptoReturnPolicy> cryptoReturnPoliciesPolicyIDDeleteWithHttpInfo(@jakarta.annotation.Nonnull String policyID) throws RestClientResponseException {
        ParameterizedTypeReference<CryptoReturnPolicy> localVarReturnType = new ParameterizedTypeReference<>() {};
        return cryptoReturnPoliciesPolicyIDDeleteRequestCreation(policyID).toEntity(localVarReturnType);
    }

    /**
     * Delete a single crypto return policy object
     * Delete a crypto return policy object from the passed in crypto return policy ID
     * <p><b>200</b> - Successfully deleted crypto return policy object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param policyID The policyID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec cryptoReturnPoliciesPolicyIDDeleteWithResponseSpec(@jakarta.annotation.Nonnull String policyID) throws RestClientResponseException {
        return cryptoReturnPoliciesPolicyIDDeleteRequestCreation(policyID);
    }

    /**
     * Update an existing crypto return policy
     * 
     * <p><b>200</b> - Crypto return policy updated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param policyID The policyID parameter
     * @param postCryptoReturnPoliciesInput Crypto return policy object to be updated
     * @return CryptoReturnPolicy
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec cryptoReturnPoliciesPolicyIDPutRequestCreation(@jakarta.annotation.Nonnull String policyID, @jakarta.annotation.Nonnull PostCryptoReturnPoliciesInput postCryptoReturnPoliciesInput) throws RestClientResponseException {
        Object postBody = postCryptoReturnPoliciesInput;
        // verify the required parameter 'policyID' is set
        if (policyID == null) {
            throw new RestClientResponseException("Missing the required parameter 'policyID' when calling cryptoReturnPoliciesPolicyIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'postCryptoReturnPoliciesInput' is set
        if (postCryptoReturnPoliciesInput == null) {
            throw new RestClientResponseException("Missing the required parameter 'postCryptoReturnPoliciesInput' when calling cryptoReturnPoliciesPolicyIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("policyID", policyID);

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

        ParameterizedTypeReference<CryptoReturnPolicy> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/crypto_return_policies/{policyID}", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update an existing crypto return policy
     * 
     * <p><b>200</b> - Crypto return policy updated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param policyID The policyID parameter
     * @param postCryptoReturnPoliciesInput Crypto return policy object to be updated
     * @return CryptoReturnPolicy
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CryptoReturnPolicy cryptoReturnPoliciesPolicyIDPut(@jakarta.annotation.Nonnull String policyID, @jakarta.annotation.Nonnull PostCryptoReturnPoliciesInput postCryptoReturnPoliciesInput) throws RestClientResponseException {
        ParameterizedTypeReference<CryptoReturnPolicy> localVarReturnType = new ParameterizedTypeReference<>() {};
        return cryptoReturnPoliciesPolicyIDPutRequestCreation(policyID, postCryptoReturnPoliciesInput).body(localVarReturnType);
    }

    /**
     * Update an existing crypto return policy
     * 
     * <p><b>200</b> - Crypto return policy updated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param policyID The policyID parameter
     * @param postCryptoReturnPoliciesInput Crypto return policy object to be updated
     * @return ResponseEntity&lt;CryptoReturnPolicy&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CryptoReturnPolicy> cryptoReturnPoliciesPolicyIDPutWithHttpInfo(@jakarta.annotation.Nonnull String policyID, @jakarta.annotation.Nonnull PostCryptoReturnPoliciesInput postCryptoReturnPoliciesInput) throws RestClientResponseException {
        ParameterizedTypeReference<CryptoReturnPolicy> localVarReturnType = new ParameterizedTypeReference<>() {};
        return cryptoReturnPoliciesPolicyIDPutRequestCreation(policyID, postCryptoReturnPoliciesInput).toEntity(localVarReturnType);
    }

    /**
     * Update an existing crypto return policy
     * 
     * <p><b>200</b> - Crypto return policy updated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param policyID The policyID parameter
     * @param postCryptoReturnPoliciesInput Crypto return policy object to be updated
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec cryptoReturnPoliciesPolicyIDPutWithResponseSpec(@jakarta.annotation.Nonnull String policyID, @jakarta.annotation.Nonnull PostCryptoReturnPoliciesInput postCryptoReturnPoliciesInput) throws RestClientResponseException {
        return cryptoReturnPoliciesPolicyIDPutRequestCreation(policyID, postCryptoReturnPoliciesInput);
    }

    /**
     * Create a new crypto return policy
     * 
     * <p><b>201</b> - Crypto return policy created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param postCryptoReturnPoliciesInput Crypto return policy object to be created
     * @return CryptoReturnPolicy
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec cryptoReturnPoliciesPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull PostCryptoReturnPoliciesInput postCryptoReturnPoliciesInput) throws RestClientResponseException {
        Object postBody = postCryptoReturnPoliciesInput;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling cryptoReturnPoliciesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'postCryptoReturnPoliciesInput' is set
        if (postCryptoReturnPoliciesInput == null) {
            throw new RestClientResponseException("Missing the required parameter 'postCryptoReturnPoliciesInput' when calling cryptoReturnPoliciesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<CryptoReturnPolicy> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/crypto_return_policies", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a new crypto return policy
     * 
     * <p><b>201</b> - Crypto return policy created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param postCryptoReturnPoliciesInput Crypto return policy object to be created
     * @return CryptoReturnPolicy
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CryptoReturnPolicy cryptoReturnPoliciesPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull PostCryptoReturnPoliciesInput postCryptoReturnPoliciesInput) throws RestClientResponseException {
        ParameterizedTypeReference<CryptoReturnPolicy> localVarReturnType = new ParameterizedTypeReference<>() {};
        return cryptoReturnPoliciesPostRequestCreation(idempotencyKey, postCryptoReturnPoliciesInput).body(localVarReturnType);
    }

    /**
     * Create a new crypto return policy
     * 
     * <p><b>201</b> - Crypto return policy created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param postCryptoReturnPoliciesInput Crypto return policy object to be created
     * @return ResponseEntity&lt;CryptoReturnPolicy&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CryptoReturnPolicy> cryptoReturnPoliciesPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull PostCryptoReturnPoliciesInput postCryptoReturnPoliciesInput) throws RestClientResponseException {
        ParameterizedTypeReference<CryptoReturnPolicy> localVarReturnType = new ParameterizedTypeReference<>() {};
        return cryptoReturnPoliciesPostRequestCreation(idempotencyKey, postCryptoReturnPoliciesInput).toEntity(localVarReturnType);
    }

    /**
     * Create a new crypto return policy
     * 
     * <p><b>201</b> - Crypto return policy created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param postCryptoReturnPoliciesInput Crypto return policy object to be created
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec cryptoReturnPoliciesPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull PostCryptoReturnPoliciesInput postCryptoReturnPoliciesInput) throws RestClientResponseException {
        return cryptoReturnPoliciesPostRequestCreation(idempotencyKey, postCryptoReturnPoliciesInput);
    }
}
