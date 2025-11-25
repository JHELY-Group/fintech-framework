package org.jhely.money.base.api.bridge;

import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.security.RolesAllowed;
import jakarta.servlet.http.HttpServletRequest;
import org.jhely.money.base.api.payments.BridgeProperties;
import org.jhely.money.base.service.BridgeApiClientFactory;
import org.jhely.money.sdk.bridge.model.SendWebhookPayload;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.security.web.csrf.CsrfToken;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.*;

@Controller
@RolesAllowed("USER")
@RequestMapping("/admin/bridge")
public class BridgeWebhooksAdminPageController {
    private static final Logger log = LoggerFactory.getLogger(BridgeWebhooksAdminPageController.class);

    private static final ObjectMapper MAPPER = new ObjectMapper()
            .findAndRegisterModules()
            .configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    private static final okhttp3.OkHttpClient HTTP = new okhttp3.OkHttpClient();

    private final BridgeProperties bridgeProps;
    private final BridgeApiClientFactory bridgeFactory;

    public BridgeWebhooksAdminPageController(BridgeProperties bridgeProps, BridgeApiClientFactory bridgeFactory) {
        this.bridgeProps = bridgeProps;
        this.bridgeFactory = bridgeFactory;
    }

    @GetMapping(produces = MediaType.TEXT_HTML_VALUE)
    @ResponseBody
    public ResponseEntity<String> page(HttpServletRequest request,
                       @RequestParam(name = "selected", required = false) String selectedId,
                       @RequestParam(name = "action", required = false) String action,
                       @RequestParam(name = "flash", required = false) String flash,
                       @RequestParam(name = "error", required = false) String error) {
        CsrfToken csrf = (CsrfToken) request.getAttribute("_csrf");
        try {
            String webhooksJson = fetchRaw("/webhooks");
            JsonNode list = extractArray(webhooksJson);
            String logsOut = null;
            String eventsOut = null;
        String rawWebhook = null;
            if (StringUtils.hasText(selectedId) && "logs".equalsIgnoreCase(action)) {
                try { logsOut = pretty(fetchRaw("/webhooks/" + selectedId + "/logs")); } catch (Exception e) { logsOut = "Error: " + e.getMessage(); }
            }
            if (StringUtils.hasText(selectedId) && "events".equalsIgnoreCase(action)) {
                try {
                    String json = fetchRaw("/webhooks/" + selectedId + "/events");
                    JsonNode node = MAPPER.readTree(json);
                    JsonNode dataNode = null;

                    if (node.isArray()) {
                        dataNode = node;
                    } else if (node.has("data") && node.get("data").isArray()) {
                        dataNode = node.get("data");
                    }

                    StringBuilder sb = new StringBuilder();
                    sb.append("<table><thead><tr><th>ID</th><th>Topic</th><th>Created At</th><th>Action</th></tr></thead><tbody>");
                    if (dataNode != null && dataNode.size() > 0) {
                        for (JsonNode e : dataNode) {
                            String eventId = e.path("event_id").asText("");
                            String eventCategory = e.path("event_category").asText("");
                            String eventType = e.path("event_type").asText("");
                            String eventCreatedAt = e.path("event_created_at").asText("");

                            sb.append("<tr>");
                            sb.append("<td>").append(escape(eventId)).append("</td>");
                            sb.append("<td>").append(escape(eventCategory + "." + eventType)).append("</td>");
                            sb.append("<td>").append(escape(eventCreatedAt)).append("</td>");
                            sb.append("<td>");
                            sb.append("<form method='post' action='/admin/bridge/send-event' style='display:inline'>");
                            if (csrf != null) sb.append("<input type='hidden' name='").append(escape(csrf.getParameterName())).append("' value='").append(escape(csrf.getToken())).append("' />");
                            sb.append("<input type='hidden' name='webhookId' value='").append(escape(selectedId)).append("' />");
                            sb.append("<input type='hidden' name='eventId' value='").append(escape(eventId)).append("' />");
                            sb.append("<button type='submit'>Send event</button>");
                            sb.append("</form>");
                            sb.append("</td>");
                            sb.append("</tr>");
                        }
                    } else {
                        sb.append("<tr><td colspan='4'>No events found.</td></tr>");
                    }
                    sb.append("</tbody></table>");
                    eventsOut = sb.toString();
                } catch (Exception e) {
                    eventsOut = "<pre>Error: " + escape(e.getMessage()) + "</pre>";
                }
            }
        if (StringUtils.hasText(selectedId) && list != null && list.isArray()) {
          for (JsonNode w : list) {
            String id = safeString(w.path("id").asText(null));
            if (selectedId.equals(id)) { rawWebhook = pretty(w.toString()); break; }
          }
          if (rawWebhook == null) rawWebhook = pretty("{\"error\":\"not_found_in_list\"}");
        }
        String html = renderPage(csrf, list, selectedId, rawWebhook, logsOut, eventsOut, flash, error);
            return ResponseEntity.ok(html);
        } catch (Exception e) {
            String html = basicError("Failed to load: " + escape(e.getMessage()));
            return ResponseEntity.status(500).body(html);
        }
    }

    @PostMapping(path = "/create", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String create(@RequestParam("url") String url,
               @RequestParam(name = "enabled", defaultValue = "false") boolean enabled,
               @RequestParam(name = "event_epoch", required = false) String eventEpoch,
               @RequestParam(name = "event_categories", required = false) List<String> eventCategories,
               @RequestParam(name = "events", required = false) String eventsCsv) {
        try {
            Map<String, Object> body = new LinkedHashMap<>();
            body.put("url", url);
        // Canonical status (active | disabled)
        body.put("status", enabled ? "active" : "disabled");
        // Backwards compatibility (may be ignored by Bridge)
        body.put("enabled", enabled);
        body.put("active", enabled);
        body.put("is_enabled", enabled);
        if (StringUtils.hasText(eventEpoch)) {
          body.put("event_epoch", eventEpoch);
        }
            List<String> events = parseCsv(eventsCsv);
        // Prefer explicit multi-select eventCategories over CSV fallback
        if (eventCategories != null && !eventCategories.isEmpty()) {
          body.put("event_categories", eventCategories);
        } else if (!events.isEmpty()) {
          body.put("event_categories", events); // fallback if only CSV provided
          body.put("events", events); // legacy fallback
        }
            String json = MAPPER.writeValueAsString(body);
            String created = requestRaw("POST", "/webhooks", json);
            String id = safeString(MAPPER.readTree(created).path("id").asText(null));
            String redirect = "/admin/bridge?flash=" + enc("Webhook created") + (id != null ? "&selected=" + enc(id) : "");
            return "redirect:" + redirect;
        } catch (Exception e) {
            log.warn("Create webhook failed: {}", e.getMessage());
            return "redirect:/admin/bridge?error=" + enc(e.getMessage() == null ? "create_failed" : e.getMessage());
        }
    }

    @PostMapping(path = "/update", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String update(@RequestParam("id") String id,
               @RequestParam(name = "url", required = false) String url,
               @RequestParam(name = "enabled", defaultValue = "false") boolean enabled,
               @RequestParam(name = "events", required = false) String eventsCsv) {
        try {
            Map<String, Object> body = new LinkedHashMap<>();
            if (StringUtils.hasText(url)) body.put("url", url);
        body.put("status", enabled ? "active" : "disabled");
        body.put("enabled", enabled);
        body.put("active", enabled);
        body.put("is_enabled", enabled);
            List<String> events = parseCsv(eventsCsv);
        if (!events.isEmpty()) {
          body.put("event_categories", events);
          body.put("events", events);
        }
            String json = MAPPER.writeValueAsString(body);
            requestRaw("PUT", "/webhooks/" + id, json);
            return "redirect:/admin/bridge?selected=" + enc(id) + "&flash=" + enc("Webhook updated");
        } catch (Exception e) {
            log.warn("Update webhook failed: {}", e.getMessage());
            return "redirect:/admin/bridge?selected=" + enc(id) + "&error=" + enc(e.getMessage() == null ? "update_failed" : e.getMessage());
        }
    }

    private String renderPage(CsrfToken csrf, JsonNode list, String selectedId, String rawWebhook, String logsOut, String eventsOut, String flash, String error) {
        String csrfInput = csrf != null ? ("<input type=\"hidden\" name=\"" + escape(csrf.getParameterName()) + "\" value=\"" + escape(csrf.getToken()) + "\" />") : "";
        StringBuilder tb = new StringBuilder();
        tb.append("<table><thead><tr><th>ID</th><th>URL</th><th>Enabled</th><th>Events</th><th>Actions</th></tr></thead><tbody>");
        if (list != null && list.isArray() && list.size() > 0) {
            for (JsonNode w : list) {
                String id = safeString(w.path("id").asText(null));
                String url = safeString(w.path("url").asText(null));
          String status = safeString(w.path("status").asText(null));
          boolean enabled = (status != null && "active".equalsIgnoreCase(status)) || toBool(w, "enabled") || toBool(w, "active") || toBool(w, "is_enabled");
          List<String> events = toArray(w, "event_categories");
          if (events.isEmpty()) events = toArray(w, "events");
          if (events.isEmpty()) events = toArray(w, "subscriptions");
                tb.append("<tr>");
                tb.append("<td style='white-space:nowrap'>").append(id == null ? spanMuted("n/a") : escape(id)).append("</td>");
                tb.append("<td>").append(url == null ? spanMuted("n/a") : escape(url)).append("</td>");
          tb.append("<td>").append(enabled ? "yes" : "no");
          if (status != null) tb.append(" <span class='muted' style='font-size:11px'>(status:" + escape(status) + ")</span>");
          tb.append("</td>");
                tb.append("<td>");
                if (events.isEmpty()) tb.append(spanMuted("n/a")); else for (String e : events) tb.append("<span class='pill'>").append(escape(e)).append("</span> ");
                tb.append("</td>");
                tb.append("<td style='white-space:nowrap'>");
                if (id != null) {
                    tb.append(link("Select", "/admin/bridge?selected=" + enc(id)));
                    tb.append(" ");
                    tb.append(link("Logs", "/admin/bridge?selected=" + enc(id) + "&action=logs"));
                    tb.append(" ");
                    tb.append(link("Events", "/admin/bridge?selected=" + enc(id) + "&action=events"));
                } else tb.append(spanMuted("n/a"));
                tb.append("</td>");
                tb.append("</tr>");
            }
        } else {
            tb.append("<tr><td colspan='5'>").append(spanMuted("No webhooks found.")).append("</td></tr>");
        }
        tb.append("</tbody></table>");

        String selectedPanel = "";
        if (StringUtils.hasText(selectedId)) {
            selectedPanel = """
                <section>
                  <h2>Update Selected Webhook</h2>
                  <form method="post" action="/admin/bridge/update">
                    %CSRF%
                    <div class="row">
                      <div>
                        <label for="id">Webhook ID</label>
                        <input id="id" name="id" type="text" value="%ID%" readonly />
                      </div>
                      <div>
                        <label>Enabled</label>
                        <div><input id="enabled" name="enabled" type="checkbox" %ENABLED% /> <span class="muted">Receive events</span></div>
                      </div>
                    </div>
                    <label for="url">URL</label>
                    <input id="url" name="url" type="url" placeholder="https://..." />
                    <label for="events">Events (comma-separated)</label>
                    <input id="events" name="events" type="text" placeholder="customer.created,customer.updated" />
                    <div class="actions" style="margin-top:10px">
                      <button class="ok" type="submit">Update</button>
                    </div>
                  </form>
                  <form method="post" action="/admin/bridge/activate" style="margin-top:10px;display:inline">
                    %CSRF%
                    <input type="hidden" name="id" value="%ID%" />
                    <button type="submit">Activate</button>
                  </form>
                  <form method="post" action="/admin/bridge/deactivate" style="margin-top:10px;display:inline">
                    %CSRF%
                    <input type="hidden" name="id" value="%ID%" />
                    <button type="submit">Deactivate</button>
                  </form>
                  <form method="post" action="/admin/bridge/delete" style="margin-top:10px;display:inline" onsubmit="return confirm('Delete webhook? This cannot be undone.');">
                    %CSRF%
                    <input type="hidden" name="id" value="%ID%" />
                    <button type="submit" class="danger">Delete</button>
                  </form>
                  <section style="margin-top:14px">
                    <h3>Raw JSON</h3>
                    <pre>%RAW%</pre>
                  </section>
                </section>
            """.replace("%ID%", escape(selectedId))
                .replace("%ENABLED%", "")
                .replace("%CSRF%", csrfInput)
                .replace("%RAW%", rawWebhook == null ? escape("n/a") : escape(rawWebhook));
        }

        String logsPanel = logsOut != null ? ("<section><h2>Logs</h2><pre>" + escape(logsOut) + "</pre></section>") : "";
        String eventsPanel = eventsOut != null ? ("<section><h2>Upcoming Events</h2>" + eventsOut + "</section>") : "";

        String alerts = "";
        if (StringUtils.hasText(flash)) alerts += "<div class='flash ok'>" + escape(flash) + "</div>";
        if (StringUtils.hasText(error)) alerts += "<div class='flash err'>" + escape(error) + "</div>";

        return """
            <!DOCTYPE html>
            <html lang="en">
            <head>
              <meta charset="utf-8" />
              <meta name="viewport" content="width=device-width, initial-scale=1" />
              <title>Bridge Webhook Admin</title>
              <style>
                :root { --bg:#0b0c10; --fg:#e6edf3; --muted:#9aa7b2; --accent:#2f81f7; --ok:#2ea043; --warn:#d29922; --err:#f85149; }
                body { font-family: system-ui, -apple-system, Segoe UI, Roboto, Ubuntu, Cantarell, Noto Sans, Helvetica, Arial, "Apple Color Emoji", "Segoe UI Emoji"; background: var(--bg); color: var(--fg); margin: 0; }
                header { padding: 16px 20px; background: #0d1117; border-bottom: 1px solid #30363d; position: sticky; top: 0; }
                header h1 { font-size: 18px; margin: 0; }
                main { padding: 16px 20px; display: grid; grid-template-columns: 1fr; gap: 16px; }
                section { background: #0d1117; border: 1px solid #30363d; border-radius: 8px; padding: 16px; }
                h2 { margin: 0 0 12px 0; font-size: 16px; }
                label { display: block; font-size: 12px; color: var(--muted); margin-bottom: 4px; }
                input[type=text], input[type=url], textarea { width: 100%; box-sizing: border-box; background: #161b22; color: var(--fg); border: 1px solid #30363d; border-radius: 6px; padding: 8px; }
                input[type=checkbox] { transform: translateY(2px); }
                .row { display: grid; grid-template-columns: 1fr 1fr; gap: 12px; }
                .actions { display: flex; gap: 8px; flex-wrap: wrap; }
                button { background: #21262d; color: var(--fg); border: 1px solid #30363d; border-radius: 6px; padding: 8px 10px; cursor: pointer; }
                button.ok { background: var(--ok); border-color: var(--ok); }
                button.danger { background: var(--err); border-color: var(--err); }
                select { width: 100%; box-sizing: border-box; background: #161b22; color: var(--fg); border:1px solid #30363d; border-radius:6px; padding:8px; }
                .grid-cats { display: grid; grid-template-columns: repeat(auto-fill,minmax(220px,1fr)); gap:4px 12px; margin-top:4px; }
                .grid-cats label { background:#161b22; padding:4px 6px; border:1px solid #30363d; border-radius:6px; font-size:12px; display:flex; gap:6px; align-items:center; }
                .grid-cats input[type=checkbox] { margin:0; }
                table { width: 100%; border-collapse: collapse; font-size: 13px; }
                th, td { border-top: 1px solid #30363d; padding: 8px; text-align: left; vertical-align: top; }
                th { color: var(--muted); font-weight: 600; }
                pre { background: #0d1117; border: 1px solid #30363d; border-radius: 8px; padding: 12px; overflow: auto; max-height: 360px; white-space: pre-wrap; word-break: break-word; overflow-wrap: anywhere; font-family: ui-monospace, SFMono-Regular, Menlo, Monaco, Consolas, "Liberation Mono", "Courier New", monospace; }
                .pill { display: inline-block; padding: 2px 6px; border: 1px solid #30363d; border-radius: 999px; font-size: 12px; margin-right: 4px; }
                .muted { color: var(--muted); }
                .flash.ok { background:#12261a; border:1px solid #224d30; padding:8px 10px; border-radius:6px; }
                .flash.err { background:#341416; border:1px solid #5d2025; padding:8px 10px; border-radius:6px; }
                a { color: var(--accent); text-decoration: none; }
              </style>
            </head>
            <body>
              <header>
                <h1>Bridge Webhook Admin</h1>
              </header>
              <main>
                %ALERTS%
                <section>
                  <h2>Webhooks List</h2>
                  %TABLE%
                </section>

                <section>
                  <h2>Create Webhook</h2>
                  <form method="post" action="/admin/bridge/create">
                    %CSRF%
                    <div class="row">
                      <div>
                        <label for="c_url">URL</label>
                        <input id="c_url" name="url" type="url" placeholder="https://your-ngrok-domain/webhook/bridge" required />
                      </div>
                      <div>
                        <label>Enabled</label>
                        <div><input id="c_enabled" name="enabled" type="checkbox" checked /> <span class="muted">Receive events</span></div>
                      </div>
                    </div>
                    <label for="c_epoch">Event Epoch (start point)</label>
                    <select id="c_epoch" name="event_epoch">
                      <option value="webhook_creation">webhook_creation</option>
                      <option value="beginning_of_time" selected>beginning_of_time</option>
                    </select>
                    <div style="margin-top:10px">
                      <label>Event Categories</label>
                      <div class="grid-cats">
                        <label><input type="checkbox" name="event_categories" value="customer" checked /> customer</label>
                        <label><input type="checkbox" name="event_categories" value="kyc_link" checked /> kyc_link</label>
                        <label><input type="checkbox" name="event_categories" value="liquidation_address.drain" /> liquidation_address.drain</label>
                        <label><input type="checkbox" name="event_categories" value="static_memo.activity" /> static_memo.activity</label>
                        <label><input type="checkbox" name="event_categories" value="transfer" /> transfer</label>
                        <label><input type="checkbox" name="event_categories" value="virtual_account.activity" /> virtual_account.activity</label>
                        <label><input type="checkbox" name="event_categories" value="card_account" /> card_account</label>
                        <label><input type="checkbox" name="event_categories" value="card_transaction" /> card_transaction</label>
                        <label><input type="checkbox" name="event_categories" value="card_withdrawal" /> card_withdrawal</label>
                        <label><input type="checkbox" name="event_categories" value="posted_card_account_transaction" /> posted_card_account_transaction</label>
                      </div>
                      <div class="muted" style="font-size:11px;margin-top:4px">Check all categories you want delivered.</div>
                    </div>
                    <label for="c_events" style="margin-top:10px">Events (comma-separated, legacy fallback)</label>
                    <input id="c_events" name="events" type="text" placeholder="customer.created,customer.updated" />
                    <div class="actions" style="margin-top:10px">
                      <button class="ok" type="submit">Create</button>
                    </div>
                  </form>
                </section>

                %SELECTED_PANEL%
                %LOGS_PANEL%
                %EVENTS_PANEL%
              </main>
            </body>
            </html>
        """
                .replace("%ALERTS%", alerts)
                .replace("%TABLE%", tb.toString())
                .replace("%CSRF%", csrfInput)
                .replace("%SELECTED_PANEL%", selectedPanel)
                .replace("%LOGS_PANEL%", logsPanel)
                .replace("%EVENTS_PANEL%", eventsPanel);
    }

    private static String basicError(String msg) {
        return "<html><body><h1>Error</h1><pre>" + escape(msg) + "</pre></body></html>";
    }

    private static String escape(String in) { return in == null ? "" : in.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;"); }
    private static String spanMuted(String s) { return "<span class='muted'>" + escape(s) + "</span>"; }
    private static String link(String text, String href) { return "<a href='" + href + "'>" + escape(text) + "</a>"; }
    private static String enc(String s) { return URLEncoder.encode(s == null ? "" : s, StandardCharsets.UTF_8); }

    private static boolean toBool(JsonNode n, String field) { return n != null && n.has(field) && n.path(field).asBoolean(false); }
    private static List<String> toArray(JsonNode n, String field) {
        List<String> out = new ArrayList<>();
        if (n != null && n.has(field) && n.path(field).isArray()) {
            for (JsonNode e : n.path(field)) out.add(e.asText());
        }
        return out;
    }
    private static String pretty(String json) {
        try { return MAPPER.writerWithDefaultPrettyPrinter().writeValueAsString(MAPPER.readTree(json)); } catch (Exception e) { return json; }
    }
    private static String safeString(String s) { return (s == null || s.isBlank()) ? null : s; }
    private static List<String> parseCsv(String csv) {
        if (!StringUtils.hasText(csv)) return Collections.emptyList();
        String[] parts = csv.split(",");
        List<String> out = new ArrayList<>();
        for (String p : parts) { String t = p.trim(); if (!t.isEmpty()) out.add(t); }
        return out;
    }

    private JsonNode extractArray(String json) throws Exception {
        JsonNode root = MAPPER.readTree(json);
        if (root.isArray()) return root;
        if (root.has("data") && root.get("data").isArray()) return root.get("data");
        if (root.has("webhooks") && root.get("webhooks").isArray()) return root.get("webhooks");
        if (root.has("items") && root.get("items").isArray()) return root.get("items");
        return MAPPER.createArrayNode();
    }

    private String fetchRaw(String path) throws Exception {
        var env = switch (Objects.requireNonNull(bridgeProps.getMode(), "bridge.mode").toLowerCase()) {
            case "live", "prod", "production" -> bridgeProps.getLive();
            case "sandbox", "sbx", "test", "" -> bridgeProps.getSandbox();
            default -> throw new IllegalArgumentException("Unknown bridge.mode=" + bridgeProps.getMode());
        };
        String baseUrl = Objects.requireNonNull(env.getBaseUrl(), "Bridge baseUrl");
        String apiKey = Objects.requireNonNull(env.getApiKey(), "Bridge apiKey");
        String url = (baseUrl.endsWith("/v0") ? baseUrl : baseUrl + "/v0") + path;
        okhttp3.Request req = new okhttp3.Request.Builder()
                .url(url)
                .get()
                .addHeader("Api-Key", apiKey)
                .build();
        try (okhttp3.Response resp = HTTP.newCall(req).execute()) {
            if (!resp.isSuccessful()) throw new IllegalStateException("Bridge API " + resp.code() + " for " + path + ": " + (resp.body() != null ? resp.body().string() : ""));
            return resp.body() != null ? resp.body().string() : "{}";
        }
    }

    private String requestRaw(String method, String path, String body) throws Exception {
        var env = switch (Objects.requireNonNull(bridgeProps.getMode(), "bridge.mode").toLowerCase()) {
            case "live", "prod", "production" -> bridgeProps.getLive();
            case "sandbox", "sbx", "test", "" -> bridgeProps.getSandbox();
            default -> throw new IllegalArgumentException("Unknown bridge.mode=" + bridgeProps.getMode());
        };
        String baseUrl = Objects.requireNonNull(env.getBaseUrl(), "Bridge baseUrl");
        String apiKey = Objects.requireNonNull(env.getApiKey(), "Bridge apiKey");
        String url = (baseUrl.endsWith("/v0") ? baseUrl : baseUrl + "/v0") + path;
        okhttp3.MediaType json = okhttp3.MediaType.parse("application/json");
        okhttp3.RequestBody reqBody = body == null ? okhttp3.RequestBody.create(new byte[0]) : okhttp3.RequestBody.create(body, json);
        okhttp3.Request.Builder b = new okhttp3.Request.Builder().url(url).addHeader("Api-Key", apiKey);
        // Bridge requires Idempotency-Key for POST requests
        if ("POST".equalsIgnoreCase(method)) {
            b.addHeader("Idempotency-Key", java.util.UUID.randomUUID().toString());
        }
        switch (method) {
            case "POST" -> b.post(reqBody);
            case "PUT" -> b.put(reqBody);
        case "DELETE" -> b.delete(reqBody);
            default -> throw new IllegalArgumentException("Unsupported method " + method);
        }
        try (okhttp3.Response resp = HTTP.newCall(b.build()).execute()) {
            if (!resp.isSuccessful()) throw new IllegalStateException("Bridge API " + resp.code() + " for " + path + ": " + (resp.body() != null ? resp.body().string() : ""));
            return resp.body() != null ? resp.body().string() : "{}";
        }
    }

    @PostMapping(path = "/activate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String activate(@RequestParam("id") String id) {
      try {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("status", "active");
        // compatibility extras
        body.put("active", true);
        body.put("enabled", true);
        body.put("is_enabled", true);
        requestRaw("PUT", "/webhooks/" + id, MAPPER.writeValueAsString(body));
        return "redirect:/admin/bridge?selected=" + enc(id) + "&flash=" + enc("Activation attempted") + "&action=logs";
      } catch (Exception e) {
        return "redirect:/admin/bridge?selected=" + enc(id) + "&error=" + enc(e.getMessage()==null?"activate_failed":e.getMessage());
      }
    }

    @PostMapping(path = "/deactivate", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String deactivate(@RequestParam("id") String id) {
      try {
        Map<String,Object> body = new LinkedHashMap<>();
        body.put("status", "disabled");
        body.put("active", false);
        body.put("enabled", false);
        body.put("is_enabled", false);
        requestRaw("PUT", "/webhooks/" + id, MAPPER.writeValueAsString(body));
        return "redirect:/admin/bridge?selected=" + enc(id) + "&flash=" + enc("Deactivation attempted");
      } catch (Exception e) {
        return "redirect:/admin/bridge?selected=" + enc(id) + "&error=" + enc(e.getMessage()==null?"deactivate_failed":e.getMessage());
      }
    }

    @PostMapping(path = "/delete", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String delete(@RequestParam("id") String id) {
        try {
            // Bridge DELETE endpoint - empty body
            requestRaw("DELETE", "/webhooks/" + id, null);
            return "redirect:/admin/bridge?flash=" + enc("Webhook deleted") + ""; // no selected param
        } catch (Exception e) {
            return "redirect:/admin/bridge?selected=" + enc(id) + "&error=" + enc(e.getMessage()==null?"delete_failed":e.getMessage());
        }
    }

    @PostMapping(path = "/send-event", consumes = MediaType.APPLICATION_FORM_URLENCODED_VALUE)
    public String sendEvent(@RequestParam("webhookId") String webhookId, @RequestParam("eventId") String eventId) {
        try {
            bridgeFactory.webhooks().webhooksWebhookIDSendPost(webhookId, UUID.randomUUID().toString(), new SendWebhookPayload().eventId(eventId));
            return "redirect:/admin/bridge?selected=" + enc(webhookId) + "&action=events&flash=" + enc("Event sent");
        } catch (Exception e) {
            return "redirect:/admin/bridge?selected=" + enc(webhookId) + "&action=events&error=" + enc(e.getMessage() == null ? "send_failed" : e.getMessage());
        }
    }
}
