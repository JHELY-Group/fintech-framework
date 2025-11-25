package org.jhely.money.base.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bridge_agreement_logs", indexes = {
        @Index(name = "idx_ba_log_user", columnList = "userId"),
        @Index(name = "idx_ba_log_email", columnList = "email")
})
public class BridgeAgreementLog {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String agreementId;

    @Column(nullable = false)
    private Instant acceptedAt;

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAgreementId() { return agreementId; }
    public void setAgreementId(String agreementId) { this.agreementId = agreementId; }
    public Instant getAcceptedAt() { return acceptedAt; }
    public void setAcceptedAt(Instant acceptedAt) { this.acceptedAt = acceptedAt; }
}
