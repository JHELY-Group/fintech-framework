package org.jhely.money.base.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.jhely.money.base.domain.UserAccount;

public interface UserAccountRepository extends JpaRepository<UserAccount, Long> {
	Optional<UserAccount> findByEmail(String email);

	boolean existsByEmail(String email);
}
