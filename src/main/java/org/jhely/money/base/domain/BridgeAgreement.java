package org.jhely.money.base.domain;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "bridge_agreements", uniqueConstraints = {
        @UniqueConstraint(name = "uq_bridge_agreement_user", columnNames = {"userId"}),
        @UniqueConstraint(name = "uq_bridge_agreement_email", columnNames = {"email"})
})
public class BridgeAgreement {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String userId;

    @Column(nullable = false)
    private String email;

    @Column(nullable = false)
    private String agreementId; // Bridge signed agreement identifier

    @Column(nullable = false)
    private Instant createdAt;

    public Long getId() { return id; }
    public String getUserId() { return userId; }
    public void setUserId(String userId) { this.userId = userId; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getAgreementId() { return agreementId; }
    public void setAgreementId(String agreementId) { this.agreementId = agreementId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
