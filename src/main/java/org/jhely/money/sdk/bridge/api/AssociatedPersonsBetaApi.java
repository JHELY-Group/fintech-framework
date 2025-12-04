package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.AssociatedPerson;
import org.jhely.money.sdk.bridge.model.AssociatedPersonResponse;
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
public class AssociatedPersonsBetaApi {
    private ApiClient apiClient;

    public AssociatedPersonsBetaApi() {
        this(new ApiClient());
    }

    public AssociatedPersonsBetaApi(ApiClient apiClient) {
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
     * @param associatedPersonID Unique identifier for an associated person
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec associatedPersonsAssociatedPersonIDDeleteRequestCreation(@jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'associatedPersonID' is set
        if (associatedPersonID == null) {
            throw new RestClientResponseException("Missing the required parameter 'associatedPersonID' when calling associatedPersonsAssociatedPersonIDDelete", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

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
        return apiClient.invokeAPI("/associated_persons/{associatedPersonID}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Delete a single associated person
     * Delete an associated person by ID
     * <p><b>200</b> - Successfully deleted associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public AssociatedPersonResponse associatedPersonsAssociatedPersonIDDelete(@jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return associatedPersonsAssociatedPersonIDDeleteRequestCreation(associatedPersonID).body(localVarReturnType);
    }

    /**
     * Delete a single associated person
     * Delete an associated person by ID
     * <p><b>200</b> - Successfully deleted associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @return ResponseEntity&lt;AssociatedPersonResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<AssociatedPersonResponse> associatedPersonsAssociatedPersonIDDeleteWithHttpInfo(@jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return associatedPersonsAssociatedPersonIDDeleteRequestCreation(associatedPersonID).toEntity(localVarReturnType);
    }

    /**
     * Delete a single associated person
     * Delete an associated person by ID
     * <p><b>200</b> - Successfully deleted associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec associatedPersonsAssociatedPersonIDDeleteWithResponseSpec(@jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        return associatedPersonsAssociatedPersonIDDeleteRequestCreation(associatedPersonID);
    }

    /**
     * Get a single associated person (Beta)
     * Retrieve an associated person by ID
     * <p><b>200</b> - Successful associated person response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec associatedPersonsAssociatedPersonIDGetRequestCreation(@jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'associatedPersonID' is set
        if (associatedPersonID == null) {
            throw new RestClientResponseException("Missing the required parameter 'associatedPersonID' when calling associatedPersonsAssociatedPersonIDGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

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
        return apiClient.invokeAPI("/associated_persons/{associatedPersonID}", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get a single associated person (Beta)
     * Retrieve an associated person by ID
     * <p><b>200</b> - Successful associated person response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public AssociatedPersonResponse associatedPersonsAssociatedPersonIDGet(@jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return associatedPersonsAssociatedPersonIDGetRequestCreation(associatedPersonID).body(localVarReturnType);
    }

    /**
     * Get a single associated person (Beta)
     * Retrieve an associated person by ID
     * <p><b>200</b> - Successful associated person response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @return ResponseEntity&lt;AssociatedPersonResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<AssociatedPersonResponse> associatedPersonsAssociatedPersonIDGetWithHttpInfo(@jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return associatedPersonsAssociatedPersonIDGetRequestCreation(associatedPersonID).toEntity(localVarReturnType);
    }

    /**
     * Get a single associated person (Beta)
     * Retrieve an associated person by ID
     * <p><b>200</b> - Successful associated person response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec associatedPersonsAssociatedPersonIDGetWithResponseSpec(@jakarta.annotation.Nonnull String associatedPersonID) throws RestClientResponseException {
        return associatedPersonsAssociatedPersonIDGetRequestCreation(associatedPersonID);
    }

    /**
     * Update a single associated person
     * 
     * <p><b>200</b> - Updated associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @param associatedPerson Associated person data to update
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec associatedPersonsAssociatedPersonIDPutRequestCreation(@jakarta.annotation.Nonnull String associatedPersonID, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        Object postBody = associatedPerson;
        // verify the required parameter 'associatedPersonID' is set
        if (associatedPersonID == null) {
            throw new RestClientResponseException("Missing the required parameter 'associatedPersonID' when calling associatedPersonsAssociatedPersonIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'associatedPerson' is set
        if (associatedPerson == null) {
            throw new RestClientResponseException("Missing the required parameter 'associatedPerson' when calling associatedPersonsAssociatedPersonIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

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
        return apiClient.invokeAPI("/associated_persons/{associatedPersonID}", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update a single associated person
     * 
     * <p><b>200</b> - Updated associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @param associatedPerson Associated person data to update
     * @return AssociatedPersonResponse
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public AssociatedPersonResponse associatedPersonsAssociatedPersonIDPut(@jakarta.annotation.Nonnull String associatedPersonID, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return associatedPersonsAssociatedPersonIDPutRequestCreation(associatedPersonID, associatedPerson).body(localVarReturnType);
    }

    /**
     * Update a single associated person
     * 
     * <p><b>200</b> - Updated associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @param associatedPerson Associated person data to update
     * @return ResponseEntity&lt;AssociatedPersonResponse&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<AssociatedPersonResponse> associatedPersonsAssociatedPersonIDPutWithHttpInfo(@jakarta.annotation.Nonnull String associatedPersonID, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        ParameterizedTypeReference<AssociatedPersonResponse> localVarReturnType = new ParameterizedTypeReference<>() {};
        return associatedPersonsAssociatedPersonIDPutRequestCreation(associatedPersonID, associatedPerson).toEntity(localVarReturnType);
    }

    /**
     * Update a single associated person
     * 
     * <p><b>200</b> - Updated associated person
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param associatedPersonID Unique identifier for an associated person
     * @param associatedPerson Associated person data to update
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec associatedPersonsAssociatedPersonIDPutWithResponseSpec(@jakarta.annotation.Nonnull String associatedPersonID, @jakarta.annotation.Nonnull AssociatedPerson associatedPerson) throws RestClientResponseException {
        return associatedPersonsAssociatedPersonIDPutRequestCreation(associatedPersonID, associatedPerson);
    }
}
