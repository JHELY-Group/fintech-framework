package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.AssociatedPerson;
import org.jhely.money.sdk.bridge.model.AssociatedPersonResponse;
import org.jhely.money.sdk.bridge.model.AssociatedPersonsList;
import org.jhely.money.sdk.bridge.model.Customer;
import org.jhely.money.sdk.bridge.model.Customers;
import org.jhely.money.sdk.bridge.model.CustomersCustomerIDKycLinkGet200Response;
import org.jhely.money.sdk.bridge.model.CustomersCustomerIDPutRequest;
import org.jhely.money.sdk.bridge.model.CustomersCustomerIDTosAcceptanceLinkGet200Response;
import org.jhely.money.sdk.bridge.model.CustomersPostRequest;
import org.jhely.money.sdk.bridge.model.CustomersTosLinksPost201Response;
import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.StaticTemplates;
import org.jhely.money.sdk.bridge.model.Transfers;

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
public class CustomersApi {
    private ApiClient apiClient;

    public CustomersApi() {
        this(new ApiClient());
    }

    public CustomersApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Delete a single associated person
     * Delete an associated person by ID
     * <p><b>200</b> - Successfully deleted associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDAssociatedPersonsAssociatedPersonIDDeleteRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDAssociatedPersonsAssociatedPersonIDDelete", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'associatedPersonID' is set
        if (associatedPersonID == null) {
            throw new RestClientResponseException("Missing the required parameter 'associatedPersonID' when calling customersCustomerIDAssociatedPersonsAssociatedPersonIDDelete", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("associatedPersonID", associatedPersonID);

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

        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/associated_persons/{associatedPersonID}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Delete a single associated person
     * Delete an associated person by ID
     * <p><b>200</b> - Successfully deleted associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public AssociatedPersonResponse customersCustomerIDAssociatedPersonsAssociatedPersonIDDelete(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDAssociatedPersonsAssociatedPersonIDDeleteRequestCreation(customerID, associatedPersonID).body(localVarReturnType);
    }

    /**
     * Delete a single associated person
     * Delete an associated person by ID
     * <p><b>200</b> - Successfully deleted associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @return ResponseEntity&lt;AssociatedPersonResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<AssociatedPersonResponse> customersCustomerIDAssociatedPersonsAssociatedPersonIDDeleteWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDAssociatedPersonsAssociatedPersonIDDeleteRequestCreation(customerID, associatedPersonID).toEntity(localVarReturnType);
    }

    /**
     * Delete a single associated person
     * Delete an associated person by ID
     * <p><b>200</b> - Successfully deleted associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDAssociatedPersonsAssociatedPersonIDDeleteWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        return customersCustomerIDAssociatedPersonsAssociatedPersonIDDeleteRequestCreation(customerID, associatedPersonID);
    }

    /**
     * Get a single associated person (Beta)
     * Retrieve an associated person by ID
     * <p><b>200</b> - Successful associated person response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDAssociatedPersonsAssociatedPersonIDGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDAssociatedPersonsAssociatedPersonIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'associatedPersonID' is set
        if (associatedPersonID == null) {
            throw new RestClientResponseException("Missing the required parameter 'associatedPersonID' when calling customersCustomerIDAssociatedPersonsAssociatedPersonIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("associatedPersonID", associatedPersonID);

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

        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/associated_persons/{associatedPersonID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a single associated person (Beta)
     * Retrieve an associated person by ID
     * <p><b>200</b> - Successful associated person response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public AssociatedPersonResponse customersCustomerIDAssociatedPersonsAssociatedPersonIDGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDAssociatedPersonsAssociatedPersonIDGetRequestCreation(customerID, associatedPersonID).body(localVarReturnType);
    }

    /**
     * Get a single associated person (Beta)
     * Retrieve an associated person by ID
     * <p><b>200</b> - Successful associated person response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @return ResponseEntity&lt;AssociatedPersonResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<AssociatedPersonResponse> customersCustomerIDAssociatedPersonsAssociatedPersonIDGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDAssociatedPersonsAssociatedPersonIDGetRequestCreation(customerID, associatedPersonID).toEntity(localVarReturnType);
    }

    /**
     * Get a single associated person (Beta)
     * Retrieve an associated person by ID
     * <p><b>200</b> - Successful associated person response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDAssociatedPersonsAssociatedPersonIDGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        return customersCustomerIDAssociatedPersonsAssociatedPersonIDGetRequestCreation(customerID, associatedPersonID);
    }

    /**
     * Update a single associated person
     * 
     * <p><b>200</b> - Updated associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @param associatedPerson Associated person data to update
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDAssociatedPersonsAssociatedPersonIDPutRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        Object postBody = associatedPerson;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDAssociatedPersonsAssociatedPersonIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'associatedPersonID' is set
        if (associatedPersonID == null) {
            throw new RestClientResponseException("Missing the required parameter 'associatedPersonID' when calling customersCustomerIDAssociatedPersonsAssociatedPersonIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'associatedPerson' is set
        if (associatedPerson == null) {
            throw new RestClientResponseException("Missing the required parameter 'associatedPerson' when calling customersCustomerIDAssociatedPersonsAssociatedPersonIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);
        pathParams.put("associatedPersonID", associatedPersonID);

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

        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/associated_persons/{associatedPersonID}", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update a single associated person
     * 
     * <p><b>200</b> - Updated associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @param associatedPerson Associated person data to update
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public AssociatedPersonResponse customersCustomerIDAssociatedPersonsAssociatedPersonIDPut(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDAssociatedPersonsAssociatedPersonIDPutRequestCreation(customerID, associatedPersonID, associatedPerson).body(localVarReturnType);
    }

    /**
     * Update a single associated person
     * 
     * <p><b>200</b> - Updated associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @param associatedPerson Associated person data to update
     * @return ResponseEntity&lt;AssociatedPersonResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<AssociatedPersonResponse> customersCustomerIDAssociatedPersonsAssociatedPersonIDPutWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDAssociatedPersonsAssociatedPersonIDPutRequestCreation(customerID, associatedPersonID, associatedPerson).toEntity(localVarReturnType);
    }

    /**
     * Update a single associated person
     * 
     * <p><b>200</b> - Updated associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param associatedPersonID Unique identifier for an associated person
     * @param associatedPerson Associated person data to update
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDAssociatedPersonsAssociatedPersonIDPutWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String associatedPersonID, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        return customersCustomerIDAssociatedPersonsAssociatedPersonIDPutRequestCreation(customerID, associatedPersonID, associatedPerson);
    }

    /**
     * Get associated persons for a business customer (Beta)
     * Get all associated persons for a business customer.
     * <p><b>200</b> - List of associated persons (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return AssociatedPersonsList
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDAssociatedPersonsGetRequestCreation(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDAssociatedPersonsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<AssociatedPersonsList> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/associated_persons", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get associated persons for a business customer (Beta)
     * Get all associated persons for a business customer.
     * <p><b>200</b> - List of associated persons (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return AssociatedPersonsList
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public AssociatedPersonsList customersCustomerIDAssociatedPersonsGet(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonsList> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDAssociatedPersonsGetRequestCreation(customerID).body(localVarReturnType);
    }

    /**
     * Get associated persons for a business customer (Beta)
     * Get all associated persons for a business customer.
     * <p><b>200</b> - List of associated persons (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ResponseEntity&lt;AssociatedPersonsList&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<AssociatedPersonsList> customersCustomerIDAssociatedPersonsGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonsList> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDAssociatedPersonsGetRequestCreation(customerID).toEntity(localVarReturnType);
    }

    /**
     * Get associated persons for a business customer (Beta)
     * Get all associated persons for a business customer.
     * <p><b>200</b> - List of associated persons (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDAssociatedPersonsGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        return customersCustomerIDAssociatedPersonsGetRequestCreation(customerID);
    }

    /**
     * Create a new associated person for a business customer (Beta)
     * 
     * <p><b>201</b> - Associated person created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param associatedPerson New associated person to be created
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDAssociatedPersonsPostRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        Object postBody = associatedPerson;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDAssociatedPersonsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersCustomerIDAssociatedPersonsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'associatedPerson' is set
        if (associatedPerson == null) {
            throw new RestClientResponseException("Missing the required parameter 'associatedPerson' when calling customersCustomerIDAssociatedPersonsPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/associated_persons", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a new associated person for a business customer (Beta)
     * 
     * <p><b>201</b> - Associated person created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param associatedPerson New associated person to be created
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public AssociatedPersonResponse customersCustomerIDAssociatedPersonsPost(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDAssociatedPersonsPostRequestCreation(customerID, idempotencyKey, associatedPerson).body(localVarReturnType);
    }

    /**
     * Create a new associated person for a business customer (Beta)
     * 
     * <p><b>201</b> - Associated person created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param associatedPerson New associated person to be created
     * @return ResponseEntity&lt;AssociatedPersonResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<AssociatedPersonResponse> customersCustomerIDAssociatedPersonsPostWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDAssociatedPersonsPostRequestCreation(customerID, idempotencyKey, associatedPerson).toEntity(localVarReturnType);
    }

    /**
     * Create a new associated person for a business customer (Beta)
     * 
     * <p><b>201</b> - Associated person created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param associatedPerson New associated person to be created
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDAssociatedPersonsPostWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        return customersCustomerIDAssociatedPersonsPostRequestCreation(customerID, idempotencyKey, associatedPerson);
    }

    /**
     * Delete a single customer object
     * Delete a customer object from the passed in customer ID
     * <p><b>200</b> - Successfully deleted customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return Customer
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDDeleteRequestCreation(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDDelete", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Delete a single customer object
     * Delete a customer object from the passed in customer ID
     * <p><b>200</b> - Successfully deleted customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return Customer
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Customer customersCustomerIDDelete(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDDeleteRequestCreation(customerID).body(localVarReturnType);
    }

    /**
     * Delete a single customer object
     * Delete a customer object from the passed in customer ID
     * <p><b>200</b> - Successfully deleted customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ResponseEntity&lt;Customer&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Customer> customersCustomerIDDeleteWithHttpInfo(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDDeleteRequestCreation(customerID).toEntity(localVarReturnType);
    }

    /**
     * Delete a single customer object
     * Delete a customer object from the passed in customer ID
     * <p><b>200</b> - Successfully deleted customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDDeleteWithResponseSpec(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        return customersCustomerIDDeleteRequestCreation(customerID);
    }

    /**
     * Get a single customer object
     * Retrieve a customer object from the passed in customer ID
     * <p><b>200</b> - Successful customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return Customer
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDGetRequestCreation(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a single customer object
     * Retrieve a customer object from the passed in customer ID
     * <p><b>200</b> - Successful customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return Customer
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Customer customersCustomerIDGet(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDGetRequestCreation(customerID).body(localVarReturnType);
    }

    /**
     * Get a single customer object
     * Retrieve a customer object from the passed in customer ID
     * <p><b>200</b> - Successful customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ResponseEntity&lt;Customer&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Customer> customersCustomerIDGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDGetRequestCreation(customerID).toEntity(localVarReturnType);
    }

    /**
     * Get a single customer object
     * Retrieve a customer object from the passed in customer ID
     * <p><b>200</b> - Successful customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        return customersCustomerIDGetRequestCreation(customerID);
    }

    /**
     * Retrieve a hosted KYC Link for an existing customer
     * The page at the returned URL will guide the user through a Bridge KYC flow. This can be used by existing customers to provide additional KYC information required for certain features or services that Bridge offers.  For example, to enable an existing customer to use the &#x60;SEPA&#x60;/&#x60;Euro&#x60; services, they are required to provide &#x60;proof of address&#x60;. An additional parameter, &#x60;endorsement&#x3D;sepa&#x60;, can be included to request a KYC link specifically for this purpose
     * <p><b>200</b> - A Hosted URL for KYC
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param endorsement An endorsement is the approval required for a customer to use a particular product or service offered by Bridge
     * @param redirectUri An optional url encoded link that users will be redirected to after completing the hosted KYC flow.
     * @return CustomersCustomerIDKycLinkGet200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDKycLinkGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable String endorsement, @jakarta.annotation.Nullable String redirectUri) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDKycLinkGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("customerID", customerID);

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "endorsement", endorsement));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "redirect_uri", redirectUri));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<CustomersCustomerIDKycLinkGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/kyc_link", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Retrieve a hosted KYC Link for an existing customer
     * The page at the returned URL will guide the user through a Bridge KYC flow. This can be used by existing customers to provide additional KYC information required for certain features or services that Bridge offers.  For example, to enable an existing customer to use the &#x60;SEPA&#x60;/&#x60;Euro&#x60; services, they are required to provide &#x60;proof of address&#x60;. An additional parameter, &#x60;endorsement&#x3D;sepa&#x60;, can be included to request a KYC link specifically for this purpose
     * <p><b>200</b> - A Hosted URL for KYC
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param endorsement An endorsement is the approval required for a customer to use a particular product or service offered by Bridge
     * @param redirectUri An optional url encoded link that users will be redirected to after completing the hosted KYC flow.
     * @return CustomersCustomerIDKycLinkGet200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CustomersCustomerIDKycLinkGet200Response customersCustomerIDKycLinkGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable String endorsement, @jakarta.annotation.Nullable String redirectUri) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDKycLinkGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDKycLinkGetRequestCreation(customerID, endorsement, redirectUri).body(localVarReturnType);
    }

    /**
     * Retrieve a hosted KYC Link for an existing customer
     * The page at the returned URL will guide the user through a Bridge KYC flow. This can be used by existing customers to provide additional KYC information required for certain features or services that Bridge offers.  For example, to enable an existing customer to use the &#x60;SEPA&#x60;/&#x60;Euro&#x60; services, they are required to provide &#x60;proof of address&#x60;. An additional parameter, &#x60;endorsement&#x3D;sepa&#x60;, can be included to request a KYC link specifically for this purpose
     * <p><b>200</b> - A Hosted URL for KYC
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param endorsement An endorsement is the approval required for a customer to use a particular product or service offered by Bridge
     * @param redirectUri An optional url encoded link that users will be redirected to after completing the hosted KYC flow.
     * @return ResponseEntity&lt;CustomersCustomerIDKycLinkGet200Response&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CustomersCustomerIDKycLinkGet200Response> customersCustomerIDKycLinkGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable String endorsement, @jakarta.annotation.Nullable String redirectUri) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDKycLinkGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDKycLinkGetRequestCreation(customerID, endorsement, redirectUri).toEntity(localVarReturnType);
    }

    /**
     * Retrieve a hosted KYC Link for an existing customer
     * The page at the returned URL will guide the user through a Bridge KYC flow. This can be used by existing customers to provide additional KYC information required for certain features or services that Bridge offers.  For example, to enable an existing customer to use the &#x60;SEPA&#x60;/&#x60;Euro&#x60; services, they are required to provide &#x60;proof of address&#x60;. An additional parameter, &#x60;endorsement&#x3D;sepa&#x60;, can be included to request a KYC link specifically for this purpose
     * <p><b>200</b> - A Hosted URL for KYC
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param endorsement An endorsement is the approval required for a customer to use a particular product or service offered by Bridge
     * @param redirectUri An optional url encoded link that users will be redirected to after completing the hosted KYC flow.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDKycLinkGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable String endorsement, @jakarta.annotation.Nullable String redirectUri) throws RestClientResponseException {
        return customersCustomerIDKycLinkGetRequestCreation(customerID, endorsement, redirectUri);
    }

    /**
     * Update a single customer object
     * Updates to be made to the specified customer.  For individual customers (soon to be businesses as well), no fields are strictly required by the API. It is generally valid to provide any subset of data in a PUT request. For business customers, associated persons cannot be updated via PUT, and should instead be managed using v0/associated_persons. 
     * <p><b>200</b> - Successful customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param customersCustomerIDPutRequest Customer object to update with
     * @return Customer
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDPutRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CustomersCustomerIDPutRequest customersCustomerIDPutRequest) throws RestClientResponseException {
        Object postBody = customersCustomerIDPutRequest;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customersCustomerIDPutRequest' is set
        if (customersCustomerIDPutRequest == null) {
            throw new RestClientResponseException("Missing the required parameter 'customersCustomerIDPutRequest' when calling customersCustomerIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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
        final String[] localVarContentTypes = { 
            "application/json"
        };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update a single customer object
     * Updates to be made to the specified customer.  For individual customers (soon to be businesses as well), no fields are strictly required by the API. It is generally valid to provide any subset of data in a PUT request. For business customers, associated persons cannot be updated via PUT, and should instead be managed using v0/associated_persons. 
     * <p><b>200</b> - Successful customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param customersCustomerIDPutRequest Customer object to update with
     * @return Customer
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Customer customersCustomerIDPut(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CustomersCustomerIDPutRequest customersCustomerIDPutRequest) throws RestClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDPutRequestCreation(customerID, customersCustomerIDPutRequest).body(localVarReturnType);
    }

    /**
     * Update a single customer object
     * Updates to be made to the specified customer.  For individual customers (soon to be businesses as well), no fields are strictly required by the API. It is generally valid to provide any subset of data in a PUT request. For business customers, associated persons cannot be updated via PUT, and should instead be managed using v0/associated_persons. 
     * <p><b>200</b> - Successful customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param customersCustomerIDPutRequest Customer object to update with
     * @return ResponseEntity&lt;Customer&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Customer> customersCustomerIDPutWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CustomersCustomerIDPutRequest customersCustomerIDPutRequest) throws RestClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDPutRequestCreation(customerID, customersCustomerIDPutRequest).toEntity(localVarReturnType);
    }

    /**
     * Update a single customer object
     * Updates to be made to the specified customer.  For individual customers (soon to be businesses as well), no fields are strictly required by the API. It is generally valid to provide any subset of data in a PUT request. For business customers, associated persons cannot be updated via PUT, and should instead be managed using v0/associated_persons. 
     * <p><b>200</b> - Successful customer object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param customersCustomerIDPutRequest Customer object to update with
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDPutWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nonnull CustomersCustomerIDPutRequest customersCustomerIDPutRequest) throws RestClientResponseException {
        return customersCustomerIDPutRequestCreation(customerID, customersCustomerIDPutRequest);
    }

    /**
     * Get all static templates for a customer
     * Get all static templates for a customer. Static templates are transfers that are used as templates for other transfers and can be created using the static_templates feature flag.
     * <p><b>200</b> - List of static templates (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return StaticTemplates
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDStaticTemplatesGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDStaticTemplatesGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<StaticTemplates> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/static_templates", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all static templates for a customer
     * Get all static templates for a customer. Static templates are transfers that are used as templates for other transfers and can be created using the static_templates feature flag.
     * <p><b>200</b> - List of static templates (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return StaticTemplates
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public StaticTemplates customersCustomerIDStaticTemplatesGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<StaticTemplates> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticTemplatesGetRequestCreation(customerID, limit, startingAfter, endingBefore).body(localVarReturnType);
    }

    /**
     * Get all static templates for a customer
     * Get all static templates for a customer. Static templates are transfers that are used as templates for other transfers and can be created using the static_templates feature flag.
     * <p><b>200</b> - List of static templates (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseEntity&lt;StaticTemplates&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<StaticTemplates> customersCustomerIDStaticTemplatesGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        ParameterizedTypeReference<StaticTemplates> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDStaticTemplatesGetRequestCreation(customerID, limit, startingAfter, endingBefore).toEntity(localVarReturnType);
    }

    /**
     * Get all static templates for a customer
     * Get all static templates for a customer. Static templates are transfers that are used as templates for other transfers and can be created using the static_templates feature flag.
     * <p><b>200</b> - List of static templates (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDStaticTemplatesGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore) throws RestClientResponseException {
        return customersCustomerIDStaticTemplatesGetRequestCreation(customerID, limit, startingAfter, endingBefore);
    }

    /**
     * Retrieve a hosted URL for ToS acceptance for an existing customer
     * The page at the returned URL will guide the user through the Bridge Terms of Service (ToS) acceptance flow. This can be used by existing customers to accept a new version of the ToS.
     * <p><b>200</b> - A Hosted URL for ToS acceptance
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return CustomersCustomerIDTosAcceptanceLinkGet200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDTosAcceptanceLinkGetRequestCreation(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDTosAcceptanceLinkGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<CustomersCustomerIDTosAcceptanceLinkGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/tos_acceptance_link", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Retrieve a hosted URL for ToS acceptance for an existing customer
     * The page at the returned URL will guide the user through the Bridge Terms of Service (ToS) acceptance flow. This can be used by existing customers to accept a new version of the ToS.
     * <p><b>200</b> - A Hosted URL for ToS acceptance
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return CustomersCustomerIDTosAcceptanceLinkGet200Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CustomersCustomerIDTosAcceptanceLinkGet200Response customersCustomerIDTosAcceptanceLinkGet(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDTosAcceptanceLinkGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDTosAcceptanceLinkGetRequestCreation(customerID).body(localVarReturnType);
    }

    /**
     * Retrieve a hosted URL for ToS acceptance for an existing customer
     * The page at the returned URL will guide the user through the Bridge Terms of Service (ToS) acceptance flow. This can be used by existing customers to accept a new version of the ToS.
     * <p><b>200</b> - A Hosted URL for ToS acceptance
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ResponseEntity&lt;CustomersCustomerIDTosAcceptanceLinkGet200Response&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CustomersCustomerIDTosAcceptanceLinkGet200Response> customersCustomerIDTosAcceptanceLinkGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersCustomerIDTosAcceptanceLinkGet200Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDTosAcceptanceLinkGetRequestCreation(customerID).toEntity(localVarReturnType);
    }

    /**
     * Retrieve a hosted URL for ToS acceptance for an existing customer
     * The page at the returned URL will guide the user through the Bridge Terms of Service (ToS) acceptance flow. This can be used by existing customers to accept a new version of the ToS.
     * <p><b>200</b> - A Hosted URL for ToS acceptance
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDTosAcceptanceLinkGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID) throws RestClientResponseException {
        return customersCustomerIDTosAcceptanceLinkGetRequestCreation(customerID);
    }

    /**
     * Get all transfers
     * Get all active and completed transfers for a customer.
     * <p><b>200</b> - List of transfers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param txHash The hash of the transaction
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @param state Filter transfers by their external state. See [Transfer States](https://apidocs.bridge.xyz/platform/orchestration/transfers/transfer-states) for more details.
     * @return Transfers
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersCustomerIDTransfersGetRequestCreation(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs, @jakarta.annotation.Nullable String state) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'customerID' is set
        if (customerID == null) {
            throw new RestClientResponseException("Missing the required parameter 'customerID' when calling customersCustomerIDTransfersGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "tx_hash", txHash));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_after_ms", updatedAfterMs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "updated_before_ms", updatedBeforeMs));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "state", state));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<Transfers> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/{customerID}/transfers", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all transfers
     * Get all active and completed transfers for a customer.
     * <p><b>200</b> - List of transfers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param txHash The hash of the transaction
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @param state Filter transfers by their external state. See [Transfer States](https://apidocs.bridge.xyz/platform/orchestration/transfers/transfer-states) for more details.
     * @return Transfers
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Transfers customersCustomerIDTransfersGet(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs, @jakarta.annotation.Nullable String state) throws RestClientResponseException {
        ParameterizedTypeReference<Transfers> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDTransfersGetRequestCreation(customerID, limit, startingAfter, endingBefore, txHash, updatedAfterMs, updatedBeforeMs, state).body(localVarReturnType);
    }

    /**
     * Get all transfers
     * Get all active and completed transfers for a customer.
     * <p><b>200</b> - List of transfers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param txHash The hash of the transaction
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @param state Filter transfers by their external state. See [Transfer States](https://apidocs.bridge.xyz/platform/orchestration/transfers/transfer-states) for more details.
     * @return ResponseEntity&lt;Transfers&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Transfers> customersCustomerIDTransfersGetWithHttpInfo(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs, @jakarta.annotation.Nullable String state) throws RestClientResponseException {
        ParameterizedTypeReference<Transfers> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersCustomerIDTransfersGetRequestCreation(customerID, limit, startingAfter, endingBefore, txHash, updatedAfterMs, updatedBeforeMs, state).toEntity(localVarReturnType);
    }

    /**
     * Get all transfers
     * Get all active and completed transfers for a customer.
     * <p><b>200</b> - List of transfers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param customerID The customerID parameter
     * @param limit The number of items to return (default of 10, max of 100)
     * @param startingAfter This is a transfer id. If this is specified, the next page that starts with a transfer right AFTER the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers older than the specified transfer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a transfer id. If this is specified, the previous page that ends with a transfer right BEFORE the specified transfer id on the transfer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that transfers newer than the specified transfer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param txHash The hash of the transaction
     * @param updatedAfterMs This is a unix timestamp in milliseconds. If this is specified, objects updated AFTER the specified timestamp will be returned
     * @param updatedBeforeMs This is a unix timestamp in milliseconds. If this is specified, objects updated BEFORE the specified timestamp will be returned
     * @param state Filter transfers by their external state. See [Transfer States](https://apidocs.bridge.xyz/platform/orchestration/transfers/transfer-states) for more details.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersCustomerIDTransfersGetWithResponseSpec(@jakarta.annotation.Nonnull String customerID, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable String txHash, @jakarta.annotation.Nullable Integer updatedAfterMs, @jakarta.annotation.Nullable Integer updatedBeforeMs, @jakarta.annotation.Nullable String state) throws RestClientResponseException {
        return customersCustomerIDTransfersGetRequestCreation(customerID, limit, startingAfter, endingBefore, txHash, updatedAfterMs, updatedBeforeMs, state);
    }

    /**
     * Get all customers
     * Get the full list of all customers created on Bridge
     * <p><b>200</b> - List of customers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param startingAfter This is a customer id. If this is specified, the next page that starts with a customer right AFTER the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers older than the specified customer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a customer id. If this is specified, the previous page that ends with a customer right BEFORE the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers newer than the specified customer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param limit The number of items to return (default of 10, max of 100)
     * @param email If included, filters to customers with the given email
     * @return Customers
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersGetRequestCreation(@jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String email) throws RestClientResponseException {
        Object postBody = null;
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        final MultiValueMap<String, String> queryParams = new LinkedMultiValueMap<>();
        final HttpHeaders headerParams = new HttpHeaders();
        final MultiValueMap<String, String> cookieParams = new LinkedMultiValueMap<>();
        final MultiValueMap<String, Object> formParams = new LinkedMultiValueMap<>();

        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "starting_after", startingAfter));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "ending_before", endingBefore));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "limit", limit));
        queryParams.putAll(apiClient.parameterToMultiValueMap(null, "email", email));

        final String[] localVarAccepts = { 
            "application/json"
        };
        final List<MediaType> localVarAccept = apiClient.selectHeaderAccept(localVarAccepts);
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<Customers> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all customers
     * Get the full list of all customers created on Bridge
     * <p><b>200</b> - List of customers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param startingAfter This is a customer id. If this is specified, the next page that starts with a customer right AFTER the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers older than the specified customer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a customer id. If this is specified, the previous page that ends with a customer right BEFORE the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers newer than the specified customer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param limit The number of items to return (default of 10, max of 100)
     * @param email If included, filters to customers with the given email
     * @return Customers
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Customers customersGet(@jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String email) throws RestClientResponseException {
        ParameterizedTypeReference<Customers> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersGetRequestCreation(startingAfter, endingBefore, limit, email).body(localVarReturnType);
    }

    /**
     * Get all customers
     * Get the full list of all customers created on Bridge
     * <p><b>200</b> - List of customers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param startingAfter This is a customer id. If this is specified, the next page that starts with a customer right AFTER the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers older than the specified customer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a customer id. If this is specified, the previous page that ends with a customer right BEFORE the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers newer than the specified customer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param limit The number of items to return (default of 10, max of 100)
     * @param email If included, filters to customers with the given email
     * @return ResponseEntity&lt;Customers&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Customers> customersGetWithHttpInfo(@jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String email) throws RestClientResponseException {
        ParameterizedTypeReference<Customers> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersGetRequestCreation(startingAfter, endingBefore, limit, email).toEntity(localVarReturnType);
    }

    /**
     * Get all customers
     * Get the full list of all customers created on Bridge
     * <p><b>200</b> - List of customers (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param startingAfter This is a customer id. If this is specified, the next page that starts with a customer right AFTER the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers older than the specified customer id will be returned (shouldn&#39;t be set if ending_before is set)
     * @param endingBefore This is a customer id. If this is specified, the previous page that ends with a customer right BEFORE the specified customer id on the customer timeline, which is always ordered from the newest to the oldest by creation time, will be returned. This also implies that customers newer than the specified customer id will be returned (shouldn&#39;t be set if starting_after is set)
     * @param limit The number of items to return (default of 10, max of 100)
     * @param email If included, filters to customers with the given email
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersGetWithResponseSpec(@jakarta.annotation.Nullable String startingAfter, @jakarta.annotation.Nullable String endingBefore, @jakarta.annotation.Nullable Integer limit, @jakarta.annotation.Nullable String email) throws RestClientResponseException {
        return customersGetRequestCreation(startingAfter, endingBefore, limit, email);
    }

    /**
     * Create a customer
     * 
     * <p><b>201</b> - Customer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customersPostRequest Customer object to be created.  For individual customers (soon to be businesses as well), no fields are strictly required by the API. For example, it is valid to create a customer without a first name, last name, or residential address, but this customer will not be granted endorsements required to transact on Bridge until the necessary information is provided, possibly via a PUT request. 
     * @return Customer
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CustomersPostRequest customersPostRequest) throws RestClientResponseException {
        Object postBody = customersPostRequest;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'customersPostRequest' is set
        if (customersPostRequest == null) {
            throw new RestClientResponseException("Missing the required parameter 'customersPostRequest' when calling customersPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a customer
     * 
     * <p><b>201</b> - Customer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customersPostRequest Customer object to be created.  For individual customers (soon to be businesses as well), no fields are strictly required by the API. For example, it is valid to create a customer without a first name, last name, or residential address, but this customer will not be granted endorsements required to transact on Bridge until the necessary information is provided, possibly via a PUT request. 
     * @return Customer
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Customer customersPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CustomersPostRequest customersPostRequest) throws RestClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersPostRequestCreation(idempotencyKey, customersPostRequest).body(localVarReturnType);
    }

    /**
     * Create a customer
     * 
     * <p><b>201</b> - Customer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customersPostRequest Customer object to be created.  For individual customers (soon to be businesses as well), no fields are strictly required by the API. For example, it is valid to create a customer without a first name, last name, or residential address, but this customer will not be granted endorsements required to transact on Bridge until the necessary information is provided, possibly via a PUT request. 
     * @return ResponseEntity&lt;Customer&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Customer> customersPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CustomersPostRequest customersPostRequest) throws RestClientResponseException {
        ParameterizedTypeReference<Customer> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersPostRequestCreation(idempotencyKey, customersPostRequest).toEntity(localVarReturnType);
    }

    /**
     * Create a customer
     * 
     * <p><b>201</b> - Customer object created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param customersPostRequest Customer object to be created.  For individual customers (soon to be businesses as well), no fields are strictly required by the API. For example, it is valid to create a customer without a first name, last name, or residential address, but this customer will not be granted endorsements required to transact on Bridge until the necessary information is provided, possibly via a PUT request. 
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CustomersPostRequest customersPostRequest) throws RestClientResponseException {
        return customersPostRequestCreation(idempotencyKey, customersPostRequest);
    }

    /**
     * Request a hosted URL for ToS acceptance for new customer creation
     * The URL endpoint returned will guide the user through a Bridge TOS flow. Signing this acceptance flow is a requirement for creating customers.
     * <p><b>201</b> - A Bridge hosted URL for users to complete terms of service signing.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @return CustomersTosLinksPost201Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec customersTosLinksPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling customersTosLinksPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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
        final String[] localVarContentTypes = { };
        final MediaType localVarContentType = apiClient.selectHeaderContentType(localVarContentTypes);

        String[] localVarAuthNames = new String[] { "ApiKey" };

        ParameterizedTypeReference<CustomersTosLinksPost201Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/customers/tos_links", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Request a hosted URL for ToS acceptance for new customer creation
     * The URL endpoint returned will guide the user through a Bridge TOS flow. Signing this acceptance flow is a requirement for creating customers.
     * <p><b>201</b> - A Bridge hosted URL for users to complete terms of service signing.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @return CustomersTosLinksPost201Response
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public CustomersTosLinksPost201Response customersTosLinksPost(@jakarta.annotation.Nonnull String idempotencyKey) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersTosLinksPost201Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersTosLinksPostRequestCreation(idempotencyKey).body(localVarReturnType);
    }

    /**
     * Request a hosted URL for ToS acceptance for new customer creation
     * The URL endpoint returned will guide the user through a Bridge TOS flow. Signing this acceptance flow is a requirement for creating customers.
     * <p><b>201</b> - A Bridge hosted URL for users to complete terms of service signing.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @return ResponseEntity&lt;CustomersTosLinksPost201Response&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<CustomersTosLinksPost201Response> customersTosLinksPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey) throws RestClientResponseException {
        ParameterizedTypeReference<CustomersTosLinksPost201Response> localVarReturnType = new ParameterizedTypeReference<>() {};
        return customersTosLinksPostRequestCreation(idempotencyKey).toEntity(localVarReturnType);
    }

    /**
     * Request a hosted URL for ToS acceptance for new customer creation
     * The URL endpoint returned will guide the user through a Bridge TOS flow. Signing this acceptance flow is a requirement for creating customers.
     * <p><b>201</b> - A Bridge hosted URL for users to complete terms of service signing.
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec customersTosLinksPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey) throws RestClientResponseException {
        return customersTosLinksPostRequestCreation(idempotencyKey);
    }
}
