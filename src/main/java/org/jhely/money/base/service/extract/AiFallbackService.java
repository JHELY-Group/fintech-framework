package org.jhely.money.base.service.extract;

import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.Map;

@Service
public class AiFallbackService {

    /**
     * Try to extract structured fields from the given receipt text using AI.
     *
     * @param text full OCR/PDF text
     * @param filename original filename
     * @param mime mime type of file
     * @return a map of extracted fields (stub: empty)
     */
    public Map<String, Object> extract(String text, String filename, String mime) {
        // TODO: integrate with AI later
        return Collections.emptyMap();
    }

    /**
     * Convenience helper: returns some JSON string with structured data.
     */
    public String extractJson(String text, String filename, String mime) {
        // TODO: call AI model and return structured JSON
        return "{}";
    }
}

