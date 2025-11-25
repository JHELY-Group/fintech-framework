# Copilot Instructions for Fintech Framework

This is a Spring Boot 3.4 + Vaadin Flow 24 application using Java 21. It features a server-side Java UI, Vite-managed frontend assets, and MySQL persistence.

## Architecture & Code Organization
- **Root Package**: `org.jhely.money.base`
- **Layered Architecture**:
  - **UI**: `ui/view/**` - Vaadin `@Route` views (e.g., `ReceiptsView`). Prefer server-side Java components.
  - **API**: `api/**` - REST controllers for external webhooks (e.g., `BridgeWebhookController`).
  - **Service**: `service/**` - Business logic. Includes `BridgeService`, `ReceiptService`, and async workers.
  - **Domain**: `domain/**` - JPA entities (e.g., `BridgeCustomer`, `ReceiptFile`).
  - **Repository**: `repository/**` - Spring Data JPA repositories.
  - **Security**: `security/**` - Spring Security configuration & `AuthenticatedUser`.

## Development Workflow
- **Run (Dev)**: `./mvnw` (Starts app on port 10000 with auto-reload).
- **Build (Prod)**: `./mvnw -Pproduction package` (Triggers Vaadin frontend build & Hibernate enhancement).
- **Test**: 
  - Unit: `./mvnw test`
  - Integration: `./mvnw -Pintegration-test verify`
- **Format Code**: `./mvnw spotless:apply` (Applies Java & TypeScript formatting).
- **Generate SDK**: `./mvnw clean generate-sources` (Regenerates Bridge API client from `src/main/resources/bridge-spec-official.json`).

## Key Implementation Patterns
- **Vaadin Views**:
  - Use `@Route` for navigation and `@RolesAllowed("USER")` for security.
  - Extend `MainLayout.class` for the app shell.
  - Complex UIs (like `AccountsOverviewView`) use broadcasters for async updates.
- **Bridge Integration**:
  - **SDK**: Do not manually write clients. Use `BridgeApiClientFactory` to obtain generated clients.
  - **Webhooks**: Handled by `BridgeWebhookController`. Admin interface at `/admin/bridge`.
  - **Auto-Provisioning**: Configured via `bridge.webhook.auto-provision` in `application.properties`.
- **AI & Async Processing**:
  - **Virtual Threads**: Enabled (`spring.threads.virtual.enabled=true`). Use for blocking I/O.
  - **Receipts**: `ReceiptService` manages uploads; `ExtractionWorker` handles async AI extraction.
  - **OpenAI**: Integrated via `spring-ai`.

## Configuration & Environment
- **Properties**: `src/main/resources/application.properties` uses placeholders (e.g., `${BRIDGE_LIVE_API_KEY}`).
- **Secrets**: Local secrets can be placed in `secrets.properties` (git-ignored).
- **Database**: MySQL 8 with `org.hibernate.dialect.MySQLDialect`.
- **Frontend**: `src/main/frontend/` contains Vaadin assets. Do not edit `generated/**` or `bundles/**`.

## Guardrails
- **No Secrets**: Never commit API keys or credentials.
- **Generated Code**: Treat `target/generated-sources/openapi/` as read-only. Re-run Maven to update.
- **Package Scope**: All new Java code must reside within `org.jhely.money.base`.
- **Testing**: Use `@SpringBootTest` with `@ActiveProfiles("test")` for integration tests.
