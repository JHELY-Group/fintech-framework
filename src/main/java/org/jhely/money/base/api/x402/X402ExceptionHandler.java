package org.jhely.money.base.api.x402;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.Map;

/**
 * Exception handler for x402 API endpoints.
 * Handles exceptions and returns proper JSON responses instead of forwarding to /error.
 */
@RestControllerAdvice(basePackageClasses = X402FacilitatorController.class)
public class X402ExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(X402ExceptionHandler.class);

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<Map<String, Object>> handleJsonParseError(HttpMessageNotReadableException ex) {
        log.warn("JSON parse error in x402 API: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Bad Request",
            "message", "Invalid JSON: " + getRootCauseMessage(ex)
        ));
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<Map<String, Object>> handleIllegalArgument(IllegalArgumentException ex) {
        log.warn("Illegal argument in x402 API: {}", ex.getMessage());
        return ResponseEntity.badRequest().body(Map.of(
            "error", "Bad Request",
            "message", ex.getMessage()
        ));
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<Map<String, Object>> handleGenericException(Exception ex) {
        log.error("Unhandled exception in x402 API", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(Map.of(
            "error", "Internal Server Error",
            "message", ex.getMessage() != null ? ex.getMessage() : "Unknown error"
        ));
    }

    private String getRootCauseMessage(Throwable t) {
        Throwable cause = t;
        while (cause.getCause() != null) {
            cause = cause.getCause();
        }
        return cause.getMessage();
    }
}
