# Copilot Instructions for Fintech Framework

Spring Boot 3.4 + Vaadin Flow 24 + Java 21 fintech application. Server-side Java UI with Bridge (fintech API) and OpenAI integrations, MySQL persistence.

## Architecture Overview
```
org.jhely.money.base/
├── ui/view/              # Vaadin @Route views (MainLayout shell)
│   └── payments/         # Finance views: AccountsOverviewView (main dashboard), Send/Receive
├── api/bridge/           # REST webhooks: BridgeWebhookController receives KYC/customer events
├── service/              # Business logic
│   ├── payments/         # BridgeOnboardingService, KycStatusBroadcaster
│   ├── BridgeApiClientFactory.java  # ★ Factory for generated SDK clients
│   └── ExtractionWorker.java        # Async AI receipt extraction
├── domain/               # JPA entities: BridgeCustomer, ReceiptFile, UserAccount
├── repository/           # Spring Data JPA repos
└── security/             # AuthenticatedUser (session-based via VaadinSession)
```

## Critical Workflows
| Task | Command |
|------|---------|
| Dev server (port 10000) | `./mvnw` |
| Regenerate Bridge SDK | `mvn generate-sources -Pgenerate-bridge-sdk` |
| Format code | `./mvnw spotless:apply` |
| Prod build | `./mvnw -Pproduction package` |
| Integration tests | `./mvnw -Pintegration-test verify` |

## Bridge Integration Patterns
**SDK Usage** – Never write HTTP clients manually. Use `BridgeApiClientFactory`:
```java
@Autowired BridgeApiClientFactory factory;
// Get typed APIs:
factory.customers().customersGet(...);
factory.webhooks().webhooksGet(...);
```
- SDK source: `src/main/java/org/jhely/money/sdk/bridge/` (committed to git, regenerate manually via Maven profile)
- OpenAPI spec: `bridge-spec-official.json` (root folder, git-ignored)

**Onboarding Flow** (`AccountsOverviewView` → `BridgeOnboardingService`):
1. Check `BridgeCustomer` exists via `onboarding.findForUser(userId, email)`
2. If not, show onboarding card with "Initiate KYC" button
3. On KYC completion, `BridgeWebhookController` receives webhook → updates entity → broadcasts via `KycStatusBroadcaster`
4. UI live-updates via broadcaster listener pattern

**Broadcaster Pattern** for real-time UI updates:
```java
// In view constructor:
kycBroadcaster.register(userId, updated -> ui.access(() -> refreshPanel(updated)));
// In detach listener:
card.addDetachListener(ev -> kycBroadcaster.unregister(userId, listener));
```

## Vaadin View Conventions
- All protected views: `@RolesAllowed("USER")` + `@Route(value = "path", layout = MainLayout.class)`
- Finance views live in `ui/view/payments/` with `PaymentsSubnav` component
- Use `ui.access(() -> ...)` for async UI updates from background threads
- Session user: `AuthenticatedUser.get()` returns `Optional<UserAccount>`

## AI/Receipt Processing
- `ReceiptService` handles uploads with MIME validation via Tika
- `ExtractionWorker.process()` is `@Async` – uploads files to OpenAI, extracts structured data
- Virtual threads enabled (`spring.threads.virtual.enabled=true`) – safe for blocking I/O

## Configuration
- **Secrets**: Use `secrets.properties` (git-ignored) or env vars: `${BRIDGE_SANDBOX_API_KEY}`, `${OPENAI_API_KEY}`, `${DB_URL}`
- **Bridge mode**: `bridge.mode=sandbox|live` switches base URL and API key
- **Vaadin scanning**: Restricted via `vaadin.allowed-packages` to avoid scanning generated SDK

## Testing
Integration tests use `@SpringBootTest` + `@ActiveProfiles("test")`:
```java
@SpringBootTest
@ActiveProfiles("test")
class CustomersSandboxIT { ... }
```
See `CustomersSandboxIT.java` for Bridge API test patterns (idempotency keys, error handling).

## Guardrails
- Never commit API keys – use env vars or `secrets.properties`
- All new code in `org.jhely.money.base.*` package
- Don't edit `src/main/frontend/generated/**`
- Bridge SDK in `src/main/java/org/jhely/money/sdk/bridge/` is auto-generated – regenerate with `mvn generate-sources -Pgenerate-bridge-sdk` instead of manual edits
- Run `./mvnw spotless:apply` before committing
