"use client";

import { useState, useEffect, useCallback } from "react";
import { 
  Connection, 
  PublicKey, 
  Transaction, 
  TransactionInstruction,
  LAMPORTS_PER_SOL 
} from "@solana/web3.js";
import { 
  TOKEN_PROGRAM_ID,
  ASSOCIATED_TOKEN_PROGRAM_ID,
  getAssociatedTokenAddress,
  createTransferInstruction,
  createAssociatedTokenAccountIdempotentInstruction
} from "@solana/spl-token";
import bs58 from "bs58";

// Phantom wallet types
interface PhantomProvider {
  isPhantom: boolean;
  publicKey: PublicKey | null;
  connect: () => Promise<{ publicKey: PublicKey }>;
  disconnect: () => Promise<void>;
  signMessage: (message: Uint8Array) => Promise<{ signature: Uint8Array }>;
  signTransaction: (transaction: Transaction) => Promise<Transaction>;
}

declare global {
  interface Window {
    phantom?: {
      solana?: PhantomProvider;
    };
  }
}

// x402 SVM Payment payload structure (includes pre-built transaction)
interface PaymentPayload {
  scheme: string;
  network: string;
  payer: string;
  recipient: string;
  amount: string;
  asset: string;
  nonce: string;
  expiry: number;
  signature?: string;
  // x402 SVM: The pre-built, partially-signed transaction
  payload?: {
    transaction: string;  // base64-encoded serialized transaction
    signature?: string;   // base58-encoded payer signature
  };
}

interface PaymentRequirements {
  scheme: string;
  network: string;
  maxAmountRequired: string;
  recipient: string;
  asset: string;
  description?: string;
  resource?: string;
}

export default function WalletPage() {
  // Configuration
  const [facilitatorUrl, setFacilitatorUrl] = useState("http://localhost:10000/api/x402");
  const [apiKey, setApiKey] = useState("");
  const [network, setNetwork] = useState<"solana-devnet" | "solana">("solana-devnet");
  
  // Wallet state
  const [provider, setProvider] = useState<PhantomProvider | null>(null);
  const [connected, setConnected] = useState(false);
  const [publicKey, setPublicKey] = useState<string | null>(null);
  
  // Payment state
  const [recipientAddress, setRecipientAddress] = useState("CdMSMvmQJiPDDAzRTDgtxxBaa64WZqcVR68pu2gT8qXE");
  const [amount, setAmount] = useState("10000"); // 0.01 USDC (6 decimals)
  const [usdcMint, setUsdcMint] = useState("4zMMC9srt5Ri5X14GAgXhaHii3GnPAEERYPJgZJDncDU"); // devnet
  // Facilitator's Solana address (fee payer) - fetched from facilitator info
  const [facilitatorAddress, setFacilitatorAddress] = useState<string | null>(null);
  
  // Flow state
  const [step, setStep] = useState<"config" | "connect" | "sign" | "verify" | "settle" | "complete">("config");
  const [loading, setLoading] = useState<string | null>(null);
  const [error, setError] = useState<string | null>(null);
  
  // Results
  const [facilitatorInfo, setFacilitatorInfo] = useState<string | null>(null);
  const [signedPayload, setSignedPayload] = useState<PaymentPayload | null>(null);
  const [signedAt, setSignedAt] = useState<number | null>(null); // Timestamp when transaction was signed
  const [verifyResult, setVerifyResult] = useState<string | null>(null);
  const [settleResult, setSettleResult] = useState<string | null>(null);
  const [txHash, setTxHash] = useState<string | null>(null);
  const [txStatus, setTxStatus] = useState<string | null>(null);
  const [, forceUpdate] = useState(0); // Force re-render for transaction age timer

  // Check for Phantom wallet on mount
  useEffect(() => {
    const checkPhantom = () => {
      if (window.phantom?.solana?.isPhantom) {
        setProvider(window.phantom.solana);
        // Check if already connected
        if (window.phantom.solana.publicKey) {
          setConnected(true);
          setPublicKey(window.phantom.solana.publicKey.toString());
        }
      }
    };
    
    // Check immediately and after a short delay (for slow loading)
    checkPhantom();
    const timeout = setTimeout(checkPhantom, 500);
    return () => clearTimeout(timeout);
  }, []);

  // Update transaction age display every second when signed
  useEffect(() => {
    if (!signedAt) return;
    const interval = setInterval(() => {
      forceUpdate(n => n + 1);
    }, 1000);
    return () => clearInterval(interval);
  }, [signedAt]);

  // Connect wallet
  const connectWallet = async () => {
    if (!provider) {
      setError("Phantom wallet not found. Please install it from phantom.app");
      return;
    }
    
    setLoading("connect");
    setError(null);
    
    try {
      const { publicKey } = await provider.connect();
      setPublicKey(publicKey.toString());
      setConnected(true);
      setStep("sign");
    } catch (err) {
      setError(`Failed to connect: ${err instanceof Error ? err.message : String(err)}`);
    }
    
    setLoading(null);
  };

  // Disconnect wallet
  const disconnectWallet = async () => {
    if (provider) {
      await provider.disconnect();
      setConnected(false);
      setPublicKey(null);
      setStep("connect");
      setSignedPayload(null);
      setVerifyResult(null);
      setSettleResult(null);
      setTxHash(null);
      setTxStatus(null);
    }
  };

  // Get facilitator info
  const getFacilitatorInfo = async () => {
    setLoading("info");
    try {
      // Fetch with API key to get the facilitator's Solana address
      const res = await fetch(`${facilitatorUrl}/`, {
        credentials: "omit",
        mode: "cors",
        headers: apiKey ? { "X-API-Key": apiKey } : {},
      });
      const data = await res.json();
      setFacilitatorInfo(JSON.stringify(data, null, 2));
      
      // Extract facilitator's Solana address for fee payer setup
      if (data.solanaAddress) {
        setFacilitatorAddress(data.solanaAddress);
      }
      
      setStep("connect");
    } catch (err) {
      setError(`Failed to fetch facilitator info: ${err instanceof Error ? err.message : String(err)}`);
    }
    setLoading(null);
  };

  // Create and sign payment transaction (x402 SVM)
  const signPayment = async () => {
    if (!provider || !publicKey) {
      setError("Wallet not connected");
      return;
    }
    
    if (!facilitatorAddress) {
      setError("Facilitator address not found. Please ensure API key is set and facilitator info fetched.");
      return;
    }
    
    setLoading("sign");
    setError(null);
    
    try {
      // Get RPC endpoint for the network
      const rpcUrl = network === "solana-devnet" 
        ? "https://api.devnet.solana.com"
        : "https://api.mainnet-beta.solana.com";
      const connection = new Connection(rpcUrl, "confirmed");
      
      // Parse addresses
      const payerPubkey = new PublicKey(publicKey);
      const recipientPubkey = new PublicKey(recipientAddress);
      const facilitatorPubkey = new PublicKey(facilitatorAddress);
      const mintPubkey = new PublicKey(usdcMint);
      
      // Get associated token accounts (allowOwnerOffCurve=true to handle all address types)
      const payerTokenAccount = await getAssociatedTokenAddress(
        mintPubkey, 
        payerPubkey,
        true  // allowOwnerOffCurve
      );
      const recipientTokenAccount = await getAssociatedTokenAddress(
        mintPubkey, 
        recipientPubkey,
        true  // allowOwnerOffCurve
      );
      
      // Check if recipient's token account exists, if not we need to create it
      const recipientAccountInfo = await connection.getAccountInfo(recipientTokenAccount);
      
      // Build the transaction with facilitator as fee payer
      const transaction = new Transaction();
      transaction.feePayer = facilitatorPubkey;  // Facilitator pays fees
      
      // Get recent blockhash
      const { blockhash, lastValidBlockHeight } = await connection.getLatestBlockhash("confirmed");
      transaction.recentBlockhash = blockhash;
      transaction.lastValidBlockHeight = lastValidBlockHeight;
      
      // If recipient's token account doesn't exist, add instruction to create it
      // The facilitator (fee payer) will pay for the account creation
      if (!recipientAccountInfo) {
        console.log("Recipient token account doesn't exist, adding create instruction");
        const createAtaInstruction = createAssociatedTokenAccountIdempotentInstruction(
          facilitatorPubkey,      // payer (facilitator pays for account creation)
          recipientTokenAccount,  // associatedToken
          recipientPubkey,        // owner
          mintPubkey              // mint
        );
        transaction.add(createAtaInstruction);
      }
      
      // Create the token transfer instruction
      const amountInSmallestUnit = BigInt(amount);
      const transferInstruction = createTransferInstruction(
        payerTokenAccount,      // source
        recipientTokenAccount,  // destination
        payerPubkey,           // owner/authority (payer signs this)
        amountInSmallestUnit    // amount
      );
      
      // Add the transfer instruction
      transaction.add(transferInstruction);
      
      // Sign the transaction with the payer's key (partial sign)
      // The facilitator will add their signature later
      const signedTransaction = await provider.signTransaction(transaction);
      
      // Serialize the transaction
      const serializedTransaction = signedTransaction.serialize({
        requireAllSignatures: false,  // We only have payer's signature, not fee payer's
        verifySignatures: false,
      });
      const transactionBase64 = Buffer.from(serializedTransaction).toString("base64");
      
      // Get the payer's signature
      const payerSignature = signedTransaction.signatures.find(
        s => s.publicKey.equals(payerPubkey)
      );
      const signatureBase58 = payerSignature?.signature 
        ? bs58.encode(payerSignature.signature)
        : undefined;
      
      // Create payment payload with transaction
      const nonce = `${Date.now()}-${Math.random().toString(36).substring(7)}`;
      const expiry = Math.floor(Date.now() / 1000) + 3600; // 1 hour from now
      
      const payload: PaymentPayload = {
        scheme: "exact",
        network,
        payer: publicKey,
        recipient: recipientAddress,
        amount,
        asset: usdcMint,
        nonce,
        expiry,
        signature: signatureBase58,
        payload: {
          transaction: transactionBase64,
          signature: signatureBase58,
        },
      };
      
      setSignedPayload(payload);
      setSignedAt(Date.now());
      setStep("verify");
    } catch (err) {
      console.error("Sign payment error:", err);
      setError(`Failed to sign: ${err instanceof Error ? err.message : String(err)}`);
    }
    
    setLoading(null);
  };

  // Verify payment with facilitator
  const verifyPayment = async () => {
    if (!signedPayload) {
      setError("No signed payload");
      return;
    }
    
    setLoading("verify");
    setError(null);
    
    try {
      const requirements: PaymentRequirements = {
        scheme: "exact",
        network,
        maxAmountRequired: amount,
        recipient: recipientAddress,
        asset: usdcMint,
        description: "x402 Test Payment",
        resource: "/api/test",
      };
      
      const res = await fetch(`${facilitatorUrl}/verify`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-API-Key": apiKey,
        },
        body: JSON.stringify({
          x402Version: 1,
          paymentRequirements: requirements,
          payment: signedPayload,
        }),
        credentials: "omit", // Don't send cookies - we use API key auth
        redirect: "error",
      });
      
      const data = await res.json();
      setVerifyResult(JSON.stringify(data, null, 2));
      
      if (data.isValid) {
        setStep("settle");
      }
    } catch (err) {
      setError(`Verify failed: ${err instanceof Error ? err.message : String(err)}`);
    }
    
    setLoading(null);
  };

  // Settle payment
  const settlePayment = async () => {
    if (!signedPayload) {
      setError("No signed payload");
      return;
    }
    
    setLoading("settle");
    setError(null);
    
    try {
      const requirements: PaymentRequirements = {
        scheme: "exact",
        network,
        maxAmountRequired: amount,
        recipient: recipientAddress,
        asset: usdcMint,
      };
      
      const res = await fetch(`${facilitatorUrl}/settle`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-API-Key": apiKey,
        },
        body: JSON.stringify({
          x402Version: 1,
          paymentRequirements: requirements,
          payment: signedPayload,
        }),
        credentials: "omit",
        mode: "cors",
      });
      
      const data = await res.json();
      setSettleResult(JSON.stringify(data, null, 2));
      
      if (data.success && data.txHash) {
        setTxHash(data.txHash);
        setStep("complete");
      }
    } catch (err) {
      setError(`Settle failed: ${err instanceof Error ? err.message : String(err)}`);
    }
    
    setLoading(null);
  };

  // Get transaction status
  const getTransactionStatus = async () => {
    if (!txHash) {
      setError("No transaction hash");
      return;
    }
    
    setLoading("txStatus");
    setError(null);
    
    try {
      const res = await fetch(`${facilitatorUrl}/transaction/${txHash}`, {
        headers: {
          "X-API-Key": apiKey,
        },
        credentials: "omit",
        mode: "cors",
      });
      
      const data = await res.json();
      setTxStatus(JSON.stringify(data, null, 2));
    } catch (err) {
      setError(`Failed to get tx status: ${err instanceof Error ? err.message : String(err)}`);
    }
    
    setLoading(null);
  };

  // Calculate transaction age in seconds
  const getTransactionAge = (): number => {
    if (!signedAt) return 0;
    return Math.floor((Date.now() - signedAt) / 1000);
  };

  // Check if transaction is likely expired (Solana blockhashes expire in ~60-90 seconds)
  const isTransactionExpired = (): boolean => {
    return getTransactionAge() > 45; // Warn after 45 seconds, likely expired after 60-90
  };

  // Re-sign and settle in one atomic operation
  const resignAndSettle = async () => {
    // First re-sign
    await signPayment();
    // Then immediately settle (signPayment sets signedPayload)
  };

  // Reset flow
  const resetFlow = () => {
    setStep("config");
    setSignedPayload(null);
    setSignedAt(null);
    setVerifyResult(null);
    setSettleResult(null);
    setTxHash(null);
    setTxStatus(null);
    setError(null);
  };

  return (
    <div className="container">
      <header className="header">
        <h1>🔐 x402 Wallet Integration</h1>
        <p>End-to-end test with Phantom wallet signing</p>
        <a href="/" style={{ color: "#00d4ff", fontSize: "0.9rem" }}>
          ← Back to API tester
        </a>
      </header>

      {/* Error display */}
      {error && (
        <div className="result error" style={{ marginBottom: "1rem" }}>
          ❌ {error}
          <button 
            onClick={() => setError(null)} 
            style={{ marginLeft: "1rem", padding: "0.25rem 0.5rem", cursor: "pointer" }}
          >
            Dismiss
          </button>
        </div>
      )}

      {/* Step 1: Configuration */}
      <section className={`card ${step === "config" ? "active" : ""}`}>
        <h2>1️⃣ Configuration</h2>
        
        <div className="flex-row" style={{ marginTop: "1rem" }}>
          <div className="input-group">
            <label>Facilitator URL</label>
            <input
              type="text"
              value={facilitatorUrl}
              onChange={(e) => setFacilitatorUrl(e.target.value)}
            />
          </div>
          <div className="input-group">
            <label>API Key</label>
            <input
              type="password"
              value={apiKey}
              onChange={(e) => setApiKey(e.target.value)}
              placeholder="x402_..."
            />
          </div>
        </div>

        <div className="flex-row" style={{ marginTop: "1rem" }}>
          <div className="input-group">
            <label>Network</label>
            <select 
              value={network} 
              onChange={(e) => {
                setNetwork(e.target.value as "solana-devnet" | "solana");
                // Update USDC mint based on network
                setUsdcMint(e.target.value === "solana" 
                  ? "EPjFWdd5AufqSSqeM2qN1xzybapC8G4wEGGkZwyTDt1v" 
                  : "4zMMC9srt5Ri5X14GAgXhaHii3GnPAEERYPJgZJDncDU"
                );
              }}
              style={{ padding: "0.75rem", borderRadius: "8px", border: "1px solid #333", background: "#1a1a2e", color: "white" }}
            >
              <option value="solana-devnet">Solana Devnet</option>
              <option value="solana">Solana Mainnet</option>
            </select>
          </div>
          <div className="input-group">
            <label>Recipient Address</label>
            <input
              type="text"
              value={recipientAddress}
              onChange={(e) => setRecipientAddress(e.target.value)}
            />
          </div>
        </div>

        <div className="flex-row" style={{ marginTop: "1rem" }}>
          <div className="input-group">
            <label>Amount (USDC atomic units, 6 decimals)</label>
            <input
              type="text"
              value={amount}
              onChange={(e) => setAmount(e.target.value)}
              placeholder="1000000 = 1 USDC"
            />
            <small style={{ color: "#888" }}>
              {(parseInt(amount) / 1_000_000).toFixed(6)} USDC
            </small>
          </div>
          <div className="input-group">
            <label>USDC Mint Address</label>
            <input
              type="text"
              value={usdcMint}
              onChange={(e) => setUsdcMint(e.target.value)}
              disabled
            />
          </div>
        </div>

        <button
          className="button"
          onClick={getFacilitatorInfo}
          disabled={loading === "info" || !apiKey}
          style={{ marginTop: "1rem" }}
        >
          {loading === "info" ? "Loading..." : "GET / - Fetch Facilitator Info"}
        </button>

        {facilitatorInfo && (
          <div className="result success" style={{ marginTop: "1rem" }}>
            <strong>GET / Response:</strong>
            <pre>{facilitatorInfo}</pre>
          </div>
        )}
      </section>

      {/* Step 2: Connect Wallet */}
      <section className={`card ${step === "connect" ? "active" : ""}`}>
        <h2>2️⃣ Connect Phantom Wallet</h2>
        
        {!provider ? (
          <div className="result info">
            <p>Phantom wallet not detected.</p>
            <a 
              href="https://phantom.app" 
              target="_blank" 
              rel="noopener noreferrer"
              style={{ color: "#00d4ff" }}
            >
              Install Phantom →
            </a>
          </div>
        ) : connected ? (
          <div>
            <div className="result success">
              <p>✅ Connected: <code>{publicKey}</code></p>
            </div>
            <div style={{ display: "flex", gap: "1rem", marginTop: "1rem" }}>
              <button className="button" onClick={() => setStep("sign")}>
                Continue to Sign →
              </button>
              <button 
                className="button" 
                onClick={disconnectWallet}
                style={{ background: "#666" }}
              >
                Disconnect
              </button>
            </div>
          </div>
        ) : (
          <button
            className="button"
            onClick={connectWallet}
            disabled={loading === "connect"}
          >
            {loading === "connect" ? "Connecting..." : "Connect Phantom"}
          </button>
        )}
        
        <p style={{ marginTop: "1rem", fontSize: "0.85rem", color: "#888" }}>
          Make sure Phantom is set to <strong>{network === "solana-devnet" ? "Devnet" : "Mainnet"}</strong> network.
        </p>
      </section>

      {/* Step 3: Sign Payment */}
      <section className={`card ${step === "sign" ? "active" : ""}`}>
        <h2>3️⃣ Sign Payment Payload</h2>
        <p>Sign the x402 payment payload with your wallet.</p>
        
        <div className="code-block" style={{ marginTop: "1rem" }}>
          <strong>Payload to sign:</strong>
          <pre>{JSON.stringify({
            scheme: "exact",
            network,
            payer: publicKey || "<your-wallet>",
            recipient: recipientAddress,
            amount,
            asset: usdcMint,
            nonce: "<generated>",
            expiry: "<1 hour from now>",
          }, null, 2)}</pre>
        </div>

        <button
          className="button"
          onClick={signPayment}
          disabled={loading === "sign" || !connected}
          style={{ marginTop: "1rem" }}
        >
          {loading === "sign" ? "Signing..." : "Sign with Phantom"}
        </button>

        {signedPayload && (
          <div className="result success" style={{ marginTop: "1rem" }}>
            <strong>✅ Signed Payload:</strong>
            {signedAt && (Date.now() - signedAt) > 45000 && (
              <div style={{ background: "#ff6b00", color: "white", padding: "0.5rem", borderRadius: "4px", marginBottom: "0.5rem" }}>
                ⚠️ Warning: Transaction signed {Math.floor((Date.now() - signedAt) / 1000)}s ago. 
                Solana blockhashes expire after ~60s. If settlement fails, click "Re-sign Transaction" below.
              </div>
            )}
            <pre>{JSON.stringify(signedPayload, null, 2)}</pre>
            {signedAt && (Date.now() - signedAt) > 30000 && (
              <button
                className="button"
                onClick={signPayment}
                style={{ marginTop: "0.5rem", background: "#ff6b00" }}
              >
                🔄 Re-sign Transaction (get fresh blockhash)
              </button>
            )}
          </div>
        )}
      </section>

      {/* Step 4: Verify Payment */}
      <section className={`card ${step === "verify" ? "active" : ""}`}>
        <h2>4️⃣ Verify Payment (POST /verify)</h2>
        <p>Send the signed payload to the facilitator for verification.</p>
        
        {/* Transaction age timer */}
        {signedAt && (
          <div className={`result ${isTransactionExpired() ? "error" : getTransactionAge() > 30 ? "warning" : "info"}`} style={{ marginBottom: "1rem" }}>
            ⏱️ Transaction age: <strong>{getTransactionAge()}s</strong> / ~60-90s max
            {isTransactionExpired() && " ⚠️ EXPIRED - Go back and re-sign!"}
            {!isTransactionExpired() && getTransactionAge() > 30 && " (Hurry! Getting old...)"}
          </div>
        )}
        
        <button
          className="button"
          onClick={verifyPayment}
          disabled={loading === "verify" || !signedPayload || isTransactionExpired()}
          style={{ marginTop: "1rem" }}
        >
          {loading === "verify" ? "Verifying..." : "POST /verify - Verify Payment"}
        </button>

        {verifyResult && (
          <div className={`result ${verifyResult.includes('"isValid":true') || verifyResult.includes('"isValid": true') ? "success" : "error"}`} style={{ marginTop: "1rem" }}>
            <strong>POST /verify Response:</strong>
            <pre>{verifyResult}</pre>
          </div>
        )}
      </section>

      {/* Step 5: Settle Payment */}
      <section className={`card ${step === "settle" ? "active" : ""}`}>
        <h2>5️⃣ Settle Payment (POST /settle)</h2>
        <p>Execute the payment on-chain.</p>
        
        {/* Transaction age warning */}
        {signedAt && (
          <div className={`result ${isTransactionExpired() ? "error" : "info"}`} style={{ marginBottom: "1rem" }}>
            {isTransactionExpired() ? (
              <>
                ⏰ <strong>Transaction likely expired!</strong> Signed {getTransactionAge()} seconds ago.
                Solana blockhashes expire in ~60-90 seconds. Please re-sign the transaction.
              </>
            ) : (
              <>
                ⏱️ Transaction signed {getTransactionAge()} seconds ago.
                {getTransactionAge() > 30 && " (Getting old - settle soon!)"}
              </>
            )}
          </div>
        )}
        
        <div className="result info" style={{ marginBottom: "1rem" }}>
          ⚠️ <strong>Note:</strong> Settlement requires the payer to have USDC tokens on {network}.
          For devnet, you can get test USDC from faucets.
        </div>

        <div style={{ display: "flex", gap: "1rem", flexWrap: "wrap" }}>
          <button
            className="button"
            onClick={settlePayment}
            disabled={loading === "settle" || !signedPayload || isTransactionExpired()}
            style={{ marginTop: "1rem" }}
          >
            {loading === "settle" ? "Settling..." : "POST /settle - Settle On-Chain"}
          </button>
          
          {isTransactionExpired() && (
            <button
              className="button"
              onClick={() => { setStep("sign"); }}
              style={{ marginTop: "1rem", background: "#ff6b35" }}
            >
              ↩️ Go Back to Re-sign
            </button>
          )}
        </div>

        {settleResult && (
          <div className={`result ${settleResult.includes('"success":true') || settleResult.includes('"success": true') ? "success" : "error"}`} style={{ marginTop: "1rem" }}>
            <strong>POST /settle Response:</strong>
            <pre>{settleResult}</pre>
          </div>
        )}
      </section>

      {/* Step 6: Transaction Status */}
      <section className={`card ${step === "complete" ? "active" : ""}`}>
        <h2>6️⃣ Transaction Status (GET /transaction/:txHash)</h2>
        
        {txHash ? (
          <div>
            <div className="result success">
              <p>✅ Transaction: <code>{txHash}</code></p>
              <a 
                href={`https://explorer.solana.com/tx/${txHash}?cluster=${network === "solana" ? "mainnet" : "devnet"}`}
                target="_blank"
                rel="noopener noreferrer"
                style={{ color: "#00d4ff" }}
              >
                View on Solana Explorer →
              </a>
            </div>

            <button
              className="button"
              onClick={getTransactionStatus}
              disabled={loading === "txStatus"}
              style={{ marginTop: "1rem" }}
            >
              {loading === "txStatus" ? "Loading..." : "GET /transaction/:txHash - Check Status"}
            </button>

            {txStatus && (
              <div className="result success" style={{ marginTop: "1rem" }}>
                <strong>GET /transaction/{txHash} Response:</strong>
                <pre>{txStatus}</pre>
              </div>
            )}
          </div>
        ) : (
          <p style={{ color: "#888" }}>Complete settlement to see transaction status.</p>
        )}
      </section>

      {/* Reset */}
      <section className="card">
        <button className="button" onClick={resetFlow} style={{ background: "#666" }}>
          🔄 Reset Flow
        </button>
      </section>

      {/* Flow Summary */}
      <section className="card">
        <h2>📋 x402 Flow Summary</h2>
        <div className="endpoint-list">
          <div style={{ display: "flex", alignItems: "center", gap: "1rem", padding: "0.5rem 0" }}>
            <span className={`badge ${facilitatorInfo ? "success" : "get"}`}>
              {facilitatorInfo ? "✓" : "1"}
            </span>
            <span><code>GET /</code> - Facilitator info</span>
          </div>
          <div style={{ display: "flex", alignItems: "center", gap: "1rem", padding: "0.5rem 0" }}>
            <span className={`badge ${connected ? "success" : "get"}`}>
              {connected ? "✓" : "2"}
            </span>
            <span>Connect Wallet</span>
          </div>
          <div style={{ display: "flex", alignItems: "center", gap: "1rem", padding: "0.5rem 0" }}>
            <span className={`badge ${signedPayload ? "success" : "get"}`}>
              {signedPayload ? "✓" : "3"}
            </span>
            <span>Sign Payment</span>
          </div>
          <div style={{ display: "flex", alignItems: "center", gap: "1rem", padding: "0.5rem 0" }}>
            <span className={`badge ${verifyResult?.includes('"isValid":true') ? "success" : "post"}`}>
              {verifyResult?.includes('"isValid":true') ? "✓" : "4"}
            </span>
            <span><code>POST /verify</code> - Verify payment</span>
          </div>
          <div style={{ display: "flex", alignItems: "center", gap: "1rem", padding: "0.5rem 0" }}>
            <span className={`badge ${txHash ? "success" : "post"}`}>
              {txHash ? "✓" : "5"}
            </span>
            <span><code>POST /settle</code> - Settle on-chain</span>
          </div>
          <div style={{ display: "flex", alignItems: "center", gap: "1rem", padding: "0.5rem 0" }}>
            <span className={`badge ${txStatus ? "success" : "get"}`}>
              {txStatus ? "✓" : "6"}
            </span>
            <span><code>GET /transaction/:txHash</code> - Transaction status</span>
          </div>
        </div>
      </section>
    </div>
  );
}
