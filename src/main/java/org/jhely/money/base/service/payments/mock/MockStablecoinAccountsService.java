package org.jhely.money.base.service.payments.mock;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.jhely.money.base.service.payments.mock.PaymentModels.*;

import jakarta.annotation.PostConstruct;
import java.math.BigDecimal;
import java.time.Instant;
import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class MockStablecoinAccountsService {

    private static final Logger log = LoggerFactory.getLogger(MockStablecoinAccountsService.class);

    private final List<FinancialAccount> accounts = new ArrayList<>();
    private final List<Transaction> transactions = new ArrayList<>();
    private final List<PaymentLink> links = new ArrayList<>();

    @PostConstruct
    void seed() {
        FinancialAccount primary = new FinancialAccount();
        primary.id = "acct_prim_001";
        primary.name = "Primary Treasury";
        primary.status = "active";
        primary.balances.add(new Balance(Asset.USDC, new BigDecimal("12500.42"), new BigDecimal("200.00")));
        primary.balances.add(new Balance(Asset.EURC, new BigDecimal("3200.00"), new BigDecimal("0.00")));
        primary.putDepositAddress(Asset.USDC, Network.BASE, "0x7a8B...A91E (Base)");
        accounts.add(primary);

        FinancialAccount ops = new FinancialAccount();
        ops.id = "acct_ops_002";
        ops.name = "Ops Wallet";
        ops.status = "active";
        ops.balances.add(new Balance(Asset.USDC, new BigDecimal("2100.00"), new BigDecimal("0")));
        ops.balances.add(new Balance(Asset.USDT, new BigDecimal("450.00"), new BigDecimal("0")));
        accounts.add(ops);

        // a few seed transactions
        addTx(tx("tx_1001", TxType.DEPOSIT, TxStatus.SETTLED, Asset.USDC, "1000.00", "BASE",
                "0x....", "acct_prim_001", "Client payment INV-3021"));
        addTx(tx("tx_1002", TxType.TRANSFER_OUT, TxStatus.SETTLED, Asset.USDC, "250.00", "BASE",
                "acct_prim_001", "0xReceiver...", "Vendor payout"));
        addTx(tx("tx_1003", TxType.TRANSFER_IN, TxStatus.SETTLED, Asset.EURC, "300.00", "ETHEREUM",
                "0xAff...", "acct_ops_002", "Affiliate settlement"));
    }
    
    public BankDetails ensureBankDetails(String accountId, BankDetails.Rail rail) {
        FinancialAccount acc = getAccount(accountId).orElseThrow();

        return acc.bankDetails.computeIfAbsent(rail, r -> {
            BankDetails d = new BankDetails();
            d.rail = r;
            d.beneficiaryName = acc.name;
            switch (r) {
                case EUR_IBAN -> {
                    d.bankName = "Example Bank Europe";
                    d.iban = genIbanLike();
                    d.bicSwift = "TRWIBEB1XXX";
                    d.reference = "ACCT-" + accountId.substring(Math.max(0, accountId.length()-6));
                }
                case USD_ACH -> {
                    d.bankName = "Example Bank USA";
                    d.accountNumber = genDigits(12);
                    d.routingNumber = genDigits(9);
                    d.accountType = "Checking";
                    d.reference = "ACCT-" + accountId.substring(Math.max(0, accountId.length()-6));
                }
            }
            log.info("Mock generated bank details {} for account={}", r, accountId);
            return d;
        });
    }

    private String genDigits(int n) {
        var r = new StringBuilder();
        var rnd = new java.util.Random();
        for (int i = 0; i < n; i++) r.append(rnd.nextInt(10));
        return r.toString();
    }

    private String genIbanLike() {
        // NOT a valid IBAN generator; just a plausible-looking demo value
        return "DE" + genDigits(2) + "50010517" + genDigits(10);
    }

    private Transaction tx(String id, TxType type, TxStatus st, Asset asset, String amt, String net, String from, String to, String memo) {
        Transaction t = new Transaction();
        t.id = id;
        t.type = type;
        t.status = st;
        t.asset = asset;
        t.amount = new BigDecimal(amt);
        t.network = net;
        t.from = from;
        t.to = to;
        t.memo = memo;
        t.createdAt = Instant.now().minusSeconds(ThreadLocalRandom.current().nextInt(0, 86400 * 15));
        t.fee = new BigDecimal("0.30");
        return t;
    }

    private void addTx(Transaction t) {
        transactions.add(0, t); // newest on top
    }

    /* ---------------- Public API (mock) ---------------- */

    public List<FinancialAccount> listAccounts() {
        return Collections.unmodifiableList(accounts);
    }

    public Optional<FinancialAccount> getAccount(String id) {
        return accounts.stream().filter(a -> a.id.equals(id)).findFirst();
    }

    public FinancialAccount getDefaultAccount() {
        return accounts.get(0);
    }

    public List<Transaction> listTransactions() {
        return Collections.unmodifiableList(transactions);
    }

    public PaymentLink createPaymentLink(String accountId, Asset asset, BigDecimal amount, String memo) {
        PaymentLink pl = new PaymentLink();
        pl.id = "plink_" + UUID.randomUUID().toString().substring(0, 8);
        pl.url = "https://pay.example/" + pl.id;
        pl.asset = asset;
        pl.amount = amount;
        pl.memo = memo;
        pl.createdAt = Instant.now();
        links.add(0, pl);
        log.info("Mock createPaymentLink: account={} asset={} amount={} url={}", accountId, asset, amount, pl.url);
        return pl;
    }

    public String ensureDepositAddress(String accountId, Asset asset, Network network) {
        FinancialAccount acc = getAccount(accountId).orElseThrow();
        return acc.getDepositAddress(asset, network).orElseGet(() -> {
            String addr = switch (network) {
                case BASE, ETHEREUM, POLYGON, ARBITRUM -> "0x" + UUID.randomUUID().toString().replace("-", "").substring(0, 36);
                case SOLANA -> UUID.randomUUID().toString().replace("-", "") + "Sol";
            };
            acc.putDepositAddress(asset, network, addr + " (" + network.name() + ")");
            log.info("Mock ensureDepositAddress: account={} {}@{} -> {}", accountId, asset, network, addr);
            return addr + " (" + network.name() + ")";
        });
    }

    public Transaction sendTransfer(String fromAccountId,
                                    String destType, // "WALLET" or "BANK"
                                    String dest,     // address or IBAN
                                    Asset asset,
                                    BigDecimal amount,
                                    Network network,
                                    String memo) {
        // naive balance deduction
        FinancialAccount from = getAccount(fromAccountId).orElseThrow();
        from.balances.stream().filter(b -> b.asset == asset).findFirst()
                .ifPresent(b -> b.available = b.available.subtract(amount.max(BigDecimal.ZERO)));

        Transaction t = new Transaction();
        t.id = "tx_" + UUID.randomUUID().toString().substring(0, 8);
        t.type = "BANK".equals(destType) ? TxType.PAYOUT : TxType.TRANSFER_OUT;
        t.status = TxStatus.PENDING;
        t.asset = asset;
        t.amount = amount;
        t.network = network.name();
        t.from = fromAccountId;
        t.to = dest;
        t.memo = memo;
        t.createdAt = Instant.now();
        t.fee = new BigDecimal("0.45");

        addTx(t);
        log.info("Mock sendTransfer: {} -> {} {} {} on {} memo={}", fromAccountId, dest, amount, asset, network, memo);
        return t;
    }
}
