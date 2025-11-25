package org.jhely.money.base.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;

@Entity
@Table(
    name = "account_balances",
    uniqueConstraints = @UniqueConstraint(name = "uk_balance_user_currency", columnNames = {"user_id", "currency"}),
    indexes = {
        @Index(name = "idx_balance_user", columnList = "user_id"),
        @Index(name = "idx_balance_currency", columnList = "currency")
    }
)
public class AccountBalance {

  @Id
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_balance_user"))
  private UserAccount user;

  @Column(nullable = false, length = 3)
  private String currency;

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal amount = BigDecimal.ZERO;

  @Version
  private Long version;

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @Column(name = "updated_at", nullable = false)
  private Instant updatedAt;

  public AccountBalance() {}

  public AccountBalance(UserAccount user, String currency, BigDecimal amount) {
    this.user = user;
    this.currency = currency;
    this.amount = (amount != null) ? amount : BigDecimal.ZERO;
  }

  @PrePersist
  protected void onCreate() {
    Instant now = Instant.now();
    this.createdAt = now;
    this.updatedAt = now;
  }

  @PreUpdate
  protected void onUpdate() {
    this.updatedAt = Instant.now();
  }

  // Getters / setters
  public Long getId() { return id; }
  public UserAccount getUser() { return user; }
  public void setUser(UserAccount user) { this.user = user; }
  public String getCurrency() { return currency; }
  public void setCurrency(String currency) { this.currency = currency; }
  public BigDecimal getAmount() { return amount; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public Long getVersion() { return version; }
  public Instant getCreatedAt() { return createdAt; }
  public Instant getUpdatedAt() { return updatedAt; }
}
