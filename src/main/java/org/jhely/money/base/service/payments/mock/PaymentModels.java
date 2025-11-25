package org.jhely.money.base.service.payments.mock;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;

public final class PaymentModels {
    private PaymentModels() {}

    public enum Asset { USDC, USDT, EURC }
    public enum Network { BASE, ETHEREUM, SOLANA, POLYGON, ARBITRUM }
    public enum TxType { DEPOSIT, TRANSFER_IN, TRANSFER_OUT, PAYOUT }
    public enum TxStatus { PENDING, SETTLED, FAILED, CANCELLED }

    public static class Balance {
        public Asset asset;
        public BigDecimal available = BigDecimal.ZERO;
        public BigDecimal pending = BigDecimal.ZERO;

        public Balance() {}
        public Balance(Asset asset, BigDecimal available, BigDecimal pending) {
            this.asset = asset;
            this.available = available;
            this.pending = pending;
        }
    }

    public static class BankDetails {
        public enum Rail { EUR_IBAN, USD_ACH }

        // Common
        public Rail rail;
        public String beneficiaryName;
        public String bankName;
        public String reference; // optional common reference/memo

        // EUR/IBAN
        public String iban;
        public String bicSwift;

        // USD/ACH
        public String accountNumber;
        public String routingNumber;
        public String accountType; // Checking/Savings
    }

    public static class FinancialAccount {
        public String id;
        public String name;
        public String status;
        public List<Balance> balances = new ArrayList<>();

        public Map<String, String> depositAddresses = new HashMap<>();

        // NEW: bank details by rail
        public Map<BankDetails.Rail, BankDetails> bankDetails = new HashMap<>();

        public String key(Asset a, Network n) { return a.name()+"@"+n.name(); }
        public Optional<String> getDepositAddress(Asset a, Network n) {
            return Optional.ofNullable(depositAddresses.get(key(a, n)));
        }
        public void putDepositAddress(Asset a, Network n, String addr) {
            depositAddresses.put(key(a, n), addr);
        }
    }

    public static class Transaction {
        public String id;
        public Instant createdAt;
        public TxType type;
        public TxStatus status;
        public Asset asset;
        public BigDecimal amount;
        public String network;     // BASE, ETHEREUM...
        public String from;
        public String to;
        public BigDecimal fee = BigDecimal.ZERO;
        public String memo;

        public boolean isCredit() {
            return type == TxType.DEPOSIT || type == TxType.TRANSFER_IN;
        }
    }

    public static class PaymentLink {
        public String id;
        public String url;
        public Asset asset;
        public BigDecimal amount;
        public String memo;
        public Instant createdAt;
    }
}

