package org.jhely.money.base.service;

import org.springframework.stereotype.Service;

// import net.sourceforge.tess4j.Tesseract;

@Service
public class OcrService {
	
	// private final Tesseract t;
	
	public OcrService() {
	    // t = new Tesseract();
	    // String tessdata = System.getProperty("TESSDATA_PREFIX",
	    //                  System.getenv().getOrDefault("TESSDATA_PREFIX",
	    //                  "/usr/share/tesseract-ocr/4.00/tessdata"));
	    // t.setDatapath(tessdata);
	    // t.setLanguage("eng+spa"); // add more as needed
	    // // t.setOcrEngineMode(1); // LSTM only, optional
	  }
	
	public record OcrResult(String text, double confidence) {
		
	}

	public OcrResult ocrImage(byte[] imageBytes) {
		// try (var in = new java.io.ByteArrayInputStream(imageBytes)) {
		// 	var img = javax.imageio.ImageIO.read(in);
			
		// 	t.setLanguage("spa+eng"); // Spanish + English
		// 	var text = t.doOCR(img);
		// 	// Approximate confidence not trivial; you can compute from result iterator if
		// 	// needed
		// 	return new OcrResult(text, 0.65);
		// } catch (Exception e) {
		// 	throw new RuntimeException(e);
		// }

		throw new UnsupportedOperationException("Implement PDF rasterization + OCR");
	}

	public OcrResult ocrPdf(byte[] pdfBytes) {
		// Convert pages to images (PDFBox), then OCR each and join text
		// Keep it simple for now; we can add code when you’re ready
		throw new UnsupportedOperationException("Implement PDF rasterization + OCR");
		// TODO
	}
}
