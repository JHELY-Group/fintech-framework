# Fintech Framework

Open-source building blocks for neobanks and fintech products.

**Led and supported by [JHELY Foundation](https://jhely.org)**

## Vision

Democratize fintech infrastructure by providing production-ready, open-source components that enable teams to build neobanks, remittance platforms, and cross-border payment solutions in weeks instead of months.

## Problem We Solve

Building fintech products requires integrating complex APIs for identity verification, payments, and banking-as-a-service, while navigating regulatory compliance and implementing secure user flows. Today, this takes 6 to 12 months and significant capital.

We provide pre-built modules that abstract this complexity, allowing founders and developers to focus on their unique value proposition rather than reinventing infrastructure.

## What We Offer

### KYC and Onboarding
- Hosted identity verification flows via Bridge API
- Document verification with support for passports and government IDs across multiple countries
- Webhook-driven status updates for real-time KYC progress tracking
- Customer status dashboard showing capabilities, requirements, and endorsements

### Cross-Border Payments
- Fiat and crypto payment rails (SEPA, SWIFT, USDC, EURC)
- Stablecoin wallets and account management
- Send and receive functionality for both traditional and digital currencies
- Real-time balance tracking and transaction history

### AI-Powered Document Processing
- Receipt and invoice extraction using OpenAI
- Automatic data extraction from uploaded documents
- Support for images, PDFs, and common document formats
- Confidence scoring with automatic model escalation

### Agentic Payments (x402 Facilitator)
- Machine-to-machine payment protocol support via [x402 protocol](https://x402.org)
- Payment verification and settlement on Solana (mainnet/devnet)
- Built-in dashboard for monitoring API requests and transactions
- API key authentication for secure integrations
- Automated payment flows for AI agents and services
- Supports USDC payments with real-time blockchain confirmation

### Extensible Architecture
- Modular design ready for cards integration
- Lending and credit modules (planned)
- White-label deployment support
- Webhook infrastructure for event-driven integrations

## Target Users

- **Fintech Startups**: Launch your neobank or payment product faster with pre-built infrastructure
- **Remittance Platforms**: Build cross-border money transfer services with fiat and crypto rails
- **Web3 Teams**: Add fiat on-ramps and off-ramps to your crypto products
- **Enterprises**: Deploy white-label neobank solutions for your customers
- **Developers**: Integrate payment and KYC capabilities into any application

## Tech Stack

- Java 21 with virtual threads for high-concurrency operations
- Spring Boot 3.4 for robust backend services
- Vaadin Flow 24 for server-side rendered UI
- MySQL for persistence
- OpenAI for document processing and AI features
- Bridge API for KYC and payment infrastructure

## Getting Started

### Prerequisites

- Java 21 or later
- Maven 3.8+
- MySQL 8.0+
- Bridge API credentials (sandbox or production)
- OpenAI API key (for document processing features)

### Configuration

1. Copy `secrets.properties.example` to `secrets.properties` (git-ignored)
2. Add your API credentials:

```properties
BRIDGE_SANDBOX_API_KEY=your-bridge-sandbox-key
BRIDGE_LIVE_API_KEY=your-bridge-live-key
OPENAI_API_KEY=your-openai-key
DB_URL=jdbc:mysql://localhost:3306/fintech
DB_USERNAME=your-db-user
DB_PASSWORD=your-db-password
```

### Running the Application

```bash
# Development mode (port 10000)
./mvnw

# Production build
./mvnw -Pproduction package

# Run tests
./mvnw test

# Integration tests
./mvnw -Pintegration-test verify
```

### Bridge API Specification

The Bridge SDK requires the official OpenAPI specification file. This file is not included in the repository and must be obtained separately:

1. **Obtain the spec file**: Request `bridge-spec-official.json` from the Bridge team
2. **Place in project root**: Copy the file to the root folder of the project

```
fintech-framework/
├── bridge-spec-official.json  ← Place the spec file here
├── pom.xml
└── ...
```

### Regenerating the Bridge SDK

The Bridge API client is generated from the OpenAPI specification and committed to source control. You only need to regenerate it when the Bridge API spec changes:

```bash
mvn generate-sources -Pgenerate-bridge-sdk
```

This generates the SDK to `src/main/java/org/jhely/money/sdk/bridge/`. The generated code is committed to git, so normal builds do not regenerate it.

## Project Structure

```
org.jhely.money.base/
├── ui/view/              # Vaadin views and UI components
│   └── payments/         # Finance dashboard, send/receive views
├── api/bridge/           # REST controllers for Bridge webhooks
├── service/              # Business logic layer
│   ├── payments/         # Bridge onboarding, KYC broadcasting
│   └── ...               # Receipt processing, user management
├── domain/               # JPA entities
├── repository/           # Spring Data repositories
└── security/             # Authentication and authorization
```

## Key Integrations

### Bridge API
- Customer creation and management
- Hosted KYC flows with identity verification
- Terms of Service acceptance
- Webhook handling for real-time status updates
- Payment capabilities (crypto and fiat)

### OpenAI
- Document and receipt extraction
- Structured data parsing from images and PDFs
- Async processing with virtual threads

## x402 Facilitator - Test App

The `example-x402/` folder contains a Next.js demo application for testing the x402 payment protocol integration.

### Quick Start

1. **Start the Java backend** (this framework):
   ```bash
   ./mvnw
   ```

2. **Configure your x402 facilitator** at http://localhost:10000/x402/config:
   - Set your Solana wallet address as the recipient
   - Enter your facilitator's Solana keypair (base58 encoded)
   - Generate an API key for authentication

3. **Run the example app**:
   ```bash
   cd example-x402
   cp .env.example .env.local
   # Edit .env.local with your API key from step 2
   npm install
   npm run dev
   ```

4. **Test the flow** at http://localhost:3000

See `example-x402/README.md` for detailed documentation on the x402 protocol flow and API integration.

## Roadmap

- [ ] Receiving crypto and fiat payments
- [ ] Sending money via crypto and fiat rails
- [ ] Cards integration (virtual and physical)
- [ ] Lending and credit modules
- [ ] Multi-currency wallet management
- [ ] Company/business account creation via Prospera API
- [ ] Enhanced AI document processing with batch/flex modes

## Contributing

We are open for open-source contributors and welcome collaboration from developers, fintech enthusiasts, and organizations who share our vision of democratizing financial infrastructure.

**Ways to contribute:**
- Submit bug reports and feature requests
- Contribute code via pull requests
- Improve documentation
- Share feedback and ideas

**Interested in contributing or exploring partnership opportunities?**

- 💬 **Slack**: [JHELY Community](https://join.slack.com/t/jhely/shared_invite/zt-3kahbwzsy-fflJ1cerEX63qeY6KRpx2g) - Chat with us about development, business development, and partnerships
- 📱 **Telegram**: [JHELYorg](https://t.me/JHELYorg)
- 📧 **Email**: Reach out to us at **info@jhely.org**

We'd love to hear from you!

## License

This project is licensed under the Apache License 2.0. See [LICENSE.md](LICENSE.md) for details.

## Links

- [JHELY Foundation](https://jhely.org) - Project sponsor and maintainer
- [Bridge API Documentation](https://apidocs.bridge.xyz)
- [x402 Protocol](https://x402.org)
- [OpenAI Platform](https://platform.openai.com)
