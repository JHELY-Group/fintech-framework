// src/main/java/pro/colubris/hr/todo/repository/AccountBalanceRepository.java
package org.jhely.money.base.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.jhely.money.base.domain.UserAccount;
import org.jhely.money.base.domain.AccountBalance;

public interface AccountBalanceRepository extends JpaRepository<AccountBalance, Long> {

  Optional<AccountBalance> findByUserAndCurrency(UserAccount user, String currency);

  List<AccountBalance> findByUser(UserAccount user);
}
