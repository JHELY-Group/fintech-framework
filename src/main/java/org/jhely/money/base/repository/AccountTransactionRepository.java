package org.jhely.money.base.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import org.jhely.money.base.domain.AccountTransaction;
import org.jhely.money.base.domain.UserAccount;

public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {
  List<AccountTransaction> findByUserOrderByCreatedAtDesc(UserAccount user);

  boolean existsByNote(String note);
}
