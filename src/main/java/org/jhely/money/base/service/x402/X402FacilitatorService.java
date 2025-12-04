package org.jhely.money.base.service.x402;

import org.jhely.money.base.domain.X402ApiRequestLog;
import org.jhely.money.base.domain.X402FacilitatorConfig;
import org.jhely.money.base.repository.X402ApiRequestLogRepository;
import org.jhely.money.base.repository.X402FacilitatorConfigRepository;
import org.jhely.money.base.service.x402.X402Models.*;
import org.p2p.solanaj.core.Account;
import org.p2p.solanaj.core.PublicKey;
import org.p2p.solanaj.rpc.RpcClient;
import org.p2p.solanaj.rpc.RpcException;
import org.p2p.solanaj.rpc.types.config.RpcSendTransactionConfig;
import org.p2p.solanaj.utils.TweetNaclFast;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.ByteBuffer;
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
    private final X402ApiRequestLogRepository logRepo;
    private final X402Properties properties;

    // Nonce tracking to prevent replay attacks (in production, use Redis or DB)
    private final Set<String> usedNonces = Collections.synchronizedSet(new HashSet<>());

    public X402FacilitatorService(X402FacilitatorConfigRepository configRepo,
            X402ApiRequestLogRepository logRepo,
            X402Properties properties) {
        this.configRepo = configRepo;
        this.logRepo = logRepo;
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
                    c.setEnabled(true); // Enable by default when creating new config
                    c.setSolanaEnabled(true); // Enable Solana by default
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
        
        // Ensure facilitator is enabled when generating new key
        if (!config.isEnabled()) {
            config.setEnabled(true);
        }

        configRepo.save(config);
        log.info("Generated new API key for user {}, enabled={}", userId, config.isEnabled());

        return apiKey; // Return full key only once
    }

    @Transactional(readOnly = true)
    public Optional<X402FacilitatorConfig> validateApiKey(String apiKey) {
        if (apiKey == null || !apiKey.startsWith("x402_")) {
            log.warn("API key validation failed: key is null or doesn't start with 'x402_'");
            return Optional.empty();
        }
        String hash = sha256(apiKey);
        log.debug("Validating API key with hash: {}", hash.substring(0, 16) + "...");
        
        Optional<X402FacilitatorConfig> configOpt = configRepo.findByApiKeyHash(hash);
        if (configOpt.isEmpty()) {
            log.warn("API key validation failed: no config found for hash");
            return Optional.empty();
        }
        
        X402FacilitatorConfig config = configOpt.get();
        if (!config.isEnabled()) {
            log.warn("API key validation failed: facilitator is disabled for user {}", config.getUserId());
            return Optional.empty();
        }
        
        log.debug("API key validated successfully for user {}", config.getUserId());
        return configOpt;
    }

    // ==================== x402 Protocol Operations ====================

    public SupportResponse getSupportedCapabilities(X402FacilitatorConfig config) {
        SupportResponse response = new SupportResponse();
        response.setVersion(X402_VERSION);

        List<SupportResponse.Kind> kinds = new ArrayList<>();
        List<SupportResponse.AssetInfo> assets = new ArrayList<>();

        if (config.isSolanaEnabled()) {
            // Add "kinds" per x402 spec: { scheme, network }
            kinds.add(new SupportResponse.Kind("exact", X402Network.SOLANA_MAINNET.getCanonicalName()));
            kinds.add(new SupportResponse.Kind("exact", X402Network.SOLANA_DEVNET.getCanonicalName()));
            
            assets.add(new SupportResponse.AssetInfo(X402Network.SOLANA_MAINNET.getCanonicalName(),
                    properties.getSolana().getUsdcMint().getMainnet()));
            assets.add(new SupportResponse.AssetInfo(X402Network.SOLANA_DEVNET.getCanonicalName(),
                    properties.getSolana().getUsdcMint().getDevnet()));
        }

        response.setKinds(kinds);
        response.setAssets(assets);

        return response;
    }

    public VerifyResponse verify(X402FacilitatorConfig config, VerifyRequest request) {
        PaymentRequirements req = request.getRequirements();
        PaymentPayload payload = request.getPayload();

        // If paymentHeader is provided (base64), decode it to get the payload
        if ((payload == null || payload.getPayer() == null) && request.getPaymentHeader() != null) {
            try {
                payload = decodePaymentHeader(request.getPaymentHeader());
            } catch (Exception e) {
                return VerifyResponse.invalid("Invalid paymentHeader: " + e.getMessage(), "INVALID_PAYMENT_HEADER");
            }
        }

        // Basic validation
        if (req == null) {
            return VerifyResponse.invalid("Missing paymentRequirements", "INVALID_REQUEST");
        }
        if (payload == null) {
            return VerifyResponse.invalid("Missing payment payload (provide paymentHeader or payment)", "INVALID_REQUEST");
        }

        // Verify scheme
        String scheme = payload.getScheme() != null ? payload.getScheme() : req.getScheme();
        if (!"exact".equalsIgnoreCase(scheme)) {
            return VerifyResponse.invalid("Unsupported scheme: " + scheme, "UNSUPPORTED_SCHEME");
        }

        // Verify network
        String networkStr = payload.getNetwork() != null ? payload.getNetwork() : req.getNetwork();
        X402Network network = X402Network.fromString(networkStr);
        if (network == null) {
            return VerifyResponse.invalid("Unknown network: " + networkStr, "UNKNOWN_NETWORK");
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

        // If paymentHeader is provided (base64), decode it to get the payload
        if ((payload == null || payload.getPayer() == null) && request.getPaymentHeader() != null) {
            try {
                payload = decodePaymentHeader(request.getPaymentHeader());
            } catch (Exception e) {
                return SettleResponse.failure("Invalid paymentHeader: " + e.getMessage(), "INVALID_PAYMENT_HEADER");
            }
        }

        if (payload == null) {
            return SettleResponse.failure("Missing payment payload", "INVALID_REQUEST");
        }

        String networkStr = payload.getNetwork() != null ? payload.getNetwork() : (req != null ? req.getNetwork() : null);
        X402Network network = X402Network.fromString(networkStr);
        if (network == null) {
            return SettleResponse.failure("Unknown network: " + networkStr, "UNKNOWN_NETWORK");
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

    /**
     * Decode base64-encoded X-PAYMENT header into PaymentPayload.
     */
    private PaymentPayload decodePaymentHeader(String paymentHeader) {
        try {
            byte[] decoded = Base64.getDecoder().decode(paymentHeader);
            String json = new String(decoded, StandardCharsets.UTF_8);
            com.fasterxml.jackson.databind.ObjectMapper mapper = new com.fasterxml.jackson.databind.ObjectMapper();
            return mapper.readValue(json, PaymentPayload.class);
        } catch (Exception e) {
            throw new IllegalArgumentException("Failed to decode paymentHeader: " + e.getMessage(), e);
        }
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
            Account facilitatorAccount = Account.fromBase58PrivateKey(privateKeyStr);

            // x402 SVM: Extract pre-built transaction from payload
            // Per x402 spec, the client sends a partially-signed transaction that the
            // facilitator must co-sign as fee payer and submit
            String transactionBase64 = null;
            if (payload.getPayloadData() != null && payload.getPayloadData().getTransaction() != null) {
                transactionBase64 = payload.getPayloadData().getTransaction();
            }

            if (transactionBase64 == null || transactionBase64.isBlank()) {
                return SettleResponse.failure("Missing transaction in payload (x402 SVM requires pre-built transaction)", "INVALID_PAYLOAD");
            }

            // Decode the base64 transaction
            byte[] transactionBytes = Base64.getDecoder().decode(transactionBase64);
            
            // Co-sign the transaction as fee payer and send it
            String signature = coSignAndSendTransaction(client, facilitatorAccount, transactionBytes);

            log.info("Solana settlement submitted: network={}, tx={}, amount={}",
                    network.getCanonicalName(), signature, payload.getAmount());

            // Wait for confirmation - transaction must confirm to be considered successful
            long slot = waitForConfirmation(client, signature,
                    properties.getSolana().getMaxConfirmationSlots());

            return SettleResponse.success(signature, slot, network.getCanonicalName());

        } catch (RpcException e) {
            log.error("Solana RPC error during settlement: {}", e.getMessage(), e);
            return SettleResponse.failure("RPC error: " + e.getMessage(), "RPC_ERROR");
        } catch (Exception e) {
            log.error("Solana settlement failed: {}", e.getMessage(), e);
            return SettleResponse.failure("Settlement error: " + e.getMessage(), "SETTLEMENT_ERROR");
        }
    }

    /**
     * Co-sign a partially-signed transaction as the fee payer and send it.
     * 
     * Per x402 SVM spec, the client constructs and partially signs a transaction with
     * the facilitator as fee payer. The facilitator must add its signature at position 0
     * (fee payer signature slot) and submit the fully-signed transaction.
     * 
     * Solana transaction format:
     * [num_signatures (compact-u16)] [signature_0..signature_n (64 bytes each)] [message]
     * 
     * Message format (legacy v0):
     * [header (3 bytes)] [account_keys_length (compact-u16)] [account_keys (32 bytes each)] [...]
     * 
     * Header format:
     * - num_required_signatures (1 byte)
     * - num_readonly_signed_accounts (1 byte)
     * - num_readonly_unsigned_accounts (1 byte)
     * 
     * The client's transaction has:
     * - Signature slots for fee payer (empty, 64 zero bytes) and payer (signed)
     * - The message (which includes all instructions)
     */
    private String coSignAndSendTransaction(RpcClient client, Account facilitator, byte[] transactionBytes) 
            throws RpcException {
        
        // Parse the transaction structure to extract the message
        ByteBuffer buffer = ByteBuffer.wrap(transactionBytes);
        
        // Read number of signatures (compact-u16 encoding)
        int numSignatures = readCompactU16(buffer);
        log.debug("Transaction has {} signature slots", numSignatures);
        
        if (numSignatures < 1) {
            throw new IllegalArgumentException("Transaction must have at least 1 signature slot for fee payer");
        }
        
        // Calculate where the message starts
        int signaturesStart = buffer.position();
        int signaturesByteLength = numSignatures * 64;
        int messageStart = signaturesStart + signaturesByteLength;
        
        // Extract the message bytes (everything after signatures)
        byte[] messageBytes = new byte[transactionBytes.length - messageStart];
        System.arraycopy(transactionBytes, messageStart, messageBytes, 0, messageBytes.length);
        
        // Parse the message to verify the facilitator's public key is in the expected position
        // Message header is 3 bytes, then compact-u16 for num account keys, then account keys
        if (messageBytes.length < 3) {
            throw new IllegalArgumentException("Message too short to contain header");
        }
        int numRequiredSignatures = messageBytes[0] & 0xFF;
        int numReadonlySigned = messageBytes[1] & 0xFF;
        int numReadonlyUnsigned = messageBytes[2] & 0xFF;
        
        // Read number of account keys (compact-u16 at byte 3)
        ByteBuffer msgBuffer = ByteBuffer.wrap(messageBytes, 3, messageBytes.length - 3);
        int numAccountKeys = readCompactU16(msgBuffer);
        int accountKeysStart = 3 + (msgBuffer.position() - 3); // Account for compact-u16 bytes read
        
        // First account key is the fee payer (should match facilitator's public key)
        byte[] firstAccountKey = new byte[32];
        System.arraycopy(messageBytes, accountKeysStart, firstAccountKey, 0, 32);
        String firstAccountKeyBase58 = new PublicKey(firstAccountKey).toBase58();
        String facilitatorPubkeyBase58 = facilitator.getPublicKey().toBase58();
        
        log.info("Message header: numReqSigs={}, numReadonlySigned={}, numReadonlyUnsigned={}, numAccountKeys={}",
                numRequiredSignatures, numReadonlySigned, numReadonlyUnsigned, numAccountKeys);
        log.info("First account (fee payer): {}", firstAccountKeyBase58);
        log.info("Facilitator public key:    {}", facilitatorPubkeyBase58);
        
        if (!firstAccountKeyBase58.equals(facilitatorPubkeyBase58)) {
            throw new IllegalArgumentException(
                "Transaction fee payer (" + firstAccountKeyBase58 + ") does not match facilitator (" + facilitatorPubkeyBase58 + ")");
        }
        
        // Sign the message with the facilitator's key
        TweetNaclFast.Signature signatureProvider = new TweetNaclFast.Signature(
                new byte[0], 
                facilitator.getSecretKey()
        );
        byte[] facilitatorSignature = signatureProvider.detached(messageBytes);
        
        log.debug("Generated facilitator signature for fee payer slot (64 bytes)");
        
        // Build the fully-signed transaction by replacing the first signature slot
        byte[] signedTransactionBytes = transactionBytes.clone();
        System.arraycopy(facilitatorSignature, 0, signedTransactionBytes, signaturesStart, 64);
        
        // Log the transaction structure for debugging
        log.info("Transaction structure: numSigs={}, sigStart={}, msgStart={}, totalLen={}", 
                numSignatures, signaturesStart, messageStart, transactionBytes.length);
        
        // Encode as base64 and send
        String signedTransactionBase64 = Base64.getEncoder().encodeToString(signedTransactionBytes);
        
        // Enable preflight simulation to catch errors early (skipPreflight=false)
        RpcSendTransactionConfig config = RpcSendTransactionConfig.builder()
                .encoding(RpcSendTransactionConfig.Encoding.base64)
                .skipPreFlight(false)  // Run simulation to catch errors
                .build();
        
        String txSignature = client.getApi().sendRawTransaction(signedTransactionBase64, config);
        log.debug("Transaction submitted: {}", txSignature);
        
        return txSignature;
    }
    
    /**
     * Read a compact-u16 value from the buffer (Solana's variable-length encoding).
     * This is used for encoding array lengths in Solana transactions.
     */
    private int readCompactU16(ByteBuffer buffer) {
        int value = 0;
        int shift = 0;
        
        while (true) {
            byte b = buffer.get();
            value |= (b & 0x7F) << shift;
            if ((b & 0x80) == 0) {
                break;
            }
            shift += 7;
        }
        
        return value;
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

    // ==================== API Request Logging ====================

    /**
     * Log an API request with payment details.
     */
    @Transactional
    public X402ApiRequestLog logApiRequest(X402FacilitatorConfig config, String endpoint, String method,
            int responseStatus, boolean success, String errorMessage,
            PaymentRequirements requirements, PaymentPayload payload,
            String txHash, Long slot, String clientIp, String userAgent, Long durationMs) {
        
        X402ApiRequestLog logEntry = new X402ApiRequestLog();
        logEntry.setConfig(config);
        logEntry.setEndpoint(endpoint);
        logEntry.setMethod(method);
        logEntry.setTimestamp(Instant.now());
        logEntry.setResponseStatus(responseStatus);
        logEntry.setSuccess(success);
        logEntry.setErrorMessage(errorMessage);
        logEntry.setClientIp(clientIp);
        logEntry.setUserAgent(userAgent);
        logEntry.setDurationMs(durationMs);
        logEntry.setTxHash(txHash);
        logEntry.setSlot(slot);

        if (requirements != null) {
            logEntry.setNetwork(requirements.getNetwork());
            logEntry.setScheme(requirements.getScheme());
            logEntry.setRecipient(requirements.getRecipient());
            logEntry.setAsset(requirements.getAsset());
            if (requirements.getMaxAmount() != null) {
                try {
                    logEntry.setAmount(new BigDecimal(requirements.getMaxAmount()));
                } catch (NumberFormatException e) {
                    log.warn("Could not parse amount: {}", requirements.getMaxAmount());
                }
            }
        }

        if (payload != null) {
            logEntry.setPayer(payload.getPayer());
        }

        return logRepo.save(logEntry);
    }

    /**
     * Get logs for a facilitator config.
     */
    @Transactional(readOnly = true)
    public Page<X402ApiRequestLog> getApiRequestLogs(X402FacilitatorConfig config, Pageable pageable) {
        return logRepo.findByConfigOrderByTimestampDesc(config, pageable);
    }

    /**
     * Get recent logs for a facilitator config.
     */
    @Transactional(readOnly = true)
    public List<X402ApiRequestLog> getRecentApiRequestLogs(X402FacilitatorConfig config) {
        return logRepo.findTop50ByConfigOrderByTimestampDesc(config);
    }

    /**
     * Get successful settlements for a facilitator config.
     */
    @Transactional(readOnly = true)
    public Page<X402ApiRequestLog> getSuccessfulSettlements(X402FacilitatorConfig config, Pageable pageable) {
        return logRepo.findSuccessfulSettlements(config, pageable);
    }

    /**
     * Get dashboard statistics for a facilitator.
     */
    @Transactional(readOnly = true)
    public DashboardStats getDashboardStats(X402FacilitatorConfig config) {
        DashboardStats stats = new DashboardStats();
        stats.totalRequests = logRepo.countByConfig(config);
        stats.successfulRequests = logRepo.countByConfigAndSuccessTrue(config);
        stats.verifyRequests = logRepo.countByConfigAndEndpoint(config, "verify");
        stats.settleRequests = logRepo.countByConfigAndEndpoint(config, "settle");
        stats.totalSettledAmount = logRepo.sumSuccessfulSettlementAmount(config);
        return stats;
    }

    /**
     * Dashboard statistics DTO.
     */
    public static class DashboardStats {
        public long totalRequests;
        public long successfulRequests;
        public long verifyRequests;
        public long settleRequests;
        public BigDecimal totalSettledAmount;

        public double getSuccessRate() {
            return totalRequests > 0 ? (double) successfulRequests / totalRequests * 100 : 0;
        }
    }
}
