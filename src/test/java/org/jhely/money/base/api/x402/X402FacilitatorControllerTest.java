package org.jhely.money.base.api.x402;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.jhely.money.base.domain.X402FacilitatorConfig;
import org.jhely.money.base.repository.X402FacilitatorConfigRepository;
import org.jhely.money.base.service.x402.X402FacilitatorService;
import org.jhely.money.base.service.x402.X402Models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.nio.charset.StandardCharsets;
import java.time.Instant;
import java.util.Base64;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Integration tests for x402 Facilitator Controller.
 * 
 * Tests validate conformance to x402 protocol specification:
 * - Endpoint paths: GET /supported, POST /verify, POST /settle
 * - Request/response formats per spec
 * - Error handling with proper codes
 * - Verification logic for Solana payments
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class X402FacilitatorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private X402FacilitatorService facilitatorService;

    @Autowired
    private X402FacilitatorConfigRepository configRepository;

    private String testApiKey;
    private X402FacilitatorConfig testConfig;

    // Test constants
    private static final String VALID_PAYER = "7EcDhSYGxXyscszYEp35KHN8vvw3svAuLKTzXwCFLtV";
    private static final String VALID_RECIPIENT = "9aE476sH92Vg7wR72yrCbAH3Bxpb3h4tSVVZxq4xdMnP";
    private static final String USDC_MINT_DEVNET = "4zMMC9srt5Ri5X14GAgXhaHii3GnPAEERYPJgZJDncDU";
    private static final String VALID_AMOUNT = "1000000"; // 1 USDC (6 decimals)

    @BeforeEach
    void setUp() {
        // Clean up any existing test config
        configRepository.findByUserId("test-user-x402").ifPresent(configRepository::delete);

        // Create a test facilitator config
        testConfig = new X402FacilitatorConfig();
        testConfig.setUserId("test-user-x402");
        testConfig.setFacilitatorName("Test Facilitator");
        testConfig.setEnabled(true);
        testConfig.setSolanaEnabled(true);
        testConfig.setSolanaRpcDevnet("https://api.devnet.solana.com");
        testConfig.setSolanaRpcMainnet("https://api.mainnet-beta.solana.com");
        configRepository.save(testConfig);

        // Generate API key for testing (uses userId)
        testApiKey = facilitatorService.generateApiKey("test-user-x402");
    }

    @Nested
    @DisplayName("GET / - Root Endpoint")
    class RootEndpointTests {

        @Test
        @DisplayName("Should return facilitator info without auth")
        void rootReturnsInfo() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/x402/"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.get("name").asText()).isEqualTo("x402-facilitator");
            assertThat(json.get("protocol").asText()).isEqualTo("x402");
            assertThat(json.get("x402Version").asInt()).isEqualTo(1);
        }
    }

    @Nested
    @DisplayName("GET /supported - Supported Capabilities")
    class SupportedEndpointTests {

        @Test
        @DisplayName("Should return 401 without API key")
        void supportedWithoutApiKeyReturns401() throws Exception {
            mockMvc.perform(get("/api/x402/supported"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("Should return kinds array per x402 spec")
        void supportedReturnsKindsArray() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/x402/supported")
                            .header("X-API-Key", testApiKey))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());

            // Verify kinds array format per spec
            assertThat(json.has("kinds")).isTrue();
            JsonNode kinds = json.get("kinds");
            assertThat(kinds.isArray()).isTrue();
            assertThat(kinds.size()).isGreaterThanOrEqualTo(1);

            // Check each kind has scheme and network
            for (JsonNode kind : kinds) {
                assertThat(kind.has("scheme")).isTrue();
                assertThat(kind.has("network")).isTrue();
                assertThat(kind.get("scheme").asText()).isEqualTo("exact");
                // Network should be "solana" or "solana-devnet"
                String network = kind.get("network").asText();
                assertThat(network).isIn("solana", "solana-devnet");
            }
        }
    }

    @Nested
    @DisplayName("POST /verify - Payment Verification")
    class VerifyEndpointTests {

        @Test
        @DisplayName("Should return 401 without API key")
        void verifyWithoutApiKeyReturns401() throws Exception {
            String body = createVerifyRequestJson();

            mockMvc.perform(post("/api/x402/verify")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.isValid").value(false))
                    .andExpect(jsonPath("$.invalidReason").exists());
        }

        @Test
        @DisplayName("Should accept x402Version + paymentHeader format")
        void verifyAcceptsPaymentHeaderFormat() throws Exception {
            PaymentPayload payload = createValidPaymentPayload();
            String paymentHeader = encodePaymentHeader(payload);

            String body = """
                {
                    "x402Version": 1,
                    "paymentHeader": "%s",
                    "paymentRequirements": {
                        "scheme": "exact",
                        "network": "solana-devnet",
                        "maxAmountRequired": "%s",
                        "recipient": "%s"
                    }
                }
                """.formatted(paymentHeader, VALID_AMOUNT, VALID_RECIPIENT);

            mockMvc.perform(post("/api/x402/verify")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andExpect(jsonPath("$.isValid").exists());
        }

        @Test
        @DisplayName("Should return isValid=false for missing paymentRequirements")
        void verifyFailsWithMissingRequirements() throws Exception {
            String body = """
                {
                    "x402Version": 1,
                    "paymentHeader": "eyJ0ZXN0IjoidmFsdWUifQ=="
                }
                """;

            MvcResult result = mockMvc.perform(post("/api/x402/verify")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.get("isValid").asBoolean()).isFalse();
            assertThat(json.has("invalidReason")).isTrue();
        }

        @Test
        @DisplayName("Should return isValid=false for wrong recipient")
        void verifyFailsWithWrongRecipient() throws Exception {
            String wrongRecipient = "WrongRecipientAddress123456789012345678901234";
            String body = createVerifyRequestJsonWithRecipient(wrongRecipient);

            MvcResult result = mockMvc.perform(post("/api/x402/verify")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.get("isValid").asBoolean()).isFalse();
            assertThat(json.get("invalidReason").asText()).contains("Recipient mismatch");
        }

        @Test
        @DisplayName("Should return isValid=false for wrong amount")
        void verifyFailsWithWrongAmount() throws Exception {
            // Payload amount differs from requirements
            String body = """
                {
                    "x402Version": 1,
                    "paymentRequirements": {
                        "scheme": "exact",
                        "network": "solana-devnet",
                        "maxAmountRequired": "2000000",
                        "recipient": "%s"
                    },
                    "payment": {
                        "scheme": "exact",
                        "network": "solana-devnet",
                        "payer": "%s",
                        "recipient": "%s",
                        "amount": "1000000",
                        "nonce": %d,
                        "expiry": %d
                    }
                }
                """.formatted(VALID_RECIPIENT, VALID_PAYER, VALID_RECIPIENT,
                    System.currentTimeMillis(), Instant.now().getEpochSecond() + 3600);

            MvcResult result = mockMvc.perform(post("/api/x402/verify")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.get("isValid").asBoolean()).isFalse();
            assertThat(json.get("invalidReason").asText()).contains("Amount mismatch");
        }

        @Test
        @DisplayName("Should return isValid=false for unsupported scheme")
        void verifyFailsWithUnsupportedScheme() throws Exception {
            String body = """
                {
                    "x402Version": 1,
                    "paymentRequirements": {
                        "scheme": "streaming",
                        "network": "solana-devnet",
                        "maxAmountRequired": "%s",
                        "recipient": "%s"
                    },
                    "payment": {
                        "scheme": "streaming",
                        "network": "solana-devnet",
                        "payer": "%s",
                        "recipient": "%s",
                        "amount": "%s",
                        "nonce": %d,
                        "expiry": %d
                    }
                }
                """.formatted(VALID_AMOUNT, VALID_RECIPIENT, VALID_PAYER, VALID_RECIPIENT,
                    VALID_AMOUNT, System.currentTimeMillis(), Instant.now().getEpochSecond() + 3600);

            MvcResult result = mockMvc.perform(post("/api/x402/verify")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.get("isValid").asBoolean()).isFalse();
            assertThat(json.get("invalidReason").asText()).contains("Unsupported scheme");
        }

        @Test
        @DisplayName("Should return isValid=false for unknown network")
        void verifyFailsWithUnknownNetwork() throws Exception {
            String body = """
                {
                    "x402Version": 1,
                    "paymentRequirements": {
                        "scheme": "exact",
                        "network": "ethereum-mainnet",
                        "maxAmountRequired": "%s",
                        "recipient": "%s"
                    },
                    "payment": {
                        "scheme": "exact",
                        "network": "ethereum-mainnet",
                        "payer": "%s",
                        "recipient": "%s",
                        "amount": "%s",
                        "nonce": %d,
                        "expiry": %d
                    }
                }
                """.formatted(VALID_AMOUNT, VALID_RECIPIENT, VALID_PAYER, VALID_RECIPIENT,
                    VALID_AMOUNT, System.currentTimeMillis(), Instant.now().getEpochSecond() + 3600);

            MvcResult result = mockMvc.perform(post("/api/x402/verify")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.get("isValid").asBoolean()).isFalse();
            assertThat(json.get("invalidReason").asText()).contains("Unknown network");
        }

        @Test
        @DisplayName("Should return isValid=false for expired payment")
        void verifyFailsWithExpiredPayment() throws Exception {
            long expiredTimestamp = Instant.now().getEpochSecond() - 3600; // 1 hour ago

            String body = """
                {
                    "x402Version": 1,
                    "paymentRequirements": {
                        "scheme": "exact",
                        "network": "solana-devnet",
                        "maxAmountRequired": "%s",
                        "recipient": "%s"
                    },
                    "payment": {
                        "scheme": "exact",
                        "network": "solana-devnet",
                        "payer": "%s",
                        "recipient": "%s",
                        "amount": "%s",
                        "nonce": %d,
                        "expiry": %d
                    }
                }
                """.formatted(VALID_AMOUNT, VALID_RECIPIENT, VALID_PAYER, VALID_RECIPIENT,
                    VALID_AMOUNT, System.currentTimeMillis(), expiredTimestamp);

            MvcResult result = mockMvc.perform(post("/api/x402/verify")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.get("isValid").asBoolean()).isFalse();
            assertThat(json.get("invalidReason").asText()).contains("expired");
        }

        @Test
        @DisplayName("Should return isValid=false for replayed nonce")
        void verifyFailsWithReplayedNonce() throws Exception {
            long fixedNonce = 12345L;
            String body = """
                {
                    "x402Version": 1,
                    "paymentRequirements": {
                        "scheme": "exact",
                        "network": "solana-devnet",
                        "maxAmountRequired": "%s",
                        "recipient": "%s"
                    },
                    "payment": {
                        "scheme": "exact",
                        "network": "solana-devnet",
                        "payer": "%s",
                        "recipient": "%s",
                        "amount": "%s",
                        "nonce": %d,
                        "expiry": %d,
                        "signature": "test-signature"
                    }
                }
                """.formatted(VALID_AMOUNT, VALID_RECIPIENT, VALID_PAYER, VALID_RECIPIENT,
                    VALID_AMOUNT, fixedNonce, Instant.now().getEpochSecond() + 3600);

            // First request - verification happens but signature may fail (that's ok for this test)
            mockMvc.perform(post("/api/x402/verify")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk());

            // Settle with the same nonce to mark it as used
            mockMvc.perform(post("/api/x402/settle")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk());

            // Second request with same nonce should fail
            MvcResult result = mockMvc.perform(post("/api/x402/verify")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.get("isValid").asBoolean()).isFalse();
            assertThat(json.get("invalidReason").asText()).containsIgnoringCase("nonce");
        }
    }

    @Nested
    @DisplayName("POST /settle - Payment Settlement")
    class SettleEndpointTests {

        @Test
        @DisplayName("Should return 401 without API key")
        void settleWithoutApiKeyReturns401() throws Exception {
            String body = createSettleRequestJson();

            mockMvc.perform(post("/api/x402/settle")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.success").value(false))
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("Should return success=false for unknown network")
        void settleFailsWithUnknownNetwork() throws Exception {
            String body = """
                {
                    "x402Version": 1,
                    "paymentRequirements": {
                        "scheme": "exact",
                        "network": "unknown-network",
                        "maxAmountRequired": "%s",
                        "recipient": "%s"
                    },
                    "payment": {
                        "scheme": "exact",
                        "network": "unknown-network",
                        "payer": "%s",
                        "recipient": "%s",
                        "amount": "%s",
                        "nonce": %d
                    }
                }
                """.formatted(VALID_AMOUNT, VALID_RECIPIENT, VALID_PAYER, VALID_RECIPIENT,
                    VALID_AMOUNT, System.currentTimeMillis());

            MvcResult result = mockMvc.perform(post("/api/x402/settle")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.get("success").asBoolean()).isFalse();
            assertThat(json.get("error").asText()).contains("Unknown network");
        }

        @Test
        @DisplayName("Should include networkId in successful response")
        void settleIncludesNetworkIdInResponse() throws Exception {
            // Note: This test won't actually succeed because we don't have valid Solana credentials,
            // but we can verify the response structure on failure
            String body = createSettleRequestJson();

            MvcResult result = mockMvc.perform(post("/api/x402/settle")
                            .header("X-API-Key", testApiKey)
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(body))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            // Response will have either success with networkId or failure with error
            if (json.get("success").asBoolean()) {
                assertThat(json.has("txHash")).isTrue();
                assertThat(json.has("networkId")).isTrue();
            } else {
                assertThat(json.has("error")).isTrue();
            }
        }
    }

    @Nested
    @DisplayName("GET /transaction/{txHash} - Transaction Status")
    class TransactionEndpointTests {

        @Test
        @DisplayName("Should return 401 without API key")
        void transactionWithoutApiKeyReturns401() throws Exception {
            mockMvc.perform(get("/api/x402/transaction/test-tx-hash"))
                    .andExpect(status().isUnauthorized())
                    .andExpect(jsonPath("$.error").exists());
        }

        @Test
        @DisplayName("Should return transaction status with API key")
        void transactionReturnsStatus() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/x402/transaction/test-tx-hash")
                            .header("X-API-Key", testApiKey))
                    .andExpect(status().isOk())
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.has("txHash")).isTrue();
            assertThat(json.get("txHash").asText()).isEqualTo("test-tx-hash");
        }
    }

    @Nested
    @DisplayName("GET /health - Health Check")
    class HealthEndpointTests {

        @Test
        @DisplayName("Should return health status without auth")
        void healthReturnsStatusWithoutAuth() throws Exception {
            MvcResult result = mockMvc.perform(get("/api/x402/health"))
                    .andExpect(status().isOk())
                    .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                    .andReturn();

            JsonNode json = objectMapper.readTree(result.getResponse().getContentAsString());
            assertThat(json.get("status").asText()).isEqualTo("healthy");
            assertThat(json.get("protocol").asText()).isEqualTo("x402");
            assertThat(json.get("x402Version").asInt()).isEqualTo(1);
        }
    }

    // Helper methods

    private String createVerifyRequestJson() {
        return """
            {
                "x402Version": 1,
                "paymentRequirements": {
                    "scheme": "exact",
                    "network": "solana-devnet",
                    "maxAmountRequired": "%s",
                    "recipient": "%s"
                },
                "payment": {
                    "scheme": "exact",
                    "network": "solana-devnet",
                    "payer": "%s",
                    "recipient": "%s",
                    "amount": "%s",
                    "nonce": %d,
                    "expiry": %d
                }
            }
            """.formatted(VALID_AMOUNT, VALID_RECIPIENT, VALID_PAYER, VALID_RECIPIENT,
                VALID_AMOUNT, System.currentTimeMillis(), Instant.now().getEpochSecond() + 3600);
    }

    private String createVerifyRequestJsonWithRecipient(String recipient) {
        return """
            {
                "x402Version": 1,
                "paymentRequirements": {
                    "scheme": "exact",
                    "network": "solana-devnet",
                    "maxAmountRequired": "%s",
                    "recipient": "%s"
                },
                "payment": {
                    "scheme": "exact",
                    "network": "solana-devnet",
                    "payer": "%s",
                    "recipient": "%s",
                    "amount": "%s",
                    "nonce": %d,
                    "expiry": %d
                }
            }
            """.formatted(VALID_AMOUNT, VALID_RECIPIENT, VALID_PAYER, recipient,
                VALID_AMOUNT, System.currentTimeMillis(), Instant.now().getEpochSecond() + 3600);
    }

    private String createSettleRequestJson() {
        return """
            {
                "x402Version": 1,
                "paymentRequirements": {
                    "scheme": "exact",
                    "network": "solana-devnet",
                    "maxAmountRequired": "%s",
                    "recipient": "%s"
                },
                "payment": {
                    "scheme": "exact",
                    "network": "solana-devnet",
                    "payer": "%s",
                    "recipient": "%s",
                    "amount": "%s",
                    "nonce": %d
                }
            }
            """.formatted(VALID_AMOUNT, VALID_RECIPIENT, VALID_PAYER, VALID_RECIPIENT,
                VALID_AMOUNT, System.currentTimeMillis());
    }

    private PaymentPayload createValidPaymentPayload() {
        PaymentPayload payload = new PaymentPayload();
        payload.setScheme("exact");
        payload.setNetwork("solana-devnet");
        payload.setPayer(VALID_PAYER);
        payload.setRecipient(VALID_RECIPIENT);
        payload.setAmount(VALID_AMOUNT);
        payload.setNonce(System.currentTimeMillis() + "-" + java.util.UUID.randomUUID().toString().substring(0, 6));
        payload.setExpiry(Instant.now().getEpochSecond() + 3600);
        return payload;
    }

    private String encodePaymentHeader(PaymentPayload payload) throws Exception {
        String json = objectMapper.writeValueAsString(payload);
        return Base64.getEncoder().encodeToString(json.getBytes(StandardCharsets.UTF_8));
    }
}
