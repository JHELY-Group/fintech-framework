package org.jhely.money.base.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.jhely.money.base.domain.Coupon;
import org.jhely.money.base.domain.CouponRedemption;
import org.jhely.money.base.domain.UserAccount;

public interface CouponRedemptionRepository extends JpaRepository<CouponRedemption, Long> {
  Optional<CouponRedemption> findByUserAndCoupon(UserAccount user, Coupon coupon);
}

