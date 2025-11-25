package org.jhely.money.base.service.payments;

import java.time.Instant;
import java.util.Optional;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.jhely.money.base.domain.BridgeAgreement;
import org.jhely.money.base.repository.BridgeAgreementRepository;
import org.jhely.money.base.repository.BridgeAgreementLogRepository;
import org.jhely.money.base.domain.BridgeAgreementLog;

@Service
public class BridgeAgreementService {

    private final BridgeAgreementRepository repo;
    private final BridgeAgreementLogRepository logRepo;

    public BridgeAgreementService(BridgeAgreementRepository repo, BridgeAgreementLogRepository logRepo) {
        this.repo = repo;
        this.logRepo = logRepo;
    }

    @Transactional
    public BridgeAgreement saveAgreement(String userId, String email, String agreementId) {
        // Upsert semantics: replace existing for user
        BridgeAgreement entity = repo.findByUserId(userId).orElse(new BridgeAgreement());
        entity.setUserId(userId);
        entity.setEmail(email);
        entity.setAgreementId(agreementId);
        // Update timestamp on every acceptance (treat as last accepted time)
        entity.setCreatedAt(Instant.now());
        BridgeAgreement saved = repo.save(entity);

        // Store a log entry for audit/history
        BridgeAgreementLog log = new BridgeAgreementLog();
        log.setUserId(userId);
        log.setEmail(email);
        log.setAgreementId(agreementId);
        log.setAcceptedAt(Instant.now());
        logRepo.save(log);

        return saved;
    }

    @Transactional(readOnly = true)
    public Optional<BridgeAgreement> findForUser(String userId, String email) {
        return repo.findByUserId(userId).or(() -> repo.findByEmail(email));
    }
}
