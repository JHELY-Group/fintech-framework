package org.jhely.money.base.domain;

import java.time.Instant;

import jakarta.persistence.Basic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "receipt_files", indexes = { @Index(name = "idx_receipt_owner", columnList = "ownerEmail"),
		@Index(name = "idx_receipt_uploaded", columnList = "uploadedAt") })
public class ReceiptFile {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false, length = 320)
	private String ownerEmail;

	@Column(nullable = false)
	private String filename;

	@Column(nullable = false)
	private String contentType;

	@Column(nullable = false)
	private long sizeBytes;

	@Column(nullable = false)
	private Instant uploadedAt = Instant.now();

	@Column(length = 64)
	private String sha256; // optional, useful for dedupe

	@Lob
	@Basic(fetch = FetchType.LAZY)
	@Column(columnDefinition = "LONGBLOB", nullable = false)
	private byte[] data;

// getters/setters
	public Long getId() {
		return id;
	}

	public String getOwnerEmail() {
		return ownerEmail;
	}

	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}

	public String getFilename() {
		return filename;
	}

	public void setFilename(String filename) {
		this.filename = filename;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public long getSizeBytes() {
		return sizeBytes;
	}

	public void setSizeBytes(long sizeBytes) {
		this.sizeBytes = sizeBytes;
	}

	public Instant getUploadedAt() {
		return uploadedAt;
	}

	public void setUploadedAt(Instant uploadedAt) {
		this.uploadedAt = uploadedAt;
	}

	public String getSha256() {
		return sha256;
	}

	public void setSha256(String sha256) {
		this.sha256 = sha256;
	}

	public byte[] getData() {
		return data;
	}

	public void setData(byte[] data) {
		this.data = data;
	}
}
