package org.jhely.money.base.repository;

import org.jhely.money.base.domain.X402ApiRequestLog;
import org.jhely.money.base.domain.X402FacilitatorConfig;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.List;

@Repository
public interface X402ApiRequestLogRepository extends JpaRepository<X402ApiRequestLog, Long> {

    /**
     * Find all logs for a specific facilitator config, ordered by timestamp descending.
     */
    Page<X402ApiRequestLog> findByConfigOrderByTimestampDesc(X402FacilitatorConfig config, Pageable pageable);

    /**
     * Find all logs for a specific facilitator config.
     */
    List<X402ApiRequestLog> findByConfigOrderByTimestampDesc(X402FacilitatorConfig config);

    /**
     * Find logs for a specific endpoint.
     */
    Page<X402ApiRequestLog> findByConfigAndEndpointOrderByTimestampDesc(
            X402FacilitatorConfig config, String endpoint, Pageable pageable);

    /**
     * Find all successful settle transactions (with tx hash).
     */
    @Query("SELECT l FROM X402ApiRequestLog l WHERE l.config = :config AND l.endpoint = 'settle' AND l.success = true AND l.txHash IS NOT NULL ORDER BY l.timestamp DESC")
    Page<X402ApiRequestLog> findSuccessfulSettlements(@Param("config") X402FacilitatorConfig config, Pageable pageable);

    /**
     * Count total requests for a config.
     */
    long countByConfig(X402FacilitatorConfig config);

    /**
     * Count successful requests for a config.
     */
    long countByConfigAndSuccessTrue(X402FacilitatorConfig config);

    /**
     * Count verify requests for a config.
     */
    long countByConfigAndEndpoint(X402FacilitatorConfig config, String endpoint);

    /**
     * Sum of amounts for successful settlements.
     */
    @Query("SELECT COALESCE(SUM(l.amount), 0) FROM X402ApiRequestLog l WHERE l.config = :config AND l.endpoint = 'settle' AND l.success = true")
    BigDecimal sumSuccessfulSettlementAmount(@Param("config") X402FacilitatorConfig config);

    /**
     * Find logs within a time range.
     */
    @Query("SELECT l FROM X402ApiRequestLog l WHERE l.config = :config AND l.timestamp >= :from AND l.timestamp <= :to ORDER BY l.timestamp DESC")
    List<X402ApiRequestLog> findByConfigAndTimestampBetween(
            @Param("config") X402FacilitatorConfig config,
            @Param("from") Instant from,
            @Param("to") Instant to);

    /**
     * Get recent logs (last N).
     */
    List<X402ApiRequestLog> findTop50ByConfigOrderByTimestampDesc(X402FacilitatorConfig config);
}
