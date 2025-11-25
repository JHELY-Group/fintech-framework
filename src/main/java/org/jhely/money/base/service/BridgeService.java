package org.jhely.money.base.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;


/**
 * Thin domain service wrapping Bridge SDK operations we need.
 */
@Service
public class BridgeService {
    private static final Logger log = LoggerFactory.getLogger(BridgeService.class);

    private final BridgeApiClientFactory clientFactory;

    public BridgeService(BridgeApiClientFactory clientFactory) {
        this.clientFactory = clientFactory;
    }

    /**
     * Example call: simple ping using Exchange Rates endpoint which is lightweight and requires no IDs.
     */
    public boolean pingWalletsTotalBalances() {
        try {
            // Use exchange rates as a lightweight GET to validate auth/base URL wiring
            // Bridge expects lowercase currency codes (e.g., "usd", "eur").
            clientFactory.exchangeRates().exchangeRatesGet("usd", "eur");
            return true; // if no exception, call succeeded (even without body mapping)
        } catch (Exception e) {
            log.warn("Bridge API ping failed: {}", e.getMessage());
            return false;
        }
    }
}
