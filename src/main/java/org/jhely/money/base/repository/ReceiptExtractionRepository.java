package org.jhely.money.base.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.jhely.money.base.domain.ReceiptExtraction;

public interface ReceiptExtractionRepository extends JpaRepository<ReceiptExtraction, Long> {
	  Optional<ReceiptExtraction> findByReceiptFileIdAndOwnerEmail(Long receiptFileId, String ownerEmail);
	  List<ReceiptExtraction> findByOwnerEmailOrderByUpdatedAtDesc(String ownerEmail);
	  Optional<ReceiptExtraction> findFirstByReceiptFileIdOrderByUpdatedAtDesc(Long receiptFileId);
	}

