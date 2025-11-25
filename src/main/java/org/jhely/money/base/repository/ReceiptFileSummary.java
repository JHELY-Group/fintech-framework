package org.jhely.money.base.repository;

import java.time.Instant;

/**
 * Lightweight projection for listing receipt files without loading the BLOB data.
 */
public interface ReceiptFileSummary {
    Long getId();
    String getOwnerEmail();
    String getFilename();
    String getContentType();
    long getSizeBytes();
    Instant getUploadedAt();
}
