package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.CreateExternalAccountInput;
import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.ExternalAccount1;
import org.jhely.money.sdk.bridge.model.ExternalAccountResponse;
import org.jhely.money.sdk.bridge.model.UpdateExternalAccountInput;

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
public class ExternalAccountsApi {
    private ApiClient apiClient;

    public ExternalAccountsApi() {
        this(new ApiClient());
    }

    public ExternalAccountsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Delete a single External Account object
     * Delete an External Account object from the passed in External Account ID
     * <p><b>200</b> - Successfully deleted External Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ExternalAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDExternalAccountsExternalAccountIDDeleteRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDExternalAccountsExternalAccountIDDelete", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'externalAccountID' is set
        if (externalAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'externalAccountID' when calling customersCustomerIDExternalAccountsExternalAccountIDDelete", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("externalAccountID", externalAccountID);

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

        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/external_accounts/{externalAccountID}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Delete a single External Account object
     * Delete an External Account object from the passed in External Account ID
     * <p><b>200</b> - Successfully deleted External Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ExternalAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ExternalAccountResponse customersCustomerIDExternalAccountsExternalAccountIDDelete(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsExternalAccountIDDeleteRequestCreation(customerID, externalAccountID).body(localVarReturnType);
    }

    /**
     * Delete a single External Account object
     * Delete an External Account object from the passed in External Account ID
     * <p><b>200</b> - Successfully deleted External Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ResponseEntity&lt;ExternalAccountResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ExternalAccountResponse> customersCustomerIDExternalAccountsExternalAccountIDDeleteWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsExternalAccountIDDeleteRequestCreation(customerID, externalAccountID).toEntity(localVarReturnType);
    }

    /**
     * Delete a single External Account object
     * Delete an External Account object from the passed in External Account ID
     * <p><b>200</b> - Successfully deleted External Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDExternalAccountsExternalAccountIDDeleteWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        return customersCustomerIDExternalAccountsExternalAccountIDDeleteRequestCreation(customerID, externalAccountID);
    }

    /**
     * Retrieve an External Account object
     * Retrieve an External Account object (banks, debit cards etc) from the passed in customer ID and External Account ID
     * <p><b>200</b> - Successful External Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ExternalAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDExternalAccountsExternalAccountIDGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDExternalAccountsExternalAccountIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'externalAccountID' is set
        if (externalAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'externalAccountID' when calling customersCustomerIDExternalAccountsExternalAccountIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("externalAccountID", externalAccountID);

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

        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/external_accounts/{externalAccountID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Retrieve an External Account object
     * Retrieve an External Account object (banks, debit cards etc) from the passed in customer ID and External Account ID
     * <p><b>200</b> - Successful External Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ExternalAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ExternalAccountResponse customersCustomerIDExternalAccountsExternalAccountIDGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsExternalAccountIDGetRequestCreation(customerID, externalAccountID).body(localVarReturnType);
    }

    /**
     * Retrieve an External Account object
     * Retrieve an External Account object (banks, debit cards etc) from the passed in customer ID and External Account ID
     * <p><b>200</b> - Successful External Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ResponseEntity&lt;ExternalAccountResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ExternalAccountResponse> customersCustomerIDExternalAccountsExternalAccountIDGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsExternalAccountIDGetRequestCreation(customerID, externalAccountID).toEntity(localVarReturnType);
    }

    /**
     * Retrieve an External Account object
     * Retrieve an External Account object (banks, debit cards etc) from the passed in customer ID and External Account ID
     * <p><b>200</b> - Successful External Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDExternalAccountsExternalAccountIDGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        return customersCustomerIDExternalAccountsExternalAccountIDGetRequestCreation(customerID, externalAccountID);
    }

    /**
     * Update an External Account
     * 
     * <p><b>200</b> - External Account object updated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @param updateExternalAccountInput External Account details to be updated
     * @return ExternalAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDExternalAccountsExternalAccountIDPutRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID, @jakarta.annotation.Nonnull UpdateExternalAccountInput updateExternalAccountInput) throws RestClientResponseException {
        Object postBody = updateExternalAccountInput;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDExternalAccountsExternalAccountIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'externalAccountID' is set
        if (externalAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'externalAccountID' when calling customersCustomerIDExternalAccountsExternalAccountIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'updateExternalAccountInput' is set
        if (updateExternalAccountInput == null) {
            throw new RestClientResponseException("Missing the required parameter 'updateExternalAccountInput' when calling customersCustomerIDExternalAccountsExternalAccountIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("externalAccountID", externalAccountID);

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

        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/external_accounts/{externalAccountID}", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update an External Account
     * 
     * <p><b>200</b> - External Account object updated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @param updateExternalAccountInput External Account details to be updated
     * @return ExternalAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ExternalAccountResponse customersCustomerIDExternalAccountsExternalAccountIDPut(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID, @jakarta.annotation.Nonnull UpdateExternalAccountInput updateExternalAccountInput) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsExternalAccountIDPutRequestCreation(customerID, externalAccountID, updateExternalAccountInput).body(localVarReturnType);
    }

    /**
     * Update an External Account
     * 
     * <p><b>200</b> - External Account object updated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @param updateExternalAccountInput External Account details to be updated
     * @return ResponseEntity&lt;ExternalAccountResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ExternalAccountResponse> customersCustomerIDExternalAccountsExternalAccountIDPutWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID, @jakarta.annotation.Nonnull UpdateExternalAccountInput updateExternalAccountInput) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsExternalAccountIDPutRequestCreation(customerID, externalAccountID, updateExternalAccountInput).toEntity(localVarReturnType);
    }

    /**
     * Update an External Account
     * 
     * <p><b>200</b> - External Account object updated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @param updateExternalAccountInput External Account details to be updated
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDExternalAccountsExternalAccountIDPutWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID, @jakarta.annotation.Nonnull UpdateExternalAccountInput updateExternalAccountInput) throws RestClientResponseException {
        return customersCustomerIDExternalAccountsExternalAccountIDPutRequestCreation(customerID, externalAccountID, updateExternalAccountInput);
    }

    /**
     * Reactivate an External Account
     * Reactivate a previously deactivated External Account
     * <p><b>200</b> - Reactivated External Account object
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ExternalAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDExternalAccountsExternalAccountIDReactivatePostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDExternalAccountsExternalAccountIDReactivatePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDExternalAccountsExternalAccountIDReactivatePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'externalAccountID' is set
        if (externalAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'externalAccountID' when calling customersCustomerIDExternalAccountsExternalAccountIDReactivatePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("externalAccountID", externalAccountID);

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

        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/external_accounts/{externalAccountID}/reactivate", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Reactivate an External Account
     * Reactivate a previously deactivated External Account
     * <p><b>200</b> - Reactivated External Account object
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ExternalAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ExternalAccountResponse customersCustomerIDExternalAccountsExternalAccountIDReactivatePost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsExternalAccountIDReactivatePostRequestCreation(idempotencyKey, customerID, externalAccountID).body(localVarReturnType);
    }

    /**
     * Reactivate an External Account
     * Reactivate a previously deactivated External Account
     * <p><b>200</b> - Reactivated External Account object
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ResponseEntity&lt;ExternalAccountResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ExternalAccountResponse> customersCustomerIDExternalAccountsExternalAccountIDReactivatePostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsExternalAccountIDReactivatePostRequestCreation(idempotencyKey, customerID, externalAccountID).toEntity(localVarReturnType);
    }

    /**
     * Reactivate an External Account
     * Reactivate a previously deactivated External Account
     * <p><b>200</b> - Reactivated External Account object
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param externalAccountID The externalAccountID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDExternalAccountsExternalAccountIDReactivatePostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String externalAccountID) throws RestClientResponseException {
        return customersCustomerIDExternalAccountsExternalAccountIDReactivatePostRequestCreation(idempotencyKey, customerID, externalAccountID);
    }

    /**
     * Get all External Accounts
     * Get all External Accounts for a passed in customer.
     * <p><b>200</b> - List of External Accounts (the returned list is empty if no External Accounts exist)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ExternalAccount1
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDExternalAccountsGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDExternalAccountsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<ExternalAccount1> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/external_accounts", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all External Accounts
     * Get all External Accounts for a passed in customer.
     * <p><b>200</b> - List of External Accounts (the returned list is empty if no External Accounts exist)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ExternalAccount1
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ExternalAccount1 customersCustomerIDExternalAccountsGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccount1> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsGetRequestCreation(customerID, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Get all External Accounts
     * Get all External Accounts for a passed in customer.
     * <p><b>200</b> - List of External Accounts (the returned list is empty if no External Accounts exist)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;ExternalAccount1&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ExternalAccount1> customersCustomerIDExternalAccountsGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccount1> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsGetRequestCreation(customerID, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Get all External Accounts
     * Get all External Accounts for a passed in customer.
     * <p><b>200</b> - List of External Accounts (the returned list is empty if no External Accounts exist)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDExternalAccountsGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return customersCustomerIDExternalAccountsGetRequestCreation(customerID, limit, startingAfter, endingBefore);
    }

    /**
     * Create a new External Account
     * _Note_: If adding US external accounts, we recommend reading through the US Beneficiary Address Validation doc ([link](https://apidocs.bridge.xyz/docs/us-beneficiary-address-validation)) to avoid issues related to incorrect addresses. 
     * <p><b>201</b> - External Account object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createExternalAccountInput New External Account object to be created
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ExternalAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDExternalAccountsPostRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateExternalAccountInput createExternalAccountInput, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = createExternalAccountInput;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDExternalAccountsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDExternalAccountsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'createExternalAccountInput' is set
        if (createExternalAccountInput == null) {
            throw new RestClientResponseException("Missing the required parameter 'createExternalAccountInput' when calling customersCustomerIDExternalAccountsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/external_accounts", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a new External Account
     * _Note_: If adding US external accounts, we recommend reading through the US Beneficiary Address Validation doc ([link](https://apidocs.bridge.xyz/docs/us-beneficiary-address-validation)) to avoid issues related to incorrect addresses. 
     * <p><b>201</b> - External Account object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createExternalAccountInput New External Account object to be created
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ExternalAccountResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ExternalAccountResponse customersCustomerIDExternalAccountsPost(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateExternalAccountInput createExternalAccountInput, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsPostRequestCreation(customerID, idempotencyKey, createExternalAccountInput, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Create a new External Account
     * _Note_: If adding US external accounts, we recommend reading through the US Beneficiary Address Validation doc ([link](https://apidocs.bridge.xyz/docs/us-beneficiary-address-validation)) to avoid issues related to incorrect addresses. 
     * <p><b>201</b> - External Account object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createExternalAccountInput New External Account object to be created
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;ExternalAccountResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ExternalAccountResponse> customersCustomerIDExternalAccountsPostWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateExternalAccountInput createExternalAccountInput, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccountResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDExternalAccountsPostRequestCreation(customerID, idempotencyKey, createExternalAccountInput, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Create a new External Account
     * _Note_: If adding US external accounts, we recommend reading through the US Beneficiary Address Validation doc ([link](https://apidocs.bridge.xyz/docs/us-beneficiary-address-validation)) to avoid issues related to incorrect addresses. 
     * <p><b>201</b> - External Account object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param createExternalAccountInput New External Account object to be created
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDExternalAccountsPostWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateExternalAccountInput createExternalAccountInput, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return customersCustomerIDExternalAccountsPostRequestCreation(customerID, idempotencyKey, createExternalAccountInput, limit, startingAfter, endingBefore);
    }

    /**
     * Get all External Accounts
     * Get all External Accounts.
     * <p><b>200</b> - List of External Accounts (the returned list is empty if no External Accounts exist)
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ExternalAccount1
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec externalAccountsGetRequestCreation(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
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

        ParameterizedTypeReference<ExternalAccount1> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/external_accounts", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all External Accounts
     * Get all External Accounts.
     * <p><b>200</b> - List of External Accounts (the returned list is empty if no External Accounts exist)
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ExternalAccount1
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ExternalAccount1 externalAccountsGet(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccount1> localVarReturnType = new ParameterizedTypeReference<>() {};
        return externalAccountsGetRequestCreation(limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Get all External Accounts
     * Get all External Accounts.
     * <p><b>200</b> - List of External Accounts (the returned list is empty if no External Accounts exist)
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;ExternalAccount1&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ExternalAccount1> externalAccountsGetWithHttpInfo(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<ExternalAccount1> localVarReturnType = new ParameterizedTypeReference<>() {};
        return externalAccountsGetRequestCreation(limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Get all External Accounts
     * Get all External Accounts.
     * <p><b>200</b> - List of External Accounts (the returned list is empty if no External Accounts exist)
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is an external account id. If this is specified, the next page that starts with an external account right AFTER the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts older than the specified external account id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is an external account id. If this is specified, the previous page that ends with an external account right BEFORE the specified external account id on the external account timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that external accounts newer than the specified external account id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec externalAccountsGetWithResponseSpec(@jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return externalAccountsGetRequestCreation(limit, startingAfter, endingBefore);
    }
}
