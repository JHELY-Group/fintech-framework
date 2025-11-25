package org.jhely.money.base.domain;

import jakarta.persistence.*;
import java.time.Instant;
import org.jhely.money.base.domain.UserAccount;

@Entity
@Table(name = "coupon_redemptions",
       uniqueConstraints = @UniqueConstraint(name = "uk_redemption_user_coupon",
                                             columnNames = {"user_id", "coupon_id"}),
       indexes = @Index(name = "idx_redemption_coupon", columnList = "coupon_id"))
public class CouponRedemption {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_redemption_user"))
  private UserAccount user;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "coupon_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_redemption_coupon"))
  private Coupon coupon;

  @Column(name = "redeemed_at", nullable = false, updatable = false)
  private Instant redeemedAt;

  @PrePersist
  void onCreate() { redeemedAt = Instant.now(); }

  public CouponRedemption() {}

  public CouponRedemption(UserAccount user, Coupon coupon) {
    this.user = user;
    this.coupon = coupon;
  }

  // getters
  public Long getId() { return id; }
  public UserAccount getUser() { return user; }
  public Coupon getCoupon() { return coupon; }
  public Instant getRedeemedAt() { return redeemedAt; }
}
