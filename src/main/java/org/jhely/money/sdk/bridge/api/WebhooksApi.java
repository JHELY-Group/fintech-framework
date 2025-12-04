package org.jhely.money.sdk.bridge.api;

import org.jhely.money.sdk.bridge.client.ApiClient;

import org.jhely.money.sdk.bridge.model.CreateWebhook;
import org.jhely.money.sdk.bridge.model.DeliveryLogs;
import org.jhely.money.sdk.bridge.model.Error;
import org.jhely.money.sdk.bridge.model.PutWebhookPayload;
import org.jhely.money.sdk.bridge.model.SendWebhookPayload;
import org.jhely.money.sdk.bridge.model.Webhook;
import org.jhely.money.sdk.bridge.model.WebhookEventSent;
import org.jhely.money.sdk.bridge.model.WebhookEvents;
import org.jhely.money.sdk.bridge.model.Webhooks;

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
public class WebhooksApi {
    private ApiClient apiClient;

    public WebhooksApi() {
        this(new ApiClient());
    }

    public WebhooksApi(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    public ApiClient getApiClient() {
        return apiClient;
    }

    public void setApiClient(ApiClient apiClient) {
        this.apiClient = apiClient;
    }

    /**
     * Get all webhook endpoints
     * Get the full list of active and disabled webhook endpoints configured on Bridge
     * <p><b>200</b> - List of webhook endpoints (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return Webhooks
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec webhooksGetRequestCreation() throws RestClientResponseException {
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

        ParameterizedTypeReference<Webhooks> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/webhooks", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Get all webhook endpoints
     * Get the full list of active and disabled webhook endpoints configured on Bridge
     * <p><b>200</b> - List of webhook endpoints (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return Webhooks
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Webhooks webhooksGet() throws RestClientResponseException {
        ParameterizedTypeReference<Webhooks> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksGetRequestCreation().body(localVarReturnType);
    }

    /**
     * Get all webhook endpoints
     * Get the full list of active and disabled webhook endpoints configured on Bridge
     * <p><b>200</b> - List of webhook endpoints (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseEntity&lt;Webhooks&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Webhooks> webhooksGetWithHttpInfo() throws RestClientResponseException {
        ParameterizedTypeReference<Webhooks> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksGetRequestCreation().toEntity(localVarReturnType);
    }

    /**
     * Get all webhook endpoints
     * Get the full list of active and disabled webhook endpoints configured on Bridge
     * <p><b>200</b> - List of webhook endpoints (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec webhooksGetWithResponseSpec() throws RestClientResponseException {
        return webhooksGetRequestCreation();
    }

    /**
     * Create a webhook endpoint
     * Create a new webhook endpoint to receive events from Bridge. Webhook endpoints begin in a disabled state and can be enabled with a PUT request. A maximum of 5 webhooks can be active or disabled at one time. Webhook endpoints can be created in Sandbox, but no webhook events will be sent.
     * <p><b>201</b> - Webhook endpoint created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createWebhook Information about the webhook endpoint to be created
     * @return Webhook
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec webhooksPostRequestCreation(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateWebhook createWebhook) throws RestClientResponseException {
        Object postBody = createWebhook;
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling webhooksPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'createWebhook' is set
        if (createWebhook == null) {
            throw new RestClientResponseException("Missing the required parameter 'createWebhook' when calling webhooksPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
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

        ParameterizedTypeReference<Webhook> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/webhooks", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Create a webhook endpoint
     * Create a new webhook endpoint to receive events from Bridge. Webhook endpoints begin in a disabled state and can be enabled with a PUT request. A maximum of 5 webhooks can be active or disabled at one time. Webhook endpoints can be created in Sandbox, but no webhook events will be sent.
     * <p><b>201</b> - Webhook endpoint created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createWebhook Information about the webhook endpoint to be created
     * @return Webhook
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Webhook webhooksPost(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateWebhook createWebhook) throws RestClientResponseException {
        ParameterizedTypeReference<Webhook> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksPostRequestCreation(idempotencyKey, createWebhook).body(localVarReturnType);
    }

    /**
     * Create a webhook endpoint
     * Create a new webhook endpoint to receive events from Bridge. Webhook endpoints begin in a disabled state and can be enabled with a PUT request. A maximum of 5 webhooks can be active or disabled at one time. Webhook endpoints can be created in Sandbox, but no webhook events will be sent.
     * <p><b>201</b> - Webhook endpoint created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createWebhook Information about the webhook endpoint to be created
     * @return ResponseEntity&lt;Webhook&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Webhook> webhooksPostWithHttpInfo(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateWebhook createWebhook) throws RestClientResponseException {
        ParameterizedTypeReference<Webhook> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksPostRequestCreation(idempotencyKey, createWebhook).toEntity(localVarReturnType);
    }

    /**
     * Create a webhook endpoint
     * Create a new webhook endpoint to receive events from Bridge. Webhook endpoints begin in a disabled state and can be enabled with a PUT request. A maximum of 5 webhooks can be active or disabled at one time. Webhook endpoints can be created in Sandbox, but no webhook events will be sent.
     * <p><b>201</b> - Webhook endpoint created
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>400</b> - Request containing missing or invalid parameters.
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param idempotencyKey The idempotencyKey parameter
     * @param createWebhook Information about the webhook endpoint to be created
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec webhooksPostWithResponseSpec(@jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull CreateWebhook createWebhook) throws RestClientResponseException {
        return webhooksPostRequestCreation(idempotencyKey, createWebhook);
    }

    /**
     * Delete a webhook
     * Delete the specified webhook object. This webhook will no longer be accessible via API.
     * <p><b>200</b> - Successfully deleted webhook response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return Webhook
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec webhooksWebhookIDDeleteRequestCreation(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'webhookID' is set
        if (webhookID == null) {
            throw new RestClientResponseException("Missing the required parameter 'webhookID' when calling webhooksWebhookIDDelete", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("webhookID", webhookID);

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

        ParameterizedTypeReference<Webhook> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/webhooks/{webhookID}", HttpMethod.DELETE, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Delete a webhook
     * Delete the specified webhook object. This webhook will no longer be accessible via API.
     * <p><b>200</b> - Successfully deleted webhook response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return Webhook
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Webhook webhooksWebhookIDDelete(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        ParameterizedTypeReference<Webhook> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksWebhookIDDeleteRequestCreation(webhookID).body(localVarReturnType);
    }

    /**
     * Delete a webhook
     * Delete the specified webhook object. This webhook will no longer be accessible via API.
     * <p><b>200</b> - Successfully deleted webhook response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return ResponseEntity&lt;Webhook&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Webhook> webhooksWebhookIDDeleteWithHttpInfo(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        ParameterizedTypeReference<Webhook> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksWebhookIDDeleteRequestCreation(webhookID).toEntity(localVarReturnType);
    }

    /**
     * Delete a webhook
     * Delete the specified webhook object. This webhook will no longer be accessible via API.
     * <p><b>200</b> - Successfully deleted webhook response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec webhooksWebhookIDDeleteWithResponseSpec(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        return webhooksWebhookIDDeleteRequestCreation(webhookID);
    }

    /**
     * List upcoming events
     * List the next 10 events that will be delivered to the specified webhook.
     * <p><b>200</b> - List of events (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return WebhookEvents
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec webhooksWebhookIDEventsGetRequestCreation(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'webhookID' is set
        if (webhookID == null) {
            throw new RestClientResponseException("Missing the required parameter 'webhookID' when calling webhooksWebhookIDEventsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("webhookID", webhookID);

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

        ParameterizedTypeReference<WebhookEvents> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/webhooks/{webhookID}/events", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * List upcoming events
     * List the next 10 events that will be delivered to the specified webhook.
     * <p><b>200</b> - List of events (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return WebhookEvents
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public WebhookEvents webhooksWebhookIDEventsGet(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        ParameterizedTypeReference<WebhookEvents> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksWebhookIDEventsGetRequestCreation(webhookID).body(localVarReturnType);
    }

    /**
     * List upcoming events
     * List the next 10 events that will be delivered to the specified webhook.
     * <p><b>200</b> - List of events (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return ResponseEntity&lt;WebhookEvents&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<WebhookEvents> webhooksWebhookIDEventsGetWithHttpInfo(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        ParameterizedTypeReference<WebhookEvents> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksWebhookIDEventsGetRequestCreation(webhookID).toEntity(localVarReturnType);
    }

    /**
     * List upcoming events
     * List the next 10 events that will be delivered to the specified webhook.
     * <p><b>200</b> - List of events (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec webhooksWebhookIDEventsGetWithResponseSpec(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        return webhooksWebhookIDEventsGetRequestCreation(webhookID);
    }

    /**
     * View logs
     * Display the most recent logs for deliveries to the specified webhook.
     * <p><b>200</b> - Recent delivery looks for the webhook (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return DeliveryLogs
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec webhooksWebhookIDLogsGetRequestCreation(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        Object postBody = null;
        // verify the required parameter 'webhookID' is set
        if (webhookID == null) {
            throw new RestClientResponseException("Missing the required parameter 'webhookID' when calling webhooksWebhookIDLogsGet", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("webhookID", webhookID);

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

        ParameterizedTypeReference<DeliveryLogs> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/webhooks/{webhookID}/logs", HttpMethod.GET, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * View logs
     * Display the most recent logs for deliveries to the specified webhook.
     * <p><b>200</b> - Recent delivery looks for the webhook (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return DeliveryLogs
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public DeliveryLogs webhooksWebhookIDLogsGet(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        ParameterizedTypeReference<DeliveryLogs> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksWebhookIDLogsGetRequestCreation(webhookID).body(localVarReturnType);
    }

    /**
     * View logs
     * Display the most recent logs for deliveries to the specified webhook.
     * <p><b>200</b> - Recent delivery looks for the webhook (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return ResponseEntity&lt;DeliveryLogs&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<DeliveryLogs> webhooksWebhookIDLogsGetWithHttpInfo(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        ParameterizedTypeReference<DeliveryLogs> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksWebhookIDLogsGetRequestCreation(webhookID).toEntity(localVarReturnType);
    }

    /**
     * View logs
     * Display the most recent logs for deliveries to the specified webhook.
     * <p><b>200</b> - Recent delivery looks for the webhook (the returned list is empty if none found)
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec webhooksWebhookIDLogsGetWithResponseSpec(@jakarta.annotation.Nonnull String webhookID) throws RestClientResponseException {
        return webhooksWebhookIDLogsGetRequestCreation(webhookID);
    }

    /**
     * Update a webhook
     * Update the specified webhook object
     * <p><b>200</b> - Successful webhook object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @param putWebhookPayload Updated webhook endpoint object
     * @return Webhook
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec webhooksWebhookIDPutRequestCreation(@jakarta.annotation.Nonnull String webhookID, @jakarta.annotation.Nonnull PutWebhookPayload putWebhookPayload) throws RestClientResponseException {
        Object postBody = putWebhookPayload;
        // verify the required parameter 'webhookID' is set
        if (webhookID == null) {
            throw new RestClientResponseException("Missing the required parameter 'webhookID' when calling webhooksWebhookIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'putWebhookPayload' is set
        if (putWebhookPayload == null) {
            throw new RestClientResponseException("Missing the required parameter 'putWebhookPayload' when calling webhooksWebhookIDPut", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("webhookID", webhookID);

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

        ParameterizedTypeReference<Webhook> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/webhooks/{webhookID}", HttpMethod.PUT, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Update a webhook
     * Update the specified webhook object
     * <p><b>200</b> - Successful webhook object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @param putWebhookPayload Updated webhook endpoint object
     * @return Webhook
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public Webhook webhooksWebhookIDPut(@jakarta.annotation.Nonnull String webhookID, @jakarta.annotation.Nonnull PutWebhookPayload putWebhookPayload) throws RestClientResponseException {
        ParameterizedTypeReference<Webhook> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksWebhookIDPutRequestCreation(webhookID, putWebhookPayload).body(localVarReturnType);
    }

    /**
     * Update a webhook
     * Update the specified webhook object
     * <p><b>200</b> - Successful webhook object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @param putWebhookPayload Updated webhook endpoint object
     * @return ResponseEntity&lt;Webhook&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<Webhook> webhooksWebhookIDPutWithHttpInfo(@jakarta.annotation.Nonnull String webhookID, @jakarta.annotation.Nonnull PutWebhookPayload putWebhookPayload) throws RestClientResponseException {
        ParameterizedTypeReference<Webhook> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksWebhookIDPutRequestCreation(webhookID, putWebhookPayload).toEntity(localVarReturnType);
    }

    /**
     * Update a webhook
     * Update the specified webhook object
     * <p><b>200</b> - Successful webhook object response
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>404</b> - No resource found
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @param putWebhookPayload Updated webhook endpoint object
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec webhooksWebhookIDPutWithResponseSpec(@jakarta.annotation.Nonnull String webhookID, @jakarta.annotation.Nonnull PutWebhookPayload putWebhookPayload) throws RestClientResponseException {
        return webhooksWebhookIDPutRequestCreation(webhookID, putWebhookPayload);
    }

    /**
     * Send event
     * Send an event to the specified webhook endpoint. This will not effect other events in the delivery queue. This operation is possible for both active and disabled webhook endpoints.
     * <p><b>200</b> - Event sent successfully
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param sendWebhookPayload Specify the event to send to your endpoint.
     * @return WebhookEventSent
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    private ResponseSpec webhooksWebhookIDSendPostRequestCreation(@jakarta.annotation.Nonnull String webhookID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull SendWebhookPayload sendWebhookPayload) throws RestClientResponseException {
        Object postBody = sendWebhookPayload;
        // verify the required parameter 'webhookID' is set
        if (webhookID == null) {
            throw new RestClientResponseException("Missing the required parameter 'webhookID' when calling webhooksWebhookIDSendPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'idempotencyKey' is set
        if (idempotencyKey == null) {
            throw new RestClientResponseException("Missing the required parameter 'idempotencyKey' when calling webhooksWebhookIDSendPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // verify the required parameter 'sendWebhookPayload' is set
        if (sendWebhookPayload == null) {
            throw new RestClientResponseException("Missing the required parameter 'sendWebhookPayload' when calling webhooksWebhookIDSendPost", HttpStatus.BAD_REQUEST.value(), HttpStatus.BAD_REQUEST.getReasonPhrase(), null, null, null);
        }
        // create path and map variables
        final Map<String, Object> pathParams = new HashMap<>();

        pathParams.put("webhookID", webhookID);

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

        ParameterizedTypeReference<WebhookEventSent> localVarReturnType = new ParameterizedTypeReference<>() {};
        return apiClient.invokeAPI("/webhooks/{webhookID}/send", HttpMethod.POST, pathParams, queryParams, postBody, headerParams, cookieParams, formParams, localVarAccept, localVarContentType, localVarAuthNames, localVarReturnType);
    }

    /**
     * Send event
     * Send an event to the specified webhook endpoint. This will not effect other events in the delivery queue. This operation is possible for both active and disabled webhook endpoints.
     * <p><b>200</b> - Event sent successfully
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param sendWebhookPayload Specify the event to send to your endpoint.
     * @return WebhookEventSent
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public WebhookEventSent webhooksWebhookIDSendPost(@jakarta.annotation.Nonnull String webhookID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull SendWebhookPayload sendWebhookPayload) throws RestClientResponseException {
        ParameterizedTypeReference<WebhookEventSent> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksWebhookIDSendPostRequestCreation(webhookID, idempotencyKey, sendWebhookPayload).body(localVarReturnType);
    }

    /**
     * Send event
     * Send an event to the specified webhook endpoint. This will not effect other events in the delivery queue. This operation is possible for both active and disabled webhook endpoints.
     * <p><b>200</b> - Event sent successfully
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param sendWebhookPayload Specify the event to send to your endpoint.
     * @return ResponseEntity&lt;WebhookEventSent&gt;
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseEntity<WebhookEventSent> webhooksWebhookIDSendPostWithHttpInfo(@jakarta.annotation.Nonnull String webhookID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull SendWebhookPayload sendWebhookPayload) throws RestClientResponseException {
        ParameterizedTypeReference<WebhookEventSent> localVarReturnType = new ParameterizedTypeReference<>() {};
        return webhooksWebhookIDSendPostRequestCreation(webhookID, idempotencyKey, sendWebhookPayload).toEntity(localVarReturnType);
    }

    /**
     * Send event
     * Send an event to the specified webhook endpoint. This will not effect other events in the delivery queue. This operation is possible for both active and disabled webhook endpoints.
     * <p><b>200</b> - Event sent successfully
     * <p><b>401</b> - Missing or invalid API key
     * <p><b>500</b> - Unexpected error. User may try and send the request again.
     * @param webhookID The webhookID parameter
     * @param idempotencyKey The idempotencyKey parameter
     * @param sendWebhookPayload Specify the event to send to your endpoint.
     * @return ResponseSpec
     * @throws RestClientResponseException if an error occurs while attempting to invoke the API
     */
    public ResponseSpec webhooksWebhookIDSendPostWithResponseSpec(@jakarta.annotation.Nonnull String webhookID, @jakarta.annotation.Nonnull String idempotencyKey, @jakarta.annotation.Nonnull SendWebhookPayload sendWebhookPayload) throws RestClientResponseException {
        return webhooksWebhookIDSendPostRequestCreation(webhookID, idempotencyKey, sendWebhookPayload);
    }
}
