package org.jhely.money.base.service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.jhely.money.sdk.bridge.api.ExternalAccountsApi;
import org.jhely.money.sdk.bridge.model.BankAccountNumberType;
import org.jhely.money.sdk.bridge.model.BankAccountOwnerType;
import org.jhely.money.sdk.bridge.model.CreateExternalAccountInput;
import org.jhely.money.sdk.bridge.model.EuroInclusiveFiatCurrency;
import org.jhely.money.sdk.bridge.model.ExternalAccountResponse;
import org.jhely.money.sdk.bridge.model.IbanBankAccount;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

/**
 * Service for managing Bridge external accounts (bank accounts for fiat
 * transfers).
 */
@Service
public class ExternalAccountService {

    private static final Logger log = LoggerFactory.getLogger(ExternalAccountService.class);

    private final ExternalAccountsApi externalAccountsApi;

    // ISO 3166-1 alpha-2 to alpha-3 country code mapping for common SEPA countries
    private static final Map<String, String> COUNTRY_CODE_MAP = Map.ofEntries(
            Map.entry("AT", "AUT"), // Austria
            Map.entry("BE", "BEL"), // Belgium
            Map.entry("BG", "BGR"), // Bulgaria
            Map.entry("HR", "HRV"), // Croatia
            Map.entry("CY", "CYP"), // Cyprus
            Map.entry("CZ", "CZE"), // Czech Republic
            Map.entry("DK", "DNK"), // Denmark
            Map.entry("EE", "EST"), // Estonia
            Map.entry("FI", "FIN"), // Finland
            Map.entry("FR", "FRA"), // France
            Map.entry("DE", "DEU"), // Germany
            Map.entry("GR", "GRC"), // Greece
            Map.entry("HU", "HUN"), // Hungary
            Map.entry("IE", "IRL"), // Ireland
            Map.entry("IT", "ITA"), // Italy
            Map.entry("LV", "LVA"), // Latvia
            Map.entry("LT", "LTU"), // Lithuania
            Map.entry("LU", "LUX"), // Luxembourg
            Map.entry("MT", "MLT"), // Malta
            Map.entry("NL", "NLD"), // Netherlands
            Map.entry("PL", "POL"), // Poland
            Map.entry("PT", "PRT"), // Portugal
            Map.entry("RO", "ROU"), // Romania
            Map.entry("SK", "SVK"), // Slovakia
            Map.entry("SI", "SVN"), // Slovenia
            Map.entry("ES", "ESP"), // Spain
            Map.entry("SE", "SWE"), // Sweden
            Map.entry("GB", "GBR"), // United Kingdom
            Map.entry("CH", "CHE"), // Switzerland
            Map.entry("NO", "NOR"), // Norway
            Map.entry("IS", "ISL"), // Iceland
            Map.entry("LI", "LIE") // Liechtenstein
    );

    public ExternalAccountService(BridgeApiClientFactory bridgeApiClientFactory) {
        this.externalAccountsApi = bridgeApiClientFactory.externalAccounts();
    }

    /**
     * Create a SEPA external account using IBAN.
     *
     * @param customerId       Bridge customer ID
     * @param accountOwnerName Full name of the account holder
     * @param firstName        First name of the account holder
     * @param lastName         Last name of the account holder
     * @param iban             International Bank Account Number
     * @param bic              Bank Identifier Code (optional)
     * @return the created external account response
     */
    public ExternalAccountResponse createSepaExternalAccount(
            String customerId,
            String accountOwnerName,
            String firstName,
            String lastName,
            String iban,
            String bic) {

        String countryAlpha3 = extractCountryFromIban(iban);
        log.info("Creating SEPA external account for customer {} with IBAN country {}", customerId, countryAlpha3);

        IbanBankAccount ibanAccount = new IbanBankAccount()
                .accountNumber(iban.replaceAll("\\s+", "").toUpperCase())
                .country(countryAlpha3);

        if (bic != null && !bic.isBlank()) {
            ibanAccount.bic(bic.toUpperCase());
        }

        CreateExternalAccountInput input = new CreateExternalAccountInput()
                .currency(EuroInclusiveFiatCurrency.EUR)
                .accountType(BankAccountNumberType.IBAN)
                .accountOwnerName(accountOwnerName)
                .accountOwnerType(BankAccountOwnerType.INDIVIDUAL)
                .firstName(firstName)
                .lastName(lastName)
                .iban(ibanAccount);

        String idempotencyKey = UUID.randomUUID().toString();

        try {
            ExternalAccountResponse response = externalAccountsApi.customersCustomerIDExternalAccountsPost(
                    customerId,
                    idempotencyKey,
                    input,
                    null, null, null);
            log.info("Created external account {} for customer {}",
                    response.getId(), customerId);
            return response;
        } catch (Exception e) {
            log.error("Failed to create external account for customer {}: {}", customerId, e.getMessage(), e);
            throw new RuntimeException("Failed to create external account: " + e.getMessage(), e);
        }
    }

    /**
     * List all external accounts for a customer.
     *
     * @param customerId Bridge customer ID
     * @return list of external account responses
     */
    public List<ExternalAccountResponse> listExternalAccounts(String customerId) {
        log.debug("Listing external accounts for customer {}", customerId);
        try {
            var response = externalAccountsApi.customersCustomerIDExternalAccountsGet(
                    customerId, null, null, null);
            return response.getData() != null ? response.getData() : List.of();
        } catch (Exception e) {
            log.warn("Failed to list external accounts for customer {}: {}", customerId, e.getMessage());
            return List.of();
        }
    }

    /**
     * Extract the ISO 3166-1 alpha-3 country code from an IBAN.
     * The first 2 characters of an IBAN are the ISO 3166-1 alpha-2 country code,
     * which is then converted to alpha-3 as required by Bridge API.
     *
     * @param iban the IBAN to extract the country from
     * @return the ISO 3166-1 alpha-3 country code in uppercase (e.g., "ESP" for
     *         Spain)
     */
    public String extractCountryFromIban(String iban) {
        if (iban == null || iban.length() < 2) {
            throw new IllegalArgumentException("Invalid IBAN: must be at least 2 characters");
        }
        String alpha2 = iban.substring(0, 2).toUpperCase();
        String alpha3 = COUNTRY_CODE_MAP.get(alpha2);
        if (alpha3 == null) {
            throw new IllegalArgumentException("Unknown country code in IBAN: " + alpha2);
        }
        return alpha3;
    }

    /**
     * Get the country name from an IBAN for display purposes.
     *
     * @param iban the IBAN
     * @return human-readable country name or null if unknown
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
            default -> alpha2;
        };
    }
}
