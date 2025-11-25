package org.jhely.money.base.domain;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.Instant;
import org.jhely.money.base.domain.UserAccount;

@Entity
@Table(name = "account_transactions",
       indexes = {
         @Index(name = "idx_tx_user_time", columnList = "user_id, created_at"),
         @Index(name = "idx_tx_type", columnList = "type")
       })
public class AccountTransaction {

  @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @ManyToOne(optional = false, fetch = FetchType.LAZY)
  @JoinColumn(name = "user_id", nullable = false,
      foreignKey = @ForeignKey(name = "fk_tx_user"))
  private UserAccount user;

  @Column(nullable = false, length = 3)
  private String currency; // "USD"

  @Enumerated(EnumType.STRING)
  @Column(nullable = false, length = 32)
  private AccountTransactionType type;

  @Column(nullable = false, precision = 19, scale = 4)
  private BigDecimal amount; // positive for top-ups, negative for charges

  @Column(length = 255)
  private String note; // e.g. coupon code, reference

  @Column(name = "created_at", nullable = false, updatable = false)
  private Instant createdAt;

  @PrePersist
  void onCreate() { this.createdAt = Instant.now(); }

  public AccountTransaction() {}

  public AccountTransaction(UserAccount user, String currency,
                            AccountTransactionType type,
                            BigDecimal amount, String note) {
    this.user = user;
    this.currency = currency;
    this.type = type;
    this.amount = amount;
    this.note = note;
  }

  // getters
  public Long getId() { return id; }
  public UserAccount getUser() { return user; }
  public String getCurrency() { return currency; }
  public AccountTransactionType getType() { return type; }
  public BigDecimal getAmount() { return amount; }
  public String getNote() { return note; }
  public Instant getCreatedAt() { return createdAt; }

  // setters (optional)
  public void setUser(UserAccount user) { this.user = user; }
  public void setCurrency(String currency) { this.currency = currency; }
  public void setType(AccountTransactionType type) { this.type = type; }
  public void setAmount(BigDecimal amount) { this.amount = amount; }
  public void setNote(String note) { this.note = note; }
}
