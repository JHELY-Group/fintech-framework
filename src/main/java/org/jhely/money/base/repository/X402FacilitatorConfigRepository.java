package org.jhely.money.base.repository;

import org.jhely.money.base.domain.X402FacilitatorConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface X402FacilitatorConfigRepository extends JpaRepository<X402FacilitatorConfig, Long> {

    Optional<X402FacilitatorConfig> findByUserId(String userId);

    Optional<X402FacilitatorConfig> findByApiKeyHash(String apiKeyHash);

    boolean existsByUserId(String userId);
}
