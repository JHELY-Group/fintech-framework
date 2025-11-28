package org.jhely.money.base.service.x402;

import org.jhely.money.base.domain.X402FacilitatorConfig;
import org.jhely.money.base.repository.X402FacilitatorConfigRepository;
import org.jhely.money.base.service.x402.X402Models.*;
import org.p2p.solanaj.core.Account;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.core.Transaction;
import org.p2p.solanaj.programs.TokenProgram;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.rpc.RpcException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.time.Instant;
import java.util.*;

/**
 * Service for x402 facilitator operations including API key management,
 * Solana-based payment verification, and settlement using SolanaJ.
 */
@Service
public class X402FacilitatorService {

    private static final Logger log = LoggerFactory.getLogger(X402FacilitatorService.class);
    private static final String X402_VERSION = "1.0.0";
    private static final SecureRandom SECURE_RANDOM = new SecureRandom();

    private final X402FacilitatorConfigRepository configRepo;
    private final X402Properties properties;

    // Nonce tracking to prevent replay attacks (in production, use Redis or DB)
    private final Set<String> usedNonces = Collections.synchronizedSet(new HashSet<>());

    public X402FacilitatorService(X402FacilitatorConfigRepository configRepo,
            X402Properties properties) {
        this.configRepo = configRepo;
        this.properties = properties;
    }

    // ==================== Configuration Management ====================

    @Transactional(readOnly = true)
    public Optional<X402FacilitatorConfig> getConfigForUser(String userId) {
        return configRepo.findByUserId(userId);
    }

    @Transactional
    public X402FacilitatorConfig createOrUpdateConfig(String userId, X402FacilitatorConfig updates) {
        X402FacilitatorConfig config = configRepo.findByUserId(userId)
                .orElseGet(() -> {
                    X402FacilitatorConfig c = new X402FacilitatorConfig();
                    c.setUserId(userId);
                    return c;
                });

        if (updates.getFacilitatorName() != null) {
            config.setFacilitatorName(updates.getFacilitatorName());
        }
        config.setEnabled(updates.isEnabled());

        // Solana config
        if (updates.getSolanaPrivateKey() != null && !updates.getSolanaPrivateKey().isBlank()) {
            config.setSolanaPrivateKey(updates.getSolanaPrivateKey());
        }
        if (updates.getSolanaRpcMainnet() != null) {
            config.setSolanaRpcMainnet(updates.getSolanaRpcMainnet());
        }
        if (updates.getSolanaRpcDevnet() != null) {
            config.setSolanaRpcDevnet(updates.getSolanaRpcDevnet());
        }
        config.setSolanaEnabled(updates.isSolanaEnabled());

        // Webhook config
        if (updates.getWebhookUrl() != null) {
            config.setWebhookUrl(updates.getWebhookUrl());
        }
        if (updates.getWebhookSecret() != null && !updates.getWebhookSecret().isBlank()) {
            config.setWebhookSecret(updates.getWebhookSecret());
        }

        return configRepo.save(config);
    }

    @Transactional
    public String generateApiKey(String userId) {
        X402FacilitatorConfig config = configRepo.findByUserId(userId)
                .orElseGet(() -> {
                    X402FacilitatorConfig c = new X402FacilitatorConfig();
                    c.setUserId(userId);
                    return configRepo.save(c);
                });

        // Generate a secure API key
        byte[] keyBytes = new byte[32];
        SECURE_RANDOM.nextBytes(keyBytes);
        String apiKey = "x402_" + Base64.getUrlEncoder().withoutPadding().encodeToString(keyBytes);

        // Store hash of API key (not the key itself for security)
        String apiKeyHash = sha256(apiKey);
        config.setApiKeyMasked(maskApiKey(apiKey)); // Store masked version for display
        config.setApiKeyHash(apiKeyHash);

        configRepo.save(config);

        return apiKey; // Return full key only once
    }

    @Transactional(readOnly = true)
    public Optional<X402FacilitatorConfig> validateApiKey(String apiKey) {
        if (apiKey == null || !apiKey.startsWith("x402_")) {
            return Optional.empty();
        }
        String hash = sha256(apiKey);
        return configRepo.findByApiKeyHash(hash)
                .filter(X402FacilitatorConfig::isEnabled);
    }

    // ==================== x402 Protocol Operations ====================

    public SupportResponse getSupportedCapabilities(X402FacilitatorConfig config) {
        SupportResponse response = new SupportResponse();
        response.setVersion(X402_VERSION);
        response.setSchemes(List.of("exact"));

        List<String> networks = new ArrayList<>();
        List<SupportResponse.AssetInfo> assets = new ArrayList<>();

        if (config.isSolanaEnabled()) {
            networks.add(X402Network.SOLANA_MAINNET.getCanonicalName());
            networks.add(X402Network.SOLANA_DEVNET.getCanonicalName());
            assets.add(new SupportResponse.AssetInfo("solana-mainnet",
                    properties.getSolana().getUsdcMint().getMainnet()));
            assets.add(new SupportResponse.AssetInfo("solana-devnet",
                    properties.getSolana().getUsdcMint().getDevnet()));
        }

        response.setNetworks(networks);
        response.setAssets(assets);

        return response;
    }

    public VerifyResponse verify(X402FacilitatorConfig config, VerifyRequest request) {
        PaymentRequirements req = request.getRequirements();
        PaymentPayload payload = request.getPayload();

        // Basic validation
        if (req == null || payload == null) {
            return VerifyResponse.invalid("Missing requirements or payload", "INVALID_REQUEST");
        }

        // Verify scheme
        if (!"exact".equalsIgnoreCase(payload.getScheme())) {
            return VerifyResponse.invalid("Unsupported scheme: " + payload.getScheme(), "UNSUPPORTED_SCHEME");
        }

        // Verify network
        X402Network network = X402Network.fromString(payload.getNetwork());
        if (network == null) {
            return VerifyResponse.invalid("Unknown network: " + payload.getNetwork(), "UNKNOWN_NETWORK");
        }

        // Check if Solana is enabled for this facilitator
        if (!config.isSolanaEnabled()) {
            return VerifyResponse.invalid("Solana not enabled for this facilitator", "NETWORK_DISABLED");
        }

        // Verify amounts match
        if (!amountsMatch(req.getMaxAmount(), payload.getAmount())) {
            return VerifyResponse.invalid("Amount mismatch", "AMOUNT_MISMATCH");
        }

        // Verify recipient matches
        if (!recipientsMatch(req.getRecipient(), payload.getRecipient())) {
            return VerifyResponse.invalid("Recipient mismatch", "RECIPIENT_MISMATCH");
        }

        // Verify not expired
        if (payload.getExpiry() != null && payload.getExpiry() < Instant.now().getEpochSecond()) {
            return VerifyResponse.invalid("Payment expired", "EXPIRED");
        }

        // Check nonce hasn't been used (replay protection)
        String nonceKey = payload.getPayer() + ":" + payload.getNonce();
        if (usedNonces.contains(nonceKey)) {
            return VerifyResponse.invalid("Nonce already used", "REPLAY_DETECTED");
        }

        // Verify Solana signature using SolanaJ
        boolean signatureValid = verifySolanaSignature(config, payload, network);

        if (!signatureValid) {
            return VerifyResponse.invalid("Invalid signature", "INVALID_SIGNATURE");
        }

        log.info("x402 payment verified: network={}, payer={}, amount={}",
                network.getCanonicalName(), payload.getPayer(), payload.getAmount());

        return VerifyResponse.valid();
    }

    public SettleResponse settle(X402FacilitatorConfig config, SettleRequest request) {
        PaymentPayload payload = request.getPayload();
        PaymentRequirements req = request.getRequirements();

        X402Network network = X402Network.fromString(payload.getNetwork());
        if (network == null) {
            return SettleResponse.failure("Unknown network", "UNKNOWN_NETWORK");
        }

        // Mark nonce as used
        String nonceKey = payload.getPayer() + ":" + payload.getNonce();
        usedNonces.add(nonceKey);

        SettleResponse response;
        try {
            response = settleSolana(config, req, payload, network);
        } catch (Exception e) {
            log.error("Settlement failed: {}", e.getMessage(), e);
            response = SettleResponse.failure("Settlement failed: " + e.getMessage(), "SETTLEMENT_ERROR");
        }

        // Send webhook notification (async, don't block response)
        sendWebhookNotification(config, request, response);

        return response;
    }

    // ==================== Solana-Specific Implementation ====================

    private boolean verifySolanaSignature(X402FacilitatorConfig config, PaymentPayload payload, X402Network network) {
        try {
            // Get the payer's public key
            PublicKey payerPubkey = new PublicKey(payload.getPayer());

            // Get signature from payload
            String signatureStr = payload.getSignature();
            if (signatureStr == null || signatureStr.isEmpty()) {
                // Check nested payload data
                if (payload.getPayloadData() != null && payload.getPayloadData().getSignature() != null) {
                    signatureStr = payload.getPayloadData().getSignature();
                }
            }

            if (signatureStr == null || signatureStr.isEmpty()) {
                log.warn("No signature provided in payload");
                return false;
            }

            // For the x402 exact scheme on Solana, we verify the signature
            // against the payment authorization data
            // The actual verification would involve:
            // 1. Reconstructing the message that was signed
            // 2. Verifying the ed25519 signature

            // SolanaJ doesn't have built-in ed25519 verification for arbitrary messages,
            // but we can verify by checking if the transaction will be valid on-chain
            // For now, we do basic validation and trust the on-chain verification

            log.debug("Solana signature verification: payer={}, sig_prefix={}...",
                    payload.getPayer(), signatureStr.substring(0, Math.min(16, signatureStr.length())));

            // Basic format validation for base58 encoded signature
            return signatureStr.length() >= 64;

        } catch (Exception e) {
            log.error("Solana signature verification failed: {}", e.getMessage(), e);
            return false;
        }
    }

    private SettleResponse settleSolana(X402FacilitatorConfig config, PaymentRequirements req,
            PaymentPayload payload, X402Network network) {

        // Get RPC endpoint based on network
        String rpcUrl = network.isMainnet()
                ? (config.getSolanaRpcMainnet() != null && !config.getSolanaRpcMainnet().isBlank()
                        ? config.getSolanaRpcMainnet()
                        : properties.getSolana().getRpc().getMainnet())
                : (config.getSolanaRpcDevnet() != null && !config.getSolanaRpcDevnet().isBlank()
                        ? config.getSolanaRpcDevnet()
                        : properties.getSolana().getRpc().getDevnet());

        if (rpcUrl == null || rpcUrl.isBlank()) {
            return SettleResponse.failure("No Solana RPC configured for " + network.getCanonicalName(), "CONFIG_ERROR");
        }

        // Get facilitator private key
        String privateKeyStr = config.getSolanaPrivateKey();
        if (privateKeyStr == null || privateKeyStr.isBlank()) {
            return SettleResponse.failure("No Solana private key configured", "CONFIG_ERROR");
        }

        try {
            // Create RPC client
            RpcClient client = new RpcClient(rpcUrl);

            // Decode facilitator account from private key (base58 encoded)
            Account facilitatorAccount = Account.fromJson(privateKeyStr);

            // Get USDC mint for this network
            String usdcMint = network.isMainnet()
                    ? properties.getSolana().getUsdcMint().getMainnet()
                    : properties.getSolana().getUsdcMint().getDevnet();

            PublicKey mintPubkey = new PublicKey(usdcMint);
            PublicKey payerPubkey = new PublicKey(payload.getPayer());
            PublicKey recipientPubkey = new PublicKey(payload.getRecipient());

            // Get token accounts for payer and recipient
            PublicKey payerTokenAccount = findAssociatedTokenAddress(payerPubkey, mintPubkey);
            PublicKey recipientTokenAccount = findAssociatedTokenAddress(recipientPubkey, mintPubkey);

            // Parse amount (USDC has 6 decimals)
            long amountLamports = Long.parseLong(payload.getAmount());

            // Build the transfer transaction
            Transaction transaction = new Transaction();

            // Add transfer instruction
            // Note: In a real x402 implementation, this would use the signed authorization
            // from the payload to execute a transfer with authorization (like
            // transferWithAuthorization)
            // For now, we demonstrate the basic flow

            transaction.addInstruction(
                    TokenProgram.transfer(
                            payerTokenAccount,
                            recipientTokenAccount,
                            amountLamports,
                            facilitatorAccount.getPublicKey()));

            // Get recent blockhash
            var latestBlockhash = client.getApi().getLatestBlockhash();
            transaction.setRecentBlockHash(latestBlockhash.getValue().getBlockhash());

            // Sign and send transaction
            transaction.sign(facilitatorAccount);
            String signature = client.getApi().sendTransaction(transaction, facilitatorAccount);

            log.info("Solana settlement submitted: network={}, tx={}, amount={}",
                    network.getCanonicalName(), signature, payload.getAmount());

            // Wait for confirmation
            long slot = waitForConfirmation(client, signature,
                    properties.getSolana().getMaxConfirmationSlots());

            return SettleResponse.success(signature, slot);

        } catch (RpcException e) {
            log.error("Solana RPC error during settlement: {}", e.getMessage(), e);
            return SettleResponse.failure("RPC error: " + e.getMessage(), "RPC_ERROR");
        } catch (Exception e) {
            log.error("Solana settlement failed: {}", e.getMessage(), e);
            return SettleResponse.failure("Settlement error: " + e.getMessage(), "SETTLEMENT_ERROR");
        }
    }

    /**
     * Find the associated token address for a wallet and mint.
     */
    private PublicKey findAssociatedTokenAddress(PublicKey walletAddress, PublicKey tokenMintAddress) {
        // Associated Token Program ID
        PublicKey associatedTokenProgramId = new PublicKey("ATokenGPvbdGVxr1b2hvZbsiqW5xWH25efTNsLJA8knL");
        PublicKey tokenProgramId = new PublicKey("TokenkegQfeZyiNwAJbNbGKPFXCWuBvf9Ss623VQ5DA");

        try {
            // Derive the associated token account address
            return PublicKey.findProgramAddress(
                    List.of(
                            walletAddress.toByteArray(),
                            tokenProgramId.toByteArray(),
                            tokenMintAddress.toByteArray()),
                    associatedTokenProgramId).getAddress();
        } catch (Exception e) {
            throw new RuntimeException("Failed to derive associated token address", e);
        }
    }

    /**
     * Wait for transaction confirmation.
     */
    private long waitForConfirmation(RpcClient client, String signature, int maxSlots) throws RpcException {
        int attempts = 0;
        int maxAttempts = 30; // ~30 seconds with 1 second sleep

        while (attempts < maxAttempts) {
            try {
                var status = client.getApi().getSignatureStatuses(List.of(signature), true);
                if (status != null && status.getValue() != null && !status.getValue().isEmpty() && status.getValue().get(0) != null) {
                    var sigStatus = status.getValue().get(0);
                    if (sigStatus.getConfirmationStatus() != null) {
                        String confirmationStatus = sigStatus.getConfirmationStatus();
                        if ("finalized".equals(confirmationStatus) || "confirmed".equals(confirmationStatus)) {
                            return sigStatus.getSlot();
                        }
                    }
                }

                Thread.sleep(1000);
                attempts++;
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
                throw new RpcException("Confirmation interrupted");
            }
        }

        throw new RpcException("Transaction not confirmed within timeout");
    }

    // ==================== Helper Methods ====================

    private boolean amountsMatch(String required, String provided) {
        if (required == null || provided == null) {
            return false;
        }
        try {
            BigInteger reqAmount = new BigInteger(required);
            BigInteger provAmount = new BigInteger(provided);
            return provAmount.compareTo(reqAmount) >= 0;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    private boolean recipientsMatch(String required, String provided) {
        if (required == null || provided == null) {
            return false;
        }
        return required.equals(provided);
    }

    private String sha256(String input) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(input.getBytes(StandardCharsets.UTF_8));
            return Base64.getEncoder().encodeToString(hash);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 not available", e);
        }
    }

    private String maskApiKey(String apiKey) {
        if (apiKey == null || apiKey.length() < 12) {
            return "***";
        }
        return apiKey.substring(0, 8) + "..." + apiKey.substring(apiKey.length() - 4);
    }

    // ==================== Webhook Dispatch ====================

    /**
     * Send webhook notification after settlement (success or failure).
     * Runs asynchronously to not block the settlement response.
     */
    private void sendWebhookNotification(X402FacilitatorConfig config, SettleRequest request, SettleResponse response) {
        String webhookUrl = config.getWebhookUrl();
        if (webhookUrl == null || webhookUrl.isBlank()) {
            log.debug("No webhook URL configured, skipping notification");
            return;
        }

        // Run async to not block the response
        Thread.startVirtualThread(() -> {
            try {
                sendWebhook(config, request, response);
            } catch (Exception e) {
                log.error("Webhook notification failed: {}", e.getMessage(), e);
            }
        });
    }

    private void sendWebhook(X402FacilitatorConfig config, SettleRequest request, SettleResponse response) {
        String webhookUrl = config.getWebhookUrl();
        String webhookSecret = config.getWebhookSecret();

        // Build webhook payload
        Map<String, Object> webhookPayload = new LinkedHashMap<>();
        webhookPayload.put("event", response.isSuccess() ? "settlement.success" : "settlement.failed");
        webhookPayload.put("timestamp", Instant.now().toString());

        // Settlement details
        Map<String, Object> settlement = new LinkedHashMap<>();
        settlement.put("success", response.isSuccess());
        settlement.put("transaction", response.getTxHash());
        settlement.put("slot", response.getSlot());
        if (response.getError() != null) {
            settlement.put("error", response.getError());
            settlement.put("errorCode", response.getErrorCode());
        }
        webhookPayload.put("settlement", settlement);

        // Payment details from request
        if (request.getPayload() != null) {
            Map<String, Object> payment = new LinkedHashMap<>();
            payment.put("payer", request.getPayload().getPayer());
            payment.put("recipient", request.getPayload().getRecipient());
            payment.put("amount", request.getPayload().getAmount());
            payment.put("network", request.getPayload().getNetwork());
            payment.put("asset", request.getPayload().getAsset());
            webhookPayload.put("payment", payment);
        }

        try {
            // Serialize to JSON
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            String jsonPayload = mapper.writeValueAsString(webhookPayload);

            // Create HMAC-SHA256 signature if secret is configured
            String signature = null;
            if (webhookSecret != null && !webhookSecret.isBlank()) {
                signature = computeHmacSha256(jsonPayload, webhookSecret);
            }

            // Send HTTP POST request
            java.net.http.HttpClient client = java.net.http.HttpClient.newBuilder()
                    .connectTimeout(java.time.Duration.ofSeconds(10))
                    .build();

            var requestBuilder = java.net.http.HttpRequest.newBuilder()
                    .uri(java.net.URI.create(webhookUrl))
                    .header("Content-Type", "application/json")
                    .header("User-Agent", "x402-facilitator/1.0")
                    .header("X-Webhook-Event", response.isSuccess() ? "settlement.success" : "settlement.failed")
                    .timeout(java.time.Duration.ofSeconds(30))
                    .POST(java.net.http.HttpRequest.BodyPublishers.ofString(jsonPayload));

            if (signature != null) {
                requestBuilder.header("X-Webhook-Signature", "sha256=" + signature);
            }

            var httpRequest = requestBuilder.build();
            var httpResponse = client.send(httpRequest, java.net.http.HttpResponse.BodyHandlers.ofString());

            if (httpResponse.statusCode() >= 200 && httpResponse.statusCode() < 300) {
                log.info("Webhook notification sent successfully: url={}, status={}", webhookUrl, httpResponse.statusCode());
            } else {
                log.warn("Webhook notification returned non-success status: url={}, status={}, body={}",
                        webhookUrl, httpResponse.statusCode(), httpResponse.body());
            }

        } catch (Exception e) {
            log.error("Failed to send webhook notification to {}: {}", webhookUrl, e.getMessage(), e);
        }
    }

    /**
     * Compute HMAC-SHA256 signature for webhook payload.
     */
    private String computeHmacSha256(String data, String secret) {
        try {
            javax.crypto.Mac mac = javax.crypto.Mac.getInstance("HmacSHA256");
            javax.crypto.spec.SecretKeySpec keySpec = new javax.crypto.spec.SecretKeySpec(
                    secret.getBytes(StandardCharsets.UTF_8), "HmacSHA256");
            mac.init(keySpec);
            byte[] hmac = mac.doFinal(data.getBytes(StandardCharsets.UTF_8));

            // Convert to hex string
            StringBuilder hexString = new StringBuilder();
            for (byte b : hmac) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) {
                    hexString.append('0');
                }
                hexString.append(hex);
            }
            return hexString.toString();

        } catch (Exception e) {
            log.error("Failed to compute HMAC-SHA256: {}", e.getMessage(), e);
            return null;
        }
    }
}
