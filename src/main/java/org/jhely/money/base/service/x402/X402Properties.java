package org.jhely.money.base.service.x402;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Configuration properties for x402 Facilitator.
 * 
 * Configure in application.properties:
 * <pre>
 * x402.solana.rpc.mainnet=https://api.mainnet-beta.solana.com
 * x402.solana.rpc.devnet=https://api.devnet.solana.com
 * x402.solana.usdc-mint.mainnet=EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v
 * x402.solana.usdc-mint.devnet=4zMMC9srt5Ri5X14GAgXhaHii3GnPAEERYPJgZJDncDU
 * x402.solana.commitment=finalized
 * x402.solana.max-confirmation-slots=150
 * </pre>
 */
@Component
@ConfigurationProperties(prefix = "x402")
public class X402Properties {

    private Solana solana = new Solana();

    public static class Solana {
        private Rpc rpc = new Rpc();
        private UsdcMint usdcMint = new UsdcMint();
        private String commitment = "finalized";
        private int maxConfirmationSlots = 150;

        public static class Rpc {
            private String mainnet = "https://api.mainnet-beta.solana.com";
            private String devnet = "https://api.devnet.solana.com";

            public String getMainnet() {
                return mainnet;
            }

            public void setMainnet(String mainnet) {
                this.mainnet = mainnet;
            }

            public String getDevnet() {
                return devnet;
            }

            public void setDevnet(String devnet) {
                this.devnet = devnet;
            }
        }

        public static class UsdcMint {
            private String mainnet = "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v";
            private String devnet = "4zMMC9srt5Ri5X14GAgXhaHii3GnPAEERYPJgZJDncDU";

            public String getMainnet() {
                return mainnet;
            }

            public void setMainnet(String mainnet) {
                this.mainnet = mainnet;
            }

            public String getDevnet() {
                return devnet;
            }

            public void setDevnet(String devnet) {
                this.devnet = devnet;
            }
        }

        public Rpc getRpc() {
            return rpc;
        }

        public void setRpc(Rpc rpc) {
            this.rpc = rpc;
        }

        public UsdcMint getUsdcMint() {
            return usdcMint;
        }

        public void setUsdcMint(UsdcMint usdcMint) {
            this.usdcMint = usdcMint;
        }

        public String getCommitment() {
            return commitment;
        }

        public void setCommitment(String commitment) {
            this.commitment = commitment;
        }

        public int getMaxConfirmationSlots() {
            return maxConfirmationSlots;
        }

        public void setMaxConfirmationSlots(int maxConfirmationSlots) {
            this.maxConfirmationSlots = maxConfirmationSlots;
        }
    }

    public Solana getSolana() {
        return solana;
    }

    public void setSolana(Solana solana) {
        this.solana = solana;
    }
}
