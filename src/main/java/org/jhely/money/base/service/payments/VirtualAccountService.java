package org.jhely.money.base.service.payments;

import org.jhely.money.base.domain.BridgeCustomer;
import org.jhely.money.base.repository.BridgeCustomerRepository;
import org.jhely.money.base.service.BridgeApiClientFactory;
import org.jhely.money.sdk.bridge.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing Bridge Virtual Accounts.
 * 
 * Virtual Accounts allow customers to receive fiat (USD via ACH/Wire, EUR via
 * SEPA, MXN via SPEI)
 * and have it automatically converted to stablecoins (USDC/EURC) and delivered
 * to a crypto address.
 * 
 * Prerequisites:
 * - Customer must have completed KYC verification
 * - Customer must have a destination crypto wallet address
 */
@Service
public class VirtualAccountService {

    private static final Logger log = LoggerFactory.getLogger(VirtualAccountService.class);

    private final BridgeApiClientFactory bridgeFactory;
    private final BridgeCustomerRepository bridgeCustomerRepo;

    public VirtualAccountService(BridgeApiClientFactory bridgeFactory,
            BridgeCustomerRepository bridgeCustomerRepo) {
        this.bridgeFactory = bridgeFactory;
        this.bridgeCustomerRepo = bridgeCustomerRepo;
    }

    /**
     * List all virtual accounts for a Bridge customer.
     */
    public List<VirtualAccountResponse> listForCustomer(String bridgeCustomerId) {
        try {
            VirtualAccounts response = bridgeFactory.virtualAccounts()
                    .customersCustomerIDVirtualAccountsGet(bridgeCustomerId, null, 100, null, null);
            return response.getData() != null ? response.getData() : List.of();
        } catch (RestClientResponseException e) {
            log.error("Failed to list virtual accounts for customer {}: {}", bridgeCustomerId, e.getMessage());
            throw new VirtualAccountException("Failed to list virtual accounts", e);
        }
    }

    /**
     * Get a specific virtual account by ID.
     */
    public Optional<VirtualAccountResponse> getAccount(String bridgeCustomerId, String virtualAccountId) {
        try {
            VirtualAccountResponse response = bridgeFactory.virtualAccounts()
                    .customersCustomerIDVirtualAccountsVirtualAccountIDGet(bridgeCustomerId, virtualAccountId);
            return Optional.ofNullable(response);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                return Optional.empty();
            }
            log.error("Failed to get virtual account {} for customer {}: {}",
                    virtualAccountId, bridgeCustomerId, e.getMessage());
            throw new VirtualAccountException("Failed to get virtual account", e);
        }
    }

    /**
     * Create a USD virtual account (receives ACH/Wire, converts to USDC).
     * 
     * @param bridgeCustomerId    Bridge customer ID
     * @param destinationAddress  Crypto wallet address to receive USDC
     * @param destinationChain    Blockchain to deliver to (e.g., "base",
     *                            "ethereum", "polygon", "solana")
     * @param developerFeePercent Optional developer fee percentage (e.g., "0.5" for
     *                            0.5%)
     * @return Created virtual account with bank deposit instructions
     */
    public VirtualAccountResponse createUsdAccount(String bridgeCustomerId,
            String destinationAddress,
            String destinationChain,
            String developerFeePercent) {
        return createAccount(bridgeCustomerId, "usd", "usdc", destinationAddress, destinationChain,
                developerFeePercent);
    }

    /**
     * Create a EUR virtual account (receives SEPA, converts to EURC or USDC).
     * 
     * @param bridgeCustomerId    Bridge customer ID
     * @param destinationCurrency Target stablecoin ("usdc" or "eurc")
     * @param destinationAddress  Crypto wallet address
     * @param destinationChain    Blockchain to deliver to
     * @param developerFeePercent Optional developer fee percentage
     * @return Created virtual account with IBAN deposit instructions
     */
    public VirtualAccountResponse createEurAccount(String bridgeCustomerId,
            String destinationCurrency,
            String destinationAddress,
            String destinationChain,
            String developerFeePercent) {
        return createAccount(bridgeCustomerId, "eur", destinationCurrency, destinationAddress, destinationChain,
                developerFeePercent);
    }

    /**
     * Create a MXN virtual account (receives SPEI, converts to USDC).
     */
    public VirtualAccountResponse createMxnAccount(String bridgeCustomerId,
            String destinationAddress,
            String destinationChain,
            String developerFeePercent) {
        return createAccount(bridgeCustomerId, "mxn", "usdc", destinationAddress, destinationChain,
                developerFeePercent);
    }

    private VirtualAccountResponse createAccount(String bridgeCustomerId,
            String sourceCurrency,
            String destinationCurrency,
            String destinationAddress,
            String destinationChain,
            String developerFeePercent) {
        try {
            // Build source (fiat currency to receive)
            VirtualAccountSourceInput source = new VirtualAccountSourceInput();
            source.setCurrency(EuroInclusiveFiatCurrency.fromValue(sourceCurrency));

            // Build destination (crypto to deliver)
            VirtualAccountDestination destination = new VirtualAccountDestination();
            destination.setCurrency(CryptoCurrency.fromValue(destinationCurrency));
            destination.setPaymentRail(OfframpChain.fromValue(destinationChain));
            destination.setAddress(destinationAddress);

            // Build request
            CreateVirtualAccount request = new CreateVirtualAccount();
            request.setSource(source);
            request.setDestination(destination);
            if (developerFeePercent != null && !developerFeePercent.isBlank()) {
                request.setDeveloperFeePercent(new BigDecimal(developerFeePercent));
            }

            String idempotencyKey = UUID.randomUUID().toString();

            VirtualAccountResponse response = bridgeFactory.virtualAccounts()
                    .customersCustomerIDVirtualAccountsPost(idempotencyKey, bridgeCustomerId, request);

            log.info("Created virtual account {} for customer {} ({}→{} on {})",
                    response.getId(), bridgeCustomerId, sourceCurrency, destinationCurrency, destinationChain);

            return response;
        } catch (RestClientResponseException e) {
            log.error("Failed to create virtual account for customer {}: {} - {}",
                    bridgeCustomerId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new VirtualAccountException("Failed to create virtual account: " + e.getResponseBodyAsString(), e);
        }
    }

    /**
     * Get activity/history for a virtual account.
     */
    public VirtualAccountHistory getActivity(String bridgeCustomerId, String virtualAccountId, int limit) {
        try {
            return bridgeFactory.virtualAccounts()
                    .customersCustomerIDVirtualAccountsVirtualAccountIDHistoryGet(
                            bridgeCustomerId, virtualAccountId,
                            null, null, null, limit, null, null, null);
        } catch (RestClientResponseException e) {
            log.error("Failed to get activity for virtual account {} customer {}: {}",
                    virtualAccountId, bridgeCustomerId, e.getMessage());
            throw new VirtualAccountException("Failed to get virtual account activity", e);
        }
    }

    /**
     * Deactivate a virtual account.
     */
    public void deactivate(String bridgeCustomerId, String virtualAccountId) {
        try {
            String idempotencyKey = UUID.randomUUID().toString();
            bridgeFactory.virtualAccounts()
                    .customersCustomerIDVirtualAccountsVirtualAccountIDDeactivatePost(
                            idempotencyKey, bridgeCustomerId, virtualAccountId);
            log.info("Deactivated virtual account {} for customer {}", virtualAccountId, bridgeCustomerId);
        } catch (RestClientResponseException e) {
            log.error("Failed to deactivate virtual account {} customer {}: {}",
                    virtualAccountId, bridgeCustomerId, e.getMessage());
            throw new VirtualAccountException("Failed to deactivate virtual account", e);
        }
    }

    /**
     * Reactivate a previously deactivated virtual account.
     */
    public void reactivate(String bridgeCustomerId, String virtualAccountId) {
        try {
            String idempotencyKey = UUID.randomUUID().toString();
            bridgeFactory.virtualAccounts()
                    .customersCustomerIDVirtualAccountsVirtualAccountIDReactivatePost(
                            idempotencyKey, bridgeCustomerId, virtualAccountId);
            log.info("Reactivated virtual account {} for customer {}", virtualAccountId, bridgeCustomerId);
        } catch (RestClientResponseException e) {
            log.error("Failed to reactivate virtual account {} customer {}: {}",
                    virtualAccountId, bridgeCustomerId, e.getMessage());
            throw new VirtualAccountException("Failed to reactivate virtual account", e);
        }
    }

    /**
     * Find Bridge customer for a user, required before virtual account operations.
     */
    public Optional<BridgeCustomer> findBridgeCustomer(Long userId) {
        return bridgeCustomerRepo.findByUserId(String.valueOf(userId));
    }

    /**
     * Check if a Bridge customer has completed KYC verification.
     */
    public boolean isKycVerified(BridgeCustomer customer) {
        // Check if customer has approved KYC status
        // Bridge API uses "active" for verified customers
        String status = customer.getKycStatus();
        return "approved".equalsIgnoreCase(status)
                || "verified".equalsIgnoreCase(status)
                || "active".equalsIgnoreCase(status);
    }

    /**
     * Exception for virtual account operations.
     */
    public static class VirtualAccountException extends RuntimeException {
        public VirtualAccountException(String message) {
            super(message);
        }

        public VirtualAccountException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
