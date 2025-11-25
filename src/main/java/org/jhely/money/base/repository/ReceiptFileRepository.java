package org.jhely.money.base.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import org.jhely.money.base.domain.ReceiptFile;

public interface ReceiptFileRepository extends JpaRepository<ReceiptFile, Long> {
	List<ReceiptFile> findByOwnerEmailOrderByUploadedAtDesc(String ownerEmail);

	Optional<ReceiptFile> findByIdAndOwnerEmail(Long id, String ownerEmail);

	boolean existsByOwnerEmailAndSha256(String ownerEmail, String sha256);
	
    @Query("select r from ReceiptFile r where r.id = :id and r.ownerEmail = :ownerEmail")
    Optional<ReceiptFile> findByIdForOwner(@Param("id") Long id, @Param("ownerEmail") String ownerEmail);

	// Summaries without loading the BLOB column
	@Query("select r.id as id, r.ownerEmail as ownerEmail, r.filename as filename, r.contentType as contentType, r.sizeBytes as sizeBytes, r.uploadedAt as uploadedAt "
		+ "from ReceiptFile r where lower(r.ownerEmail) = lower(:ownerEmail) order by r.uploadedAt desc")
	List<ReceiptFileSummary> findSummariesByOwnerEmailOrderByUploadedAtDesc(@Param("ownerEmail") String ownerEmail);

	// Paged summaries (server-side paging)
	@Query(value = "select r.id as id, r.ownerEmail as ownerEmail, r.filename as filename, r.contentType as contentType, r.sizeBytes as sizeBytes, r.uploadedAt as uploadedAt from ReceiptFile r where lower(r.ownerEmail) = lower(:ownerEmail)",
		   countQuery = "select count(r) from ReceiptFile r where lower(r.ownerEmail) = lower(:ownerEmail)")
	Page<ReceiptFileSummary> findPageSummariesByOwnerEmail(@Param("ownerEmail") String ownerEmail, Pageable pageable);

	long countByOwnerEmailIgnoreCase(String ownerEmail);

}
