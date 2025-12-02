# x402 Example - Next.js Demo App

A minimal Next.js application demonstrating the x402 protocol for machine-to-machine payments using our custom Java facilitator.

## What is x402?

x402 is a protocol for machine-to-machine payments that enables AI agents and automated services to pay for resources using blockchain (Solana/USDC). The "402" refers to HTTP status code 402 "Payment Required".

## Architecture

```
┌─────────────────┐     ┌─────────────────┐     ┌─────────────────┐
│  Client/Agent   │────▶│  Resource API   │────▶│   Facilitator   │
│  (pays USDC)    │     │  (this app)     │     │  (Java backend) │
└─────────────────┘     └─────────────────┘     └─────────────────┘
                                                        │
                                                        ▼
                                                ┌─────────────────┐
                                                │     Solana      │
                                                │   (settlement)  │
                                                └─────────────────┘
```

## Prerequisites

1. **Java Facilitator Running**: Make sure the fintech-framework is running at `http://localhost:10000`
   ```bash
   cd /path/to/fintech-framework
   mvn spring-boot:run
   ```

2. **API Key**: Generate an API key from the facilitator UI at `http://localhost:10000/x402`

3. **Node.js**: Version 18+ required

## Setup

1. Install dependencies:
   ```bash
   npm install
   ```

2. Configure environment:
   ```bash
   cp .env.example .env.local
   # Edit .env.local with your API key
   ```

3. Start the dev server:
   ```bash
   npm run dev
   ```

4. Open http://localhost:3000

## Testing the Facilitator

The demo app provides a UI to test:

- **Health Check**: Verify facilitator is running (no auth)
- **Supported Networks**: Check supported Solana networks
- **Verify Payment**: Test payment verification flow

## API Routes

### GET /api/paid

A sample paid API endpoint protected by x402.

**Without payment:**
```bash
curl http://localhost:3000/api/paid
# Returns 402 Payment Required with payment requirements
```

**With payment (requires valid X-PAYMENT header):**
```bash
curl -H "X-PAYMENT: <base64-encoded-payment>" http://localhost:3000/api/paid
# Returns premium content after settlement
```

## x402 Flow

1. Client requests protected resource
2. Server returns `402 Payment Required` with `paymentRequirements`
3. Client signs payment authorization with Solana wallet
4. Client retries request with `X-PAYMENT` header
5. Server verifies payment via facilitator
6. Server settles payment via facilitator
7. Server returns protected content

## Integration with x402-next

For production, use the `x402-next` middleware:

```typescript
import { paymentMiddleware } from "x402-next";

export const GET = paymentMiddleware(
  async (req) => {
    return Response.json({ data: "premium content" });
  },
  {
    facilitatorUrl: "http://localhost:10000/api/x402",
    apiKey: process.env.X402_API_KEY,
    paymentRequirements: {
      scheme: "exact",
      network: "solana-devnet",
      maxAmountRequired: "1000000",
      recipient: "your-wallet",
    },
  }
);
```

## Facilitator Endpoints

| Endpoint | Method | Auth | Description |
|----------|--------|------|-------------|
| `/` | GET | No | Facilitator info |
| `/health` | GET | No | Health check |
| `/supported` | GET | Yes | Supported networks (kinds) |
| `/verify` | POST | Yes | Verify payment |
| `/settle` | POST | Yes | Settle payment on-chain |
| `/transaction/:txHash` | GET | Yes | Transaction status |

## Environment Variables

| Variable | Description | Default |
|----------|-------------|---------|
| `X402_FACILITATOR_URL` | Facilitator base URL | `http://localhost:10000/api/x402` |
| `X402_API_KEY` | Your facilitator API key | - |
| `X402_NETWORK` | Solana network | `solana-devnet` |
| `RECIPIENT_ADDRESS` | Payment recipient wallet | - |

## Learn More

- [x402 Protocol](https://x402.org)
- [x402-next NPM](https://www.npmjs.com/package/x402-next)
- [x402-express NPM](https://www.npmjs.com/package/x402-express)
- [Solana Web3.js](https://solana-labs.github.io/solana-web3.js/)
