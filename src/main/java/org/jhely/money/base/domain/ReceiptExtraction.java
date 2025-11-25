package org.jhely.money.base.domain;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Index;
import jakarta.persistence.Lob;
import jakarta.persistence.Table;

@Entity
@Table(name = "receipt_extractions", indexes = { @Index(name = "idx_owner_date", columnList = "ownerEmail, issueDate"),
		@Index(name = "idx_owner_supplier", columnList = "ownerEmail, supplierName") })
public class ReceiptExtraction {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@Column(nullable = false)
	Long receiptFileId;
	@Column(nullable = false, length = 320)
	String ownerEmail;

// denormalized searchable columns
	String supplierName;
	String supplierTaxId;
	String invoiceNumber;
	java.time.LocalDate issueDate;
	String currency;
	java.math.BigDecimal total;

// raw JSON with everything (header, taxes, lines, meta, confidence)
	@Lob
	@Column(columnDefinition = "LONGTEXT")
	String payloadJson;

	@Column(nullable = false)
	String status; // PROCESSING, PARSED, NEEDS_REVIEW, FAILED
	java.time.Instant updatedAt = java.time.Instant.now();
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public Long getReceiptFileId() {
		return receiptFileId;
	}
	public void setReceiptFileId(Long receiptFileId) {
		this.receiptFileId = receiptFileId;
	}
	public String getOwnerEmail() {
		return ownerEmail;
	}
	public void setOwnerEmail(String ownerEmail) {
		this.ownerEmail = ownerEmail;
	}
	public String getSupplierName() {
		return supplierName;
	}
	public void setSupplierName(String supplierName) {
		this.supplierName = supplierName;
	}
	public String getSupplierTaxId() {
		return supplierTaxId;
	}
	public void setSupplierTaxId(String supplierTaxId) {
		this.supplierTaxId = supplierTaxId;
	}
	public String getInvoiceNumber() {
		return invoiceNumber;
	}
	public void setInvoiceNumber(String invoiceNumber) {
		this.invoiceNumber = invoiceNumber;
	}
	public java.time.LocalDate getIssueDate() {
		return issueDate;
	}
	public void setIssueDate(java.time.LocalDate issueDate) {
		this.issueDate = issueDate;
	}
	public String getCurrency() {
		return currency;
	}
	public void setCurrency(String currency) {
		this.currency = currency;
	}
	public java.math.BigDecimal getTotal() {
		return total;
	}
	public void setTotal(java.math.BigDecimal total) {
		this.total = total;
	}
	public String getPayloadJson() {
		return payloadJson;
	}
	public void setPayloadJson(String payloadJson) {
		this.payloadJson = payloadJson;
	}
	public String getStatus() {
		return status;
	}
	public void setStatus(String status) {
		this.status = status;
	}
	public java.time.Instant getUpdatedAt() {
		return updatedAt;
	}
	public void setUpdatedAt(java.time.Instant updatedAt) {
		this.updatedAt = updatedAt;
	}
	
}
