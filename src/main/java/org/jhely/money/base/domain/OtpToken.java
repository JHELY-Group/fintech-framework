package org.jhely.money.base.domain;

import java.time.Instant;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Table;

@Entity
@Table(name = "otp_tokens", indexes = { @Index(name = "idx_otp_email", columnList = "email"),
		@Index(name = "idx_otp_expires", columnList = "expiresAt") })
public class OtpToken {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String email;

	@Column(nullable = false)
	private String codeHash; // store hashed OTP

	@Column(nullable = false)
	private Instant createdAt = Instant.now();

	@Column(nullable = false)
	private Instant expiresAt;

	@Column(nullable = false)
	private boolean used = false;

// getters/setters
	public Long getId() {
		return id;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getCodeHash() {
		return codeHash;
	}

	public void setCodeHash(String codeHash) {
		this.codeHash = codeHash;
	}

	public Instant getCreatedAt() {
		return createdAt;
	}

	public void setCreatedAt(Instant createdAt) {
		this.createdAt = createdAt;
	}

	public Instant getExpiresAt() {
		return expiresAt;
	}

	public void setExpiresAt(Instant expiresAt) {
		this.expiresAt = expiresAt;
	}

	public boolean isUsed() {
		return used;
	}

	public void setUsed(boolean used) {
		this.used = used;
	}
}