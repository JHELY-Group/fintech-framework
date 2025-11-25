package org.jhely.money.base.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bridge_customers", uniqueConstraints = {
        @UniqueConstraint(name = "uq_bridge_customer_user", columnNames = {"userId"}),
        @UniqueConstraint(name = "uq_bridge_customer_email", columnNames = {"email"})
})
public class BridgeCustomer {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Your app user identifier (e.g., from your Users table) */
    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String email;

    /** Bridge’s customer id */
    @Column(nullable = false)
    private String bridgeCustomerId;

    /** e.g., active, pending_kyc, disabled (free text from Bridge response) */
    @Column(nullable = false)
    private String status;

    /** Optional: we keep the full response for audit/debug */
    @Lob
    private String rawJson;

    @Column(nullable = false)
    private Instant createdAt;

    @Column(nullable = false)
    private Instant updatedAt;

    // --- KYC tracking fields ---
    /** KYC status reported by Bridge (e.g., pending, in_review, approved, rejected). */
    private String kycStatus;

    /** Timestamp of the last KYC status update from Bridge. */
    private Instant kycUpdatedAt;

    /** Optional reason/details when KYC is rejected or requires resubmission. */
    @Column(length = 1000)
    private String kycRejectionReason;

    /** Optional raw KYC-related payload for audit/debug. */
    @Lob
    private String kycDetailsJson;

    /* getters/setters */
    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getBridgeCustomerId() { return bridgeCustomerId; }
    public void setBridgeCustomerId(String bridgeCustomerId) { this.bridgeCustomerId = bridgeCustomerId; }
    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }
    public String getRawJson() { return rawJson; }
    public void setRawJson(String rawJson) { this.rawJson = rawJson; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
    public Instant getUpdatedAt() { return updatedAt; }
    public void setUpdatedAt(Instant updatedAt) { this.updatedAt = updatedAt; }

    public String getKycStatus() { return kycStatus; }
    public void setKycStatus(String kycStatus) { this.kycStatus = kycStatus; }
    public Instant getKycUpdatedAt() { return kycUpdatedAt; }
    public void setKycUpdatedAt(Instant kycUpdatedAt) { this.kycUpdatedAt = kycUpdatedAt; }
    public String getKycRejectionReason() { return kycRejectionReason; }
    public void setKycRejectionReason(String kycRejectionReason) { this.kycRejectionReason = kycRejectionReason; }
    public String getKycDetailsJson() { return kycDetailsJson; }
    public void setKycDetailsJson(String kycDetailsJson) { this.kycDetailsJson = kycDetailsJson; }
}

