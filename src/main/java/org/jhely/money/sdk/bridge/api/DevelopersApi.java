package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.CreateFeeExternalAccountInput;
import org.jhely.money.sdk.bridge.model.DeveloperFees;
import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.FeeExternalAccount;

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
public class DevelopersApi {
    private ApiClient apiClient;

    public DevelopersApi() {
        this(new ApiClient());
    }

    public DevelopersApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get the configured fee External Account
     * Get the configured fee External Account.
     * <p><b>200</b> - Get the configured fee External Account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return FeeExternalAccount
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec developerFeeExternalAccountGetRequestCreation() throws RestClientResponseException {
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

        ParameterizedTypeReference<FeeExternalAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/developer/fee_external_account", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get the configured fee External Account
     * Get the configured fee External Account.
     * <p><b>200</b> - Get the configured fee External Account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return FeeExternalAccount
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public FeeExternalAccount developerFeeExternalAccountGet() throws RestClientResponseException {
        ParameterizedTypeReference<FeeExternalAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerFeeExternalAccountGetRequestCreation().body(localVarReturnType);
    }

    /**
     * Get the configured fee External Account
     * Get the configured fee External Account.
     * <p><b>200</b> - Get the configured fee External Account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseEntity&lt;FeeExternalAccount&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<FeeExternalAccount> developerFeeExternalAccountGetWithHttpInfo() throws RestClientResponseException {
        ParameterizedTypeReference<FeeExternalAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerFeeExternalAccountGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Get the configured fee External Account
     * Get the configured fee External Account.
     * <p><b>200</b> - Get the configured fee External Account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec developerFeeExternalAccountGetWithResponseSpec() throws RestClientResponseException {
        return developerFeeExternalAccountGetRequestCreation();
    }

    /**
     * Configure a fee External Account
     * Configure a fee External Account.
     * <p><b>201</b> - External Account object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createFeeExternalAccountInput New External Account object to be created
     * @return FeeExternalAccount
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec developerFeeExternalAccountPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateFeeExternalAccountInput createFeeExternalAccountInput) throws RestClientResponseException {
        Object postBody = createFeeExternalAccountInput;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling developerFeeExternalAccountPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'createFeeExternalAccountInput' is set
        if (createFeeExternalAccountInput == null) {
            throw new RestClientResponseException("Missing the required parameter 'createFeeExternalAccountInput' when calling developerFeeExternalAccountPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<FeeExternalAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/developer/fee_external_account", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Configure a fee External Account
     * Configure a fee External Account.
     * <p><b>201</b> - External Account object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createFeeExternalAccountInput New External Account object to be created
     * @return FeeExternalAccount
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public FeeExternalAccount developerFeeExternalAccountPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateFeeExternalAccountInput createFeeExternalAccountInput) throws RestClientResponseException {
        ParameterizedTypeReference<FeeExternalAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerFeeExternalAccountPostRequestCreation(idempotencyKey, createFeeExternalAccountInput).body(localVarReturnType);
    }

    /**
     * Configure a fee External Account
     * Configure a fee External Account.
     * <p><b>201</b> - External Account object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createFeeExternalAccountInput New External Account object to be created
     * @return ResponseEntity&lt;FeeExternalAccount&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<FeeExternalAccount> developerFeeExternalAccountPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateFeeExternalAccountInput createFeeExternalAccountInput) throws RestClientResponseException {
        ParameterizedTypeReference<FeeExternalAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerFeeExternalAccountPostRequestCreation(idempotencyKey, createFeeExternalAccountInput).toEntity(localVarReturnType);
    }

    /**
     * Configure a fee External Account
     * Configure a fee External Account.
     * <p><b>201</b> - External Account object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createFeeExternalAccountInput New External Account object to be created
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec developerFeeExternalAccountPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateFeeExternalAccountInput createFeeExternalAccountInput) throws RestClientResponseException {
        return developerFeeExternalAccountPostRequestCreation(idempotencyKey, createFeeExternalAccountInput);
    }

    /**
     * Get the configured fees
     * Get fees that have been configured for supported products.
     * <p><b>200</b> - The configured fees for your developer account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return DeveloperFees
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec developerFeesGetRequestCreation() throws RestClientResponseException {
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

        ParameterizedTypeReference<DeveloperFees> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/developer/fees", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get the configured fees
     * Get fees that have been configured for supported products.
     * <p><b>200</b> - The configured fees for your developer account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return DeveloperFees
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public DeveloperFees developerFeesGet() throws RestClientResponseException {
        ParameterizedTypeReference<DeveloperFees> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerFeesGetRequestCreation().body(localVarReturnType);
    }

    /**
     * Get the configured fees
     * Get fees that have been configured for supported products.
     * <p><b>200</b> - The configured fees for your developer account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseEntity&lt;DeveloperFees&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DeveloperFees> developerFeesGetWithHttpInfo() throws RestClientResponseException {
        ParameterizedTypeReference<DeveloperFees> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerFeesGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Get the configured fees
     * Get fees that have been configured for supported products.
     * <p><b>200</b> - The configured fees for your developer account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec developerFeesGetWithResponseSpec() throws RestClientResponseException {
        return developerFeesGetRequestCreation();
    }

    /**
     * Update the configured fees
     * Update fees for supported products.
     * <p><b>200</b> - The configured fees have been updated for your developer account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param developerFees The default fee percent that will be applied to all your Liquidation Addresses. The value is a base 100 percentage, i.e. 10.2% is 10.2 in the API.
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec developerFeesPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull DeveloperFees developerFees) throws RestClientResponseException {
        Object postBody = developerFees;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling developerFeesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'developerFees' is set
        if (developerFees == null) {
            throw new RestClientResponseException("Missing the required parameter 'developerFees' when calling developerFeesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/developer/fees", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update the configured fees
     * Update fees for supported products.
     * <p><b>200</b> - The configured fees have been updated for your developer account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param developerFees The default fee percent that will be applied to all your Liquidation Addresses. The value is a base 100 percentage, i.e. 10.2% is 10.2 in the API.
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public void developerFeesPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull DeveloperFees developerFees) throws RestClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<>() {};
        developerFeesPostRequestCreation(idempotencyKey, developerFees).body(localVarReturnType);
    }

    /**
     * Update the configured fees
     * Update fees for supported products.
     * <p><b>200</b> - The configured fees have been updated for your developer account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param developerFees The default fee percent that will be applied to all your Liquidation Addresses. The value is a base 100 percentage, i.e. 10.2% is 10.2 in the API.
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Void> developerFeesPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull DeveloperFees developerFees) throws RestClientResponseException {
        ParameterizedTypeReference<Void> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerFeesPostRequestCreation(idempotencyKey, developerFees).toEntity(localVarReturnType);
    }

    /**
     * Update the configured fees
     * Update fees for supported products.
     * <p><b>200</b> - The configured fees have been updated for your developer account.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param developerFees The default fee percent that will be applied to all your Liquidation Addresses. The value is a base 100 percentage, i.e. 10.2% is 10.2 in the API.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec developerFeesPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull DeveloperFees developerFees) throws RestClientResponseException {
        return developerFeesPostRequestCreation(idempotencyKey, developerFees);
    }
}
