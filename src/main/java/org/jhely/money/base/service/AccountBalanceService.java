// src/main/java/pro/colubris/hr/todo/service/AccountBalanceService.java
package org.jhely.money.base.service;

import java.math.BigDecimal;
import java.util.Objects;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.jhely.money.base.domain.UserAccount;
import org.jhely.money.base.repository.UserAccountRepository;
import org.jhely.money.base.domain.AccountBalance;
import org.jhely.money.base.repository.AccountBalanceRepository;

@Service
public class AccountBalanceService {

  private static final String USD = "USD";
  private static final BigDecimal WELCOME_USD_CREDIT = new BigDecimal("0.10");

  private final AccountBalanceRepository balances;
  private final UserAccountRepository users;

  public AccountBalanceService(AccountBalanceRepository balances, UserAccountRepository users) {
    this.balances = balances;
    this.users = users;
  }

  // ---- Read helpers

  @Transactional(readOnly = true)
  public BigDecimal getUsdBalanceFor(String userEmail) {
    UserAccount user = requireUser(userEmail);
    return balances.findByUserAndCurrency(user, USD)
                   .map(AccountBalance::getAmount)
                   .orElse(BigDecimal.ZERO);
  }

   /**
   * Grants a one-time welcome credit if the user does not yet have a USD balance row.
   * Returns true if credit was granted in this call, false otherwise.
   */
  @Transactional
  public boolean grantWelcomeUsdIfAbsent(String userEmail) {
    UserAccount user = requireUser(userEmail);
    return balances.findByUserAndCurrency(user, USD)
        .map(bal -> false) // already has USD row -> do nothing
        .orElseGet(() -> {
          balances.save(new AccountBalance(user, USD, WELCOME_USD_CREDIT));
          return true;
        });
  }

  // ---- Mutations

  @Transactional
  public BigDecimal creditUsd(String userEmail, BigDecimal amount) {
    validateAmount(amount);
    AccountBalance bal = ensureRow(userEmail, USD);
    bal.setAmount(bal.getAmount().add(amount));
    balances.save(bal);
    return bal.getAmount();
  }

  @Transactional
  public BigDecimal debitUsd(String userEmail, BigDecimal amount) {
    validateAmount(amount);
    AccountBalance bal = ensureRow(userEmail, USD);
    BigDecimal newAmt = bal.getAmount().subtract(amount);
    if (newAmt.signum() < 0) {
      throw new IllegalArgumentException("Insufficient funds");
    }
    bal.setAmount(newAmt);
    balances.save(bal);
    return bal.getAmount();
  }

  @Transactional
  public BigDecimal setUsdBalance(String userEmail, BigDecimal amount) {
    if (amount == null || amount.signum() < 0 || amount.scale() > 4) {
      throw new IllegalArgumentException("Amount must be non-negative with scale <= 4");
    }
    AccountBalance bal = ensureRow(userEmail, USD);
    bal.setAmount(amount);
    balances.save(bal);
    return bal.getAmount();
  }

  // ---- Internals

  @Transactional
  protected AccountBalance ensureRow(String userEmail, String currency) {
    UserAccount user = requireUser(userEmail);
    return balances.findByUserAndCurrency(user, currency)
        .orElseGet(() -> balances.save(new AccountBalance(user, currency, BigDecimal.ZERO)));
  }

  private UserAccount requireUser(String email) {
    return users.findByEmail(email)
        .orElseThrow(() -> new IllegalArgumentException("User not found: " + email));
  }

  private void validateAmount(BigDecimal amount) {
    if (Objects.isNull(amount) || amount.signum() <= 0 || amount.scale() > 4) {
      throw new IllegalArgumentException("Amount must be positive with scale <= 4");
    }
  }
}
