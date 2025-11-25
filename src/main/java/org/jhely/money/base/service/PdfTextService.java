package org.jhely.money.base.service;

import org.springframework.stereotype.Service;

@Service
public class PdfTextService {
	public String extract(byte[] pdfBytes) {
		// try (var is = new java.io.ByteArrayInputStream(pdfBytes);
		// 		var doc = org.apache.pdfbox.pdmodel.PDDocument.load(is)) {
		// 	var stripper = new org.apache.pdfbox.text.PDFTextStripper();
		// 	stripper.setSortByPosition(true);
		// 	return stripper.getText(doc);
		// } catch (Exception e) {
		// 	return "";
		// }

		throw new UnsupportedOperationException("Implement PDF rasterization + OCR");
	}
}
