package org.jhely.money.base.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

/**
 * The fields we want the model to return as JSON.
 * Keep names aligned with ReceiptExtraction so mapping is trivial.
 */
public class AiParsedReceipt {
    public String supplierName;
    public String supplierTaxId;
    public String invoiceNumber;
    /** ISO-8601 date (yyyy-MM-dd). Let the model output this format. */
    public String issueDate; 
    public String currency;
    public BigDecimal total;

    /** Optional: include more details if you want to store in payloadJson */
    public List<Line> lines;
    public List<Tax> taxes;

    public Double confidence; // model self-estimate 0..1

    public static class Line {
        public String description;
        public BigDecimal quantity;
        public BigDecimal unitPrice;
        public BigDecimal lineTotal;
    }

    public static class Tax {
        public String name;
        public BigDecimal rate;      // e.g. 0.20 for 20%
        public BigDecimal amount;
    }
}

