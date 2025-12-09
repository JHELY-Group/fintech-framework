package org.jhely.money.base.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jhely.money.sdk.bridge.api.BridgeWalletsApi;
import org.jhely.money.sdk.bridge.model.BridgeWallet;
import org.jhely.money.sdk.bridge.model.BridgeWalletBalance;
import org.jhely.money.sdk.bridge.model.BridgeWalletChain;
import org.jhely.money.sdk.bridge.model.BridgeWalletWithBalances;
import org.jhely.money.sdk.bridge.model.BridgeWalletsList;
import org.jhely.money.sdk.bridge.model.CreateBridgeWallet;
import org.jhely.money.sdk.bridge.model.CreateBridgeWalletResponse;
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
     * Get all Bridge wallets with their balances for a customer.
     *
     * @param customerId Bridge customer ID
     * @return list of wallets with balances
     */
    public List<BridgeWalletWithBalances> getWalletsWithBalances(String customerId) {
        List<BridgeWalletWithBalances> result = new ArrayList<>();
        try {
            List<BridgeWallet> wallets = listWalletsForCustomer(customerId);
            for (BridgeWallet wallet : wallets) {
                BridgeWalletWithBalances detailed = getWalletWithBalances(customerId, wallet.getId());
                if (detailed != null) {
                    result.add(detailed);
                }
            }
        } catch (Exception e) {
            log.error("Failed to get wallets with balances for customer {}: {}", customerId, e.getMessage());
        }
        return result;
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
     * Create a new Bridge wallet on a specified blockchain.
     *
     * @param customerId Bridge customer ID
     * @param chain      Blockchain to create wallet on (base, ethereum, solana)
     * @return created wallet response
     * @throws WalletCreationException if creation fails
     */
    public CreateBridgeWalletResponse createWallet(String customerId, BridgeWalletChain chain)
            throws WalletCreationException {
        log.info("Creating wallet for customer {} on chain {}", customerId, chain);
        try {
            CreateBridgeWallet request = new CreateBridgeWallet().chain(chain);
            String idempotencyKey = UUID.randomUUID().toString();
            CreateBridgeWalletResponse response = walletsApi.customersCustomerIDWalletsPost(
                    customerId, idempotencyKey, request);
            log.info("Created wallet {} for customer {} on chain {}",
                    response.getId(), customerId, chain);
            return response;
        } catch (Exception e) {
            log.error("Failed to create wallet for customer {} on chain {}: {}",
                    customerId, chain, e.getMessage(), e);
            throw new WalletCreationException("Failed to create wallet: " + e.getMessage(), e);
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

    /**
     * Exception thrown when wallet creation fails.
     */
    public static class WalletCreationException extends Exception {
        public WalletCreationException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
