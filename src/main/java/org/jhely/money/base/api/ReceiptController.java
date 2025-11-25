package org.jhely.money.base.api;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.jhely.money.base.service.ReceiptService;
import org.jhely.money.base.service.dto.ReceiptFileData;

@RestController
@RequestMapping("/api/receipts")
public class ReceiptController {

    private final ReceiptService receiptService;

    public ReceiptController(ReceiptService receiptService) {
        this.receiptService = receiptService;
    }

    @GetMapping("/{id}")
    public ResponseEntity<byte[]> getReceipt(@PathVariable Long id, @RequestParam String ownerEmail) {
        ReceiptFileData file = receiptService.getFileDataForOwnerOrThrow(id, ownerEmail);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=\"" + file.filename() + "\"")
                .contentType(MediaType.parseMediaType(file.contentType()))
                .body(file.data());
    }
}
