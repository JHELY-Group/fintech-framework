package org.jhely.money.base.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.springframework.stereotype.Service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.jhely.money.base.service.extract.EsReceiptRegex;

@Service
public class ReceiptParser {

	public record ParseResult(String supplierName, String supplierTaxId, String invoiceNumber,
			java.time.LocalDate issueDate, String currency, java.math.BigDecimal subtotal,
			java.math.BigDecimal taxAmount, java.math.BigDecimal total, double confidence, java.util.List<Line> lines,
			java.util.Map<String, Object> meta) {

		public record Line(String description, Double qty, java.math.BigDecimal unitPrice,
				java.math.BigDecimal lineTotal, String taxRate) {
		}
	}
	
	private final ObjectMapper mapper;

	 public ReceiptParser(ObjectMapper mapper) {
	    this.mapper = mapper; // Spring's mapper has JavaTimeModule
	 }

    public String toJson(ParseResult result) {
        try {
            return mapper.writeValueAsString(result);
        } catch (JsonProcessingException e) {
            throw new RuntimeException("Failed to serialize ParseResult to JSON", e);
        }
    }

//    // ParseResult as a record or class
//    public static record ParseResult(
//        String supplierName,
//        String supplierTaxId,
//        String invoiceNumber,
//        java.time.LocalDate issueDate,
//        String currency,
//        java.math.BigDecimal subtotal,
//        java.math.BigDecimal taxAmount,
//        java.math.BigDecimal total,
//        double confidence,
//        java.util.List<Object> lines,
//        java.util.Map<String,Object> meta
//    ) {}

	// --- Patterns for invoice/ticket numbers (simple but effective) ---
	// e.g., "Factura Nº: 2024-00123", "Invoice No 000123", "Ticket 987-AB"
	private static final Pattern INVOICE_NO_PATTERN = Pattern.compile(
			"(?i)\\b(?:factura|invoice|ticket|recibo|n[°ºo]?|num(?:ero)?)\\s*(?:n[°ºo]?\\s*)?[:#]?\\s*([A-Z0-9][A-Z0-9\\-_/\\.]{2,30})");

	// Pick a reasonable “issue date” from a list of dates:
	// 1) prefer latest date not in the future
	private static LocalDate pickIssueDate(List<LocalDate> candidates) {
		if (candidates == null || candidates.isEmpty())
			return null;
		LocalDate today = LocalDate.now(ZoneId.systemDefault());
		return candidates.stream().filter(d -> !d.isAfter(today)).max(LocalDate::compareTo) // latest past/now
				.orElse(candidates.get(0));
	}

	// Extract invoice/ticket number by scanning lines; returns first match
	private static String findInvoiceNumber(String text) {
		for (String line : text.split("\\R")) {
			Matcher m = INVOICE_NO_PATTERN.matcher(line);
			if (m.find()) {
				String val = m.group(1).trim();
				// Trim trailing punctuation
				val = val.replaceAll("[,.;:]+$", "");
				return val;
			}
		}
		return null;
	}

	// Guess supplier name:
	// 1) If we see a tax ID, take the previous non-empty line nearby as the name.
	// 2) Otherwise, take the first non-empty, mostly-letter line near the top.
	private static String guessSupplierName(String text, String supplierTaxId) {
		String[] lines = text.split("\\R");
		// Normalize a copy to search the taxId in a case-insensitive manner
		String normalized = text.toLowerCase(Locale.ROOT);
		String taxLower = supplierTaxId == null ? null : supplierTaxId.toLowerCase(Locale.ROOT);

		if (taxLower != null && normalized.contains(taxLower)) {
			// Find the line index that contains the tax id
			for (int i = 0; i < lines.length; i++) {
				if (lines[i].toLowerCase(Locale.ROOT).contains(taxLower)) {
					// Look upward a few lines for a candidate business name
					for (int j = Math.max(0, i - 3); j < i; j++) {
						String candidate = cleanName(lines[j]);
						if (isLikelyBusinessName(candidate))
							return candidate;
					}
					// Or fallback to the same line
					String same = cleanName(lines[i]);
					if (isLikelyBusinessName(same))
						return same;
					break;
				}
			}
		}

		// Fallback: first good-looking line near the top
		for (int i = 0; i < Math.min(lines.length, 8); i++) {
			String candidate = cleanName(lines[i]);
			if (isLikelyBusinessName(candidate))
				return candidate;
		}
		return null;
	}

	private static String cleanName(String s) {
		if (s == null)
			return null;
		String t = s.trim();
		// Drop common labels
		t = t.replaceAll("(?i)^(empresa|proveedor|supplier)\\s*[:.-]?\\s*", "");
		// Collapse spaces
		t = t.replaceAll("\\s{2,}", " ");
		// Remove trailing noise
		t = t.replaceAll("[,;:]+$", "");
		return t;
	}

	private static boolean isLikelyBusinessName(String s) {
		if (s == null || s.isBlank())
			return false;
		// Avoid lines that are obviously addresses or totals
		String lower = s.toLowerCase(Locale.ROOT);
		if (lower.matches(".*\\b(total|iva|importe|subtotal|iva incl)\\b.*"))
			return false;
		if (lower.matches(".*\\d{2,}.*") && !lower.matches(".*(s\\.l\\.|s\\.a\\.|sl\\b|sa\\b).*")) {
			// if contains many digits and no common company suffix, skip
			return false;
		}
		// Looks like a name if it has several letters/spaces and not too long
		return s.matches("(?U).*[\\p{L}]{3,}.*") && s.length() <= 80;
	}

	private static double score(double ocrConfidence, boolean hasDate, boolean hasTotal, boolean hasSupplier,
			boolean hasInvoice) {
		double s = 0.2 * Math.max(0.0, Math.min(1.0, ocrConfidence));
		if (hasDate)
			s += 0.2;
		if (hasTotal)
			s += 0.3;
		if (hasSupplier)
			s += 0.2;
		if (hasInvoice)
			s += 0.1;
		return Math.min(1.0, s);
	}

	public ParseResult parse(String text, String filename, String mime, double ocrConfidence) {
		// Use EsReceiptRegex helpers directly (they handle normalization internally)
		var dates = EsReceiptRegex.findDates(text);
		LocalDate issueDate = pickIssueDate(dates);

		BigDecimal total = EsReceiptRegex.findLikelyTotal(text).orElse(null);
		String currency = EsReceiptRegex.detectCurrency(text);

		String supplierTaxId = EsReceiptRegex.findFirstTaxId(text).orElse(null);
		String invoiceNum = findInvoiceNumber(text);
		String supplierName = guessSupplierName(text, supplierTaxId);

		double conf = score(ocrConfidence, issueDate != null, total != null, supplierName != null, invoiceNum != null);

		return new ParseResult(supplierName, supplierTaxId, invoiceNum, issueDate, currency, null, // subtotal (fill
																									// later when you
																									// parse IVA lines)
				null, // taxAmount
				total, conf, java.util.List.of(), // lines (add later)
				java.util.Map.of("filename", filename, "mime", mime));
	}
}
