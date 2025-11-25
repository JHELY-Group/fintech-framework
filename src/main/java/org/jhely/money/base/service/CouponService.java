package org.jhely.money.base.service;


import java.math.BigDecimal;

import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import org.jhely.money.base.domain.AccountTransaction;
import org.jhely.money.base.domain.AccountTransactionType;
import org.jhely.money.base.domain.Coupon;
import org.jhely.money.base.domain.CouponRedemption;
import org.jhely.money.base.domain.UserAccount;
import org.jhely.money.base.repository.AccountTransactionRepository;
import org.jhely.money.base.repository.CouponRedemptionRepository;
import org.jhely.money.base.repository.CouponRepository;
import org.jhely.money.base.repository.UserAccountRepository;

@Service
public class CouponService {

  private static final String USD = "USD";

  private final CouponRepository coupons;
  private final CouponRedemptionRepository redemptions;
  private final AccountTransactionRepository txRepo;
  private final UserAccountRepository users;
  private final AccountBalanceService balances;

  public CouponService(CouponRepository coupons,
                       CouponRedemptionRepository redemptions,
                       AccountTransactionRepository txRepo,
                       UserAccountRepository users,
                       AccountBalanceService balances) {
    this.coupons = coupons;
    this.redemptions = redemptions;
    this.txRepo = txRepo;
    this.users = users;
    this.balances = balances;
  }

  /**
   * Redeem a coupon for the given user:
   * - user can redeem a given coupon only once
   * - coupon has a global maxRedemptions limit (optimistic locking protects against races)
   * - credits user's USD balance and records an account transaction + redemption row
   */
  @Transactional
  public BigDecimal redeemCoupon(String userEmail, String couponCode) {
    String code = couponCode == null ? "" : couponCode.trim();
    if (code.isEmpty()) {
      throw new IllegalArgumentException("Coupon code is required");
    }

    UserAccount user = users.findByEmail(userEmail)
        .orElseThrow(() -> new IllegalArgumentException("User not found: " + userEmail));

    Coupon coupon = coupons.findByCodeIgnoreCaseAndActiveTrue(code)
        .orElseThrow(() -> new IllegalArgumentException("Coupon not found or inactive"));

    // Check user hasn't used it yet
    redemptions.findByUserAndCoupon(user, coupon).ifPresent(r -> {
      throw new IllegalStateException("You have already used this coupon");
    });

    // Enforce global limit using optimistic locking
    try {
      if (coupon.getRedeemedCount() >= coupon.getMaxRedemptions()) {
        throw new IllegalStateException("Coupon usage limit reached");
      }
      coupon.setRedeemedCount(coupon.getRedeemedCount() + 1);
      coupons.save(coupon); // may throw OptimisticLockingFailureException under race
    } catch (OptimisticLockingFailureException e) {
      throw new IllegalStateException("Coupon has just reached its usage limit, try another one");
    }

    // Credit balance
    BigDecimal newBalance = balances.creditUsd(userEmail, coupon.getAmount());

    // Record redemption + transaction
    redemptions.save(new CouponRedemption(user, coupon));

    AccountTransaction tx = new AccountTransaction(
        user, USD, AccountTransactionType.TOPUP_COUPON, coupon.getAmount(), "Coupon: " + coupon.getCode());
    txRepo.save(tx);

    return newBalance;
  }
}
