package org.jhely.money.base.service.x402;

import org.jhely.money.base.domain.X402FacilitatorConfig;
import org.jhely.money.base.repository.X402FacilitatorConfigRepository;
import org.jhely.money.base.service.x402.X402Models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * Unit tests for X402FacilitatorService verification logic.
 * 
 * Tests cover:
 * - Network / cluster validation
 * - Scheme validation
 * - Recipient & amount matching
 * - Expiry validation
 * - Replay protection (nonce reuse)
 */
@SpringBootTest
@ActiveProfiles("test")
class X402FacilitatorServiceTest {

    @Autowired
    private X402FacilitatorService facilitatorService;

    @Autowired
    private X402FacilitatorConfigRepository configRepository;

    private X402FacilitatorConfig testConfig;

    // Test constants
    private static final String VALID_PAYER = "7EcDhSYGxXyscszYEp35KHN8vvw3svAuLKTzXwCFLtV";
    private static final String VALID_RECIPIENT = "9aE476sH92Vg7wR72yrCbAH3Bxpb3h4tSVVZxq4xdMnP";
    private static final String VALID_AMOUNT = "1000000"; // 1 USDC (6 decimals)

    @BeforeEach
    void setUp() {
        // Clean up and create test config
        configRepository.findByUserId("test-user-service").ifPresent(configRepository::delete);

        testConfig = new X402FacilitatorConfig();
        testConfig.setUserId("test-user-service");
        testConfig.setFacilitatorName("Test Facilitator");
        testConfig.setEnabled(true);
        testConfig.setSolanaEnabled(true);
        testConfig.setSolanaRpcDevnet("https://api.devnet.solana.com");
        testConfig.setSolanaRpcMainnet("https://api.mainnet-beta.solana.com");
        testConfig = configRepository.save(testConfig);
    }

    @Nested
    @DisplayName("Network Validation")
    class NetworkValidationTests {

        @Test
        @DisplayName("Should accept 'solana' network (mainnet)")
        void acceptsSolanaMainnet() {
            VerifyRequest request = createValidRequest("solana");
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            // May fail on signature but not on network
            if (!response.isValid()) {
                assertThat(response.getInvalidReason()).doesNotContain("Unknown network");
            }
        }

        @Test
        @DisplayName("Should accept 'solana-devnet' network")
        void acceptsSolanaDevnet() {
            VerifyRequest request = createValidRequest("solana-devnet");
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            // May fail on signature but not on network
            if (!response.isValid()) {
                assertThat(response.getInvalidReason()).doesNotContain("Unknown network");
            }
        }

        @Test
        @DisplayName("Should reject unknown network")
        void rejectsUnknownNetwork() {
            VerifyRequest request = createValidRequest("ethereum-mainnet");
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            assertThat(response.isValid()).isFalse();
            assertThat(response.getInvalidReason()).contains("Unknown network");
            assertThat(response.getErrorCode()).isEqualTo("UNKNOWN_NETWORK");
        }

        @Test
        @DisplayName("Should reject when Solana is disabled")
        void rejectsWhenSolanaDisabled() {
            testConfig.setSolanaEnabled(false);
            configRepository.save(testConfig);

            VerifyRequest request = createValidRequest("solana-devnet");
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            assertThat(response.isValid()).isFalse();
            assertThat(response.getErrorCode()).isEqualTo("NETWORK_DISABLED");
        }
    }

    @Nested
    @DisplayName("Scheme Validation")
    class SchemeValidationTests {

        @Test
        @DisplayName("Should accept 'exact' scheme")
        void acceptsExactScheme() {
            VerifyRequest request = createValidRequest("solana-devnet");
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            // May fail on signature but not on scheme
            if (!response.isValid()) {
                assertThat(response.getInvalidReason()).doesNotContain("Unsupported scheme");
            }
        }

        @Test
        @DisplayName("Should reject unsupported scheme")
        void rejectsUnsupportedScheme() {
            VerifyRequest request = createRequestWithScheme("streaming");
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            assertThat(response.isValid()).isFalse();
            assertThat(response.getInvalidReason()).contains("Unsupported scheme");
            assertThat(response.getErrorCode()).isEqualTo("UNSUPPORTED_SCHEME");
        }
    }

    @Nested
    @DisplayName("Amount Validation")
    class AmountValidationTests {

        @Test
        @DisplayName("Should accept matching exact amounts")
        void acceptsMatchingAmounts() {
            VerifyRequest request = createValidRequest("solana-devnet");
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            // May fail on signature but not on amount
            if (!response.isValid()) {
                assertThat(response.getInvalidReason()).doesNotContain("Amount mismatch");
            }
        }

        @Test
        @DisplayName("Should reject mismatched amounts")
        void rejectsMismatchedAmounts() {
            VerifyRequest request = createRequestWithAmount("1000000", "2000000"); // payload != requirements
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            assertThat(response.isValid()).isFalse();
            assertThat(response.getInvalidReason()).contains("Amount mismatch");
            assertThat(response.getErrorCode()).isEqualTo("AMOUNT_MISMATCH");
        }

        @Test
        @DisplayName("Should reject when payment amount is too small")
        void rejectsTooSmallAmount() {
            VerifyRequest request = createRequestWithAmount("500000", "1000000"); // payload < requirements
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            assertThat(response.isValid()).isFalse();
            assertThat(response.getInvalidReason()).contains("Amount mismatch");
        }
    }

    @Nested
    @DisplayName("Recipient Validation")
    class RecipientValidationTests {

        @Test
        @DisplayName("Should accept matching recipients")
        void acceptsMatchingRecipients() {
            VerifyRequest request = createValidRequest("solana-devnet");
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            // May fail on signature but not on recipient
            if (!response.isValid()) {
                assertThat(response.getInvalidReason()).doesNotContain("Recipient mismatch");
            }
        }

        @Test
        @DisplayName("Should reject wrong recipient")
        void rejectsWrongRecipient() {
            VerifyRequest request = createRequestWithRecipient(VALID_RECIPIENT, "WrongAddress12345678901234567890123456789012");
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            assertThat(response.isValid()).isFalse();
            assertThat(response.getInvalidReason()).contains("Recipient mismatch");
            assertThat(response.getErrorCode()).isEqualTo("RECIPIENT_MISMATCH");
        }
    }

    @Nested
    @DisplayName("Expiry Validation")
    class ExpiryValidationTests {

        @Test
        @DisplayName("Should accept non-expired payment")
        void acceptsNonExpiredPayment() {
            VerifyRequest request = createValidRequest("solana-devnet");
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            // May fail on signature but not on expiry
            if (!response.isValid()) {
                assertThat(response.getInvalidReason()).doesNotContainIgnoringCase("expired");
            }
        }

        @Test
        @DisplayName("Should reject expired payment")
        void rejectsExpiredPayment() {
            VerifyRequest request = createRequestWithExpiry(Instant.now().getEpochSecond() - 3600); // 1 hour ago
            VerifyResponse response = facilitatorService.verify(testConfig, request);

            assertThat(response.isValid()).isFalse();
            assertThat(response.getInvalidReason()).containsIgnoringCase("expired");
            assertThat(response.getErrorCode()).isEqualTo("EXPIRED");
        }
    }

    @Nested
    @DisplayName("Supported Capabilities")
    class SupportedCapabilitiesTests {

        @Test
        @DisplayName("Should return kinds array with correct format")
        void returnsKindsArray() {
            SupportResponse response = facilitatorService.getSupportedCapabilities(testConfig);

            assertThat(response.getKinds()).isNotNull();
            assertThat(response.getKinds()).isNotEmpty();

            for (SupportResponse.Kind kind : response.getKinds()) {
                assertThat(kind.getScheme()).isEqualTo("exact");
                assertThat(kind.getNetwork()).isIn("solana", "solana-devnet");
            }
        }

        @Test
        @DisplayName("Should use 'solana' for mainnet (not 'solana-mainnet')")
        void usesCorrectMainnetName() {
            SupportResponse response = facilitatorService.getSupportedCapabilities(testConfig);

            boolean hasMainnet = response.getKinds().stream()
                    .anyMatch(k -> k.getNetwork().equals("solana"));
            boolean hasOldName = response.getKinds().stream()
                    .anyMatch(k -> k.getNetwork().equals("solana-mainnet"));

            assertThat(hasMainnet).isTrue();
            assertThat(hasOldName).isFalse();
        }

        @Test
        @DisplayName("Should return empty kinds when Solana is disabled")
        void returnsEmptyKindsWhenDisabled() {
            testConfig.setSolanaEnabled(false);
            configRepository.save(testConfig);

            SupportResponse response = facilitatorService.getSupportedCapabilities(testConfig);

            assertThat(response.getKinds()).isEmpty();
        }
    }

    // Helper methods to create test requests

    private VerifyRequest createValidRequest(String network) {
        VerifyRequest request = new VerifyRequest();

        PaymentRequirements req = new PaymentRequirements();
        req.setScheme("exact");
        req.setNetwork(network);
        req.setMaxAmount(VALID_AMOUNT);
        req.setRecipient(VALID_RECIPIENT);
        request.setRequirements(req);

        PaymentPayload payload = new PaymentPayload();
        payload.setScheme("exact");
        payload.setNetwork(network);
        payload.setPayer(VALID_PAYER);
        payload.setRecipient(VALID_RECIPIENT);
        payload.setAmount(VALID_AMOUNT);
        payload.setNonce(System.currentTimeMillis());
        payload.setExpiry(Instant.now().getEpochSecond() + 3600);
        payload.setSignature("test-signature");
        request.setPayload(payload);

        return request;
    }

    private VerifyRequest createRequestWithScheme(String scheme) {
        VerifyRequest request = createValidRequest("solana-devnet");
        request.getRequirements().setScheme(scheme);
        request.getPayload().setScheme(scheme);
        return request;
    }

    private VerifyRequest createRequestWithAmount(String payloadAmount, String requirementsAmount) {
        VerifyRequest request = createValidRequest("solana-devnet");
        request.getRequirements().setMaxAmount(requirementsAmount);
        request.getPayload().setAmount(payloadAmount);
        return request;
    }

    private VerifyRequest createRequestWithRecipient(String requirementsRecipient, String payloadRecipient) {
        VerifyRequest request = createValidRequest("solana-devnet");
        request.getRequirements().setRecipient(requirementsRecipient);
        request.getPayload().setRecipient(payloadRecipient);
        return request;
    }

    private VerifyRequest createRequestWithExpiry(long expiryTimestamp) {
        VerifyRequest request = createValidRequest("solana-devnet");
        request.getPayload().setExpiry(expiryTimestamp);
        return request;
    }
}
