package org.jhely.money.base.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.jhely.money.base.domain.BridgeCustomer;

public interface BridgeCustomerRepository extends JpaRepository<BridgeCustomer, Long> {
    Optional<BridgeCustomer> findByUserId(String userId);
    Optional<BridgeCustomer> findByEmail(String email);
    Optional<BridgeCustomer> findByBridgeCustomerId(String bridgeCustomerId);
}

