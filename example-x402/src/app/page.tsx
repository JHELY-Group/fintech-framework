"use client";

import { useState } from "react";

export default function Home() {
  const [facilitatorUrl, setFacilitatorUrl] = useState("http://localhost:10000/api/x402");
  const [apiKey, setApiKey] = useState("");
  const [healthResult, setHealthResult] = useState<string | null>(null);
  const [supportedResult, setSupportedResult] = useState<string | null>(null);
  const [verifyResult, setVerifyResult] = useState<string | null>(null);
  const [loading, setLoading] = useState<string | null>(null);

  // Check facilitator health
  const checkHealth = async () => {
    setLoading("health");
    setHealthResult(null);
    try {
      const res = await fetch(`${facilitatorUrl}/health`);
      const data = await res.json();
      setHealthResult(JSON.stringify(data, null, 2));
    } catch (err) {
      setHealthResult(`Error: ${err instanceof Error ? err.message : String(err)}`);
    }
    setLoading(null);
  };

  // Get supported networks
  const getSupported = async () => {
    setLoading("supported");
    setSupportedResult(null);
    try {
      const res = await fetch(`${facilitatorUrl}/supported`, {
        headers: {
          "X-API-Key": apiKey,
        },
      });
      const data = await res.json();
      setSupportedResult(JSON.stringify(data, null, 2));
    } catch (err) {
      setSupportedResult(`Error: ${err instanceof Error ? err.message : String(err)}`);
    }
    setLoading(null);
  };

  // Test verify endpoint with sample payload
  const testVerify = async () => {
    setLoading("verify");
    setVerifyResult(null);

    // Sample x402 verify request
    const sampleRequest = {
      x402Version: 1,
      paymentRequirements: {
        scheme: "exact",
        network: "solana-devnet",
        maxAmountRequired: "1000000", // 1 USDC
        recipient: "CdMSMvmQJiPDDAzRTDgtxxBaa64WZqcVR68pu2gT8qXE",
      },
      payment: {
        scheme: "exact",
        network: "solana-devnet",
        payer: "7EcDhSYGxXyscszYEp35KHN8vvw3svAuLKTzXwCFLtV",
        recipient: "CdMSMvmQJiPDDAzRTDgtxxBaa64WZqcVR68pu2gT8qXE",
        amount: "1000000",
        nonce: Date.now(),
        expiry: Math.floor(Date.now() / 1000) + 3600,
        signature: "test-signature",
      },
    };

    try {
      const res = await fetch(`${facilitatorUrl}/verify`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          "X-API-Key": apiKey,
        },
        body: JSON.stringify(sampleRequest),
      });
      const data = await res.json();
      setVerifyResult(JSON.stringify(data, null, 2));
    } catch (err) {
      setVerifyResult(`Error: ${err instanceof Error ? err.message : String(err)}`);
    }
    setLoading(null);
  };

  return (
    <div className="container">
      <header className="header">
        <h1>x402 Protocol Demo</h1>
        <p>Test your Java x402 Facilitator implementation</p>
      </header>

      {/* Wallet Integration Link */}
      <section className="card" style={{ background: "linear-gradient(135deg, #1a1a2e 0%, #16213e 100%)", border: "1px solid #00d4ff" }}>
        <h2>🔐 Full Wallet Integration</h2>
        <p>Test the complete x402 flow with Phantom wallet signing, verification, and settlement.</p>
        <a 
          href="/wallet" 
          className="button" 
          style={{ display: "inline-block", marginTop: "1rem", textDecoration: "none" }}
        >
          Open Wallet Integration Demo →
        </a>
      </section>

      {/* Configuration */}
      <section className="card">
        <h2>⚙️ Configuration</h2>
        <p>Configure the connection to your Java x402 facilitator.</p>
        
        <div className="flex-row" style={{ marginTop: "1rem" }}>
          <div className="input-group">
            <label>Facilitator URL</label>
            <input
              type="text"
              value={facilitatorUrl}
              onChange={(e) => setFacilitatorUrl(e.target.value)}
              placeholder="http://localhost:10000/api/x402"
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

        <p style={{ marginTop: "1rem", fontSize: "0.9rem" }}>
          Generate an API key from the x402 Facilitator UI at{" "}
          <code style={{ color: "#00d4ff" }}>http://localhost:10000/x402</code>
        </p>
      </section>

      {/* Health Check */}
      <section className="card">
        <h2>🏥 Health Check</h2>
        <p>Check if the facilitator is running and healthy (no auth required).</p>
        
        <div className="code-block">
          <code>GET {facilitatorUrl}/health</code>
        </div>

        <button
          className="button"
          onClick={checkHealth}
          disabled={loading === "health"}
        >
          {loading === "health" ? "Checking..." : "Check Health"}
        </button>

        {healthResult && (
          <div className={`result ${healthResult.includes("Error") ? "error" : "success"}`}>
            {healthResult}
          </div>
        )}
      </section>

      {/* Supported Networks */}
      <section className="card">
        <h2>🌐 Supported Networks</h2>
        <p>Get the list of supported networks and schemes (requires API key).</p>
        
        <div className="code-block">
          <code>GET {facilitatorUrl}/supported</code>
        </div>

        <button
          className="button"
          onClick={getSupported}
          disabled={loading === "supported" || !apiKey}
        >
          {loading === "supported" ? "Loading..." : "Get Supported"}
        </button>

        {!apiKey && (
          <div className="result info">
            ⚠️ Enter your API key above to test this endpoint
          </div>
        )}

        {supportedResult && (
          <div className={`result ${supportedResult.includes("Error") ? "error" : "success"}`}>
            {supportedResult}
          </div>
        )}
      </section>

      {/* Verify Payment */}
      <section className="card">
        <h2>✅ Verify Payment</h2>
        <p>Test the verify endpoint with a sample payment payload.</p>
        <p style={{ fontSize: "0.85rem", color: "#ff9f43", marginTop: "0.5rem" }}>
          ⚠️ <strong>Note:</strong> This test uses a dummy signature. The facilitator will correctly 
          reject it with &quot;Invalid signature&quot;. In production, the payer&apos;s wallet signs the payload.
        </p>
        
        <div className="code-block">
          <code>POST {facilitatorUrl}/verify</code>
        </div>

        <details style={{ marginBottom: "1rem" }}>
          <summary style={{ cursor: "pointer", color: "#00d4ff" }}>
            View sample request payload
          </summary>
          <div className="code-block">
            <code>{`{
  "x402Version": 1,
  "paymentRequirements": {
    "scheme": "exact",
    "network": "solana-devnet",
    "maxAmountRequired": "1000000",
    "recipient": "9aE476sH92..."
  },
  "payment": {
    "scheme": "exact",
    "network": "solana-devnet",
    "payer": "7EcDhSYGxX...",
    "recipient": "9aE476sH92...",
    "amount": "1000000",
    "nonce": "<generated at runtime>",
    "expiry": "<1 hour from now>",
    "signature": "<wallet signature - dummy in test>"
  }
}`}</code>
          </div>
        </details>

        <button
          className="button"
          onClick={testVerify}
          disabled={loading === "verify" || !apiKey}
        >
          {loading === "verify" ? "Verifying..." : "Test Verify (expect rejection)"}
        </button>

        {!apiKey && (
          <div className="result info">
            ⚠️ Enter your API key above to test this endpoint
          </div>
        )}

        {verifyResult && (
          <div className={`result ${verifyResult.includes("Error:") ? "error" : verifyResult.includes("INVALID_SIGNATURE") ? "info" : verifyResult.includes('"isValid":false') || verifyResult.includes('"isValid": false') ? "error" : "success"}`}>
            {verifyResult.includes("INVALID_SIGNATURE") && (
              <div style={{ marginBottom: "0.5rem", color: "#00d4ff" }}>
                ✓ Expected result: Facilitator correctly rejected the dummy signature
              </div>
            )}
            {verifyResult}
          </div>
        )}
      </section>

      {/* API Reference */}
      <section className="card">
        <h2>📚 API Reference</h2>
        <p>x402 Facilitator endpoints (per specification):</p>
        
        <ul className="endpoint-list">
          <li>
            <span className="badge get">GET</span>
            <code>/</code> - Facilitator info
          </li>
          <li>
            <span className="badge get">GET</span>
            <code>/supported</code> - Supported networks (kinds)
          </li>
          <li>
            <span className="badge post">POST</span>
            <code>/verify</code> - Verify payment authorization
          </li>
          <li>
            <span className="badge post">POST</span>
            <code>/settle</code> - Settle payment on-chain
          </li>
          <li>
            <span className="badge get">GET</span>
            <code>/transaction/:txHash</code> - Get transaction status
          </li>
          <li>
            <span className="badge get">GET</span>
            <code>/health</code> - Health check (no auth)
          </li>
        </ul>
      </section>

      {/* Integration Guide */}
      <section className="card">
        <h2>🔧 Integration Guide</h2>
        <p>To integrate x402 payments in your API routes:</p>
        
        <div className="code-block">
          <code>{`// Manual x402 implementation in API route
import { NextRequest, NextResponse } from "next/server";

export async function GET(request: NextRequest) {
  const paymentHeader = request.headers.get("X-PAYMENT");
  
  if (!paymentHeader) {
    // Return 402 Payment Required
    return NextResponse.json(
      { error: "Payment Required", paymentRequirements },
      { status: 402 }
    );
  }

  // Verify with facilitator
  const verify = await fetch("${facilitatorUrl}/verify", {
    method: "POST",
    headers: { "X-API-Key": apiKey },
    body: JSON.stringify({
      x402Version: 1,
      paymentHeader,
      paymentRequirements,
    }),
  });
  
  // Settle and return content...
}`}</code>
        </div>

        <p style={{ marginTop: "1rem" }}>
          See the <code>/api/paid</code> route for a complete example.
        </p>
      </section>
    </div>
  );
}
