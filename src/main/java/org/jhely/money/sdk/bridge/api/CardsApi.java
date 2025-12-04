package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.ArrayOfAllCardAccounts;
import org.jhely.money.sdk.bridge.model.AuthorizationControls;
import org.jhely.money.sdk.bridge.model.CardAccount;
import org.jhely.money.sdk.bridge.model.CardAccountEphemeralKeyResponse;
import org.jhely.money.sdk.bridge.model.CardAccountFundingInstructions;
import org.jhely.money.sdk.bridge.model.CardDesigns;
import org.jhely.money.sdk.bridge.model.CardPinUpdateResponse;
import org.jhely.money.sdk.bridge.model.CardProgramSummary;
import org.jhely.money.sdk.bridge.model.CardPushProvisioningResponse;
import org.jhely.money.sdk.bridge.model.CardTransaction;
import org.jhely.money.sdk.bridge.model.CardUnfreezeResponse;
import org.jhely.money.sdk.bridge.model.CardWithdrawal;
import org.jhely.money.sdk.bridge.model.CustomersCustomerIDCardAccountsCardAccountIDFreezePost200Response;
import org.jhely.money.sdk.bridge.model.Error;
import java.io.File;
import org.jhely.money.sdk.bridge.model.ListOfCardTransactions;
import org.jhely.money.sdk.bridge.model.ListOfPendingCardAuthorizations;
import org.jhely.money.sdk.bridge.model.ListOfWithdrawals;
import org.jhely.money.sdk.bridge.model.PostCardAccountDepositAddressInput;
import org.jhely.money.sdk.bridge.model.PostCardAccountEphemeralKeyInput;
import org.jhely.money.sdk.bridge.model.PostCardAccountsInput;
import org.jhely.money.sdk.bridge.model.PostCardFreezeInput;
import org.jhely.money.sdk.bridge.model.PostCardPushProvisioningInput;
import org.jhely.money.sdk.bridge.model.PostCardUnfreezeInput;
import java.util.UUID;

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
public class CardsApi {
    private ApiClient apiClient;

    public CardsApi() {
        this(new ApiClient());
    }

    public CardsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Retrieve authorization controls
     * Retrieve the applicable spend limits for the given card account
     * <p><b>200</b> - The authorization controls
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @return AuthorizationControls
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDAuthControlsGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDAuthControlsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDAuthControlsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

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

        ParameterizedTypeReference<AuthorizationControls> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/auth_controls", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Retrieve authorization controls
     * Retrieve the applicable spend limits for the given card account
     * <p><b>200</b> - The authorization controls
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @return AuthorizationControls
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public AuthorizationControls customersCustomerIDCardAccountsCardAccountIDAuthControlsGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<AuthorizationControls> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDAuthControlsGetRequestCreation(customerID, cardAccountID).body(localVarReturnType);
    }

    /**
     * Retrieve authorization controls
     * Retrieve the applicable spend limits for the given card account
     * <p><b>200</b> - The authorization controls
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @return ResponseEntity&lt;AuthorizationControls&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<AuthorizationControls> customersCustomerIDCardAccountsCardAccountIDAuthControlsGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<AuthorizationControls> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDAuthControlsGetRequestCreation(customerID, cardAccountID).toEntity(localVarReturnType);
    }

    /**
     * Retrieve authorization controls
     * Retrieve the applicable spend limits for the given card account
     * <p><b>200</b> - The authorization controls
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDAuthControlsGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDAuthControlsGetRequestCreation(customerID, cardAccountID);
    }

    /**
     * Retrieve pending card authorizations
     * Retrieve pending card authorizations. Note: this endpoint is not paginated
     * <p><b>200</b> - Pending card authorizations
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of pending card authorizations to return, with a max of 200. The default is 100
     * @param startingTime The starting time in ISO8601 format. Default is 50 days ago.
     * @param endingTime The exclusive ending time in ISO8601 format. Default is now.
     * @param status A status to filter the authorizations by. If not provided, will default to &#x60;approved&#x60;.
     * @return ListOfPendingCardAuthorizations
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDAuthorizationsGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingTime, @jakarta.annotation.Nullable String endingTime, @jakarta.annotation.Nullable String status) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDAuthorizationsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDAuthorizationsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "starting_time", startingTime));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ending_time", endingTime));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "status", status));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<ListOfPendingCardAuthorizations> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/authorizations", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Retrieve pending card authorizations
     * Retrieve pending card authorizations. Note: this endpoint is not paginated
     * <p><b>200</b> - Pending card authorizations
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of pending card authorizations to return, with a max of 200. The default is 100
     * @param startingTime The starting time in ISO8601 format. Default is 50 days ago.
     * @param endingTime The exclusive ending time in ISO8601 format. Default is now.
     * @param status A status to filter the authorizations by. If not provided, will default to &#x60;approved&#x60;.
     * @return ListOfPendingCardAuthorizations
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ListOfPendingCardAuthorizations customersCustomerIDCardAccountsCardAccountIDAuthorizationsGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingTime, @jakarta.annotation.Nullable String endingTime, @jakarta.annotation.Nullable String status) throws RestClientResponseException {
        ParameterizedTypeReference<ListOfPendingCardAuthorizations> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDAuthorizationsGetRequestCreation(customerID, cardAccountID, limit, startingTime, endingTime, status).body(localVarReturnType);
    }

    /**
     * Retrieve pending card authorizations
     * Retrieve pending card authorizations. Note: this endpoint is not paginated
     * <p><b>200</b> - Pending card authorizations
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of pending card authorizations to return, with a max of 200. The default is 100
     * @param startingTime The starting time in ISO8601 format. Default is 50 days ago.
     * @param endingTime The exclusive ending time in ISO8601 format. Default is now.
     * @param status A status to filter the authorizations by. If not provided, will default to &#x60;approved&#x60;.
     * @return ResponseEntity&lt;ListOfPendingCardAuthorizations&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListOfPendingCardAuthorizations> customersCustomerIDCardAccountsCardAccountIDAuthorizationsGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingTime, @jakarta.annotation.Nullable String endingTime, @jakarta.annotation.Nullable String status) throws RestClientResponseException {
        ParameterizedTypeReference<ListOfPendingCardAuthorizations> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDAuthorizationsGetRequestCreation(customerID, cardAccountID, limit, startingTime, endingTime, status).toEntity(localVarReturnType);
    }

    /**
     * Retrieve pending card authorizations
     * Retrieve pending card authorizations. Note: this endpoint is not paginated
     * <p><b>200</b> - Pending card authorizations
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of pending card authorizations to return, with a max of 200. The default is 100
     * @param startingTime The starting time in ISO8601 format. Default is 50 days ago.
     * @param endingTime The exclusive ending time in ISO8601 format. Default is now.
     * @param status A status to filter the authorizations by. If not provided, will default to &#x60;approved&#x60;.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDAuthorizationsGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingTime, @jakarta.annotation.Nullable String endingTime, @jakarta.annotation.Nullable String status) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDAuthorizationsGetRequestCreation(customerID, cardAccountID, limit, startingTime, endingTime, status);
    }

    /**
     * Create a mobile wallet push provisioning request
     * Create a request to push-provision a virtual card to a mobile wallet. This endpoint is part of a multiple-step integration that must be completed with each mobile wallet partner
     * <p><b>200</b> - A successful push provisioning response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardPushProvisioningInput A mobile wallet push provisioning request
     * @return CardPushProvisioningResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDCreateMobileWalletProvisioningRequestPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardPushProvisioningInput postCardPushProvisioningInput) throws RestClientResponseException {
        Object postBody = postCardPushProvisioningInput;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDCardAccountsCardAccountIDCreateMobileWalletProvisioningRequestPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDCreateMobileWalletProvisioningRequestPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDCreateMobileWalletProvisioningRequestPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

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

        ParameterizedTypeReference<CardPushProvisioningResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/create_mobile_wallet_provisioning_request", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a mobile wallet push provisioning request
     * Create a request to push-provision a virtual card to a mobile wallet. This endpoint is part of a multiple-step integration that must be completed with each mobile wallet partner
     * <p><b>200</b> - A successful push provisioning response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardPushProvisioningInput A mobile wallet push provisioning request
     * @return CardPushProvisioningResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardPushProvisioningResponse customersCustomerIDCardAccountsCardAccountIDCreateMobileWalletProvisioningRequestPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardPushProvisioningInput postCardPushProvisioningInput) throws RestClientResponseException {
        ParameterizedTypeReference<CardPushProvisioningResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDCreateMobileWalletProvisioningRequestPostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardPushProvisioningInput).body(localVarReturnType);
    }

    /**
     * Create a mobile wallet push provisioning request
     * Create a request to push-provision a virtual card to a mobile wallet. This endpoint is part of a multiple-step integration that must be completed with each mobile wallet partner
     * <p><b>200</b> - A successful push provisioning response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardPushProvisioningInput A mobile wallet push provisioning request
     * @return ResponseEntity&lt;CardPushProvisioningResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardPushProvisioningResponse> customersCustomerIDCardAccountsCardAccountIDCreateMobileWalletProvisioningRequestPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardPushProvisioningInput postCardPushProvisioningInput) throws RestClientResponseException {
        ParameterizedTypeReference<CardPushProvisioningResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDCreateMobileWalletProvisioningRequestPostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardPushProvisioningInput).toEntity(localVarReturnType);
    }

    /**
     * Create a mobile wallet push provisioning request
     * Create a request to push-provision a virtual card to a mobile wallet. This endpoint is part of a multiple-step integration that must be completed with each mobile wallet partner
     * <p><b>200</b> - A successful push provisioning response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardPushProvisioningInput A mobile wallet push provisioning request
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDCreateMobileWalletProvisioningRequestPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardPushProvisioningInput postCardPushProvisioningInput) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDCreateMobileWalletProvisioningRequestPostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardPushProvisioningInput);
    }

    /**
     * Provision an additional top-up deposit address for the card account
     * Provision an additional deposit address for the card account, to allow topping up the card from multiple chains. This is only applicable to Bridge-custodied top-up card accounts. These additional deposit addresses will also be shown in the &#x60;additional_funding_instructions&#x60; field when fetching the card account details afterwards.
     * <p><b>200</b> - Details of the successfully created deposit address
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardAccountDepositAddressInput The chain to provision the new deposit address on.
     * @return CardAccountFundingInstructions
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDDepositAddressesPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull PostCardAccountDepositAddressInput postCardAccountDepositAddressInput) throws RestClientResponseException {
        Object postBody = postCardAccountDepositAddressInput;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDCardAccountsCardAccountIDDepositAddressesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDDepositAddressesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDDepositAddressesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'postCardAccountDepositAddressInput' is set
        if (postCardAccountDepositAddressInput == null) {
            throw new RestClientResponseException("Missing the required parameter 'postCardAccountDepositAddressInput' when calling customersCustomerIDCardAccountsCardAccountIDDepositAddressesPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

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

        ParameterizedTypeReference<CardAccountFundingInstructions> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/deposit_addresses", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Provision an additional top-up deposit address for the card account
     * Provision an additional deposit address for the card account, to allow topping up the card from multiple chains. This is only applicable to Bridge-custodied top-up card accounts. These additional deposit addresses will also be shown in the &#x60;additional_funding_instructions&#x60; field when fetching the card account details afterwards.
     * <p><b>200</b> - Details of the successfully created deposit address
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardAccountDepositAddressInput The chain to provision the new deposit address on.
     * @return CardAccountFundingInstructions
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardAccountFundingInstructions customersCustomerIDCardAccountsCardAccountIDDepositAddressesPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull PostCardAccountDepositAddressInput postCardAccountDepositAddressInput) throws RestClientResponseException {
        ParameterizedTypeReference<CardAccountFundingInstructions> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDDepositAddressesPostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardAccountDepositAddressInput).body(localVarReturnType);
    }

    /**
     * Provision an additional top-up deposit address for the card account
     * Provision an additional deposit address for the card account, to allow topping up the card from multiple chains. This is only applicable to Bridge-custodied top-up card accounts. These additional deposit addresses will also be shown in the &#x60;additional_funding_instructions&#x60; field when fetching the card account details afterwards.
     * <p><b>200</b> - Details of the successfully created deposit address
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardAccountDepositAddressInput The chain to provision the new deposit address on.
     * @return ResponseEntity&lt;CardAccountFundingInstructions&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardAccountFundingInstructions> customersCustomerIDCardAccountsCardAccountIDDepositAddressesPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull PostCardAccountDepositAddressInput postCardAccountDepositAddressInput) throws RestClientResponseException {
        ParameterizedTypeReference<CardAccountFundingInstructions> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDDepositAddressesPostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardAccountDepositAddressInput).toEntity(localVarReturnType);
    }

    /**
     * Provision an additional top-up deposit address for the card account
     * Provision an additional deposit address for the card account, to allow topping up the card from multiple chains. This is only applicable to Bridge-custodied top-up card accounts. These additional deposit addresses will also be shown in the &#x60;additional_funding_instructions&#x60; field when fetching the card account details afterwards.
     * <p><b>200</b> - Details of the successfully created deposit address
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardAccountDepositAddressInput The chain to provision the new deposit address on.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDDepositAddressesPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull PostCardAccountDepositAddressInput postCardAccountDepositAddressInput) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDDepositAddressesPostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardAccountDepositAddressInput);
    }

    /**
     * Generate an Ephemeral Key to Reveal Card Details
     * Generates a one-time ephemeral key that can be used to reveal card details. Please see the integration guide on [safely revealing card details](https://apidocs.bridge.xyz/docs/safely-reveal-card-details-to-customers) for more information.
     * <p><b>200</b> - Ephemeral key to reveal card details
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param cardAccountID The ID of the card account
     * @param postCardAccountEphemeralKeyInput The client-side nonce that will be associated with the ephemeral key
     * @return CardAccountEphemeralKeyResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDEphemeralKeysPostRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull UUID cardAccountID, @jakarta.annotation.Nullable PostCardAccountEphemeralKeyInput postCardAccountEphemeralKeyInput) throws RestClientResponseException {
        Object postBody = postCardAccountEphemeralKeyInput;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDEphemeralKeysPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDCardAccountsCardAccountIDEphemeralKeysPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDEphemeralKeysPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

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

        ParameterizedTypeReference<CardAccountEphemeralKeyResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/ephemeral_keys", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Generate an Ephemeral Key to Reveal Card Details
     * Generates a one-time ephemeral key that can be used to reveal card details. Please see the integration guide on [safely revealing card details](https://apidocs.bridge.xyz/docs/safely-reveal-card-details-to-customers) for more information.
     * <p><b>200</b> - Ephemeral key to reveal card details
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param cardAccountID The ID of the card account
     * @param postCardAccountEphemeralKeyInput The client-side nonce that will be associated with the ephemeral key
     * @return CardAccountEphemeralKeyResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardAccountEphemeralKeyResponse customersCustomerIDCardAccountsCardAccountIDEphemeralKeysPost(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull UUID cardAccountID, @jakarta.annotation.Nullable PostCardAccountEphemeralKeyInput postCardAccountEphemeralKeyInput) throws RestClientResponseException {
        ParameterizedTypeReference<CardAccountEphemeralKeyResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDEphemeralKeysPostRequestCreation(customerID, idempotencyKey, cardAccountID, postCardAccountEphemeralKeyInput).body(localVarReturnType);
    }

    /**
     * Generate an Ephemeral Key to Reveal Card Details
     * Generates a one-time ephemeral key that can be used to reveal card details. Please see the integration guide on [safely revealing card details](https://apidocs.bridge.xyz/docs/safely-reveal-card-details-to-customers) for more information.
     * <p><b>200</b> - Ephemeral key to reveal card details
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param cardAccountID The ID of the card account
     * @param postCardAccountEphemeralKeyInput The client-side nonce that will be associated with the ephemeral key
     * @return ResponseEntity&lt;CardAccountEphemeralKeyResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardAccountEphemeralKeyResponse> customersCustomerIDCardAccountsCardAccountIDEphemeralKeysPostWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull UUID cardAccountID, @jakarta.annotation.Nullable PostCardAccountEphemeralKeyInput postCardAccountEphemeralKeyInput) throws RestClientResponseException {
        ParameterizedTypeReference<CardAccountEphemeralKeyResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDEphemeralKeysPostRequestCreation(customerID, idempotencyKey, cardAccountID, postCardAccountEphemeralKeyInput).toEntity(localVarReturnType);
    }

    /**
     * Generate an Ephemeral Key to Reveal Card Details
     * Generates a one-time ephemeral key that can be used to reveal card details. Please see the integration guide on [safely revealing card details](https://apidocs.bridge.xyz/docs/safely-reveal-card-details-to-customers) for more information.
     * <p><b>200</b> - Ephemeral key to reveal card details
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param cardAccountID The ID of the card account
     * @param postCardAccountEphemeralKeyInput The client-side nonce that will be associated with the ephemeral key
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDEphemeralKeysPostWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull UUID cardAccountID, @jakarta.annotation.Nullable PostCardAccountEphemeralKeyInput postCardAccountEphemeralKeyInput) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDEphemeralKeysPostRequestCreation(customerID, idempotencyKey, cardAccountID, postCardAccountEphemeralKeyInput);
    }

    /**
     * Place a freeze on the card account
     * Place a freeze on the card account
     * <p><b>200</b> - The successfully placed freeze
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardFreezeInput The freeze to be placed on the card account
     * @return CustomersCustomerIDCardAccountsCardAccountIDFreezePost200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDFreezePostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardFreezeInput postCardFreezeInput) throws RestClientResponseException {
        Object postBody = postCardFreezeInput;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDCardAccountsCardAccountIDFreezePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDFreezePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDFreezePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

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

        ParameterizedTypeReference<CustomersCustomerIDCardAccountsCardAccountIDFreezePost200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/freeze", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Place a freeze on the card account
     * Place a freeze on the card account
     * <p><b>200</b> - The successfully placed freeze
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardFreezeInput The freeze to be placed on the card account
     * @return CustomersCustomerIDCardAccountsCardAccountIDFreezePost200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CustomersCustomerIDCardAccountsCardAccountIDFreezePost200Response customersCustomerIDCardAccountsCardAccountIDFreezePost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardFreezeInput postCardFreezeInput) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDCardAccountsCardAccountIDFreezePost200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDFreezePostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardFreezeInput).body(localVarReturnType);
    }

    /**
     * Place a freeze on the card account
     * Place a freeze on the card account
     * <p><b>200</b> - The successfully placed freeze
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardFreezeInput The freeze to be placed on the card account
     * @return ResponseEntity&lt;CustomersCustomerIDCardAccountsCardAccountIDFreezePost200Response&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CustomersCustomerIDCardAccountsCardAccountIDFreezePost200Response> customersCustomerIDCardAccountsCardAccountIDFreezePostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardFreezeInput postCardFreezeInput) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDCardAccountsCardAccountIDFreezePost200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDFreezePostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardFreezeInput).toEntity(localVarReturnType);
    }

    /**
     * Place a freeze on the card account
     * Place a freeze on the card account
     * <p><b>200</b> - The successfully placed freeze
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardFreezeInput The freeze to be placed on the card account
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDFreezePostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardFreezeInput postCardFreezeInput) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDFreezePostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardFreezeInput);
    }

    /**
     * Retrieve a card account
     * Retrieve the card account with the specified ID
     * <p><b>200</b> - The retrieved card account
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @return CardAccount
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

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

        ParameterizedTypeReference<CardAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Retrieve a card account
     * Retrieve the card account with the specified ID
     * <p><b>200</b> - The retrieved card account
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @return CardAccount
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardAccount customersCustomerIDCardAccountsCardAccountIDGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<CardAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDGetRequestCreation(customerID, cardAccountID).body(localVarReturnType);
    }

    /**
     * Retrieve a card account
     * Retrieve the card account with the specified ID
     * <p><b>200</b> - The retrieved card account
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @return ResponseEntity&lt;CardAccount&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardAccount> customersCustomerIDCardAccountsCardAccountIDGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<CardAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDGetRequestCreation(customerID, cardAccountID).toEntity(localVarReturnType);
    }

    /**
     * Retrieve a card account
     * Retrieve the card account with the specified ID
     * <p><b>200</b> - The retrieved card account
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDGetRequestCreation(customerID, cardAccountID);
    }

    /**
     * Create Card PIN Update URL
     * Generates a URL that can be used to render a secure frame to update the PIN for a card account. The URL is single-use and time-limited.
     * <p><b>200</b> - URL to update the card PIN
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param cardAccountID The ID of the card account
     * @return CardPinUpdateResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDPinPostRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull UUID cardAccountID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDPinPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDCardAccountsCardAccountIDPinPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDPinPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

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

        ParameterizedTypeReference<CardPinUpdateResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/pin", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create Card PIN Update URL
     * Generates a URL that can be used to render a secure frame to update the PIN for a card account. The URL is single-use and time-limited.
     * <p><b>200</b> - URL to update the card PIN
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param cardAccountID The ID of the card account
     * @return CardPinUpdateResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardPinUpdateResponse customersCustomerIDCardAccountsCardAccountIDPinPost(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull UUID cardAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<CardPinUpdateResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDPinPostRequestCreation(customerID, idempotencyKey, cardAccountID).body(localVarReturnType);
    }

    /**
     * Create Card PIN Update URL
     * Generates a URL that can be used to render a secure frame to update the PIN for a card account. The URL is single-use and time-limited.
     * <p><b>200</b> - URL to update the card PIN
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param cardAccountID The ID of the card account
     * @return ResponseEntity&lt;CardPinUpdateResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardPinUpdateResponse> customersCustomerIDCardAccountsCardAccountIDPinPostWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull UUID cardAccountID) throws RestClientResponseException {
        ParameterizedTypeReference<CardPinUpdateResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDPinPostRequestCreation(customerID, idempotencyKey, cardAccountID).toEntity(localVarReturnType);
    }

    /**
     * Create Card PIN Update URL
     * Generates a URL that can be used to render a secure frame to update the PIN for a card account. The URL is single-use and time-limited.
     * <p><b>200</b> - URL to update the card PIN
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param cardAccountID The ID of the card account
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDPinPostWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull UUID cardAccountID) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDPinPostRequestCreation(customerID, idempotencyKey, cardAccountID);
    }

    /**
     * Generate a card account statement
     * Generate a card account statement for the specified period
     * <p><b>200</b> - The HTTP response that includes a PDF file as an attachment, with the &#x60;Content-Type&#x60; set to &#x60;application/pdf&#x60; and the &#x60;Content-Disposition&#x60; header configured to indicate it is a statement PDF attachment
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param period A string in the &#x60;YYYYMM&#x60; format representing a card statement period
     * @return File
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDStatementsPeriodPdfPostRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String period) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDStatementsPeriodPdfPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDStatementsPeriodPdfPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'period' is set
        if (period == null) {
            throw new RestClientResponseException("Missing the required parameter 'period' when calling customersCustomerIDCardAccountsCardAccountIDStatementsPeriodPdfPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);
        pathParams.put("period", period);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        final String[] localVarAccepts = { 
            "application/pdf", "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<File> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/statements/{period}.pdf", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Generate a card account statement
     * Generate a card account statement for the specified period
     * <p><b>200</b> - The HTTP response that includes a PDF file as an attachment, with the &#x60;Content-Type&#x60; set to &#x60;application/pdf&#x60; and the &#x60;Content-Disposition&#x60; header configured to indicate it is a statement PDF attachment
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param period A string in the &#x60;YYYYMM&#x60; format representing a card statement period
     * @return File
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public File customersCustomerIDCardAccountsCardAccountIDStatementsPeriodPdfPost(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String period) throws RestClientResponseException {
        ParameterizedTypeReference<File> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDStatementsPeriodPdfPostRequestCreation(customerID, cardAccountID, period).body(localVarReturnType);
    }

    /**
     * Generate a card account statement
     * Generate a card account statement for the specified period
     * <p><b>200</b> - The HTTP response that includes a PDF file as an attachment, with the &#x60;Content-Type&#x60; set to &#x60;application/pdf&#x60; and the &#x60;Content-Disposition&#x60; header configured to indicate it is a statement PDF attachment
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param period A string in the &#x60;YYYYMM&#x60; format representing a card statement period
     * @return ResponseEntity&lt;File&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<File> customersCustomerIDCardAccountsCardAccountIDStatementsPeriodPdfPostWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String period) throws RestClientResponseException {
        ParameterizedTypeReference<File> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDStatementsPeriodPdfPostRequestCreation(customerID, cardAccountID, period).toEntity(localVarReturnType);
    }

    /**
     * Generate a card account statement
     * Generate a card account statement for the specified period
     * <p><b>200</b> - The HTTP response that includes a PDF file as an attachment, with the &#x60;Content-Type&#x60; set to &#x60;application/pdf&#x60; and the &#x60;Content-Disposition&#x60; header configured to indicate it is a statement PDF attachment
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param period A string in the &#x60;YYYYMM&#x60; format representing a card statement period
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDStatementsPeriodPdfPostWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String period) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDStatementsPeriodPdfPostRequestCreation(customerID, cardAccountID, period);
    }

    /**
     * Retrieve card transactions
     * Retrieve completed card transactions and card-related crypto transaction activities
     * <p><b>200</b> - Card transactions
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of transactions to return, with a max of 200. The default is 100
     * @param startingTime The starting time in ISO8601 format. Default is 50 days ago.
     * @param endingTime The exclusive ending time in ISO8601 format. Default is now.
     * @param pageSize The max number of items to return for the requested page, with a max of 200. The default is 100
     * @param page A specific page to fetch. If omitted, the first page (starting at 1) will be returned. Note that this is only supported for navigating posted transactions.
     * @param status A status to filter the transactions by. If not provided, will default to &#x60;posted&#x60;.
     * @param paginationToken A pagination token to fetch the next page of transactions.
     * @param categoryFamily A category family to filter the transactions by. If set to &#x60;cards&#x60;, only card purchase related transactions will be returned. If set to &#x60;crypto&#x60;, only funding related transactions will be returned.&#x60; If not provided, all posted transactions will be returned.
     * @return ListOfCardTransactions
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDTransactionsGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingTime, @jakarta.annotation.Nullable String endingTime, @jakarta.annotation.Nullable String pageSize, @jakarta.annotation.Nullable String page, @jakarta.annotation.Nullable List<String> status, @jakarta.annotation.Nullable String paginationToken, @jakarta.annotation.Nullable String categoryFamily) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDTransactionsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDTransactionsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "starting_time", startingTime));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ending_time", endingTime));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page_size", pageSize));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "page", page));
        queryParams.putAll(apiClient.parameterToMultiValueMap(ApiClient.CollectionFormat.valueOf("multi".toUpperCase(Locale.ROOT)), "status", status));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "pagination_token", paginationToken));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "category_family", categoryFamily));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<ListOfCardTransactions> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/transactions", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Retrieve card transactions
     * Retrieve completed card transactions and card-related crypto transaction activities
     * <p><b>200</b> - Card transactions
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of transactions to return, with a max of 200. The default is 100
     * @param startingTime The starting time in ISO8601 format. Default is 50 days ago.
     * @param endingTime The exclusive ending time in ISO8601 format. Default is now.
     * @param pageSize The max number of items to return for the requested page, with a max of 200. The default is 100
     * @param page A specific page to fetch. If omitted, the first page (starting at 1) will be returned. Note that this is only supported for navigating posted transactions.
     * @param status A status to filter the transactions by. If not provided, will default to &#x60;posted&#x60;.
     * @param paginationToken A pagination token to fetch the next page of transactions.
     * @param categoryFamily A category family to filter the transactions by. If set to &#x60;cards&#x60;, only card purchase related transactions will be returned. If set to &#x60;crypto&#x60;, only funding related transactions will be returned.&#x60; If not provided, all posted transactions will be returned.
     * @return ListOfCardTransactions
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ListOfCardTransactions customersCustomerIDCardAccountsCardAccountIDTransactionsGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingTime, @jakarta.annotation.Nullable String endingTime, @jakarta.annotation.Nullable String pageSize, @jakarta.annotation.Nullable String page, @jakarta.annotation.Nullable List<String> status, @jakarta.annotation.Nullable String paginationToken, @jakarta.annotation.Nullable String categoryFamily) throws RestClientResponseException {
        ParameterizedTypeReference<ListOfCardTransactions> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDTransactionsGetRequestCreation(customerID, cardAccountID, limit, startingTime, endingTime, pageSize, page, status, paginationToken, categoryFamily).body(localVarReturnType);
    }

    /**
     * Retrieve card transactions
     * Retrieve completed card transactions and card-related crypto transaction activities
     * <p><b>200</b> - Card transactions
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of transactions to return, with a max of 200. The default is 100
     * @param startingTime The starting time in ISO8601 format. Default is 50 days ago.
     * @param endingTime The exclusive ending time in ISO8601 format. Default is now.
     * @param pageSize The max number of items to return for the requested page, with a max of 200. The default is 100
     * @param page A specific page to fetch. If omitted, the first page (starting at 1) will be returned. Note that this is only supported for navigating posted transactions.
     * @param status A status to filter the transactions by. If not provided, will default to &#x60;posted&#x60;.
     * @param paginationToken A pagination token to fetch the next page of transactions.
     * @param categoryFamily A category family to filter the transactions by. If set to &#x60;cards&#x60;, only card purchase related transactions will be returned. If set to &#x60;crypto&#x60;, only funding related transactions will be returned.&#x60; If not provided, all posted transactions will be returned.
     * @return ResponseEntity&lt;ListOfCardTransactions&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListOfCardTransactions> customersCustomerIDCardAccountsCardAccountIDTransactionsGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingTime, @jakarta.annotation.Nullable String endingTime, @jakarta.annotation.Nullable String pageSize, @jakarta.annotation.Nullable String page, @jakarta.annotation.Nullable List<String> status, @jakarta.annotation.Nullable String paginationToken, @jakarta.annotation.Nullable String categoryFamily) throws RestClientResponseException {
        ParameterizedTypeReference<ListOfCardTransactions> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDTransactionsGetRequestCreation(customerID, cardAccountID, limit, startingTime, endingTime, pageSize, page, status, paginationToken, categoryFamily).toEntity(localVarReturnType);
    }

    /**
     * Retrieve card transactions
     * Retrieve completed card transactions and card-related crypto transaction activities
     * <p><b>200</b> - Card transactions
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of transactions to return, with a max of 200. The default is 100
     * @param startingTime The starting time in ISO8601 format. Default is 50 days ago.
     * @param endingTime The exclusive ending time in ISO8601 format. Default is now.
     * @param pageSize The max number of items to return for the requested page, with a max of 200. The default is 100
     * @param page A specific page to fetch. If omitted, the first page (starting at 1) will be returned. Note that this is only supported for navigating posted transactions.
     * @param status A status to filter the transactions by. If not provided, will default to &#x60;posted&#x60;.
     * @param paginationToken A pagination token to fetch the next page of transactions.
     * @param categoryFamily A category family to filter the transactions by. If set to &#x60;cards&#x60;, only card purchase related transactions will be returned. If set to &#x60;crypto&#x60;, only funding related transactions will be returned.&#x60; If not provided, all posted transactions will be returned.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDTransactionsGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingTime, @jakarta.annotation.Nullable String endingTime, @jakarta.annotation.Nullable String pageSize, @jakarta.annotation.Nullable String page, @jakarta.annotation.Nullable List<String> status, @jakarta.annotation.Nullable String paginationToken, @jakarta.annotation.Nullable String categoryFamily) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDTransactionsGetRequestCreation(customerID, cardAccountID, limit, startingTime, endingTime, pageSize, page, status, paginationToken, categoryFamily);
    }

    /**
     * Retrieve a card transaction
     * Retrieve a card transaction with the specified ID
     * <p><b>200</b> - Card transactions
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param transactionID The transactionID parameter
     * @return CardTransaction
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDTransactionsTransactionIDGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String transactionID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDTransactionsTransactionIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDTransactionsTransactionIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'transactionID' is set
        if (transactionID == null) {
            throw new RestClientResponseException("Missing the required parameter 'transactionID' when calling customersCustomerIDCardAccountsCardAccountIDTransactionsTransactionIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);
        pathParams.put("transactionID", transactionID);

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

        ParameterizedTypeReference<CardTransaction> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/transactions/{transactionID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Retrieve a card transaction
     * Retrieve a card transaction with the specified ID
     * <p><b>200</b> - Card transactions
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param transactionID The transactionID parameter
     * @return CardTransaction
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardTransaction customersCustomerIDCardAccountsCardAccountIDTransactionsTransactionIDGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String transactionID) throws RestClientResponseException {
        ParameterizedTypeReference<CardTransaction> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDTransactionsTransactionIDGetRequestCreation(customerID, cardAccountID, transactionID).body(localVarReturnType);
    }

    /**
     * Retrieve a card transaction
     * Retrieve a card transaction with the specified ID
     * <p><b>200</b> - Card transactions
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param transactionID The transactionID parameter
     * @return ResponseEntity&lt;CardTransaction&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardTransaction> customersCustomerIDCardAccountsCardAccountIDTransactionsTransactionIDGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String transactionID) throws RestClientResponseException {
        ParameterizedTypeReference<CardTransaction> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDTransactionsTransactionIDGetRequestCreation(customerID, cardAccountID, transactionID).toEntity(localVarReturnType);
    }

    /**
     * Retrieve a card transaction
     * Retrieve a card transaction with the specified ID
     * <p><b>200</b> - Card transactions
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param transactionID The transactionID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDTransactionsTransactionIDGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String transactionID) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDTransactionsTransactionIDGetRequestCreation(customerID, cardAccountID, transactionID);
    }

    /**
     * Unfreeze the card account
     * Remove the freeze on the card account placed by the specified initiator
     * <p><b>200</b> - The successfully removed freeze
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardUnfreezeInput A request to unfreeze the card account
     * @return CardUnfreezeResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDUnfreezePostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardUnfreezeInput postCardUnfreezeInput) throws RestClientResponseException {
        Object postBody = postCardUnfreezeInput;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDCardAccountsCardAccountIDUnfreezePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDUnfreezePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDUnfreezePost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

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

        ParameterizedTypeReference<CardUnfreezeResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/unfreeze", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Unfreeze the card account
     * Remove the freeze on the card account placed by the specified initiator
     * <p><b>200</b> - The successfully removed freeze
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardUnfreezeInput A request to unfreeze the card account
     * @return CardUnfreezeResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardUnfreezeResponse customersCustomerIDCardAccountsCardAccountIDUnfreezePost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardUnfreezeInput postCardUnfreezeInput) throws RestClientResponseException {
        ParameterizedTypeReference<CardUnfreezeResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDUnfreezePostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardUnfreezeInput).body(localVarReturnType);
    }

    /**
     * Unfreeze the card account
     * Remove the freeze on the card account placed by the specified initiator
     * <p><b>200</b> - The successfully removed freeze
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardUnfreezeInput A request to unfreeze the card account
     * @return ResponseEntity&lt;CardUnfreezeResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardUnfreezeResponse> customersCustomerIDCardAccountsCardAccountIDUnfreezePostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardUnfreezeInput postCardUnfreezeInput) throws RestClientResponseException {
        ParameterizedTypeReference<CardUnfreezeResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDUnfreezePostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardUnfreezeInput).toEntity(localVarReturnType);
    }

    /**
     * Unfreeze the card account
     * Remove the freeze on the card account placed by the specified initiator
     * <p><b>200</b> - The successfully removed freeze
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param postCardUnfreezeInput A request to unfreeze the card account
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDUnfreezePostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable PostCardUnfreezeInput postCardUnfreezeInput) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDUnfreezePostRequestCreation(idempotencyKey, customerID, cardAccountID, postCardUnfreezeInput);
    }

    /**
     * Retrieve a card withdrawal
     * Retrieve a card withdrawal with the specified ID, applicable to top-up accounts only
     * <p><b>200</b> - The retrieved withdrawal
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param cardWithdrawalID The cardWithdrawalID parameter
     * @return CardWithdrawal
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDWithdrawalsCardWithdrawalIDGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String cardWithdrawalID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDWithdrawalsCardWithdrawalIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDWithdrawalsCardWithdrawalIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardWithdrawalID' is set
        if (cardWithdrawalID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardWithdrawalID' when calling customersCustomerIDCardAccountsCardAccountIDWithdrawalsCardWithdrawalIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);
        pathParams.put("cardWithdrawalID", cardWithdrawalID);

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

        ParameterizedTypeReference<CardWithdrawal> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/withdrawals/{cardWithdrawalID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Retrieve a card withdrawal
     * Retrieve a card withdrawal with the specified ID, applicable to top-up accounts only
     * <p><b>200</b> - The retrieved withdrawal
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param cardWithdrawalID The cardWithdrawalID parameter
     * @return CardWithdrawal
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardWithdrawal customersCustomerIDCardAccountsCardAccountIDWithdrawalsCardWithdrawalIDGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String cardWithdrawalID) throws RestClientResponseException {
        ParameterizedTypeReference<CardWithdrawal> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDWithdrawalsCardWithdrawalIDGetRequestCreation(customerID, cardAccountID, cardWithdrawalID).body(localVarReturnType);
    }

    /**
     * Retrieve a card withdrawal
     * Retrieve a card withdrawal with the specified ID, applicable to top-up accounts only
     * <p><b>200</b> - The retrieved withdrawal
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param cardWithdrawalID The cardWithdrawalID parameter
     * @return ResponseEntity&lt;CardWithdrawal&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardWithdrawal> customersCustomerIDCardAccountsCardAccountIDWithdrawalsCardWithdrawalIDGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String cardWithdrawalID) throws RestClientResponseException {
        ParameterizedTypeReference<CardWithdrawal> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDWithdrawalsCardWithdrawalIDGetRequestCreation(customerID, cardAccountID, cardWithdrawalID).toEntity(localVarReturnType);
    }

    /**
     * Retrieve a card withdrawal
     * Retrieve a card withdrawal with the specified ID, applicable to top-up accounts only
     * <p><b>200</b> - The retrieved withdrawal
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param cardWithdrawalID The cardWithdrawalID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDWithdrawalsCardWithdrawalIDGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull String cardWithdrawalID) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDWithdrawalsCardWithdrawalIDGetRequestCreation(customerID, cardAccountID, cardWithdrawalID);
    }

    /**
     * Retrieve the withdrawal history of funds
     * Retrieve the withdrawal history of funds, applicable to top-up accounts only
     * <p><b>200</b> - The withdrawal history
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a withdrawal id. If this is specified, the next page that starts with a withdrawal right AFTER the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that withdrawals older than the specified id will be returned (shouldn&#39;t be set if &#x60;ending_before&#x60; is set)
     * @param endingBefore This is a withdrawal id. If this is specified, the previous page that ends with a withdrawal right BEFORE the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that withdrawals newer than the specified id will be returned (shouldn&#39;t be set if &#x60;starting_after&#x60; is set)
     * @return ListOfWithdrawals
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDWithdrawalsGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDWithdrawalsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDWithdrawalsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

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

        ParameterizedTypeReference<ListOfWithdrawals> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/withdrawals", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Retrieve the withdrawal history of funds
     * Retrieve the withdrawal history of funds, applicable to top-up accounts only
     * <p><b>200</b> - The withdrawal history
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a withdrawal id. If this is specified, the next page that starts with a withdrawal right AFTER the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that withdrawals older than the specified id will be returned (shouldn&#39;t be set if &#x60;ending_before&#x60; is set)
     * @param endingBefore This is a withdrawal id. If this is specified, the previous page that ends with a withdrawal right BEFORE the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that withdrawals newer than the specified id will be returned (shouldn&#39;t be set if &#x60;starting_after&#x60; is set)
     * @return ListOfWithdrawals
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ListOfWithdrawals customersCustomerIDCardAccountsCardAccountIDWithdrawalsGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<ListOfWithdrawals> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDWithdrawalsGetRequestCreation(customerID, cardAccountID, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Retrieve the withdrawal history of funds
     * Retrieve the withdrawal history of funds, applicable to top-up accounts only
     * <p><b>200</b> - The withdrawal history
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a withdrawal id. If this is specified, the next page that starts with a withdrawal right AFTER the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that withdrawals older than the specified id will be returned (shouldn&#39;t be set if &#x60;ending_before&#x60; is set)
     * @param endingBefore This is a withdrawal id. If this is specified, the previous page that ends with a withdrawal right BEFORE the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that withdrawals newer than the specified id will be returned (shouldn&#39;t be set if &#x60;starting_after&#x60; is set)
     * @return ResponseEntity&lt;ListOfWithdrawals&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ListOfWithdrawals> customersCustomerIDCardAccountsCardAccountIDWithdrawalsGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<ListOfWithdrawals> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDWithdrawalsGetRequestCreation(customerID, cardAccountID, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Retrieve the withdrawal history of funds
     * Retrieve the withdrawal history of funds, applicable to top-up accounts only
     * <p><b>200</b> - The withdrawal history
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a withdrawal id. If this is specified, the next page that starts with a withdrawal right AFTER the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that withdrawals older than the specified id will be returned (shouldn&#39;t be set if &#x60;ending_before&#x60; is set)
     * @param endingBefore This is a withdrawal id. If this is specified, the previous page that ends with a withdrawal right BEFORE the specified id on the timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that withdrawals newer than the specified id will be returned (shouldn&#39;t be set if &#x60;starting_after&#x60; is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDWithdrawalsGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDWithdrawalsGetRequestCreation(customerID, cardAccountID, limit, startingAfter, endingBefore);
    }

    /**
     * Create a funds withdrawal request
     * Request a funds withdrawal from the card account, applicable to top-up accounts only
     * <p><b>201</b> - The successfully created card funds withdrawal
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param cardWithdrawal The withdrawal request
     * @return CardWithdrawal
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsCardAccountIDWithdrawalsPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull CardWithdrawal cardWithdrawal) throws RestClientResponseException {
        Object postBody = cardWithdrawal;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDCardAccountsCardAccountIDWithdrawalsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsCardAccountIDWithdrawalsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardAccountID' is set
        if (cardAccountID == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardAccountID' when calling customersCustomerIDCardAccountsCardAccountIDWithdrawalsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'cardWithdrawal' is set
        if (cardWithdrawal == null) {
            throw new RestClientResponseException("Missing the required parameter 'cardWithdrawal' when calling customersCustomerIDCardAccountsCardAccountIDWithdrawalsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("cardAccountID", cardAccountID);

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

        ParameterizedTypeReference<CardWithdrawal> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts/{cardAccountID}/withdrawals", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a funds withdrawal request
     * Request a funds withdrawal from the card account, applicable to top-up accounts only
     * <p><b>201</b> - The successfully created card funds withdrawal
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param cardWithdrawal The withdrawal request
     * @return CardWithdrawal
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardWithdrawal customersCustomerIDCardAccountsCardAccountIDWithdrawalsPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull CardWithdrawal cardWithdrawal) throws RestClientResponseException {
        ParameterizedTypeReference<CardWithdrawal> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDWithdrawalsPostRequestCreation(idempotencyKey, customerID, cardAccountID, cardWithdrawal).body(localVarReturnType);
    }

    /**
     * Create a funds withdrawal request
     * Request a funds withdrawal from the card account, applicable to top-up accounts only
     * <p><b>201</b> - The successfully created card funds withdrawal
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param cardWithdrawal The withdrawal request
     * @return ResponseEntity&lt;CardWithdrawal&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardWithdrawal> customersCustomerIDCardAccountsCardAccountIDWithdrawalsPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull CardWithdrawal cardWithdrawal) throws RestClientResponseException {
        ParameterizedTypeReference<CardWithdrawal> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsCardAccountIDWithdrawalsPostRequestCreation(idempotencyKey, customerID, cardAccountID, cardWithdrawal).toEntity(localVarReturnType);
    }

    /**
     * Create a funds withdrawal request
     * Request a funds withdrawal from the card account, applicable to top-up accounts only
     * <p><b>201</b> - The successfully created card funds withdrawal
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param cardAccountID The cardAccountID parameter
     * @param cardWithdrawal The withdrawal request
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsCardAccountIDWithdrawalsPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String cardAccountID, @jakarta.annotation.Nonnull CardWithdrawal cardWithdrawal) throws RestClientResponseException {
        return customersCustomerIDCardAccountsCardAccountIDWithdrawalsPostRequestCreation(idempotencyKey, customerID, cardAccountID, cardWithdrawal);
    }

    /**
     * Get all card accounts
     * Retrieve all card accounts for a customer. Currently, only one account is supported. An empty array will be returned if no card has been provisioned
     * <p><b>200</b> - List of all card accounts owned by the customer
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ArrayOfAllCardAccounts
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsGetRequestCreation(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);

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

        ParameterizedTypeReference<ArrayOfAllCardAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all card accounts
     * Retrieve all card accounts for a customer. Currently, only one account is supported. An empty array will be returned if no card has been provisioned
     * <p><b>200</b> - List of all card accounts owned by the customer
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ArrayOfAllCardAccounts
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ArrayOfAllCardAccounts customersCustomerIDCardAccountsGet(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<ArrayOfAllCardAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsGetRequestCreation(customerID).body(localVarReturnType);
    }

    /**
     * Get all card accounts
     * Retrieve all card accounts for a customer. Currently, only one account is supported. An empty array will be returned if no card has been provisioned
     * <p><b>200</b> - List of all card accounts owned by the customer
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ResponseEntity&lt;ArrayOfAllCardAccounts&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<ArrayOfAllCardAccounts> customersCustomerIDCardAccountsGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<ArrayOfAllCardAccounts> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsGetRequestCreation(customerID).toEntity(localVarReturnType);
    }

    /**
     * Get all card accounts
     * Retrieve all card accounts for a customer. Currently, only one account is supported. An empty array will be returned if no card has been provisioned
     * <p><b>200</b> - List of all card accounts owned by the customer
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        return customersCustomerIDCardAccountsGetRequestCreation(customerID);
    }

    /**
     * Provision a card account
     * Provision a card account. Currently, only one account can be provisioned per customer
     * <p><b>201</b> - The successfully created card account
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param postCardAccountsInput The card account to be provisioned
     * @return CardAccount
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDCardAccountsPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull PostCardAccountsInput postCardAccountsInput) throws RestClientResponseException {
        Object postBody = postCardAccountsInput;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDCardAccountsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDCardAccountsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'postCardAccountsInput' is set
        if (postCardAccountsInput == null) {
            throw new RestClientResponseException("Missing the required parameter 'postCardAccountsInput' when calling customersCustomerIDCardAccountsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<CardAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/card_accounts", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Provision a card account
     * Provision a card account. Currently, only one account can be provisioned per customer
     * <p><b>201</b> - The successfully created card account
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param postCardAccountsInput The card account to be provisioned
     * @return CardAccount
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardAccount customersCustomerIDCardAccountsPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull PostCardAccountsInput postCardAccountsInput) throws RestClientResponseException {
        ParameterizedTypeReference<CardAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsPostRequestCreation(idempotencyKey, customerID, postCardAccountsInput).body(localVarReturnType);
    }

    /**
     * Provision a card account
     * Provision a card account. Currently, only one account can be provisioned per customer
     * <p><b>201</b> - The successfully created card account
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param postCardAccountsInput The card account to be provisioned
     * @return ResponseEntity&lt;CardAccount&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardAccount> customersCustomerIDCardAccountsPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull PostCardAccountsInput postCardAccountsInput) throws RestClientResponseException {
        ParameterizedTypeReference<CardAccount> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDCardAccountsPostRequestCreation(idempotencyKey, customerID, postCardAccountsInput).toEntity(localVarReturnType);
    }

    /**
     * Provision a card account
     * Provision a card account. Currently, only one account can be provisioned per customer
     * <p><b>201</b> - The successfully created card account
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customerID The customerID parameter
     * @param postCardAccountsInput The card account to be provisioned
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDCardAccountsPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull PostCardAccountsInput postCardAccountsInput) throws RestClientResponseException {
        return customersCustomerIDCardAccountsPostRequestCreation(idempotencyKey, customerID, postCardAccountsInput);
    }

    /**
     * Get a listing of your card program&#39;s card designs
     * Get a listing of the designs that you can use to issue a card with.
     * <p><b>200</b> - A listing of the designs that you can use to issue a card with.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return CardDesigns
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec developerCardsDesignsGetRequestCreation() throws RestClientResponseException {
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

        ParameterizedTypeReference<CardDesigns> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/developer/cards/designs", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a listing of your card program&#39;s card designs
     * Get a listing of the designs that you can use to issue a card with.
     * <p><b>200</b> - A listing of the designs that you can use to issue a card with.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return CardDesigns
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardDesigns developerCardsDesignsGet() throws RestClientResponseException {
        ParameterizedTypeReference<CardDesigns> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerCardsDesignsGetRequestCreation().body(localVarReturnType);
    }

    /**
     * Get a listing of your card program&#39;s card designs
     * Get a listing of the designs that you can use to issue a card with.
     * <p><b>200</b> - A listing of the designs that you can use to issue a card with.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseEntity&lt;CardDesigns&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardDesigns> developerCardsDesignsGetWithHttpInfo() throws RestClientResponseException {
        ParameterizedTypeReference<CardDesigns> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerCardsDesignsGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Get a listing of your card program&#39;s card designs
     * Get a listing of the designs that you can use to issue a card with.
     * <p><b>200</b> - A listing of the designs that you can use to issue a card with.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec developerCardsDesignsGetWithResponseSpec() throws RestClientResponseException {
        return developerCardsDesignsGetRequestCreation();
    }

    /**
     * Get a summary of your card program
     * Get a summary of your card program, optionally for a specific period.
     * <p><b>200</b> - The summary of a card program
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param period The type of period to summarize the card program by. If &#x60;lifetime&#x60; is specified, the &#x60;period_key&#x60; is not required.
     * @param periodKey A string to indicate the period to retrieve the card program summary for. - For &#x60;year&#x60;, the period key should be in the &#x60;YYYY&#x60; format - For &#x60;month&#x60;, the period key should be in the &#x60;YYYYMM&#x60; format - For &#x60;week&#x60;, the period key should be in the &#x60;YYYYMMDD&#x60; format indicating the beginning day of the week. - For &#x60;day&#x60;, the period key should be in the &#x60;YYYYMMDD&#x60; format - For &#x60;lifetime&#x60;, the period key is not required Note that if a specific period is specified, this endpoint currently only supports fetching for periods with complete data (e.g. only up to the previous year, month, week, or day). 
     * @return CardProgramSummary
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec developerCardsSummaryGetRequestCreation(@jakarta.annotation.Nonnull String period, @jakarta.annotation.Nullable String periodKey) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'period' is set
        if (period == null) {
            throw new RestClientResponseException("Missing the required parameter 'period' when calling developerCardsSummaryGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "period", period));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "period_key", periodKey));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<CardProgramSummary> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/developer/cards/summary", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a summary of your card program
     * Get a summary of your card program, optionally for a specific period.
     * <p><b>200</b> - The summary of a card program
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param period The type of period to summarize the card program by. If &#x60;lifetime&#x60; is specified, the &#x60;period_key&#x60; is not required.
     * @param periodKey A string to indicate the period to retrieve the card program summary for. - For &#x60;year&#x60;, the period key should be in the &#x60;YYYY&#x60; format - For &#x60;month&#x60;, the period key should be in the &#x60;YYYYMM&#x60; format - For &#x60;week&#x60;, the period key should be in the &#x60;YYYYMMDD&#x60; format indicating the beginning day of the week. - For &#x60;day&#x60;, the period key should be in the &#x60;YYYYMMDD&#x60; format - For &#x60;lifetime&#x60;, the period key is not required Note that if a specific period is specified, this endpoint currently only supports fetching for periods with complete data (e.g. only up to the previous year, month, week, or day). 
     * @return CardProgramSummary
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CardProgramSummary developerCardsSummaryGet(@jakarta.annotation.Nonnull String period, @jakarta.annotation.Nullable String periodKey) throws RestClientResponseException {
        ParameterizedTypeReference<CardProgramSummary> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerCardsSummaryGetRequestCreation(period, periodKey).body(localVarReturnType);
    }

    /**
     * Get a summary of your card program
     * Get a summary of your card program, optionally for a specific period.
     * <p><b>200</b> - The summary of a card program
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param period The type of period to summarize the card program by. If &#x60;lifetime&#x60; is specified, the &#x60;period_key&#x60; is not required.
     * @param periodKey A string to indicate the period to retrieve the card program summary for. - For &#x60;year&#x60;, the period key should be in the &#x60;YYYY&#x60; format - For &#x60;month&#x60;, the period key should be in the &#x60;YYYYMM&#x60; format - For &#x60;week&#x60;, the period key should be in the &#x60;YYYYMMDD&#x60; format indicating the beginning day of the week. - For &#x60;day&#x60;, the period key should be in the &#x60;YYYYMMDD&#x60; format - For &#x60;lifetime&#x60;, the period key is not required Note that if a specific period is specified, this endpoint currently only supports fetching for periods with complete data (e.g. only up to the previous year, month, week, or day). 
     * @return ResponseEntity&lt;CardProgramSummary&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CardProgramSummary> developerCardsSummaryGetWithHttpInfo(@jakarta.annotation.Nonnull String period, @jakarta.annotation.Nullable String periodKey) throws RestClientResponseException {
        ParameterizedTypeReference<CardProgramSummary> localVarReturnType = new ParameterizedTypeReference<>() {};
        return developerCardsSummaryGetRequestCreation(period, periodKey).toEntity(localVarReturnType);
    }

    /**
     * Get a summary of your card program
     * Get a summary of your card program, optionally for a specific period.
     * <p><b>200</b> - The summary of a card program
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param period The type of period to summarize the card program by. If &#x60;lifetime&#x60; is specified, the &#x60;period_key&#x60; is not required.
     * @param periodKey A string to indicate the period to retrieve the card program summary for. - For &#x60;year&#x60;, the period key should be in the &#x60;YYYY&#x60; format - For &#x60;month&#x60;, the period key should be in the &#x60;YYYYMM&#x60; format - For &#x60;week&#x60;, the period key should be in the &#x60;YYYYMMDD&#x60; format indicating the beginning day of the week. - For &#x60;day&#x60;, the period key should be in the &#x60;YYYYMMDD&#x60; format - For &#x60;lifetime&#x60;, the period key is not required Note that if a specific period is specified, this endpoint currently only supports fetching for periods with complete data (e.g. only up to the previous year, month, week, or day). 
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec developerCardsSummaryGetWithResponseSpec(@jakarta.annotation.Nonnull String period, @jakarta.annotation.Nullable String periodKey) throws RestClientResponseException {
        return developerCardsSummaryGetRequestCreation(period, periodKey);
    }
}
