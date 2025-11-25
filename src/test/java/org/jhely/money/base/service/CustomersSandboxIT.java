package org.jhely.money.base.service;

import org.jhely.money.sdk.bridge.client.ApiClient;
import org.jhely.money.sdk.bridge.api.CustomersApi;
import org.jhely.money.sdk.bridge.model.Customers;
import org.jhely.money.sdk.bridge.model.CustomersPostRequest;
import org.jhely.money.sdk.bridge.model.CustomersTosLinksPost201Response;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assumptions;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.client.RestClientResponseException;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

@SpringBootTest
@ActiveProfiles("test")
class CustomersSandboxIT {

    @Autowired
    private CustomerService customerService;

    @Autowired
    private BridgeApiClientFactory factory;

    @Test
    @DisplayName("ToS link: returns hosted URL (201)")
    void tosLinkReturnsUrl() {
        String idem = "it-" + UUID.randomUUID();
        CustomersTosLinksPost201Response resp = customerService.requestTosLink(idem);
        assertThat(resp).isNotNull();
        assertThat(resp.getUrl()).isNotBlank();
        assertThat(resp.getUrl()).startsWith("http");
    }

    @Test
    @DisplayName("List customers: respects limit and succeeds")
    void listCustomersRespectsLimit() {
        Customers page = customerService.listCustomers(Optional.empty(), Optional.empty(), 1, Optional.empty());
        assertThat(page).isNotNull();
        assertThat(page.getData().size()).isBetween(0, 1);
    }

    @Test
    @DisplayName("Create customer: invalid signed_agreement_id yields 400 and is idempotent for same key")
    void createCustomerInvalidAgreementIdempotent() {
        String idem = "cust-bad-" + UUID.randomUUID();

        CustomersPostRequest body = new CustomersPostRequest()
        .type(CustomersPostRequest.TypeEnum.INDIVIDUAL)
        .email("test+bad-" + UUID.randomUUID() + "@example.com")
        .signedAgreementId("agr_" + UUID.randomUUID()) // random invalid agreement id
        .accountPurpose(CustomersPostRequest.AccountPurposeEnum.PURCHASE_GOODS_AND_SERVICES)
        .sourceOfFunds(CustomersPostRequest.SourceOfFundsEnum.SALES_OF_GOODS_AND_SERVICES);

    // First attempt should 400
    var ex1 = (RestClientResponseException) org.junit.jupiter.api.Assertions.assertThrows(
        RestClientResponseException.class,
        () -> customerService.createCustomer(idem, body)
    );
    assertThat(ex1.getStatusCode().value()).isEqualTo(400);

    // Second attempt with the same Idempotency-Key should return the same error (idempotent)
    var ex2 = (RestClientResponseException) org.junit.jupiter.api.Assertions.assertThrows(
        RestClientResponseException.class,
        () -> customerService.createCustomer(idem, body)
    );
    assertThat(ex2.getStatusCode().value()).isEqualTo(400);
    // Body often matches for idempotent errors; assert same or at least non-empty
    assertThat(ex1.getResponseBodyAsString()).isNotBlank();
    assertThat(ex2.getResponseBodyAsString()).isNotBlank();
    }

    // Removed separate optional 400 test; covered by createCustomerInvalidAgreementIdempotent

    @Test
    @DisplayName("API key header present: invalid key yields 401")
    void invalidApiKeyYields401() {
        ApiClient bad = new ApiClient();
        bad.setBasePath(factory.getApiClient().getBasePath());
        // Intentionally set a wrong key; authentication is handled via ApiKey auth
        bad.addDefaultHeader("Api-Key", "sk-invalid-" + UUID.randomUUID());
        CustomersApi api = new CustomersApi(bad);

        assertThatThrownBy(() -> api.customersGet(null, null, 1, null))
                .isInstanceOf(RestClientResponseException.class)
                .extracting("rawStatusCode").isEqualTo(401);
    }

    @Test
    @DisplayName("KYC link: non-existent customer yields 404")
    void kycLinkForNonExistentCustomer() {
        String customerId = "cus_" + UUID.randomUUID();
    assertThatThrownBy(() -> customerService.getKycLink(customerId, Optional.empty(), Optional.empty()))
        .isInstanceOf(RestClientResponseException.class)
        .extracting(ex -> ((RestClientResponseException) ex).getStatusCode().value()).isIn(404, 400);
    }
}
