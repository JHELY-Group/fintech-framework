import { NextRequest, NextResponse } from "next/server";

/**
 * Example paid API endpoint using x402 protocol.
 * 
 * This demonstrates how to protect an API route with x402 payments
 * using the Java facilitator implementation.
 * 
 * In production, you would use x402-next middleware. This is a manual
 * implementation to show the underlying flow.
 */

const FACILITATOR_URL = process.env.X402_FACILITATOR_URL || "http://localhost:10000/api/x402";
const API_KEY = process.env.X402_API_KEY || "";
const RECIPIENT = process.env.RECIPIENT_ADDRESS || "CdMSMvmQJiPDDAzRTDgtxxBaa64WZqcVR68pu2gT8qXE";

// Payment requirements for this endpoint
const paymentRequirements = {
  scheme: "exact",
  network: "solana-devnet",
  maxAmountRequired: "100000", // 0.1 USDC (6 decimals)
  recipient: RECIPIENT,
  description: "Access premium AI data",
  resource: "/api/paid",
};

export async function GET(request: NextRequest) {
  // Check for X-PAYMENT header
  const paymentHeader = request.headers.get("X-PAYMENT");

  if (!paymentHeader) {
    // Return 402 Payment Required with requirements
    return NextResponse.json(
      {
        error: "Payment Required",
        paymentRequirements,
        facilitatorUrl: FACILITATOR_URL,
        message: "Include X-PAYMENT header with payment authorization",
      },
      {
        status: 402,
        headers: {
          "X-Payment-Requirements": JSON.stringify(paymentRequirements),
        },
      }
    );
  }

  // Verify payment with facilitator
  try {
    const verifyResponse = await fetch(`${FACILITATOR_URL}/verify`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-API-Key": API_KEY,
      },
      body: JSON.stringify({
        x402Version: 1,
        paymentHeader,
        paymentRequirements,
      }),
    });

    const verifyResult = await verifyResponse.json();

    if (!verifyResult.isValid) {
      return NextResponse.json(
        {
          error: "Payment verification failed",
          reason: verifyResult.invalidReason || verifyResult.error,
          paymentRequirements,
        },
        { status: 402 }
      );
    }

    // Payment verified - settle it
    const settleResponse = await fetch(`${FACILITATOR_URL}/settle`, {
      method: "POST",
      headers: {
        "Content-Type": "application/json",
        "X-API-Key": API_KEY,
      },
      body: JSON.stringify({
        x402Version: 1,
        paymentHeader,
        paymentRequirements,
      }),
    });

    const settleResult = await settleResponse.json();

    if (!settleResult.success) {
      return NextResponse.json(
        {
          error: "Payment settlement failed",
          reason: settleResult.error,
        },
        { status: 402 }
      );
    }

    // Payment successful - return premium content
    return NextResponse.json({
      success: true,
      data: {
        message: "🎉 Payment successful! Here is your premium content.",
        timestamp: new Date().toISOString(),
        txHash: settleResult.txHash,
        networkId: settleResult.networkId,
        premiumData: {
          secretValue: Math.random().toString(36).substring(7),
          aiPrediction: "BTC will reach $150k by 2025",
          confidence: 0.87,
        },
      },
    });
  } catch (error) {
    console.error("Payment processing error:", error);
    return NextResponse.json(
      {
        error: "Payment processing failed",
        message: error instanceof Error ? error.message : "Unknown error",
      },
      { status: 500 }
    );
  }
}

// Also support POST for payment submission
export async function POST(request: NextRequest) {
  return GET(request);
}
