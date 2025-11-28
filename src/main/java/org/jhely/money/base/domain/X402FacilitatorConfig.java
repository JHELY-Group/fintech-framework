package org.jhely.money.base.domain;

import jakarta.persistence.*;
import java.time.Instant;

/**
 * Stores user-specific x402 facilitator configuration and API keys.
 * Each user can configure their own facilitator endpoints and credentials
 * for Solana-based x402 payments.
 */
@Entity
@Table(name = "x402_facilitator_config",
       uniqueConstraints = @UniqueConstraint(columnNames = {"user_id"}))
public class X402FacilitatorConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "user_id", nullable = false)
    private String userId;

    @Column(name = "api_key_masked", length = 64)
    private String apiKeyMasked;

    @Column(name = "api_key_hash", length = 64)
    private String apiKeyHash;

    @Column(name = "facilitator_name")
    private String facilitatorName;

    @Column(name = "enabled", nullable = false)
    private boolean enabled = false;

    // Solana configuration
    @Column(name = "solana_private_key", length = 512)
    private String solanaPrivateKey;

    @Column(name = "solana_rpc_mainnet", length = 512)
    private String solanaRpcMainnet;

    @Column(name = "solana_rpc_devnet", length = 512)
    private String solanaRpcDevnet;

    @Column(name = "solana_enabled", nullable = false)
    private boolean solanaEnabled = false;

    // Webhook configuration
    @Column(name = "webhook_url", length = 512)
    private String webhookUrl;

    @Column(name = "webhook_secret", length = 256)
    private String webhookSecret;

    @Column(name = "created_at", nullable = false)
    private Instant createdAt;

    @Column(name = "updated_at")
    private Instant updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = Instant.now();
        updatedAt = createdAt;
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = Instant.now();
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getApiKeyMasked() {
        return apiKeyMasked;
    }

    public void setApiKeyMasked(String apiKeyMasked) {
        this.apiKeyMasked = apiKeyMasked;
    }

    public String getApiKeyHash() {
        return apiKeyHash;
    }

    public void setApiKeyHash(String apiKeyHash) {
        this.apiKeyHash = apiKeyHash;
    }

    public String getFacilitatorName() {
        return facilitatorName;
    }

    public void setFacilitatorName(String facilitatorName) {
        this.facilitatorName = facilitatorName;
    }

    public boolean isEnabled() {
        return enabled;
    }

    public void setEnabled(boolean enabled) {
        this.enabled = enabled;
    }

    public String getSolanaPrivateKey() {
        return solanaPrivateKey;
    }

    public void setSolanaPrivateKey(String solanaPrivateKey) {
        this.solanaPrivateKey = solanaPrivateKey;
    }

    public String getSolanaRpcMainnet() {
        return solanaRpcMainnet;
    }

    public void setSolanaRpcMainnet(String solanaRpcMainnet) {
        this.solanaRpcMainnet = solanaRpcMainnet;
    }

    public String getSolanaRpcDevnet() {
        return solanaRpcDevnet;
    }

    public void setSolanaRpcDevnet(String solanaRpcDevnet) {
        this.solanaRpcDevnet = solanaRpcDevnet;
    }

    public boolean isSolanaEnabled() {
        return solanaEnabled;
    }

    public void setSolanaEnabled(boolean solanaEnabled) {
        this.solanaEnabled = solanaEnabled;
    }

    public String getWebhookUrl() {
        return webhookUrl;
    }

    public void setWebhookUrl(String webhookUrl) {
        this.webhookUrl = webhookUrl;
    }

    public String getWebhookSecret() {
        return webhookSecret;
    }

    public void setWebhookSecret(String webhookSecret) {
        this.webhookSecret = webhookSecret;
    }

    public Instant getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Instant createdAt) {
        this.createdAt = createdAt;
    }

    public Instant getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(Instant updatedAt) {
        this.updatedAt = updatedAt;
    }
}
