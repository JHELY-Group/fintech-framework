package org.jhely.money.base.service.payments;

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
 * Service for managing Bridge Transfers.
 * 
 * Transfers enable moving funds:
 * - From stablecoin (USDC/EURC) to fiat bank accounts (ACH, Wire, SEPA, SWIFT)
 * - From stablecoin to crypto wallet on various chains
 * - From Bridge Wallet to external destinations
 * 
 * Bridge handles automatic currency conversion (e.g., USDC → USD, EURC → EUR).
 */
@Service
public class TransferService {

    private static final Logger log = LoggerFactory.getLogger(TransferService.class);

    private final BridgeApiClientFactory bridgeFactory;

    public TransferService(BridgeApiClientFactory bridgeFactory) {
        this.bridgeFactory = bridgeFactory;
    }

    // ========== Transfer to External Bank Account ==========

    /**
     * Send USD from Bridge Wallet to a saved external bank account via ACH.
     * 
     * @param bridgeCustomerId   Customer ID (on_behalf_of)
     * @param bridgeWalletId     Source Bridge Wallet ID
     * @param externalAccountId  Destination saved external account ID
     * @param amount             Amount in USD
     * @param developerFee       Optional fixed fee amount
     * @return Transfer response
     */
    public TransfersPost201Response sendToExternalAccountAch(
            String bridgeCustomerId,
            String bridgeWalletId,
            String externalAccountId,
            BigDecimal amount,
            BigDecimal developerFee) {
        
        return sendToExternalAccount(
                bridgeCustomerId, bridgeWalletId, externalAccountId,
                EuroInclusiveCurrency.USDC, // source currency (stablecoin)
                EuroInclusiveCurrency.USD,  // destination currency (fiat)
                BridgeWalletSepaSwiftInclusivePaymentRail.BRIDGE_WALLET, // source rail
                SepaSwiftInclusiveOfframpPaymentRail.ACH, // destination rail
                amount, developerFee, null);
    }

    /**
     * Send USD from Bridge Wallet to a saved external bank account via Wire.
     */
    public TransfersPost201Response sendToExternalAccountWire(
            String bridgeCustomerId,
            String bridgeWalletId,
            String externalAccountId,
            BigDecimal amount,
            BigDecimal developerFee) {
        
        return sendToExternalAccount(
                bridgeCustomerId, bridgeWalletId, externalAccountId,
                EuroInclusiveCurrency.USDC,
                EuroInclusiveCurrency.USD,
                BridgeWalletSepaSwiftInclusivePaymentRail.BRIDGE_WALLET,
                SepaSwiftInclusiveOfframpPaymentRail.WIRE,
                amount, developerFee, null);
    }

    /**
     * Send EUR from Bridge Wallet to a saved external bank account via SEPA.
     */
    public TransfersPost201Response sendToExternalAccountSepa(
            String bridgeCustomerId,
            String bridgeWalletId,
            String externalAccountId,
            BigDecimal amount,
            BigDecimal developerFee) {
        
        return sendToExternalAccount(
                bridgeCustomerId, bridgeWalletId, externalAccountId,
                EuroInclusiveCurrency.EURC,
                EuroInclusiveCurrency.EUR,
                BridgeWalletSepaSwiftInclusivePaymentRail.BRIDGE_WALLET,
                SepaSwiftInclusiveOfframpPaymentRail.SEPA,
                amount, developerFee, null);
    }

    /**
     * Send EUR from Bridge Wallet to a saved external bank account via SWIFT.
     */
    public TransfersPost201Response sendToExternalAccountSwift(
            String bridgeCustomerId,
            String bridgeWalletId,
            String externalAccountId,
            BigDecimal amount,
            BigDecimal developerFee) {
        
        return sendToExternalAccount(
                bridgeCustomerId, bridgeWalletId, externalAccountId,
                EuroInclusiveCurrency.EURC,
                EuroInclusiveCurrency.EUR,
                BridgeWalletSepaSwiftInclusivePaymentRail.BRIDGE_WALLET,
                SepaSwiftInclusiveOfframpPaymentRail.SWIFT,
                amount, developerFee, null);
    }

    /**
     * Send MXN from Bridge Wallet to an external account via SPEI.
     */
    public TransfersPost201Response sendToExternalAccountSpei(
            String bridgeCustomerId,
            String bridgeWalletId,
            String externalAccountId,
            BigDecimal amount,
            BigDecimal developerFee) {
        
        return sendToExternalAccount(
                bridgeCustomerId, bridgeWalletId, externalAccountId,
                EuroInclusiveCurrency.USDC,
                EuroInclusiveCurrency.MXN,
                BridgeWalletSepaSwiftInclusivePaymentRail.BRIDGE_WALLET,
                SepaSwiftInclusiveOfframpPaymentRail.SPEI,
                amount, developerFee, null);
    }

    // ========== Transfer to Crypto Wallet ==========

    /**
     * Send stablecoin from Bridge Wallet to an external crypto wallet address.
     * 
     * @param bridgeCustomerId   Customer ID
     * @param bridgeWalletId     Source Bridge Wallet ID
     * @param destinationAddress Destination wallet address
     * @param currency           Stablecoin to send (USDC, EURC, USDT, etc.)
     * @param chain              Destination blockchain
     * @param amount             Amount to send
     * @param developerFee       Optional fee
     * @return Transfer response
     */
    public TransfersPost201Response sendToCryptoWallet(
            String bridgeCustomerId,
            String bridgeWalletId,
            String destinationAddress,
            EuroInclusiveCurrency currency,
            SepaSwiftInclusiveOfframpPaymentRail chain,
            BigDecimal amount,
            BigDecimal developerFee) {
        return sendToCryptoWallet(bridgeCustomerId, bridgeWalletId, destinationAddress, 
                currency, chain, amount, developerFee, null);
    }

    /**
     * Send stablecoin from Bridge Wallet to an external crypto wallet address.
     * 
     * @param bridgeCustomerId   Customer ID
     * @param bridgeWalletId     Source Bridge Wallet ID
     * @param destinationAddress Destination wallet address
     * @param currency           Stablecoin to send (USDC, EURC, USDT, etc.)
     * @param chain              Destination blockchain
     * @param amount             Amount to send
     * @param developerFee       Optional fee
     * @param blockchainMemo     Optional memo (required for Stellar, optional for Tron)
     * @return Transfer response
     */
    public TransfersPost201Response sendToCryptoWallet(
            String bridgeCustomerId,
            String bridgeWalletId,
            String destinationAddress,
            EuroInclusiveCurrency currency,
            SepaSwiftInclusiveOfframpPaymentRail chain,
            BigDecimal amount,
            BigDecimal developerFee,
            String blockchainMemo) {

        try {
            // Build source (Bridge Wallet)
            TransferRequestSource source = new TransferRequestSource();
            source.setCurrency(currency);
            source.setPaymentRail(BridgeWalletSepaSwiftInclusivePaymentRail.BRIDGE_WALLET);
            source.setBridgeWalletId(bridgeWalletId);

            // Build destination (crypto address on chain)
            TransferRequestDestination destination = new TransferRequestDestination();
            destination.setCurrency(currency);
            destination.setPaymentRail(chain);
            destination.setToAddress(destinationAddress);
            
            // Set memo for Stellar/Tron if provided
            if (blockchainMemo != null && !blockchainMemo.isBlank()) {
                destination.setBlockchainMemo(blockchainMemo);
            }

            // Build request
            TransferRequest request = new TransferRequest();
            request.setOnBehalfOf(bridgeCustomerId);
            request.setAmount(amount.toPlainString());
            request.setSource(source);
            request.setDestination(destination);
            if (developerFee != null && developerFee.compareTo(BigDecimal.ZERO) > 0) {
                request.setDeveloperFee(developerFee.toPlainString());
            }

            String idempotencyKey = UUID.randomUUID().toString();
            TransfersPost201Response response = bridgeFactory.transfers()
                    .transfersPost(idempotencyKey, request);

            log.info("Crypto transfer created: {} {} on {} from wallet {} to {}{}",
                    amount, currency, chain, bridgeWalletId, destinationAddress,
                    blockchainMemo != null ? " (memo: " + blockchainMemo + ")" : "");

            return response;
        } catch (RestClientResponseException e) {
            log.error("Failed to send crypto: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new TransferException("Failed to send to crypto wallet: " + e.getResponseBodyAsString(), e);
        }
    }

    // ========== Transfer with Currency Conversion ==========

    /**
     * Send with automatic currency conversion (e.g., EURC balance → USD bank account).
     * Bridge handles the conversion at market rates.
     * 
     * @param bridgeCustomerId   Customer ID
     * @param bridgeWalletId     Source Bridge Wallet ID
     * @param externalAccountId  Destination bank account
     * @param sourceCurrency     Source stablecoin (what user has)
     * @param destinationCurrency Target fiat currency (what recipient gets)
     * @param destinationRail    Payment rail for destination
     * @param amount             Amount in destination currency
     * @param developerFee       Optional fee
     * @return Transfer response
     */
    public TransfersPost201Response sendWithConversion(
            String bridgeCustomerId,
            String bridgeWalletId,
            String externalAccountId,
            EuroInclusiveCurrency sourceCurrency,
            EuroInclusiveCurrency destinationCurrency,
            SepaSwiftInclusiveOfframpPaymentRail destinationRail,
            BigDecimal amount,
            BigDecimal developerFee) {

        return sendToExternalAccount(
                bridgeCustomerId, bridgeWalletId, externalAccountId,
                sourceCurrency, destinationCurrency,
                BridgeWalletSepaSwiftInclusivePaymentRail.BRIDGE_WALLET,
                destinationRail, amount, developerFee, null);
    }

    // ========== Core Transfer Method ==========

    private TransfersPost201Response sendToExternalAccount(
            String bridgeCustomerId,
            String bridgeWalletId,
            String externalAccountId,
            EuroInclusiveCurrency sourceCurrency,
            EuroInclusiveCurrency destinationCurrency,
            BridgeWalletSepaSwiftInclusivePaymentRail sourceRail,
            SepaSwiftInclusiveOfframpPaymentRail destinationRail,
            BigDecimal amount,
            BigDecimal developerFee,
            String reference) {

        try {
            // Build source
            TransferRequestSource source = new TransferRequestSource();
            source.setCurrency(sourceCurrency);
            source.setPaymentRail(sourceRail);
            source.setBridgeWalletId(bridgeWalletId);

            // Build destination
            TransferRequestDestination destination = new TransferRequestDestination();
            destination.setCurrency(destinationCurrency);
            destination.setPaymentRail(destinationRail);
            destination.setExternalAccountId(externalAccountId);
            if (reference != null && !reference.isBlank()) {
                // Set appropriate reference based on rail
                switch (destinationRail) {
                    case SEPA -> destination.setSepaReference(reference);
                    case SWIFT -> destination.setSwiftReference(reference);
                    case ACH, ACH_SAME_DAY, ACH_PUSH -> destination.setAchReference(reference);
                    case SPEI -> destination.setSpeiReference(reference);
                    default -> destination.setReference(reference);
                }
            }

            // Build request
            TransferRequest request = new TransferRequest();
            request.setOnBehalfOf(bridgeCustomerId);
            request.setAmount(amount.toPlainString());
            request.setSource(source);
            request.setDestination(destination);
            if (developerFee != null && developerFee.compareTo(BigDecimal.ZERO) > 0) {
                request.setDeveloperFee(developerFee.toPlainString());
            }

            String idempotencyKey = UUID.randomUUID().toString();
            TransfersPost201Response response = bridgeFactory.transfers()
                    .transfersPost(idempotencyKey, request);

            log.info("Transfer created: {} {} → {} {} via {} (customer={})",
                    amount, sourceCurrency, destinationCurrency, destinationRail,
                    externalAccountId, bridgeCustomerId);

            return response;
        } catch (RestClientResponseException e) {
            log.error("Transfer failed: {} - {}", e.getStatusCode(), e.getResponseBodyAsString());
            throw new TransferException("Failed to create transfer: " + e.getResponseBodyAsString(), e);
        }
    }

    // ========== Transfer Status ==========

    /**
     * Get a transfer by ID.
     */
    public Optional<TransferResponse> getTransfer(String transferId) {
        try {
            TransferResponse response = bridgeFactory.transfers().transfersTransferIDGet(transferId);
            return Optional.ofNullable(response);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                return Optional.empty();
            }
            throw new TransferException("Failed to get transfer", e);
        }
    }

    /**
     * List transfers for a customer.
     */
    public List<TransferResponse> listTransfers(String bridgeCustomerId, int limit) {
        try {
            Transfers response = bridgeFactory.transfers().transfersGet(limit, null, null, null, null, null, null, null);
            return response.getData() != null ? response.getData() : List.of();
        } catch (RestClientResponseException e) {
            log.error("Failed to list transfers: {}", e.getMessage());
            throw new TransferException("Failed to list transfers", e);
        }
    }

    /**
     * Cancel a pending transfer (must be in awaiting_funds state).
     */
    public void cancelTransfer(String transferId) {
        try {
            bridgeFactory.transfers().transfersTransferIDDelete(transferId);
            log.info("Transfer {} cancelled", transferId);
        } catch (RestClientResponseException e) {
            log.error("Failed to cancel transfer {}: {}", transferId, e.getMessage());
            throw new TransferException("Failed to cancel transfer", e);
        }
    }

    // ========== Helper Methods ==========

    /**
     * Determine the appropriate payment rail for a fiat currency offramp.
     */
    public SepaSwiftInclusiveOfframpPaymentRail suggestRailForCurrency(EuroInclusiveCurrency currency, String countryCode) {
        return switch (currency) {
            case USD -> SepaSwiftInclusiveOfframpPaymentRail.ACH; // Default to ACH for US
            case EUR -> "GB".equalsIgnoreCase(countryCode)
                    ? SepaSwiftInclusiveOfframpPaymentRail.SWIFT
                    : SepaSwiftInclusiveOfframpPaymentRail.SEPA; // SEPA for EU, SWIFT for UK
            case MXN -> SepaSwiftInclusiveOfframpPaymentRail.SPEI;
            default -> SepaSwiftInclusiveOfframpPaymentRail.SWIFT; // Default fallback
        };
    }

    /**
     * Determine which stablecoin to use for a given fiat currency.
     */
    public EuroInclusiveCurrency suggestStablecoinForFiat(EuroInclusiveCurrency fiatCurrency) {
        return switch (fiatCurrency) {
            case EUR -> EuroInclusiveCurrency.EURC;
            case USD, MXN -> EuroInclusiveCurrency.USDC;
            default -> EuroInclusiveCurrency.USDC;
        };
    }

    /**
     * Check if a payment rail is for crypto (on-chain transfer).
     */
    public boolean isCryptoRail(SepaSwiftInclusiveOfframpPaymentRail rail) {
        return switch (rail) {
            case BASE, ETHEREUM, POLYGON, SOLANA, ARBITRUM, OPTIMISM, 
                 AVALANCHE_C_CHAIN, TRON, STELLAR -> true;
            default -> false;
        };
    }

    /**
     * Exception for transfer operations.
     */
    public static class TransferException extends RuntimeException {
        public TransferException(String message) {
            super(message);
        }

        public TransferException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
