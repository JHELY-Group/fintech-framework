package org.jhely.money.base.repository;

import java.time.Instant;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.transaction.annotation.Transactional;

import org.jhely.money.base.domain.OtpToken;

public interface OtpTokenRepository extends JpaRepository<OtpToken, Long> {
	@Query("select t from OtpToken t where t.email = :email and t.used = false and t.expiresAt > :now order by t.createdAt desc")
	Optional<OtpToken> findFirstByEmailAndUsedFalseAndExpiresAtAfterOrderByCreatedAtDesc(String email, Instant now);

	@Transactional
	@Modifying
	@Query("update OtpToken t set t.used = true where t.email = :email and t.used = false")
	int markAllUsedForEmail(String email);
}
