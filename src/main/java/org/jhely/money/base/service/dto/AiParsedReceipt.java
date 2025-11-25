package org.jhely.money.base.service.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

/** What we expect back from the AI in JSON form. */
public class AiParsedReceipt {
    public String supplierName;
    public String supplierTaxId;
    public String invoiceNumber;
    public LocalDate issueDate;
    public String currency;
    public BigDecimal total;
    /** Optional raw or structured payload we ask the AI to include (tax lines, items, confidences, etc). */
    public Object details;
    /** Optional overall confidence [0..1] the AI reports. */
    public Double confidence;
}
