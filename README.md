# NGROK COMMAND

ngrok http 10000

# DEBUGGING BRIDGE:

Webhooks admin page: http://localhost:10000/admin/bridge

Logs: http://localhost:10000/api/bridge/admin/webhook/logs?webhookId=wep_tVvs51Fhn6s2kk18yLugBX
Upcoming events: http://localhost:10000/api/bridge/admin/webhook/events?webhookId=wep_tVvs51Fhn6s2kk18yLugBX
All endpoints: http://localhost:10000/api/bridge/admin/webhooks
Simulate send (requires an eventId from Bridge): http://localhost:10000/api/bridge/admin/webhook/send?eventId=evt_abc123&webhookId=wep_tVvs51Fhn6s2kk18yLugBX
Create webhook (POST JSON body): http://localhost:10000/api/bridge/admin/webhooks/create
Update webhook (PUT JSON body): http://localhost:10000/api/bridge/admin/webhooks/{webhookId}

Example create body:
```
{
	"url": "https://your-ngrok-domain/webhook/bridge",
	"enabled": true,
	"events": ["customer.created", "customer.updated"],
	"description": "Primary hosted KYC webhook"
}
```

Signature header preference: Bridge now sends `x-webhook-signature`; legacy `Bridge-Signature` is still accepted as fallback.

# Hr Questionnaire README


To start the application in development mode, run: `./mvnw`

To build the application in production mode, run: `./mvnw -Pproduction package`

## Getting Started

The [Getting Started](https://vaadin.com/docs/latest/getting-started) guide will quickly familiarize you with your new Hr Questionnaire implementation. You'll learn how to set up your development environment, understand the project structure, and find resources to help you add muscles to your skeleton — transforming it into a fully-featured application.

## License

This project is licensed under the GNU Affero General Public License v3.0 (AGPL-3.0). See `LICENSE.md` for the full text.

## Bridge API client generation

The Bridge API stubs are generated from the official spec at `src/main/resources/bridge-spec-official.json` via the OpenAPI Generator Maven plugin.

- We no longer use the Postman-exported YAML (`bridge-spec.yaml`) for codegen. Keep it only for ad-hoc exploration.
- The official spec is authoritative but contained a few non-compliant constructs (`type: object` alongside `$ref`). We maintain small local patches to remove those so generation succeeds.
- Generation happens on every build phase that triggers `generate-sources`. If you update the spec, run a clean generation:

```bash
./mvnw clean generate-sources
```

The generated sources live under `target/generated-sources/openapi/` and are wired into the app via `BridgeApiClientFactory`.
