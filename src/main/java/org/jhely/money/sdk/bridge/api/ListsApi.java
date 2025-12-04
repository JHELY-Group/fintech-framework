package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.Countries;
import org.jhely.money.sdk.bridge.model.ListsOccupationCodesGet200ResponseInner;

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
public class ListsApi {
    private ApiClient apiClient;

    public ListsApi() {
        this(new ApiClient());
    }

    public ListsApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get countries
     * Returns a list of countries and subdivisions recognized by Bridge. Inclusion in this list does not guarantee Bridge product support for any country or subdivision.
     * <p><b>200</b> - List of supported countries
     * @return Countries
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec listsCountriesGetRequestCreation() throws RestClientResponseException {
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

        ParameterizedTypeReference<Countries> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/lists/countries", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get countries
     * Returns a list of countries and subdivisions recognized by Bridge. Inclusion in this list does not guarantee Bridge product support for any country or subdivision.
     * <p><b>200</b> - List of supported countries
     * @return Countries
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Countries listsCountriesGet() throws RestClientResponseException {
        ParameterizedTypeReference<Countries> localVarReturnType = new ParameterizedTypeReference<>() {};
        return listsCountriesGetRequestCreation().body(localVarReturnType);
    }

    /**
     * Get countries
     * Returns a list of countries and subdivisions recognized by Bridge. Inclusion in this list does not guarantee Bridge product support for any country or subdivision.
     * <p><b>200</b> - List of supported countries
     * @return ResponseEntity&lt;Countries&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Countries> listsCountriesGetWithHttpInfo() throws RestClientResponseException {
        ParameterizedTypeReference<Countries> localVarReturnType = new ParameterizedTypeReference<>() {};
        return listsCountriesGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Get countries
     * Returns a list of countries and subdivisions recognized by Bridge. Inclusion in this list does not guarantee Bridge product support for any country or subdivision.
     * <p><b>200</b> - List of supported countries
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec listsCountriesGetWithResponseSpec() throws RestClientResponseException {
        return listsCountriesGetRequestCreation();
    }

    /**
     * Get occupation codes
     * Provide this list of occupation codes to your users as possible answers to the Source of Funds section in the KYC flow. The returned list will resemble the list of occupation codes found [here](https://apidocs.bridge.xyz/page/sof-eu-most-recent-occupation-list). 
     * <p><b>200</b> - The list of occupations that can be used during the KYC flow.
     * @return List&lt;ListsOccupationCodesGet200ResponseInner&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec listsOccupationCodesGetRequestCreation() throws RestClientResponseException {
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

        ParameterizedTypeReference<List<ListsOccupationCodesGet200ResponseInner>> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/lists/occupation_codes", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get occupation codes
     * Provide this list of occupation codes to your users as possible answers to the Source of Funds section in the KYC flow. The returned list will resemble the list of occupation codes found [here](https://apidocs.bridge.xyz/page/sof-eu-most-recent-occupation-list). 
     * <p><b>200</b> - The list of occupations that can be used during the KYC flow.
     * @return List&lt;ListsOccupationCodesGet200ResponseInner&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public List<ListsOccupationCodesGet200ResponseInner> listsOccupationCodesGet() throws RestClientResponseException {
        ParameterizedTypeReference<List<ListsOccupationCodesGet200ResponseInner>> localVarReturnType = new ParameterizedTypeReference<>() {};
        return listsOccupationCodesGetRequestCreation().body(localVarReturnType);
    }

    /**
     * Get occupation codes
     * Provide this list of occupation codes to your users as possible answers to the Source of Funds section in the KYC flow. The returned list will resemble the list of occupation codes found [here](https://apidocs.bridge.xyz/page/sof-eu-most-recent-occupation-list). 
     * <p><b>200</b> - The list of occupations that can be used during the KYC flow.
     * @return ResponseEntity&lt;List&lt;ListsOccupationCodesGet200ResponseInner&gt;&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<List<ListsOccupationCodesGet200ResponseInner>> listsOccupationCodesGetWithHttpInfo() throws RestClientResponseException {
        ParameterizedTypeReference<List<ListsOccupationCodesGet200ResponseInner>> localVarReturnType = new ParameterizedTypeReference<>() {};
        return listsOccupationCodesGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Get occupation codes
     * Provide this list of occupation codes to your users as possible answers to the Source of Funds section in the KYC flow. The returned list will resemble the list of occupation codes found [here](https://apidocs.bridge.xyz/page/sof-eu-most-recent-occupation-list). 
     * <p><b>200</b> - The list of occupations that can be used during the KYC flow.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec listsOccupationCodesGetWithResponseSpec() throws RestClientResponseException {
        return listsOccupationCodesGetRequestCreation();
    }
}
