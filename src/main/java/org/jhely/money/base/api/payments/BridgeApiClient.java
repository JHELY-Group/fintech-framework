package org.jhely.money.base.api.payments;

import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;

import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

// Generated OpenAPI client (Bridge SDK)
import org.jhely.money.sdk.bridge.api.KycLinksApi;
import org.jhely.money.sdk.bridge.client.ApiClient;
import org.jhely.money.sdk.bridge.model.CreateKycLinks;
import org.jhely.money.sdk.bridge.model.IndividualKycLinkResponse;
import org.jhely.money.sdk.bridge.model.EndorsementType;

public class BridgeApiClient {
	
	private static final Logger log = LoggerFactory.getLogger(BridgeApiClient.class);


    private static final MediaType JSON = MediaType.parse("application/json; charset=utf-8");
    
 // ---- ENDPOINT CONSTANTS (easy to tweak) ----
    private static final String EP_CUSTOMERS       = "/v0/customers";
    private static final String EP_WALLETS         = "/v0/wallets";
    private static final String EP_CAPABILITIES    = "/v0/capabilities";       // rails + supported currencies
    // already used:
    // Removed unused endpoint constants to quiet warnings

    private final OkHttpClient http;
    private final ObjectMapper mapper;
    private final String baseUrl;
    private final String apiKey;

    public BridgeApiClient(String baseUrl, String apiKey) {
        this.baseUrl = baseUrl.endsWith("/") ? baseUrl.substring(0, baseUrl.length()-1) : baseUrl;
        this.apiKey = apiKey;
        this.http = new OkHttpClient.Builder()
                .connectTimeout(Duration.ofSeconds(20))
                .readTimeout(Duration.ofSeconds(60))
                .build();
        this.mapper = new ObjectMapper()
                .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /* ---------- Onboarding ---------- */

    /** Get ToS acceptance URL for a new customer. */
    public String createTosLink() throws IOException {
        Request req = new Request.Builder()
                .url(baseUrl + "/v0/customers/tos_links")
                .post(RequestBody.create(new byte[0], null))
                .header("Api-Key", apiKey)
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .build();
        try (Response res = http.newCall(req).execute()) {
            ensure2xx(res);
            var node = mapper.readTree(res.body().string());
            return node.get("url").asText();
        }
    }

    /** Get KYC link for an existing or new customer. Provide a redirect URI. */
    public String createKycLink(String customerId, String redirectUri) throws IOException {
        var payload = Map.of(
            "customer_id", customerId,            // if you already created a customer
            "redirect_uri", redirectUri           // where Bridge should return the user
        );
        Request req = new Request.Builder()
                .url(baseUrl + "/v0/kyc_links")
                .post(RequestBody.create(mapper.writeValueAsBytes(payload), JSON))
                .header("Api-Key", apiKey)
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .build();
        try (Response res = http.newCall(req).execute()) {
            ensure2xx(res);
            var body = res.body() != null ? res.body().string() : "";
            var node = mapper.readTree(body);
            String link = safeGetText(node, "kyc_link");
            if (link == null) link = safeGetText(node, "url");
            if (link == null || link.isBlank()) {
                throw new IOException("Bridge KYC link missing in response: " + body);
            }
            return link;
        }
    }

    /**
     * Get a KYC link for creating a NEW customer via hosted Bridge flow.
     * Required per spec: email and type ("individual" or "business").
     * Optional: full_name and redirect_uri.
     */
    public String createKycLinkForNewCustomer(String email,
                                              String type,
                                              String fullName,
                                              String redirectUri) throws IOException {
        // Prefer generated API for type-safe request/response
        KycLinksApi api = getGeneratedKycLinksApi();
        CreateKycLinks body = new CreateKycLinks()
                .email(email)
                .type("business".equalsIgnoreCase(type)
                        ? CreateKycLinks.TypeEnum.BUSINESS
                        : CreateKycLinks.TypeEnum.INDIVIDUAL);
        // Bridge requires endorsements to be present; include minimal BASE by default
        body.addEndorsementsItem(EndorsementType.BASE);
        if (fullName != null && !fullName.isBlank()) {
            body.fullName(fullName);
        }
        if (redirectUri != null && !redirectUri.isBlank()) {
            body.redirectUri(redirectUri);
        }

        IndividualKycLinkResponse resp;
        try {
            resp = api.kycLinksPost(UUID.randomUUID().toString(), body);
        } catch (org.springframework.web.client.RestClientResponseException e) {
            // Bridge may return 400 duplicate_record with an existing_kyc_link payload containing the URL
            try {
                int status = e.getStatusCode().value();
                String errBody = e.getResponseBodyAsString();
                if (status == 400 && errBody != null && (errBody.contains("existing_kyc_link") || errBody.contains("duplicate_record"))) {
                    var root = mapper.readTree(errBody);
                    var existing = root.get("existing_kyc_link");
                    if (existing != null && !existing.isNull()) {
                        String link = safeGetText(existing, "kyc_link");
                        if (link == null) link = safeGetText(existing, "url");
                        if (link != null && !link.isBlank()) {
                            log.debug("Bridge returned existing KYC link for {}: {}", email, link);
                            return link;
                        }
                    }
                }
            } catch (Exception parseEx) {
                // fall through to original error handling
                log.debug("Failed to parse Bridge error body for duplicate_record fallback: {}", parseEx.toString());
            }
            throw new IOException("Bridge API error " + e.getStatusCode().value() + ": " + e.getResponseBodyAsString(), e);
        } catch (Exception e) {
            throw new IOException("Bridge API call failed: " + e.getMessage(), e);
        }
        String link = resp != null ? resp.getKycLink() : null;
        if (link == null || link.isBlank()) {
            throw new IOException("Bridge hosted KYC link missing in response model: " + String.valueOf(resp));
        }
        return link;
    }

    private KycLinksApi getGeneratedKycLinksApi() {
        ApiClient gen = new ApiClient();
        // Generated default basePath is https://api.bridge.xyz/v0; ensure we include "/v0" when overriding
        String base = this.baseUrl;
        if (!base.endsWith("/v0")) {
            base = base + "/v0";
        }
        gen.setBasePath(base);
        gen.setApiKey(this.apiKey);
        return new KycLinksApi(gen);
    }

    private static String safeGetText(com.fasterxml.jackson.databind.JsonNode node, String field) {
        if (node == null || field == null) return null;
        var n = node.get(field);
        return (n != null && !n.isNull()) ? n.asText(null) : null;
    }

    /* ---------- Virtual Accounts (Receive) ---------- */

    /** Create a virtual account/IBAN for deposits (ACH/SEPA/MXN, etc.). */
    public VirtualAccount createVirtualAccount(String customerId,
                                               String sourceCurrency,         // e.g. "USD", "EUR", "MXN"
                                               String destinationType,        // e.g. "wallet" or "bank_account"
                                               String destinationWalletId,    // if wallet
                                               String developerFeePercent)    // optional, e.g. "0.1"
            throws IOException {

        var destination = Map.of(
            "type", destinationType,       // "wallet" (on-chain) or "bank_account" (fiat payout)
            "wallet_id", destinationWalletId
        );

        var payload = Map.of(
            "customer_id", customerId,
            "source", Map.of("currency", sourceCurrency),
            "destination", destination,
            "developer_fee_percent", developerFeePercent
        );

        Request req = new Request.Builder()
                .url(baseUrl + "/v0/virtual_accounts")
                .post(RequestBody.create(mapper.writeValueAsBytes(payload), JSON))
                .header("Api-Key", apiKey)
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .build();

        try (Response res = http.newCall(req).execute()) {
            ensure2xx(res);
            return mapper.readValue(res.body().bytes(), VirtualAccount.class);
        }
    }

    /* ---------- Transfers (Send) ---------- */

    /**
     * Create a transfer:
     * - fiat -> crypto (to wallet address)
     * - crypto -> crypto
     * - crypto -> fiat (to bank)
     * - fiat -> fiat
     */
    public Transfer createTransfer(TransferRequest body) throws IOException {
        Request req = new Request.Builder()
                .url(baseUrl + "/v0/transfers")
                .post(RequestBody.create(mapper.writeValueAsBytes(body), JSON))
                .header("Api-Key", apiKey)
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .build();
        try (Response res = http.newCall(req).execute()) {
            ensure2xx(res);
            return mapper.readValue(res.body().bytes(), Transfer.class);
        }
    }

    private static void ensure2xx(Response res) throws IOException {
        if (!res.isSuccessful()) {
            String body = res.body() != null ? res.body().string() : "";
            throw new IOException("Bridge API error " + res.code() + ": " + body);
        }
    }
    
    /* ======================= Customers ======================= */
    
 // BridgeApiClient.java (only the createCustomer bits shown)
    public Customer createCustomer(String fullName,
                                   String email,
                                   String idType,            // optional when Bridge collects PII via hosted flows
                                   String issuingCountryA3,  // optional when Bridge collects PII via hosted flows
                                   String idNumber) throws IOException {

        String url = baseUrl + "/v0/customers";

        String firstName;
        String lastName;
        String input = (fullName != null) ? fullName.trim() : "";

        if (!input.isEmpty() && input.contains(" ")) {
            // Use last space to better capture multi-part first names
            int sp = input.lastIndexOf(' ');
            firstName = input.substring(0, sp).trim();
            lastName  = input.substring(sp + 1).trim();
            if (firstName.isEmpty()) firstName = "Customer";
            if (lastName.isEmpty())  lastName  = "User";
        } else {
            // Neutral placeholders when we don't have a reliable split
            firstName = "Customer";
            lastName  = "User";
        }

        // ---- Optionally add identifying_information only if provided ----
        List<Map<String, Object>> identifyingInfo = new ArrayList<>();
        if (idType != null && issuingCountryA3 != null && idNumber != null
                && !idType.isBlank() && !issuingCountryA3.isBlank() && !idNumber.isBlank()) {
            Map<String, Object> taxId = new HashMap<>();
            taxId.put("type", idType);
            taxId.put("issuing_country", issuingCountryA3.toLowerCase()); // ISO-3166-1 alpha-3, lowercased per docs
            taxId.put("number", idNumber);
            identifyingInfo.add(taxId);
        }

        // Minimal viable payload for individual (extend as needed)
        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "individual");
        body.put("first_name", firstName);
        body.put("last_name", lastName);
        body.put("email", email);
        body.put("endorsements", List.of("base"));                       // optional, but useful
        if (!identifyingInfo.isEmpty()) {
            body.put("identifying_information", identifyingInfo);        // Only when provided
        }

        String json = mapper.writeValueAsString(body);
        log.info("Bridge createCustomer request: {}", json);

        Request req = new Request.Builder()
                .url(url)
                .addHeader("Api-Key", apiKey)
                .addHeader("Content-Type", "application/json")
                .addHeader("Idempotency-Key", UUID.randomUUID().toString())
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        try (Response resp = http.newCall(req).execute()) {
            String respBody = (resp.body() != null) ? resp.body().string() : "";
            log.info("Bridge createCustomer response ({}): {}", resp.code(), respBody);

            if (!resp.isSuccessful()) {
                throw new IOException("Bridge createCustomer failed " + resp.code() + ": " + respBody);
            }
            return mapper.readValue(respBody, Customer.class);
        }
    }

    // More flexible variant allowing multiple identifying_information entries
    public Customer createCustomer(String fullName,
                                   String email,
                                   String birthDate,
                                   List<IdentDoc> identifyingDocs) throws IOException {
        return createCustomer(fullName, email, birthDate, identifyingDocs, null);
    }

    // Variant with residential address
    public Customer createCustomer(String fullName,
                                   String email,
                                   String birthDate,
                                   List<IdentDoc> identifyingDocs,
                                   Address residentialAddress) throws IOException {
        return createCustomer(fullName, email, birthDate, identifyingDocs, residentialAddress, null);
    }

    // Variant including signed agreement id
    public Customer createCustomer(String fullName,
                                   String email,
                                   String birthDate,
                                   List<IdentDoc> identifyingDocs,
                                   Address residentialAddress,
                                   String signedAgreementId) throws IOException {

        String url = baseUrl + "/v0/customers";

        String firstName;
        String lastName;
        String input = (fullName != null) ? fullName.trim() : "";

        if (!input.isEmpty() && input.contains(" ")) {
            int sp = input.lastIndexOf(' ');
            firstName = input.substring(0, sp).trim();
            lastName  = input.substring(sp + 1).trim();
            if (firstName.isEmpty()) firstName = "Customer";
            if (lastName.isEmpty())  lastName  = "User";
        } else {
            firstName = "Customer";
            lastName  = "User";
        }

        Map<String, Object> body = new LinkedHashMap<>();
        body.put("type", "individual");
        body.put("first_name", firstName);
        body.put("last_name", lastName);
        body.put("email", email);
        body.put("endorsements", List.of("base"));
        if (birthDate != null && !birthDate.isBlank()) {
            body.put("birth_date", birthDate); // yyyy-MM-dd
        }
        if (signedAgreementId != null && !signedAgreementId.isBlank()) {
            body.put("signed_agreement_id", signedAgreementId);
        }

        if (residentialAddress != null) {
            Map<String, Object> addr = new LinkedHashMap<>();
            if (residentialAddress.street_line_1 != null && !residentialAddress.street_line_1.isBlank()) addr.put("street_line_1", residentialAddress.street_line_1);
            if (residentialAddress.street_line_2 != null && !residentialAddress.street_line_2.isBlank()) addr.put("street_line_2", residentialAddress.street_line_2);
            if (residentialAddress.city != null && !residentialAddress.city.isBlank()) addr.put("city", residentialAddress.city);

            String countryA3 = residentialAddress.country;
            boolean isUsa = countryA3 != null && countryA3.equalsIgnoreCase("USA");
            // Only include subdivision for US addresses (required there and must be a valid state code)
            if (isUsa && residentialAddress.subdivision != null && !residentialAddress.subdivision.isBlank()) {
                String sub = residentialAddress.subdivision.trim().toUpperCase();
                // US state codes are 2 letters; keep loose but enforce common length
                if (sub.length() == 2) addr.put("subdivision", sub);
            }

            if (residentialAddress.postal_code != null && !residentialAddress.postal_code.isBlank()) addr.put("postal_code", residentialAddress.postal_code);
            if (countryA3 != null && !countryA3.isBlank()) addr.put("country", countryA3.toLowerCase());
            if (!addr.isEmpty()) body.put("residential_address", addr);
        }

        if (identifyingDocs != null && !identifyingDocs.isEmpty()) {
            List<Map<String, Object>> list = new ArrayList<>();
            for (IdentDoc d : identifyingDocs) {
                if (d == null) continue;
                Map<String, Object> m = new LinkedHashMap<>();
                if (d.type != null && !d.type.isBlank()) m.put("type", d.type);
                if (d.issuing_country != null && !d.issuing_country.isBlank()) m.put("issuing_country", d.issuing_country.toLowerCase());
                if (d.number != null && !d.number.isBlank()) m.put("number", d.number);
                if (d.description != null && !d.description.isBlank()) m.put("description", d.description);
                if (d.image_front != null && !d.image_front.isBlank()) m.put("image_front", d.image_front);
                if (d.image_back != null && !d.image_back.isBlank()) m.put("image_back", d.image_back);
                if (!m.isEmpty()) list.add(m);
            }
            if (!list.isEmpty()) body.put("identifying_information", list);
        }

        String json = mapper.writeValueAsString(body);
        log.info("Bridge createCustomer request: {}", json);

        Request req = new Request.Builder()
                .url(url)
                .addHeader("Api-Key", apiKey)
                .addHeader("Content-Type", "application/json")
                .addHeader("Idempotency-Key", UUID.randomUUID().toString())
                .post(RequestBody.create(json, MediaType.parse("application/json")))
                .build();

        try (Response resp = http.newCall(req).execute()) {
            String respBody = (resp.body() != null) ? resp.body().string() : "";
            log.info("Bridge createCustomer response ({}): {}", resp.code(), respBody);

            if (!resp.isSuccessful()) {
                throw new IOException("Bridge createCustomer failed " + resp.code() + ": " + respBody);
            }
            return mapper.readValue(respBody, Customer.class);
        }
    }

    // Minimal doc structure for identifying_information
    public static class IdentDoc {
        public String type;
        public String issuing_country; // ISO3
        public String number;
        public String description;
        public String image_front;
        public String image_back;

        public static IdentDoc of(String type, String issuingCountryA3, String number) {
            IdentDoc d = new IdentDoc();
            d.type = type;
            d.issuing_country = issuingCountryA3;
            d.number = number;
            return d;
        }
    }

    // Residential address shape (Address2025WinterRefresh subset)
    public static class Address {
        public String street_line_1; // required
        public String street_line_2; // optional
        public String city;          // required
        public String subdivision;   // ISO 3166-2 short code (e.g., CA). Required for US.
        public String postal_code;   // required in many countries
        public String country;       // ISO-3166-1 alpha-3

        public static Address of(String line1, String city, String countryA3) {
            Address a = new Address();
            a.street_line_1 = line1;
            a.city = city;
            a.country = countryA3;
            return a;
        }
    }



//    public Customer createCustomer(String email, String name) throws IOException {
//    	return createCustomer(email, name, null, null);
//    }

    public Customer getCustomer(String customerId) throws IOException {
        Request req = new Request.Builder()
                .url(baseUrl + EP_CUSTOMERS + "/" + customerId)
                .get()
                .header("Api-Key", apiKey)
                .build();
        try (Response res = http.newCall(req).execute()) {
            ensure2xx(res);
            return mapper.readValue(res.body().bytes(), Customer.class);
        }
    }

    /* ======================== Wallets ======================== */

    public Wallet createWallet(String customerId, String chain, String assetSymbol) throws IOException {
        var payload = Map.of(
                "customer_id", customerId,
                "chain", chain,            // e.g., "ethereum", "base", "solana"
                "asset", assetSymbol       // e.g., "USDC", "ETH", "SOL"
        );
        Request req = new Request.Builder()
                .url(baseUrl + EP_WALLETS)
                .post(RequestBody.create(mapper.writeValueAsBytes(payload), JSON))
                .header("Api-Key", apiKey)
                .header("Idempotency-Key", UUID.randomUUID().toString())
                .build();
        try (Response res = http.newCall(req).execute()) {
            ensure2xx(res);
            return mapper.readValue(res.body().bytes(), Wallet.class);
        }
    }

    public WalletPage listWallets(String customerId, int page, int pageSize) throws IOException {
        HttpUrl url = HttpUrl.parse(baseUrl + EP_WALLETS).newBuilder()
                .addQueryParameter("customer_id", customerId)
                .addQueryParameter("page", String.valueOf(page))
                .addQueryParameter("page_size", String.valueOf(pageSize))
                .build();
        Request req = new Request.Builder()
                .url(url)
                .get()
                .header("Api-Key", apiKey)
                .build();
        try (Response res = http.newCall(req).execute()) {
            ensure2xx(res);
            return mapper.readValue(res.body().bytes(), WalletPage.class);
        }
    }

    /* ===================== Capabilities ====================== */

    public Capabilities getCapabilities() throws IOException {
        Request req = new Request.Builder()
                .url(baseUrl + EP_CAPABILITIES)
                .get()
                .header("Api-Key", apiKey)
                .build();
        try (Response res = http.newCall(req).execute()) {
            ensure2xx(res);
            return mapper.readValue(res.body().bytes(), Capabilities.class);
        }
    }

    /* ========================= DTOs ========================== */

 // Minimal DTO for /v0/customers
    public static class Customer {
        // keep fields public for simple Jackson mapping
        public String id;
        public String email;
        public String name;        // Bridge may return name OR first/last
        public String first_name;  // sometimes returned
        public String last_name;   // sometimes returned
        public String status;      // e.g., "active", "pending_kyc"
        public String country;     // optional
    }



    public static class Wallet {
        public String id;
        public String customer_id;
        public String chain;  // ethereum, base, solana
        public String asset;  // USDC, ETH, SOL
        public String address;
        public String status; // active, disabled
    }

    public static class WalletPage {
        public java.util.List<Wallet> data;
        public Integer page;
        public Integer page_size;
        public Integer total;
    }

    public static class Capabilities {
        // Keep loose to accommodate API differences
        public java.util.List<String> supported_source_currencies; // ["USD","EUR","MXN",...]
        public java.util.List<String> supported_destination_currencies;
        public java.util.List<String> supported_chains;            // ["ethereum","base","solana",...]
        public java.util.List<String> rails;                       // ["ach","sepa","mxn_spei","swift",...]
    }

    /* ---------- Minimal DTOs (adjust fields as needed) ---------- */

    public static class VirtualAccount {
        public String id;
        public String status; // activated/deactivated
        public String customer_id;
        public SourceDepositInstructions source_deposit_instructions;

        public static class SourceDepositInstructions {
            // IBAN/ACH/MXN etc. Bridge returns rail-specific fields
            public String iban;
            public String account_number;
            public String routing_number;
            public String bank_name;
            public String recipient_name;
            public String reference;
        }
    }

    public static class TransferRequest {
        // shape is flexible; include only what you need
        public Map<String, Object> source;      // e.g., {"type":"wallet","wallet_id": "...", "currency":"USDC"}
        public Map<String, Object> destination; // e.g., {"type":"crypto_address","address":"...","chain":"base"}
        public String amount;                   // as string per API convention
        public String currency;                 // "USD","EUR","USDC",...
        public String customer_id;

        public static TransferRequest of(Map<String,Object> source, Map<String,Object> dest,
                                         String amount, String currency, String customerId) {
            var tr = new TransferRequest();
            tr.source = source;
            tr.destination = dest;
            tr.amount = amount;
            tr.currency = currency;
            tr.customer_id = customerId;
            return tr;
        }
    }

    public static class Transfer {
        public String id;
        public String status;     // pending/completed/failed
        public String amount;
        public String currency;
    }
}
