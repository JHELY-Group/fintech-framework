package org.jhely.money.base.service;

import java.io.File;
import java.nio.file.Files;
import java.time.Instant;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.StringUtils;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.openai.client.OpenAIClient;
import com.openai.models.ChatModel;
import com.openai.models.files.FileCreateParams;
import com.openai.models.files.FileObject;
import com.openai.models.files.FilePurpose;
import com.openai.models.responses.Response;
import com.openai.models.responses.ResponseCreateParams;
import com.openai.models.responses.ResponseInputFile;
import com.openai.models.responses.ResponseInputItem;
import com.openai.models.responses.ResponseInputItem.Message.Role;

import org.jhely.money.base.domain.ReceiptExtraction;
import org.jhely.money.base.domain.ReceiptFile;
import org.jhely.money.base.repository.ReceiptExtractionRepository;

@Service
public class ExtractionWorker {
	
	private static final Logger log = LoggerFactory.getLogger(ExtractionWorker.class);


    private final ReceiptService receipts;
    private final ReceiptExtractionRepository repo;
    private final OpenAIClient openai;
    private final ObjectMapper mapper;

    public ExtractionWorker(
            ReceiptService receipts,
            ReceiptExtractionRepository repo,
            OpenAIClient openai,
            ObjectMapper mapper
    ) {
        this.receipts = receipts;
        this.repo = repo;
        this.mapper = mapper;
        this.openai = openai;
    }
    
    private String uploadBytesToOpenAI(byte[] bytes, String filename) {
        try {
            // Write to a temp file
            File temp = File.createTempFile("upload-", "-" + filename);
            Files.write(temp.toPath(), bytes);

            FileCreateParams params = FileCreateParams.builder()
                .purpose(FilePurpose.ASSISTANTS)  // or FilePurpose.USER_DATA
                .file(temp.toPath())              // Path upload is safest
                .build();

            FileObject fo = openai.files().create(params);
            log.debug("Uploaded file to OpenAI: id={} (name='{}', size={} bytes)", fo.id(), filename, bytes.length);

            return fo.id();
        } catch (Exception ex) {
            throw new RuntimeException("Failed to upload file to OpenAI", ex);
        }
    }


    @Async
    @Transactional()
    public void process(Long receiptFileId, String ownerEmail) {
    	log.info("Starting extraction for fileId={} owner={}", receiptFileId, ownerEmail);

        ReceiptFile rf = receipts.findByIdForOwner(receiptFileId, ownerEmail)
                .orElseThrow(() -> new IllegalArgumentException("Not found"));

        ReceiptExtraction ex = repo.findByReceiptFileIdAndOwnerEmail(receiptFileId, ownerEmail).orElseGet(() -> {
            var e = new ReceiptExtraction();
            e.setReceiptFileId(receiptFileId);
            e.setOwnerEmail(ownerEmail);
            e.setStatus("PROCESSING");
            return repo.save(e);
        });
        
        final int MAX_BYTES = 1_800_000; // ~1.8MB
        byte[] data = rf.getData();
        byte[] trimmed = data.length > MAX_BYTES ? slice(data, MAX_BYTES) : data;


        try {
            
        	String fileId = uploadBytesToOpenAI(trimmed, safe(rf.getFilename()));

            // --- 3) Build clear instructions -------------------------------------
            String system = """
                You are a strict invoice/receipt extraction engine.
                Read the attached file and return ONLY a single JSON object:
                {
                  "supplierName": string | null,
                  "supplierAddress": string | null,
                  "supplierTaxId": string | null,
                  "clientName": string | null,
                  "clientAddress": string | null,
                  "clientTaxId": string | null,
                  "invoiceNumber": string | null,
                  "issueDate": "YYYY-MM-DD" | null,
                  "currency": string | null,
                  "total": number | null,
                  "confidence": number (0..1)
                }
                If uncertain, set fields to null. Never add any text outside the JSON.
                """;

            String userMeta = """
                File meta:
                - filename: %s
                - contentType: %s
                - totalBytes: %d (server uploaded %d bytes)
                Please extract fields from the attached file.
                """.formatted(
                    safe(rf.getFilename()),
                    safe(rf.getContentType()),
                    data.length,
                    trimmed.length
                );

            // --- 4) Build Responses API input with messages + file ----------------
            var inputs = new java.util.ArrayList<ResponseInputItem>();

            // System instructions
            inputs.add(ResponseInputItem.ofMessage(
                ResponseInputItem.Message.builder()
                    .role(Role.SYSTEM)
                    .addInputTextContent(system)
                    .build()
            ));

            // User message with meta + the file attachment
            inputs.add(ResponseInputItem.ofMessage(
                ResponseInputItem.Message.builder()
                    .role(Role.USER)
                    .addInputTextContent(userMeta)
                    .addContent( // attach uploaded file by id
                        ResponseInputFile.builder().fileId(fileId).build()
                    )
                    .build()
            ));

            // Create request (Responses API)
            ResponseCreateParams createParams = ResponseCreateParams.builder()
                .model(ChatModel.GPT_5_MINI)        // pick a model you have access to
                .input(ResponseCreateParams.Input.ofResponse(inputs))
                .temperature(0.0)                // keep outputs terse & deterministic
                .maxOutputTokens(256L)        // uncomment if available in your SDK
                .build();

            Response response = openai.responses().create(createParams);

            // --- 5) Get the raw JSON text from the response ----------------------
            // Many SDK versions let you do response.outputText(), but this works across versions:
            
         // Easiest: use helper
            String jsonText = response.output().stream()
                    .flatMap(ro -> ro.message().stream())              // message() returns list of messages
                    .flatMap(msg -> msg.content().stream())            // content list
                    .flatMap(cnt -> cnt.outputText().stream())         // outputText elements
                    .map(ot -> ot.text())
                    .collect(Collectors.joining());

            log.debug("Model response JSON: {}", jsonText);

            // --- 6) Parse JSON and persist ---------------------------------------
            AiParsedReceipt parsed = mapper.readValue(jsonText, AiParsedReceipt.class);

            // 5) Update extraction entity
            ex.setPayloadJson(jsonText);
            ex.setSupplierName(emptyToNull(parsed.getSupplierName()));
            ex.setSupplierTaxId(emptyToNull(parsed.getSupplierTaxId()));
            ex.setInvoiceNumber(emptyToNull(parsed.getInvoiceNumber()));
            ex.setIssueDate(parsed.safeIssueDate());
            ex.setCurrency(emptyToNull(parsed.getCurrency()));
            ex.setTotal(parsed.getTotal());
            double conf = parsed.getConfidence() == null ? 0.5 : parsed.getConfidence();
            ex.setStatus(conf >= 0.7 ? "PARSED" : "NEEDS_REVIEW");
            ex.setUpdatedAt(Instant.now());

            repo.save(ex);

        } catch (Exception e) {
            e.printStackTrace();
            ex.setStatus("FAILED");
            ex.setUpdatedAt(Instant.now());
            repo.save(ex);
            throw new RuntimeException("AI extraction failed", e);
        }
    }

    private static byte[] slice(byte[] src, int max) {
        byte[] out = new byte[max];
        System.arraycopy(src, 0, out, 0, max);
        return out;
    }

    private static String safe(String s) {
        return s == null ? "" : s;
    }

    private static String emptyToNull(String s) {
        return StringUtils.hasText(s) ? s : null;
    }

    // Shape that matches the JSON we ask the model to return
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class AiParsedReceipt {
        private String supplierName;
        private String supplierTaxId;
        private String invoiceNumber;
        private String issueDate; // "YYYY-MM-DD" or null
        private String currency;
        private java.math.BigDecimal total;
        private Double confidence;

        public String getSupplierName() { return supplierName; }
        public void setSupplierName(String supplierName) { this.supplierName = supplierName; }

        public String getSupplierTaxId() { return supplierTaxId; }
        public void setSupplierTaxId(String supplierTaxId) { this.supplierTaxId = supplierTaxId; }

        public String getInvoiceNumber() { return invoiceNumber; }
        public void setInvoiceNumber(String invoiceNumber) { this.invoiceNumber = invoiceNumber; }

        public String getIssueDate() { return issueDate; }
        public void setIssueDate(String issueDate) { this.issueDate = issueDate; }

        public String getCurrency() { return currency; }
        public void setCurrency(String currency) { this.currency = currency; }

        public java.math.BigDecimal getTotal() { return total; }
        public void setTotal(java.math.BigDecimal total) { this.total = total; }

        public Double getConfidence() { return confidence; }
        public void setConfidence(Double confidence) { this.confidence = confidence; }

        /** Safely converts YYYY-MM-DD to LocalDate or returns null */
        public java.time.LocalDate safeIssueDate() {
            try {
                return (issueDate == null || issueDate.isBlank())
                        ? null
                        : java.time.LocalDate.parse(issueDate);
            } catch (Exception ignore) {
                return null;
            }
        }
    }
}
