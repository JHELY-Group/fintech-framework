package org.jhely.money.base.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(name = "coupons",
       uniqueConstraints = @UniqueConstraint(name = "uk_coupon_code", columnNames = "code"))
public class Coupon {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(nullable = false, length = 64)
  private String code; // case-insensitive semantics handled in service

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal amount; // USD amount to credit

  @Column(name = "max_redemptions", nullable = false)
  private int maxRedemptions;

  @Column(name = "redeemed_count", nullable = false)
  private int redeemedCount = 0;

  @Column(nullable = false)
  private boolean active = true;

  @Version
  private Long version;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  @PrePersist
  void onCreate() {
    Instant now = Instant.now();
    createdAt = now;
    updatedAt = now;
  }

  @PreUpdate
  void onUpdate() { updatedAt = Instant.now(); }

  public Coupon() {}

  public Coupon(String code, BigDecimal amount, int maxRedemptions) {
    this.code = code;
    this.amount = amount;
    this.maxRedemptions = maxRedemptions;
    this.active = true;
  }

  // getters/setters
  public Long getId() { return id; }
  public String getCode() { return code; }
  public void setCode(String code) { this.code = code; }
  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public int getMaxRedemptions() { return maxRedemptions; }
  public void setMaxRedemptions(int maxRedemptions) { this.maxRedemptions = maxRedemptions; }
  public int getRedeemedCount() { return redeemedCount; }
  public void setRedeemedCount(int redeemedCount) { this.redeemedCount = redeemedCount; }
  public boolean isActive() { return active; }
  public void setActive(boolean active) { this.active = active; }
  public Long getVersion() { return version; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
}
