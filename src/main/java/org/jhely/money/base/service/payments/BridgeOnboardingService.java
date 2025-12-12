package org.jhely.money.base.service.payments;

import java.time.Instant;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.client.RestClientResponseException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.jhely.money.base.api.payments.BridgeApiClient;
import org.jhely.money.base.domain.BridgeCustomer;
import org.jhely.money.base.repository.BridgeCustomerRepository;
import org.jhely.money.base.service.BridgeApiClientFactory;
import org.jhely.money.base.service.payments.BridgeAgreementService;
import org.jhely.money.sdk.bridge.model.Customer;
import org.jhely.money.sdk.bridge.model.Customers;

@Service
public class BridgeOnboardingService {
	
	private static final Logger log = LoggerFactory.getLogger(BridgeOnboardingService.class);

    private final BridgeCustomerRepository repo;
    private final BridgeApiClient bridge;
    private final BridgeApiClientFactory bridgeFactory;
    private final BridgeAgreementService agreements;
    private final ObjectMapper mapper;
    private final KycStatusBroadcaster kycBroadcaster;

    public BridgeOnboardingService(BridgeCustomerRepository repo, BridgeApiClient bridge, 
                                   BridgeApiClientFactory bridgeFactory,
                                   ObjectMapper mapper, BridgeAgreementService agreements, 
                                   KycStatusBroadcaster kycBroadcaster) {
        this.repo = repo;
        this.bridge = bridge;
        this.bridgeFactory = bridgeFactory;
        this.mapper = mapper;
        this.agreements = agreements;
        this.kycBroadcaster = kycBroadcaster;
    }

    /** Returns an existing BridgeCustomer if onboarded. */
    @Transactional(readOnly = true)
    public Optional<BridgeCustomer> findForUser(String userId, String email) {
        // Prefer lookup by userId; fallback by email
        return repo.findByUserId(userId).or(() -> repo.findByEmail(email));
    }
    
//    public BridgeCustomer createAndSave(String userId, String email, String fullName) {
//    	return createAndSave(userId, email, fullName, null, null);
//    }
    
 // BridgeOnboardingService.java (only relevant parts)
    @Transactional
    public BridgeCustomer createAndSave(String userId,
                                        String email,
                                        String fullName,
                                        String idType,
                                        String issuingCountryA3,
                                        String idNumber) {
        try {
            var dto = bridge.createCustomer(fullName, email, idType, issuingCountryA3, idNumber);

            BridgeCustomer entity = new BridgeCustomer();
            entity.setUserId(userId);
            entity.setEmail(dto.email != null ? dto.email : email);
            entity.setBridgeCustomerId(dto.id);
            entity.setStatus(dto.status != null ? dto.status : "created");
            entity.setRawJson(mapper.writeValueAsString(dto));
            entity.setCreatedAt(java.time.Instant.now());
            entity.setUpdatedAt(java.time.Instant.now());
            
            repo.save(entity);
            log.info("Saved Bridge customer userId={} bridgeId={} status={}", userId, dto.id, dto.status);
            return entity;
        } catch (Exception e) {
            log.error("createAndSave failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create Bridge customer", e);
        }
    }

    /** Minimal creation path: let Bridge collect PII via hosted flows; we only pass name/email. */
    @Transactional
    public BridgeCustomer createAndSave(String userId, String email, String fullName) {
        try {
            var dto = bridge.createCustomer(fullName, email, (String) null, (String) null, (String) null);

            BridgeCustomer entity = new BridgeCustomer();
            entity.setUserId(userId);
            entity.setEmail(dto.email != null ? dto.email : email);
            entity.setBridgeCustomerId(dto.id);
            entity.setStatus(dto.status != null ? dto.status : "created");
            entity.setRawJson(mapper.writeValueAsString(dto));
            entity.setCreatedAt(java.time.Instant.now());
            entity.setUpdatedAt(java.time.Instant.now());

            repo.save(entity);
            log.info("Saved Bridge customer (minimal) userId={} bridgeId={} status={}", userId, dto.id, dto.status);
            return entity;
        } catch (Exception e) {
            log.error("createAndSave(minimal) failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create Bridge customer (minimal)", e);
        }
    }

    /** Creation path with multiple identifying documents (e.g., TIN + photo ID for non-US). */
    @Transactional
    public BridgeCustomer createAndSave(String userId, String email, String fullName,
                                        String birthDate,
                                        java.util.List<BridgeApiClient.IdentDoc> docs) {
        try {
            var dto = bridge.createCustomer(fullName, email, birthDate, docs);

            BridgeCustomer entity = new BridgeCustomer();
            entity.setUserId(userId);
            entity.setEmail(dto.email != null ? dto.email : email);
            entity.setBridgeCustomerId(dto.id);
            entity.setStatus(dto.status != null ? dto.status : "created");
            entity.setRawJson(mapper.writeValueAsString(dto));
            entity.setCreatedAt(Instant.now());
            entity.setUpdatedAt(Instant.now());

            repo.save(entity);
            log.info("Saved Bridge customer (multi-docs) userId={} bridgeId={} status={} birthDate={}", userId, dto.id, dto.status, birthDate);
            return entity;
        } catch (Exception e) {
            log.error("createAndSave(multi-docs) failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create Bridge customer (multi-docs)", e);
        }
    }

    /** Creation path with multiple identifying documents and residential address. */
    @Transactional
    public BridgeCustomer createAndSave(String userId, String email, String fullName,
                                        String birthDate,
                                        java.util.List<BridgeApiClient.IdentDoc> docs,
                                        BridgeApiClient.Address address) {
        try {
            var agreementOpt = agreements.findForUser(userId, email);
            if (agreementOpt.isEmpty()) {
                throw new IllegalStateException("Terms of Service not accepted yet; please accept before creating a customer.");
            }
            String signedAgreementId = agreementOpt.get().getAgreementId();
            var dto = bridge.createCustomer(fullName, email, birthDate, docs, address, signedAgreementId);

            BridgeCustomer entity = new BridgeCustomer();
            entity.setUserId(userId);
            entity.setEmail(dto.email != null ? dto.email : email);
            entity.setBridgeCustomerId(dto.id);
            entity.setStatus(dto.status != null ? dto.status : "created");
            entity.setRawJson(mapper.writeValueAsString(dto));
            entity.setCreatedAt(Instant.now());
            entity.setUpdatedAt(Instant.now());

            repo.save(entity);
            log.info("Saved Bridge customer (multi-docs+address) userId={} bridgeId={} status={} birthDate={} agreementId={}", userId, dto.id, dto.status, birthDate, signedAgreementId);
            return entity;
        } catch (Exception e) {
            log.error("createAndSave(multi-docs+address) failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create Bridge customer (multi-docs+address)", e);
        }
    }

    /**
     * Request a hosted KYC link that will create a NEW customer (alternative onboarding path).
     * This does NOT immediately persist a BridgeCustomer locally; completion should be handled
     * via a webhook or callback that captures the created customer_id and then invokes
     * one of the createAndSave(...) methods above.
     */
    @Transactional(readOnly = true)
    public String requestHostedKycLinkForNewCustomer(String email, String fullName, String redirectUri) {
        try {
            // Per Bridge spec, email and type are required when creating a hosted KYC link for a new customer.
            // We default to an individual customer for the current app use case.
            String type = "individual";
            return bridge.createKycLinkForNewCustomer(email, type, fullName, redirectUri);
        } catch (Exception e) {
            log.error("requestHostedKycLinkForNewCustomer failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to create hosted KYC link", e);
        }
    }

    /** Persist a remote Bridge customer returned via callback or webhook. */
    @Transactional
    public BridgeCustomer persistRemoteCustomer(String userId, String email, BridgeApiClient.Customer remote) {
        // If already persisted by bridgeCustomerId, return existing
        Optional<BridgeCustomer> existing = repo.findByBridgeCustomerId(remote.id);
        if (existing.isPresent()) {
            BridgeCustomer ec = existing.get();
            // Update status/raw JSON if changed
            ec.setStatus(remote.status != null ? remote.status : ec.getStatus());
            try { ec.setRawJson(mapper.writeValueAsString(remote)); } catch (Exception ignore) {}
            ec.setUpdatedAt(Instant.now());
            BridgeCustomer saved = repo.save(ec);
            kycBroadcaster.broadcast(saved);
            return saved;
        }
        BridgeCustomer entity = new BridgeCustomer();
        entity.setUserId(userId);
        entity.setEmail(remote.email != null ? remote.email : email);
        entity.setBridgeCustomerId(remote.id);
        entity.setStatus(remote.status != null ? remote.status : "created");
        try { entity.setRawJson(mapper.writeValueAsString(remote)); } catch (Exception ignore) {}
        entity.setCreatedAt(Instant.now());
        entity.setUpdatedAt(Instant.now());
        repo.save(entity);
        log.info("Persisted remote Bridge customer via callback userId={} bridgeId={} status={}", userId, remote.id, remote.status);
        kycBroadcaster.broadcast(entity);
        return entity;
    }

    /** Update KYC fields and status safely based on webhook payload. Creates record if missing and email is present. */
    @Transactional
    public void updateKycFromWebhook(String bridgeCustomerId, String email, String customerStatus, String kycStatus,
                                     String rejectionReason, String detailsJson) {
        Optional<BridgeCustomer> existing = repo.findByBridgeCustomerId(bridgeCustomerId);
        
        BridgeCustomer bc;
        if (existing.isPresent()) {
            bc = existing.get();
        } else {
            // Not found by ID. Try to find by email to link or create new.
            if (email != null && !email.isBlank()) {
                Optional<BridgeCustomer> byEmail = repo.findByEmail(email);
                if (byEmail.isPresent()) {
                    bc = byEmail.get();
                    log.info("Linking existing local customer (found by email) to bridgeId={}", bridgeCustomerId);
                } else {
                    bc = new BridgeCustomer();
                    bc.setUserId(email); // Assuming email is the userId/principal
                    bc.setEmail(email);
                    bc.setCreatedAt(Instant.now());
                    bc.setStatus("pending"); // Default status for new customers from webhook
                    log.info("Creating new local customer from webhook for email={} bridgeId={}", email, bridgeCustomerId);
                }
                bc.setBridgeCustomerId(bridgeCustomerId);
            } else {
                log.warn("Webhook received for unknown bridgeId={} and no email provided. Cannot create/link local record.", bridgeCustomerId);
                return;
            }
        }

        if (customerStatus != null && !customerStatus.isBlank()) {
            bc.setStatus(customerStatus);
        }
        if (kycStatus != null && !kycStatus.isBlank()) {
            bc.setKycStatus(kycStatus);
            bc.setKycUpdatedAt(Instant.now());
        }
        if (rejectionReason != null) {
            bc.setKycRejectionReason(rejectionReason);
        }
        if (detailsJson != null) {
            bc.setKycDetailsJson(detailsJson);
        }
        bc.setUpdatedAt(Instant.now());
        repo.save(bc);
        kycBroadcaster.broadcast(bc);
        log.info("Updated KYC via webhook bridgeId={} status={} kycStatus={}", bridgeCustomerId, customerStatus, kycStatus);
    }


    /** Persist an already-created Bridge customer fetched from Bridge API (hosted KYC flow). */
    @Transactional
    public BridgeCustomer persistFetched(String userId, String email, org.jhely.money.sdk.bridge.model.Customer remote) {
        // If we already have one, just return existing
        Optional<BridgeCustomer> existing = repo.findByUserId(userId).or(() -> repo.findByEmail(email));
        if (existing.isPresent()) {
            return existing.get();
        }
        try {
            BridgeCustomer entity = new BridgeCustomer();
            entity.setUserId(userId);
            entity.setEmail(remote.getEmail() != null ? remote.getEmail() : email);
            entity.setBridgeCustomerId(remote.getId());
            entity.setStatus(remote.getStatus() != null ? remote.getStatus().toString() : "created");
            entity.setRawJson(mapper.writeValueAsString(remote));
            entity.setCreatedAt(Instant.now());
            entity.setUpdatedAt(Instant.now());
            repo.save(entity);
            log.info("Persisted fetched Bridge customer userId={} bridgeId={} status={}", userId, remote.getId(), remote.getStatus());
            return entity;
        } catch (Exception e) {
            log.error("persistFetched failed: {}", e.getMessage(), e);
            throw new RuntimeException("Failed to persist fetched Bridge customer", e);
        }
    }

    /**
     * Synchronizes local BridgeCustomer data from Bridge API if needed.
     * 
     * This is useful when the same user (by email) exists in Bridge from another app instance
     * sharing the same API key. If the local record is missing or KYC is not yet approved,
     * we fetch the customer from Bridge API and populate/update the local database.
     * 
     * @param userId Local user identifier (can be null)
     * @param email  User's email address (required for Bridge lookup)
     * @return Updated BridgeCustomer if sync occurred, or existing if already up-to-date
     */
    @Transactional
    public Optional<BridgeCustomer> syncFromBridgeIfNeeded(String userId, String email) {
        if (email == null || email.isBlank()) {
            log.warn("syncFromBridgeIfNeeded called with null/empty email");
            return Optional.empty();
        }

        // Check if we already have an approved customer locally
        Optional<BridgeCustomer> existing = findForUser(userId, email);
        if (existing.isPresent()) {
            BridgeCustomer bc = existing.get();
            // If already approved, no need to sync
            if ("approved".equalsIgnoreCase(bc.getKycStatus()) || "active".equalsIgnoreCase(bc.getStatus())) {
                log.debug("Local BridgeCustomer already approved/active for email={}, skipping sync", email);
                return existing;
            }
        }

        // Search Bridge API for this email
        try {
            log.info("Searching Bridge API for customer with email={}", email);
            Customers customers = bridgeFactory.customers().customersGet(null, null, 10, email);
            
            if (customers == null || customers.getData() == null || customers.getData().isEmpty()) {
                log.info("No customer found in Bridge API for email={}", email);
                return existing; // Return existing (possibly empty) if nothing in Bridge
            }

            // Take the first match (should be unique by email)
            Customer remote = customers.getData().get(0);
            log.info("Found Bridge customer id={} status={} for email={}", 
                    remote.getId(), remote.getStatus(), email);

            // Create or update local record
            BridgeCustomer bc;
            if (existing.isPresent()) {
                bc = existing.get();
                log.info("Updating existing local BridgeCustomer from Bridge API");
            } else {
                bc = new BridgeCustomer();
                bc.setUserId(userId);
                bc.setEmail(email);
                bc.setCreatedAt(Instant.now());
                log.info("Creating new local BridgeCustomer from Bridge API");
            }

            // Populate from remote
            bc.setBridgeCustomerId(remote.getId());
            bc.setStatus(remote.getStatus() != null ? remote.getStatus().getValue() : bc.getStatus());
            
            // Map customer status to KYC status
            // Bridge uses: pending, approved, rejected, active, inactive
            if (remote.getStatus() != null) {
                String statusValue = remote.getStatus().getValue();
                if ("active".equalsIgnoreCase(statusValue) || "approved".equalsIgnoreCase(statusValue)) {
                    bc.setKycStatus("approved");
                } else if ("rejected".equalsIgnoreCase(statusValue)) {
                    bc.setKycStatus("rejected");
                } else {
                    bc.setKycStatus("pending");
                }
            }

            // Store rejection reasons if any
            if (remote.getRejectionReasons() != null && !remote.getRejectionReasons().isEmpty()) {
                try {
                    bc.setKycRejectionReason(mapper.writeValueAsString(remote.getRejectionReasons()));
                } catch (Exception e) {
                    bc.setKycRejectionReason(remote.getRejectionReasons().toString());
                }
            }

            // Store capabilities as details JSON
            if (remote.getCapabilities() != null) {
                try {
                    bc.setKycDetailsJson(mapper.writeValueAsString(remote.getCapabilities()));
                } catch (Exception e) {
                    log.warn("Failed to serialize capabilities: {}", e.getMessage());
                }
            }

            // Store full raw JSON
            try {
                bc.setRawJson(mapper.writeValueAsString(remote));
            } catch (Exception e) {
                log.warn("Failed to serialize remote customer: {}", e.getMessage());
            }

            bc.setKycUpdatedAt(Instant.now());
            bc.setUpdatedAt(Instant.now());
            repo.save(bc);

            log.info("Synced BridgeCustomer from Bridge API: userId={} bridgeId={} status={} kycStatus={}",
                    userId, remote.getId(), bc.getStatus(), bc.getKycStatus());
            
            kycBroadcaster.broadcast(bc);
            return Optional.of(bc);

        } catch (RestClientResponseException e) {
            log.error("Failed to fetch customer from Bridge API: {} - {}", 
                    e.getStatusCode(), e.getResponseBodyAsString());
            return existing;
        } catch (Exception e) {
            log.error("Error syncing from Bridge API: {}", e.getMessage(), e);
            return existing;
        }
    }

}

