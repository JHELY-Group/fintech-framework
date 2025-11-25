package org.jhely.money.base.service;

import org.jhely.money.sdk.bridge.api.CustomersApi;
import org.jhely.money.sdk.bridge.model.Customer;
import org.jhely.money.sdk.bridge.model.Customers;
import org.jhely.money.sdk.bridge.model.CustomersCustomerIDKycLinkGet200Response;
import org.jhely.money.sdk.bridge.model.CustomersCustomerIDTosAcceptanceLinkGet200Response;
import org.jhely.money.sdk.bridge.model.CustomersPostRequest;
import org.jhely.money.sdk.bridge.model.CustomersTosLinksPost201Response;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * High-level facade for Customer operations backed by the generated CustomersApi.
 * Adds a tiny layer for idempotency and pagination ergonomics.
 */
@Service
public class CustomerService {

    private final CustomersApi customersApi;

    public CustomerService(BridgeApiClientFactory clientFactory) {
        this.customersApi = clientFactory.customers();
    }

    /**
     * Create a customer using an Idempotency-Key. Returns the created/returned Customer.
     * The body must include fields required by Bridge (e.g., signed_agreement_id from ToS flow).
     */
    public Customer createCustomer(String idempotencyKey, CustomersPostRequest body) {
        return customersApi.customersPost(idempotencyKey, body);
    }

    /**
     * Get a customer by id.
     */
    public Customer getCustomer(String customerId) {
        return customersApi.customersCustomerIDGet(customerId);
    }

    /**
     * List customers with optional email filter and cursor-style pagination.
     */
    public Customers listCustomers(Optional<String> startingAfter, Optional<String> endingBefore, Integer limit, Optional<String> email) {
        return customersApi.customersGet(startingAfter.orElse(null), endingBefore.orElse(null), limit, email.orElse(null));
    }

    /**
     * Get a hosted ToS acceptance URL for new customer creation flow.
     */
    public CustomersTosLinksPost201Response requestTosLink(String idempotencyKey) {
        return customersApi.customersTosLinksPost(idempotencyKey);
    }

    /**
     * Get a hosted ToS acceptance URL for an existing customer.
     */
    public CustomersCustomerIDTosAcceptanceLinkGet200Response getTosAcceptanceLink(String customerId) {
        return customersApi.customersCustomerIDTosAcceptanceLinkGet(customerId);
    }

    /**
     * Get a hosted KYC link for an existing customer, optionally scoping by endorsement and redirect URL.
     */
    public CustomersCustomerIDKycLinkGet200Response getKycLink(String customerId, Optional<String> endorsement, Optional<String> redirectUri) {
        return customersApi.customersCustomerIDKycLinkGet(customerId, endorsement.orElse(null), redirectUri.orElse(null));
    }
}
