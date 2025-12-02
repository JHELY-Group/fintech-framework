package org.jhely.money.base.service.x402;

/**
 * Supported x402 payment networks (Solana only).
 */
public enum X402Network {
    SOLANA_MAINNET("solana", true),
    SOLANA_DEVNET("solana-devnet", false);

    private final String canonicalName;
    private final boolean mainnet;

    X402Network(String canonicalName, boolean mainnet) {
        this.canonicalName = canonicalName;
        this.mainnet = mainnet;
    }

    public String getCanonicalName() {
        return canonicalName;
    }

    public boolean isMainnet() {
        return mainnet;
    }

    public static X402Network fromString(String network) {
        if (network == null) {
            return null;
        }
        String normalized = network.toLowerCase().trim();
        for (X402Network n : values()) {
            if (n.canonicalName.equals(normalized)) {
                return n;
            }
        }
        return null;
    }
}
