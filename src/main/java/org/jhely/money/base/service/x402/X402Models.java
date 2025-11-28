package org.jhely.money.base.service.x402;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.List;

/**
 * x402 protocol data models following the x402 specification.
 * Compatible with x402-express, x402-next, and other x402 clients.
 * 
 * @see <a href="https://x402.org">x402 Protocol</a>
 */
public final class X402Models {
    private X402Models() {
    }

    /**
     * Payment requirements sent by resource server.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaymentRequirements {
        private String scheme;
        private String network;
        private String asset;
        private String recipient;
        @JsonProperty("maxAmountRequired")
        private String maxAmount;
        private String description;
        private String resource;
        private Long expiry;
        private Extra extra;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class Extra {
            private String name;
            private String version;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getVersion() {
                return version;
            }

            public void setVersion(String version) {
                this.version = version;
            }
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getAsset() {
            return asset;
        }

        public void setAsset(String asset) {
            this.asset = asset;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getMaxAmount() {
            return maxAmount;
        }

        public void setMaxAmount(String maxAmount) {
            this.maxAmount = maxAmount;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getResource() {
            return resource;
        }

        public void setResource(String resource) {
            this.resource = resource;
        }

        public Long getExpiry() {
            return expiry;
        }

        public void setExpiry(Long expiry) {
            this.expiry = expiry;
        }

        public Extra getExtra() {
            return extra;
        }

        public void setExtra(Extra extra) {
            this.extra = extra;
        }
    }

    /**
     * Payment payload from client (X-PAYMENT header content, base64 decoded).
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class PaymentPayload {
        private String scheme;
        private String network;
        private String payer;
        private String recipient;
        private String amount;
        private String asset;
        private String signature;
        private Long nonce;
        private Long expiry;
        @JsonProperty("payload")
        private PayloadData payloadData;

        @JsonIgnoreProperties(ignoreUnknown = true)
        public static class PayloadData {
            private String signature;
            private Authorization authorization;

            @JsonIgnoreProperties(ignoreUnknown = true)
            public static class Authorization {
                private String from;
                private String to;
                private String value;
                @JsonProperty("validAfter")
                private Long validAfter;
                @JsonProperty("validBefore")
                private Long validBefore;
                private String nonce;

                public String getFrom() {
                    return from;
                }

                public void setFrom(String from) {
                    this.from = from;
                }

                public String getTo() {
                    return to;
                }

                public void setTo(String to) {
                    this.to = to;
                }

                public String getValue() {
                    return value;
                }

                public void setValue(String value) {
                    this.value = value;
                }

                public Long getValidAfter() {
                    return validAfter;
                }

                public void setValidAfter(Long validAfter) {
                    this.validAfter = validAfter;
                }

                public Long getValidBefore() {
                    return validBefore;
                }

                public void setValidBefore(Long validBefore) {
                    this.validBefore = validBefore;
                }

                public String getNonce() {
                    return nonce;
                }

                public void setNonce(String nonce) {
                    this.nonce = nonce;
                }
            }

            public String getSignature() {
                return signature;
            }

            public void setSignature(String signature) {
                this.signature = signature;
            }

            public Authorization getAuthorization() {
                return authorization;
            }

            public void setAuthorization(Authorization authorization) {
                this.authorization = authorization;
            }
        }

        public String getScheme() {
            return scheme;
        }

        public void setScheme(String scheme) {
            this.scheme = scheme;
        }

        public String getNetwork() {
            return network;
        }

        public void setNetwork(String network) {
            this.network = network;
        }

        public String getPayer() {
            return payer;
        }

        public void setPayer(String payer) {
            this.payer = payer;
        }

        public String getRecipient() {
            return recipient;
        }

        public void setRecipient(String recipient) {
            this.recipient = recipient;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getAsset() {
            return asset;
        }

        public void setAsset(String asset) {
            this.asset = asset;
        }

        public String getSignature() {
            return signature;
        }

        public void setSignature(String signature) {
            this.signature = signature;
        }

        public Long getNonce() {
            return nonce;
        }

        public void setNonce(Long nonce) {
            this.nonce = nonce;
        }

        public Long getExpiry() {
            return expiry;
        }

        public void setExpiry(Long expiry) {
            this.expiry = expiry;
        }

        public PayloadData getPayloadData() {
            return payloadData;
        }

        public void setPayloadData(PayloadData payloadData) {
            this.payloadData = payloadData;
        }
    }

    /**
     * Verification request to facilitator.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class VerifyRequest {
        @JsonProperty("paymentRequirements")
        private PaymentRequirements requirements;
        @JsonProperty("payment")
        private PaymentPayload payload;

        public PaymentRequirements getRequirements() {
            return requirements;
        }

        public void setRequirements(PaymentRequirements requirements) {
            this.requirements = requirements;
        }

        public PaymentPayload getPayload() {
            return payload;
        }

        public void setPayload(PaymentPayload payload) {
            this.payload = payload;
        }
    }

    /**
     * Verification response from facilitator.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class VerifyResponse {
        @JsonProperty("isValid")
        private boolean valid;
        private String error;
        @JsonProperty("errorCode")
        private String errorCode;
        @JsonProperty("invalidReason")
        private String invalidReason;

        public static VerifyResponse valid() {
            var r = new VerifyResponse();
            r.valid = true;
            return r;
        }

        public static VerifyResponse invalid(String error, String errorCode) {
            var r = new VerifyResponse();
            r.valid = false;
            r.error = error;
            r.errorCode = errorCode;
            r.invalidReason = error;
            return r;
        }

        public boolean isValid() {
            return valid;
        }

        public void setValid(boolean valid) {
            this.valid = valid;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }

        public String getInvalidReason() {
            return invalidReason;
        }

        public void setInvalidReason(String invalidReason) {
            this.invalidReason = invalidReason;
        }
    }

    /**
     * Settlement request to facilitator.
     */
    @JsonIgnoreProperties(ignoreUnknown = true)
    public static class SettleRequest {
        @JsonProperty("paymentRequirements")
        private PaymentRequirements requirements;
        @JsonProperty("payment")
        private PaymentPayload payload;

        public PaymentRequirements getRequirements() {
            return requirements;
        }

        public void setRequirements(PaymentRequirements requirements) {
            this.requirements = requirements;
        }

        public PaymentPayload getPayload() {
            return payload;
        }

        public void setPayload(PaymentPayload payload) {
            this.payload = payload;
        }
    }

    /**
     * Settlement response from facilitator.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SettleResponse {
        private boolean success;
        @JsonProperty("transaction")
        private String txHash;
        private Long slot;
        private String error;
        @JsonProperty("errorCode")
        private String errorCode;

        public static SettleResponse success(String txHash, Long slot) {
            var r = new SettleResponse();
            r.success = true;
            r.txHash = txHash;
            r.slot = slot;
            return r;
        }

        public static SettleResponse failure(String error, String errorCode) {
            var r = new SettleResponse();
            r.success = false;
            r.error = error;
            r.errorCode = errorCode;
            return r;
        }

        public boolean isSuccess() {
            return success;
        }

        public void setSuccess(boolean success) {
            this.success = success;
        }

        public String getTxHash() {
            return txHash;
        }

        public void setTxHash(String txHash) {
            this.txHash = txHash;
        }

        public Long getSlot() {
            return slot;
        }

        public void setSlot(Long slot) {
            this.slot = slot;
        }

        public String getError() {
            return error;
        }

        public void setError(String error) {
            this.error = error;
        }

        public String getErrorCode() {
            return errorCode;
        }

        public void setErrorCode(String errorCode) {
            this.errorCode = errorCode;
        }
    }

    /**
     * Supported networks/capabilities response.
     */
    @JsonInclude(JsonInclude.Include.NON_NULL)
    public static class SupportResponse {
        @JsonProperty("x402Version")
        private int x402Version = 1;
        private List<String> networks;
        private List<String> schemes;
        private List<AssetInfo> assets;
        private String version;

        public static class AssetInfo {
            private String network;
            private String asset;

            public AssetInfo() {
            }

            public AssetInfo(String network, String asset) {
                this.network = network;
                this.asset = asset;
            }

            public String getNetwork() {
                return network;
            }

            public void setNetwork(String network) {
                this.network = network;
            }

            public String getAsset() {
                return asset;
            }

            public void setAsset(String asset) {
                this.asset = asset;
            }
        }

        public int getX402Version() {
            return x402Version;
        }

        public void setX402Version(int x402Version) {
            this.x402Version = x402Version;
        }

        public List<String> getNetworks() {
            return networks;
        }

        public void setNetworks(List<String> networks) {
            this.networks = networks;
        }

        public List<String> getSchemes() {
            return schemes;
        }

        public void setSchemes(List<String> schemes) {
            this.schemes = schemes;
        }

        public List<AssetInfo> getAssets() {
            return assets;
        }

        public void setAssets(List<AssetInfo> assets) {
            this.assets = assets;
        }

        public String getVersion() {
            return version;
        }

        public void setVersion(String version) {
            this.version = version;
        }
    }
}
