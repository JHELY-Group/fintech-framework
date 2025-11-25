package org.jhely.money.base.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.jhely.money.base.domain.BridgeAgreement;

public interface BridgeAgreementRepository extends JpaRepository<BridgeAgreement, Long> {
    Optional<BridgeAgreement> findByUserId(String userId);
    Optional<BridgeAgreement> findByEmail(String email);
}
