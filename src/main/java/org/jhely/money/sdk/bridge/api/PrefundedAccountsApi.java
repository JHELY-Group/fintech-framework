package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.PrefundedAccount;
import org.jhely.money.sdk.bridge.model.PrefundedAccountHistory;
import org.jhely.money.sdk.bridge.model.PrefundedAccounts;

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
public class PrefundedAccountsApi {
    private ApiClient apiClient;

    public PrefundedAccountsApi() {
        this(new ApiClient());
    }

    public PrefundedAccountsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get a list of all Prefunded Account
     * Retrieve a all Prefunded Accounts
     * <p><b>200</b> - List of prefunded accounts.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return PrefundedAccounts
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec prefundedAccountsGetRequestCreation() throws RestClientResponseException {
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

        ParameterizedTypeReference<PrefundedAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/prefunded_accounts", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a list of all Prefunded Account
     * Retrieve a all Prefunded Accounts
     * <p><b>200</b> - List of prefunded accounts.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return PrefundedAccounts
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public PrefundedAccounts prefundedAccountsGet() throws RestClientResponseException {
        ParameterizedTypeReference<PrefundedAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return prefundedAccountsGetRequestCreation().body(localVarReturnType);
    }

    /**
     * Get a list of all Prefunded Account
     * Retrieve a all Prefunded Accounts
     * <p><b>200</b> - List of prefunded accounts.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseEntity&lt;PrefundedAccounts&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PrefundedAccounts> prefundedAccountsGetWithHttpInfo() throws RestClientResponseException {
        ParameterizedTypeReference<PrefundedAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return prefundedAccountsGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Get a list of all Prefunded Account
     * Retrieve a all Prefunded Accounts
     * <p><b>200</b> - List of prefunded accounts.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec prefundedAccountsGetWithResponseSpec() throws RestClientResponseException {
        return prefundedAccountsGetRequestCreation();
    }

    /**
     * Get details for a specific Prefunded Account
     * Retrieve a Prefunded Account
     * <p><b>200</b> - Successful Prefunded Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param prefundedAccountID The prefundedAccountID parameter
     * @return PrefundedAccount
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec prefundedAccountsPrefundedAccountIDGetRequestCreation(@jakarta.annotation.Nonnull String prefundedAccountID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'prefundedAccountID' is set
        if (prefundedAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'prefundedAccountID' when calling prefundedAccountsPrefundedAccountIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("prefundedAccountID", prefundedAccountID);

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

        ParameterizedTypeReference<PrefundedAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/prefunded_accounts/{prefundedAccountID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get details for a specific Prefunded Account
     * Retrieve a Prefunded Account
     * <p><b>200</b> - Successful Prefunded Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param prefundedAccountID The prefundedAccountID parameter
     * @return PrefundedAccount
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public PrefundedAccount prefundedAccountsPrefundedAccountIDGet(@jakarta.annotation.Nonnull String prefundedAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<PrefundedAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return prefundedAccountsPrefundedAccountIDGetRequestCreation(prefundedAccountID).body(localVarReturnType);
    }

    /**
     * Get details for a specific Prefunded Account
     * Retrieve a Prefunded Account
     * <p><b>200</b> - Successful Prefunded Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param prefundedAccountID The prefundedAccountID parameter
     * @return ResponseEntity&lt;PrefundedAccount&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PrefundedAccount> prefundedAccountsPrefundedAccountIDGetWithHttpInfo(@jakarta.annotation.Nonnull String prefundedAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<PrefundedAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return prefundedAccountsPrefundedAccountIDGetRequestCreation(prefundedAccountID).toEntity(localVarReturnType);
    }

    /**
     * Get details for a specific Prefunded Account
     * Retrieve a Prefunded Account
     * <p><b>200</b> - Successful Prefunded Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param prefundedAccountID The prefundedAccountID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec prefundedAccountsPrefundedAccountIDGetWithResponseSpec(@jakarta.annotation.Nonnull String prefundedAccountID) throws RestClientResponseException {
        return prefundedAccountsPrefundedAccountIDGetRequestCreation(prefundedAccountID);
    }

    /**
     * Get funding history of a Prefunded Account
     * Retrieve the funding events and returns for a Prefunded Account
     * <p><b>200</b> - Successful Prefunded Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param prefundedAccountID The prefundedAccountID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a prefunded event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a prefunded event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return PrefundedAccountHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec prefundedAccountsPrefundedAccountIDHistoryGetRequestCreation(@jakarta.annotation.Nonnull String prefundedAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'prefundedAccountID' is set
        if (prefundedAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'prefundedAccountID' when calling prefundedAccountsPrefundedAccountIDHistoryGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("prefundedAccountID", prefundedAccountID);

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

        ParameterizedTypeReference<PrefundedAccountHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/prefunded_accounts/{prefundedAccountID}/history", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get funding history of a Prefunded Account
     * Retrieve the funding events and returns for a Prefunded Account
     * <p><b>200</b> - Successful Prefunded Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param prefundedAccountID The prefundedAccountID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a prefunded event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a prefunded event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return PrefundedAccountHistory
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public PrefundedAccountHistory prefundedAccountsPrefundedAccountIDHistoryGet(@jakarta.annotation.Nonnull String prefundedAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<PrefundedAccountHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return prefundedAccountsPrefundedAccountIDHistoryGetRequestCreation(prefundedAccountID, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Get funding history of a Prefunded Account
     * Retrieve the funding events and returns for a Prefunded Account
     * <p><b>200</b> - Successful Prefunded Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param prefundedAccountID The prefundedAccountID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a prefunded event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a prefunded event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;PrefundedAccountHistory&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PrefundedAccountHistory> prefundedAccountsPrefundedAccountIDHistoryGetWithHttpInfo(@jakarta.annotation.Nonnull String prefundedAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<PrefundedAccountHistory> localVarReturnType = new ParameterizedTypeReference<>() {};
        return prefundedAccountsPrefundedAccountIDHistoryGetRequestCreation(prefundedAccountID, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Get funding history of a Prefunded Account
     * Retrieve the funding events and returns for a Prefunded Account
     * <p><b>200</b> - Successful Prefunded Account object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param prefundedAccountID The prefundedAccountID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a prefunded event id. If this is specified, the next page that starts with an event right AFTER the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events older than the specified event id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a prefunded event id. If this is specified, the previous page that ends with an event right BEFORE the specified event id on the event timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that events newer than the specified event id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec prefundedAccountsPrefundedAccountIDHistoryGetWithResponseSpec(@jakarta.annotation.Nonnull String prefundedAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return prefundedAccountsPrefundedAccountIDHistoryGetRequestCreation(prefundedAccountID, limit, startingAfter, endingBefore);
    }
}
