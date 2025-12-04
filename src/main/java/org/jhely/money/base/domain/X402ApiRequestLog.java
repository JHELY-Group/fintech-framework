package org.jhely.money.base.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

/**
 * Logs all API requests made to the x402 facilitator endpoints.
 * Tracks request type, response, financial impact, and transaction details.
 */
@Entity
@Table(name = "x402_api_request_log", indexes = {
        @Index(name = "idx_x402_log_config", columnList = "config_id"),
        @Index(name = "idx_x402_log_timestamp", columnList = "timestamp"),
        @Index(name = "idx_x402_log_endpoint", columnList = "endpoint"),
        @Index(name = "idx_x402_log_tx_hash", columnList = "tx_hash")
})
public class X402ApiRequestLog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "config_id", nullable = false)
    private X402FacilitatorConfig config;

    @Column(name = "endpoint", nullable = false, length = 64)
    private String endpoint;

    @Column(name = "method", nullable = false, length = 10)
    private String method;

    @Column(name = "timestamp", nullable = false)
    private Instant timestamp;

    @Column(name = "response_status", nullable = false)
    private int responseStatus;

    @Column(name = "success", nullable = false)
    private boolean success;

    @Column(name = "error_message", length = 512)
    private String errorMessage;

    // For verify/settle requests - payment details
    @Column(name = "network", length = 64)
    private String network;

    @Column(name = "scheme", length = 32)
    private String scheme;

    @Column(name = "amount", precision = 38, scale = 18)
    private BigDecimal amount;

    @Column(name = "asset", length = 128)
    private String asset;

    @Column(name = "recipient", length = 128)
    private String recipient;

    @Column(name = "payer", length = 128)
    private String payer;

    // For settle requests - transaction details
    @Column(name = "tx_hash", length = 128)
    private String txHash;

    @Column(name = "slot")
    private Long slot;

    // Request metadata
    @Column(name = "client_ip", length = 64)
    private String clientIp;

    @Column(name = "user_agent", length = 512)
    private String userAgent;

    @Column(name = "duration_ms")
    private Long durationMs;

    @PrePersist
    protected void onCreate() {
        if (timestamp == null) {
            timestamp = Instant.now();
        }
    }

    // Getters and setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public X402FacilitatorConfig getConfig() {
        return config;
    }

    public void setConfig(X402FacilitatorConfig config) {
        this.config = config;
    }

    public String getEndpoint() {
        return endpoint;
    }

    public void setEndpoint(String endpoint) {
        this.endpoint = endpoint;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public Instant getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Instant timestamp) {
        this.timestamp = timestamp;
    }

    public int getResponseStatus() {
        return responseStatus;
    }

    public void setResponseStatus(int responseStatus) {
        this.responseStatus = responseStatus;
    }

    public boolean isSuccess() {
        return success;
    }

    public void setSuccess(boolean success) {
        this.success = success;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getNetwork() {
        return network;
    }

    public void setNetwork(String network) {
        this.network = network;
    }

    public String getScheme() {
        return scheme;
    }

    public void setScheme(String scheme) {
        this.scheme = scheme;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public String getAsset() {
        return asset;
    }

    public void setAsset(String asset) {
        this.asset = asset;
    }

    public String getRecipient() {
        return recipient;
    }

    public void setRecipient(String recipient) {
        this.recipient = recipient;
    }

    public String getPayer() {
        return payer;
    }

    public void setPayer(String payer) {
        this.payer = payer;
    }

    public String getTxHash() {
        return txHash;
    }

    public void setTxHash(String txHash) {
        this.txHash = txHash;
    }

    public Long getSlot() {
        return slot;
    }

    public void setSlot(Long slot) {
        this.slot = slot;
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
    }

    public String getUserAgent() {
        return userAgent;
    }

    public void setUserAgent(String userAgent) {
        this.userAgent = userAgent;
    }

    public Long getDurationMs() {
        return durationMs;
    }

    public void setDurationMs(Long durationMs) {
        this.durationMs = durationMs;
    }
}
