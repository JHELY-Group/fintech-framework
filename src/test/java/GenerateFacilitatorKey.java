import org.p2p.solanaj.core.Account;
import org.bitcoinj.core.Base58;

/**
 * Utility to generate a new Solana keypair for the x402 facilitator.
 * Run with: mvn exec:java -Dexec.mainClass=GenerateFacilitatorKey -Dexec.classpathScope=test
 */
public class GenerateFacilitatorKey {
    public static void main(String[] args) {
        // Generate new random keypair
        Account account = new Account();
        
        // Get the 64-byte secret key (32-byte private key + 32-byte public key)
        byte[] secretKey = account.getSecretKey();
        
        // Encode as base58 for storage
        String privateKeyBase58 = Base58.encode(secretKey);
        
        System.out.println("====================================");
        System.out.println("NEW FACILITATOR KEYPAIR GENERATED");
        System.out.println("====================================");
        System.out.println("Public Address: " + account.getPublicKey().toBase58());
        System.out.println("Private Key (base58): " + privateKeyBase58);
        System.out.println("====================================");
        System.out.println("\nTo use this keypair:");
        System.out.println("1. Fund the address with SOL on devnet: https://faucet.solana.com/");
        System.out.println("2. Update the x402_facilitator_config table:");
        System.out.println("   UPDATE x402_facilitator_config SET solana_private_key='" + privateKeyBase58 + "' WHERE id=1;");
        System.out.println("3. Restart the server");
    }
}
