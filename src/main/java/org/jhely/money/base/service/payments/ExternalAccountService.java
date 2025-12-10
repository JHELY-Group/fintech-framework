package org.jhely.money.base.service.payments;

import org.jhely.money.base.service.BridgeApiClientFactory;
import org.jhely.money.sdk.bridge.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientResponseException;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

/**
 * Service for managing External Accounts (saved bank accounts) in Bridge.
 * 
 * External accounts represent verified bank accounts that customers can send money to.
 * Supported types:
 * - US accounts (account number + routing number, for ACH/Wire)
 * - EU accounts (IBAN, for SEPA)
 * - International accounts (SWIFT/BIC)
 * - MX accounts (CLABE, for SPEI)
 */
@Service
public class ExternalAccountService {

    private static final Logger log = LoggerFactory.getLogger(ExternalAccountService.class);

    private final BridgeApiClientFactory bridgeFactory;

    public ExternalAccountService(BridgeApiClientFactory bridgeFactory) {
        this.bridgeFactory = bridgeFactory;
    }

    // ========== List & Get ==========

    /**
     * List all external accounts for a customer.
     */
    public List<ExternalAccountResponse> listAccounts(String bridgeCustomerId) {
        try {
            ExternalAccount1 response = bridgeFactory.externalAccounts()
                    .customersCustomerIDExternalAccountsGet(bridgeCustomerId, 100, null, null);
            return response.getData() != null ? response.getData() : List.of();
        } catch (RestClientResponseException e) {
            log.error("Failed to list external accounts for customer {}: {}", bridgeCustomerId, e.getMessage());
            throw new ExternalAccountException("Failed to list external accounts", e);
        }
    }

    /**
     * Get a specific external account.
     */
    public Optional<ExternalAccountResponse> getAccount(String bridgeCustomerId, String externalAccountId) {
        try {
            ExternalAccountResponse response = bridgeFactory.externalAccounts()
                    .customersCustomerIDExternalAccountsExternalAccountIDGet(bridgeCustomerId, externalAccountId);
            return Optional.ofNullable(response);
        } catch (RestClientResponseException e) {
            if (e.getStatusCode().value() == 404) {
                return Optional.empty();
            }
            throw new ExternalAccountException("Failed to get external account", e);
        }
    }

    // ========== Create US Account (ACH/Wire) ==========

    /**
     * Create a US bank account (for ACH/Wire transfers).
     * 
     * @param bridgeCustomerId Customer ID
     * @param accountOwnerName Name on the bank account
     * @param routingNumber    ABA routing number (9 digits)
     * @param accountNumber    Bank account number
     * @param accountType      "checking" or "savings"
     * @return Created external account
     */
    public ExternalAccountResponse createUsAccount(
            String bridgeCustomerId,
            String accountOwnerName,
            String routingNumber,
            String accountNumber,
            String accountType) {

        try {
            CreateExternalAccountInput input = new CreateExternalAccountInput();
            input.setCurrency(EuroInclusiveFiatCurrency.USD);
            input.setAccountOwnerName(accountOwnerName);
            input.setRoutingNumber(routingNumber);
            input.setAccountNumber(accountNumber);
            input.setAccountType(BankAccountNumberType.fromValue(accountType.toLowerCase()));
            input.setAccountOwnerType(BankAccountOwnerType.INDIVIDUAL);

            String idempotencyKey = UUID.randomUUID().toString();
            ExternalAccountResponse response = bridgeFactory.externalAccounts()
                    .customersCustomerIDExternalAccountsPost(bridgeCustomerId, idempotencyKey, input, null, null, null);

            log.info("Created US external account {} for customer {}", response.getId(), bridgeCustomerId);
            return response;
        } catch (RestClientResponseException e) {
            log.error("Failed to create US account for customer {}: {} - {}",
                    bridgeCustomerId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalAccountException("Failed to create US account: " + e.getResponseBodyAsString(), e);
        }
    }

    // ========== Create EU Account (SEPA) ==========

    /**
     * Create an EU bank account (for SEPA transfers).
     * 
     * @param bridgeCustomerId Customer ID
     * @param firstName        First name of account owner
     * @param lastName         Last name of account owner
     * @param iban             IBAN
     * @param bic              BIC/SWIFT code (optional for SEPA)
     * @param countryCode      Two-letter country code (e.g., "DE", "FR")
     * @return Created external account
     */
    public ExternalAccountResponse createEuAccount(
            String bridgeCustomerId,
            String firstName,
            String lastName,
            String iban,
            String bic,
            String countryCode) {

        // Sanitize IBAN: remove spaces and convert to uppercase
        String sanitizedIban = iban != null ? iban.replaceAll("\\s+", "").toUpperCase() : "";

        try {
            CreateExternalAccountInput input = new CreateExternalAccountInput();
            input.setCurrency(EuroInclusiveFiatCurrency.EUR);
            input.setAccountOwnerName(firstName + " " + lastName);
            input.setAccountOwnerType(BankAccountOwnerType.INDIVIDUAL);
            input.setAccountType(BankAccountNumberType.IBAN);  // Required for IBAN accounts
            input.setFirstName(firstName);
            input.setLastName(lastName);

            // Create IBAN bank account object
            IbanBankAccount ibanAccount = new IbanBankAccount();
            ibanAccount.setAccountNumber(sanitizedIban);
            if (bic != null && !bic.isBlank()) {
                // Sanitize BIC as well
                ibanAccount.setBic(bic.replaceAll("\\s+", "").toUpperCase());
            }
            // Bridge API requires ISO alpha-3 country code
            String alpha3Country;
            if (countryCode != null && !countryCode.isBlank()) {
                // If country code provided, convert if it's alpha-2
                alpha3Country = countryCode.length() == 2 ? convertAlpha2ToAlpha3(countryCode) : countryCode;
            } else {
                // Extract from IBAN and convert
                alpha3Country = extractCountryFromIban(sanitizedIban);
            }
            ibanAccount.setCountry(alpha3Country);
            input.setIban(ibanAccount);

            String idempotencyKey = UUID.randomUUID().toString();
            ExternalAccountResponse response = bridgeFactory.externalAccounts()
                    .customersCustomerIDExternalAccountsPost(bridgeCustomerId, idempotencyKey, input, null, null, null);

            log.info("Created EU external account {} for customer {}", response.getId(), bridgeCustomerId);
            return response;
        } catch (RestClientResponseException e) {
            log.error("Failed to create EU account for customer {}: {} - {}",
                    bridgeCustomerId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalAccountException("Failed to create EU account: " + e.getResponseBodyAsString(), e);
        }
    }

    // ========== Create MX Account (SPEI) ==========

    /**
     * Create a Mexican bank account (for SPEI transfers).
     * 
     * @param bridgeCustomerId Customer ID
     * @param accountOwnerName Name on the bank account
     * @param clabe            CLABE number (18 digits)
     * @return Created external account
     */
    public ExternalAccountResponse createMxAccount(
            String bridgeCustomerId,
            String accountOwnerName,
            String clabe) {

        try {
            CreateExternalAccountInput input = new CreateExternalAccountInput();
            input.setCurrency(EuroInclusiveFiatCurrency.MXN);
            input.setAccountOwnerName(accountOwnerName);
            input.setAccountOwnerType(BankAccountOwnerType.INDIVIDUAL);

            // Create CLABE bank account object
            ClabeBankAccount clabeAccount = new ClabeBankAccount();
            clabeAccount.setAccountNumber(clabe);
            input.setClabe(clabeAccount);

            String idempotencyKey = UUID.randomUUID().toString();
            ExternalAccountResponse response = bridgeFactory.externalAccounts()
                    .customersCustomerIDExternalAccountsPost(bridgeCustomerId, idempotencyKey, input, null, null, null);

            log.info("Created MX external account {} for customer {}", response.getId(), bridgeCustomerId);
            return response;
        } catch (RestClientResponseException e) {
            log.error("Failed to create MX account for customer {}: {} - {}",
                    bridgeCustomerId, e.getStatusCode(), e.getResponseBodyAsString());
            throw new ExternalAccountException("Failed to create MX account: " + e.getResponseBodyAsString(), e);
        }
    }

    // ========== Delete ==========

    /**
     * Delete an external account.
     */
    public void deleteAccount(String bridgeCustomerId, String externalAccountId) {
        try {
            bridgeFactory.externalAccounts()
                    .customersCustomerIDExternalAccountsExternalAccountIDDelete(bridgeCustomerId, externalAccountId);
            log.info("Deleted external account {} for customer {}", externalAccountId, bridgeCustomerId);
        } catch (RestClientResponseException e) {
            log.error("Failed to delete external account {} for customer {}: {}",
                    externalAccountId, bridgeCustomerId, e.getMessage());
            throw new ExternalAccountException("Failed to delete external account", e);
        }
    }

    // ========== Helpers ==========

    /**
     * Get a display label for an external account.
     */
    public String getDisplayLabel(ExternalAccountResponse account) {
        StringBuilder label = new StringBuilder();
        
        if (account.getBankName() != null) {
            label.append(account.getBankName()).append(" ");
        }
        
        if (account.getLast4() != null) {
            label.append("••••").append(account.getLast4());
        } else if (account.getIban() != null) {
            String ibanStr = account.getIban().toString();
            if (ibanStr.length() > 4) {
                label.append("••••").append(ibanStr.substring(ibanStr.length() - 4));
            }
        }
        
        String currency = account.getCurrency() != null ? account.getCurrency().toString() : "";
        if (!currency.isEmpty()) {
            label.append(" (").append(currency.toUpperCase()).append(")");
        }
        
        return label.toString().trim();
    }

    /**
     * Determine the account type/rail from an external account.
     */
    public String getAccountType(ExternalAccountResponse account) {
        if (account.getRoutingNumber() != null || account.getAccountNumber() != null) {
            return "US (ACH/Wire)";
        } else if (account.getIban() != null) {
            return "EU (SEPA)";
        } else if (account.getClabe() != null) {
            return "MX (SPEI)";
        } else if (account.getSwift() != null) {
            return "International (SWIFT)";
        }
        return "Unknown";
    }

    /**
     * Determine the payment rail based on account and currency.
     */
    public SepaSwiftInclusiveOfframpPaymentRail determinePaymentRail(ExternalAccountResponse account) {
        Object currency = account.getCurrency();
        String currencyStr = currency != null ? currency.toString().toLowerCase() : "";
        
        if (account.getRoutingNumber() != null || account.getAccountNumber() != null) {
            // US account - ACH or Wire
            return SepaSwiftInclusiveOfframpPaymentRail.ACH;
        } else if (account.getIban() != null) {
            // EU account - SEPA
            return SepaSwiftInclusiveOfframpPaymentRail.SEPA;
        } else if (account.getClabe() != null) {
            // Mexican account - SPEI
            return SepaSwiftInclusiveOfframpPaymentRail.SPEI;
        } else if (account.getSwift() != null) {
            // International account - SWIFT
            return SepaSwiftInclusiveOfframpPaymentRail.SWIFT;
        }
        
        // Fallback based on currency
        return switch (currencyStr) {
            case "usd" -> SepaSwiftInclusiveOfframpPaymentRail.ACH;
            case "eur" -> SepaSwiftInclusiveOfframpPaymentRail.SEPA;
            case "mxn" -> SepaSwiftInclusiveOfframpPaymentRail.SPEI;
            default -> SepaSwiftInclusiveOfframpPaymentRail.SWIFT;
        };
    }

    /**
     * Extract country code from IBAN (first 2 characters) and convert to ISO alpha-3.
     * Bridge API requires 3-letter country codes.
     */
    public String extractCountryFromIban(String iban) {
        if (iban != null && iban.length() >= 2) {
            String alpha2 = iban.substring(0, 2).toUpperCase();
            return convertAlpha2ToAlpha3(alpha2);
        }
        return "XXX";
    }

    /**
     * Convert ISO 3166-1 alpha-2 country code to alpha-3.
     * Bridge API requires alpha-3 codes for IBAN country field.
     */
    private String convertAlpha2ToAlpha3(String alpha2) {
        return switch (alpha2) {
            case "ES" -> "ESP";
            case "DE" -> "DEU";
            case "FR" -> "FRA";
            case "IT" -> "ITA";
            case "NL" -> "NLD";
            case "BE" -> "BEL";
            case "PT" -> "PRT";
            case "AT" -> "AUT";
            case "IE" -> "IRL";
            case "GB" -> "GBR";
            case "CH" -> "CHE";
            case "PL" -> "POL";
            case "GR" -> "GRC";
            case "LU" -> "LUX";
            case "FI" -> "FIN";
            case "SE" -> "SWE";
            case "DK" -> "DNK";
            case "NO" -> "NOR";
            case "CZ" -> "CZE";
            case "SK" -> "SVK";
            case "HU" -> "HUN";
            case "RO" -> "ROU";
            case "BG" -> "BGR";
            case "HR" -> "HRV";
            case "SI" -> "SVN";
            case "EE" -> "EST";
            case "LV" -> "LVA";
            case "LT" -> "LTU";
            case "MT" -> "MLT";
            case "CY" -> "CYP";
            case "IS" -> "ISL";
            case "LI" -> "LIE";
            case "MC" -> "MCO";
            case "SM" -> "SMR";
            case "VA" -> "VAT";
            case "AD" -> "AND";
            default -> alpha2 + "X"; // Fallback, will likely fail validation
        };
    }

    /**
     * Get the country name from an IBAN for display purposes.
     *
     * @param iban the IBAN
     * @return human-readable country name or the country code if unknown
     */
    public String getCountryNameFromIban(String iban) {
        if (iban == null || iban.length() < 2) {
            return null;
        }
        String alpha2 = iban.substring(0, 2).toUpperCase();
        return switch (alpha2) {
            case "ES" -> "Spain";
            case "DE" -> "Germany";
            case "FR" -> "France";
            case "IT" -> "Italy";
            case "NL" -> "Netherlands";
            case "BE" -> "Belgium";
            case "PT" -> "Portugal";
            case "AT" -> "Austria";
            case "IE" -> "Ireland";
            case "GB" -> "United Kingdom";
            case "CH" -> "Switzerland";
            case "PL" -> "Poland";
            case "GR" -> "Greece";
            case "LU" -> "Luxembourg";
            case "FI" -> "Finland";
            case "SE" -> "Sweden";
            case "DK" -> "Denmark";
            case "NO" -> "Norway";
            case "CZ" -> "Czech Republic";
            case "SK" -> "Slovakia";
            case "HU" -> "Hungary";
            case "RO" -> "Romania";
            case "BG" -> "Bulgaria";
            case "HR" -> "Croatia";
            case "SI" -> "Slovenia";
            case "EE" -> "Estonia";
            case "LV" -> "Latvia";
            case "LT" -> "Lithuania";
            case "MT" -> "Malta";
            case "CY" -> "Cyprus";
            case "IS" -> "Iceland";
            case "LI" -> "Liechtenstein";
            default -> alpha2;
        };
    }

    /**
     * Exception for external account operations.
     */
    public static class ExternalAccountException extends RuntimeException {
        public ExternalAccountException(String message) {
            super(message);
        }

        public ExternalAccountException(String message, Throwable cause) {
            super(message, cause);
        }
    }
}
