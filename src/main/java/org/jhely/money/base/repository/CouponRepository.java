package org.jhely.money.base.repository;


import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import org.jhely.money.base.domain.Coupon;


public interface CouponRepository extends JpaRepository<Coupon, Long> {
  Optional<Coupon> findByCodeIgnoreCaseAndActiveTrue(String code);
}

