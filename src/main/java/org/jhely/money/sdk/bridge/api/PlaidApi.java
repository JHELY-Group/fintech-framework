package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.PlaidExchangePublicToken;
import org.jhely.money.sdk.bridge.model.PlaidLinkRequest;
import org.jhely.money.sdk.bridge.model.PlaidPublicTokenExchanged;

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
public class PlaidApi {
    private ApiClient apiClient;

    public PlaidApi() {
        this(new ApiClient());
    }

    public PlaidApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Generate a Plaid Link token for a customer
     * 
     * <p><b>201</b> - Plaid Link token generated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @return PlaidLinkRequest
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDPlaidLinkRequestsPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDPlaidLinkRequestsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDPlaidLinkRequestsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<PlaidLinkRequest> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/plaid_link_requests", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Generate a Plaid Link token for a customer
     * 
     * <p><b>201</b> - Plaid Link token generated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @return PlaidLinkRequest
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public PlaidLinkRequest customersCustomerIDPlaidLinkRequestsPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<PlaidLinkRequest> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDPlaidLinkRequestsPostRequestCreation(idempotencyKey, customerID).body(localVarReturnType);
    }

    /**
     * Generate a Plaid Link token for a customer
     * 
     * <p><b>201</b> - Plaid Link token generated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @return ResponseEntity&lt;PlaidLinkRequest&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PlaidLinkRequest> customersCustomerIDPlaidLinkRequestsPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<PlaidLinkRequest> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDPlaidLinkRequestsPostRequestCreation(idempotencyKey, customerID).toEntity(localVarReturnType);
    }

    /**
     * Generate a Plaid Link token for a customer
     * 
     * <p><b>201</b> - Plaid Link token generated
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDPlaidLinkRequestsPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        return customersCustomerIDPlaidLinkRequestsPostRequestCreation(idempotencyKey, customerID);
    }

    /**
     * Exchange Plaid public token for an access token
     * 
     * <p><b>201</b> - Plaid public token exchanged
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param linkToken Plaid Link token
     * @param plaidExchangePublicToken Plaid public token to be exchanged
     * @return PlaidPublicTokenExchanged
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec plaidExchangePublicTokenLinkTokenPostRequestCreation(@jakarta.annotation.Nonnull String linkToken, @jakarta.annotation.Nonnull PlaidExchangePublicToken plaidExchangePublicToken) throws RestClientResponseException {
        Object postBody = plaidExchangePublicToken;
        // verify the required parameter 'linkToken' is set
        if (linkToken == null) {
            throw new RestClientResponseException("Missing the required parameter 'linkToken' when calling plaidExchangePublicTokenLinkTokenPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'plaidExchangePublicToken' is set
        if (plaidExchangePublicToken == null) {
            throw new RestClientResponseException("Missing the required parameter 'plaidExchangePublicToken' when calling plaidExchangePublicTokenLinkTokenPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("link_token", linkToken);

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

        ParameterizedTypeReference<PlaidPublicTokenExchanged> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/plaid_exchange_public_token/{link_token}", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Exchange Plaid public token for an access token
     * 
     * <p><b>201</b> - Plaid public token exchanged
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param linkToken Plaid Link token
     * @param plaidExchangePublicToken Plaid public token to be exchanged
     * @return PlaidPublicTokenExchanged
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public PlaidPublicTokenExchanged plaidExchangePublicTokenLinkTokenPost(@jakarta.annotation.Nonnull String linkToken, @jakarta.annotation.Nonnull PlaidExchangePublicToken plaidExchangePublicToken) throws RestClientResponseException {
        ParameterizedTypeReference<PlaidPublicTokenExchanged> localVarReturnType = new ParameterizedTypeReference<>() {};
        return plaidExchangePublicTokenLinkTokenPostRequestCreation(linkToken, plaidExchangePublicToken).body(localVarReturnType);
    }

    /**
     * Exchange Plaid public token for an access token
     * 
     * <p><b>201</b> - Plaid public token exchanged
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param linkToken Plaid Link token
     * @param plaidExchangePublicToken Plaid public token to be exchanged
     * @return ResponseEntity&lt;PlaidPublicTokenExchanged&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<PlaidPublicTokenExchanged> plaidExchangePublicTokenLinkTokenPostWithHttpInfo(@jakarta.annotation.Nonnull String linkToken, @jakarta.annotation.Nonnull PlaidExchangePublicToken plaidExchangePublicToken) throws RestClientResponseException {
        ParameterizedTypeReference<PlaidPublicTokenExchanged> localVarReturnType = new ParameterizedTypeReference<>() {};
        return plaidExchangePublicTokenLinkTokenPostRequestCreation(linkToken, plaidExchangePublicToken).toEntity(localVarReturnType);
    }

    /**
     * Exchange Plaid public token for an access token
     * 
     * <p><b>201</b> - Plaid public token exchanged
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param linkToken Plaid Link token
     * @param plaidExchangePublicToken Plaid public token to be exchanged
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec plaidExchangePublicTokenLinkTokenPostWithResponseSpec(@jakarta.annotation.Nonnull String linkToken, @jakarta.annotation.Nonnull PlaidExchangePublicToken plaidExchangePublicToken) throws RestClientResponseException {
        return plaidExchangePublicTokenLinkTokenPostRequestCreation(linkToken, plaidExchangePublicToken);
    }
}
