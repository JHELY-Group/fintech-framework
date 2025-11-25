package org.jhely.money.base.service;

import java.io.InputStream;
import java.security.MessageDigest;
import java.time.Instant;
import java.util.HexFormat;
import java.util.List;
import java.util.Optional;

import org.apache.tika.Tika;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.jhely.money.base.domain.ReceiptFile;
import org.jhely.money.base.repository.ReceiptExtractionRepository;
import org.jhely.money.base.repository.ReceiptFileRepository;
import org.jhely.money.base.repository.ReceiptFileSummary;
import org.jhely.money.base.service.dto.ReceiptFileData;

@Service
public class ReceiptService {
	private final ReceiptFileRepository repo;
	private final ReceiptExtractionRepository extractionRepo;
	
	// in class
	private static final Tika TIKA = new Tika();

	// allow-list helper
	private static boolean isAllowedReceiptType(String mime) {
	  if (mime == null) return false;
	  mime = mime.toLowerCase();

	  // 1) any image/*
	  if (mime.startsWith("image/")) return true;

	  // 2) common docs
	  return switch (mime) {
	    case "application/pdf",
	         "application/msword",
	         "application/vnd.openxmlformats-officedocument.wordprocessingml.document",
	         "application/rtf",
	         "text/plain",
	         "message/rfc822",                    // .eml
	         "application/vnd.ms-outlook"         // .msg (may show as application/octet-stream on some systems)
	         -> true;
	    default -> false;
	  };
	}

	public ReceiptService(ReceiptFileRepository repo,
            ReceiptExtractionRepository extractionRepo) {
		this.repo = repo;
		this.extractionRepo = extractionRepo;
	}

	public List<ReceiptFile> listForOwner(String email) {
		return repo.findByOwnerEmailOrderByUploadedAtDesc(email.toLowerCase());
	}

	@Transactional(readOnly = true)
	public List<ReceiptFileSummary> listSummariesForOwner(String email) {
		return repo.findSummariesByOwnerEmailOrderByUploadedAtDesc(email);
	}

	@Transactional(readOnly = true)
	public Page<ReceiptFileSummary> pageSummariesForOwner(String email, Pageable pageable) {
		return repo.findPageSummariesByOwnerEmail(email, pageable);
	}

	@Transactional(readOnly = true)
	public long countForOwner(String email) {
		return repo.countByOwnerEmailIgnoreCase(email);
	}
	
	@Transactional(readOnly = true)
	public Optional<org.jhely.money.base.domain.ReceiptExtraction>
	findExtractionForReceipt(Long receiptFileId) {
	  return extractionRepo.findFirstByReceiptFileIdOrderByUpdatedAtDesc(receiptFileId);
	}
	
	  @Transactional(readOnly = true)
	  public Optional<ReceiptFile> findByIdForOwner(Long id, String ownerEmail) {
	    return repo.findByIdAndOwnerEmail(id, ownerEmail.toLowerCase());
	  }

	  @Transactional(readOnly = true)
	  public ReceiptFile getByIdForOwnerOrThrow(Long id, String ownerEmail) {
	    return findByIdForOwner(id, ownerEmail)
	        .orElseThrow(() -> new IllegalArgumentException("Receipt not found or not owned by user"));
	  }

		@Transactional(readOnly = true)
		public ReceiptFileData getFileDataForOwnerOrThrow(Long id, String ownerEmail) {
			var rf = repo.findByIdAndOwnerEmail(id, ownerEmail.toLowerCase())
					.orElseThrow(() -> new IllegalArgumentException("Receipt not found or not owned by user"));
			// Touch the lazy attribute within transaction
			byte[] bytes = rf.getData();
			return new ReceiptFileData(rf.getFilename(), rf.getContentType(), bytes);
		}

	@Transactional
	public ReceiptFile save(String ownerEmail, String filename, String contentType, long size, InputStream dataStream) {
		try {
			// Read fully to a byte[] (OK for controlled size; we also cap in UI)
			byte[] bytes = dataStream.readAllBytes();

			// Basic guard (server-side)
			if (size != bytes.length)
				size = bytes.length;
			if (size <= 0)
				throw new IllegalArgumentException("Empty file");
			if (size > 10 * 1024 * 1024) { // 10 MB hard cap on server
				throw new IllegalArgumentException("File too large (>10MB)");
			}
			
			// Sniff real mime from content
		    String detected = TIKA.detect(bytes, filename);
		    String finalMime = (detected != null && !detected.isBlank())
		        ? detected
		        : (contentType == null || contentType.isBlank() ? "application/octet-stream" : contentType);

		    // Gate: only allow images + approved doc/email types
		    if (!isAllowedReceiptType(finalMime)) {
		      // Some MSG files get detected as octet-stream; optionally relax:
		      if (!("application/octet-stream".equals(finalMime) && filename.toLowerCase().endsWith(".msg"))) {
		        throw new IllegalArgumentException("Unsupported file type: " + finalMime);
		      }
		      finalMime = "application/vnd.ms-outlook"; // normalize MSG
		    }

			String sha = sha256(bytes);

			// Optional dedupe per user
			if (repo.existsByOwnerEmailAndSha256(ownerEmail.toLowerCase(), sha)) {
				// You may prefer to throw or just allow duplicates; here we allow but could
				// skip
			}

			var rf = new ReceiptFile();
			rf.setOwnerEmail(ownerEmail.toLowerCase());
			rf.setFilename(filename);
			rf.setContentType(finalMime);
			rf.setSizeBytes(size);
			rf.setUploadedAt(Instant.now());
			rf.setSha256(sha);
			rf.setData(bytes);

			return repo.save(rf);
		} catch (Exception e) {
			throw new RuntimeException("Failed to save file", e);
		}
	}

	public void delete(String ownerEmail, Long id) {
		repo.findByIdAndOwnerEmail(id, ownerEmail.toLowerCase()).ifPresent(repo::delete);
	}

	private static String sha256(byte[] bytes) {
		try {
			var md = MessageDigest.getInstance("SHA-256");
			return HexFormat.of().formatHex(md.digest(bytes));
		} catch (Exception e) {
			return null;
		}
	}
}
