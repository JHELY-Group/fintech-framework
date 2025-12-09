package org.jhely.money.base.service;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.jhely.money.sdk.bridge.api.BridgeWalletsApi;
import org.jhely.money.sdk.bridge.model.BridgeWallet;
import org.jhely.money.sdk.bridge.model.BridgeWalletBalance;
import org.jhely.money.sdk.bridge.model.BridgeWalletWithBalances;
import org.jhely.money.sdk.bridge.model.BridgeWalletsList;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for fetching Bridge wallet data and balances.
 */
@Service
public class BridgeWalletService {

    private static final Logger log = LoggerFactory.getLogger(BridgeWalletService.class);

    private final BridgeWalletsApi walletsApi;

    public BridgeWalletService(BridgeApiClientFactory bridgeApiClientFactory) {
        this.walletsApi = bridgeApiClientFactory.wallets();
    }

    /**
     * Get all Bridge wallets for a customer.
     *
     * @param customerId Bridge customer ID
     * @return list of wallet responses
     */
    public List<BridgeWallet> listWalletsForCustomer(String customerId) {
        log.debug("Listing wallets for customer {}", customerId);
        try {
            BridgeWalletsList response = walletsApi.customersCustomerIDWalletsGet(
                    customerId, null, null, null);
            return response.getData() != null ? response.getData() : List.of();
        } catch (Exception e) {
            log.warn("Failed to list wallets for customer {}: {}", customerId, e.getMessage());
            return List.of();
        }
    }

    /**
     * Get a specific wallet with balances.
     *
     * @param customerId Bridge customer ID
     * @param walletId   Bridge wallet ID
     * @return wallet with balances or null if not found
     */
    public BridgeWalletWithBalances getWalletWithBalances(String customerId, String walletId) {
        log.debug("Getting wallet {} for customer {}", walletId, customerId);
        try {
            return walletsApi.customersCustomerIDWalletsBridgeWalletIDGet(customerId, walletId);
        } catch (Exception e) {
            log.warn("Failed to get wallet {} for customer {}: {}", walletId, customerId, e.getMessage());
            return null;
        }
    }

    /**
     * Get aggregated balances across all wallets for a customer, keyed by currency
     * name.
     * Returns balances for currencies like "usdc", "eurc", "usdt".
     *
     * @param customerId Bridge customer ID
     * @return map of currency -> total balance as BigDecimal
     */
    public Map<String, BigDecimal> getAggregatedBalances(String customerId) {
        Map<String, BigDecimal> totals = new HashMap<>();
        totals.put("usdc", BigDecimal.ZERO);
        totals.put("eurc", BigDecimal.ZERO);
        totals.put("usdt", BigDecimal.ZERO);

        try {
            List<BridgeWallet> wallets = listWalletsForCustomer(customerId);
            for (BridgeWallet wallet : wallets) {
                // Get detailed wallet with balances
                BridgeWalletWithBalances detailed = getWalletWithBalances(customerId, wallet.getId());
                if (detailed != null && detailed.getBalances() != null) {
                    for (BridgeWalletBalance balance : detailed.getBalances()) {
                        String currency = balance.getCurrency() != null
                                ? balance.getCurrency().getValue().toLowerCase()
                                : null;
                        if (currency != null && totals.containsKey(currency)) {
                            BigDecimal amount = parseBalance(balance.getBalance());
                            totals.merge(currency, amount, BigDecimal::add);
                        }
                    }
                }
            }
        } catch (Exception e) {
            log.error("Failed to get aggregated balances for customer {}: {}", customerId, e.getMessage());
        }

        return totals;
    }

    /**
     * Parse a balance string to BigDecimal, defaulting to ZERO on error.
     */
    private BigDecimal parseBalance(String balance) {
        if (balance == null || balance.isBlank()) {
            return BigDecimal.ZERO;
        }
        try {
            return new BigDecimal(balance);
        } catch (NumberFormatException e) {
            log.warn("Failed to parse balance '{}': {}", balance, e.getMessage());
            return BigDecimal.ZERO;
        }
    }
}
