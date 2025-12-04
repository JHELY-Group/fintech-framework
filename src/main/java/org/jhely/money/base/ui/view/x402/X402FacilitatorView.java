package org.jhely.money.base.ui.view.x402;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.component.orderedlayout.FlexComponent.JustifyContentMode;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.domain.X402FacilitatorConfig;
import org.jhely.money.base.security.AuthenticatedUser;
import org.jhely.money.base.service.x402.X402FacilitatorService;
import org.jhely.money.base.ui.view.MainLayout;

/**
 * View for configuring x402 Facilitator settings.
 * Allows users to set up their Solana-based x402 facilitator with API keys.
 */
@RolesAllowed("USER")
@Route(value = "x402/config", layout = MainLayout.class)
@PageTitle("x402 Configuration")
public class X402FacilitatorView extends VerticalLayout {

    private static final long serialVersionUID = 1L;

    private final X402FacilitatorService facilitatorService;
    private final AuthenticatedUser auth;

    // Form fields
    private TextField facilitatorName;
    private Checkbox enabledCheckbox;
    private TextField apiKeyDisplay;

    // Solana fields
    private Checkbox solanaEnabled;
    private PasswordField solanaPrivateKey;
    private TextField solanaRpcMainnet;
    private TextField solanaRpcDevnet;

    // Webhook fields
    private TextField webhookUrl;
    private PasswordField webhookSecret;

    public X402FacilitatorView(X402FacilitatorService facilitatorService,
            AuthenticatedUser auth) {
        this.facilitatorService = facilitatorService;
        this.auth = auth;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(buildHeader());
        add(buildInfoSection());
        add(buildConfigSection());

        loadConfig();
    }

    private Component buildHeader() {
        var header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        var title = new H2("x402 Configuration");
        var subtitle = new Paragraph("Configure your x402 payment facilitator for machine-to-machine payments on Solana");
        subtitle.getStyle().set("color", "var(--lumo-secondary-text-color)");

        var left = new VerticalLayout(title, subtitle);
        left.setPadding(false);
        left.setSpacing(false);

        // Right side with dashboard link and save button
        var dashboardBtn = new Button("Dashboard", new Icon(VaadinIcon.DASHBOARD));
        dashboardBtn.addClickListener(e -> getUI().ifPresent(ui -> ui.navigate(X402DashboardView.class)));
        
        var saveBtn = new Button("Save Configuration", new Icon(VaadinIcon.CHECK));
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.addClickListener(e -> saveConfig());

        var rightLayout = new HorizontalLayout(dashboardBtn, saveBtn);
        rightLayout.setSpacing(true);

        header.add(left, rightLayout);
        return header;
    }

    private Component buildInfoSection() {
        var card = new Div();
        card.getStyle()
                .set("padding", "20px")
                .set("borderRadius", "16px")
                .set("background", "var(--lumo-primary-color-10pct)")
                .set("marginBottom", "16px");

        var infoTitle = new H4("What is x402?");
        infoTitle.getStyle().set("marginTop", "0");

        var infoText = new Paragraph(
                "x402 is a protocol for machine-to-machine payments that enables AI agents and " +
                        "automated services to pay for resources using Solana and USDC. As a facilitator, you can verify and " +
                        "settle payments on behalf of your users.");

        var linksLayout = new HorizontalLayout();
        linksLayout.setSpacing(true);

        var x402Link = new Anchor("https://x402.org", "x402 Protocol →");
        x402Link.setTarget("_blank");

        var expressLink = new Anchor("https://www.npmjs.com/package/x402-express", "x402-express →");
        expressLink.setTarget("_blank");

        var nextLink = new Anchor("https://www.npmjs.com/package/x402-next", "x402-next →");
        nextLink.setTarget("_blank");

        linksLayout.add(x402Link, expressLink, nextLink);

        card.add(infoTitle, infoText, linksLayout);
        return card;
    }

    private Component buildConfigSection() {
        var wrapper = new VerticalLayout();
        wrapper.setPadding(false);
        wrapper.setSpacing(true);

        // API Key section
        wrapper.add(buildApiKeySection());

        // General settings
        wrapper.add(buildGeneralSection());

        // Solana configuration
        wrapper.add(buildSolanaSection());

        // Webhook configuration
        wrapper.add(buildWebhookSection());

        // Actions
        wrapper.add(buildActionsSection());

        return wrapper;
    }

    private Component buildApiKeySection() {
        var card = sectionCard("API Key");

        var description = new Paragraph(
                "Your API key is used to authenticate requests to your facilitator endpoints. " +
                        "Keep it secret and use it in the X-API-Key header.");
        description.getStyle().set("marginBottom", "16px");

        apiKeyDisplay = new TextField("Current API Key");
        apiKeyDisplay.setReadOnly(true);
        apiKeyDisplay.setWidthFull();
        apiKeyDisplay.setHelperText("API key is shown masked. Generate a new one to see the full key.");

        var generateBtn = new Button("Generate New API Key", new Icon(VaadinIcon.KEY));
        generateBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        generateBtn.addClickListener(e -> generateApiKey());

        var endpointsInfo = new Div();
        endpointsInfo.getStyle()
                .set("marginTop", "16px")
                .set("padding", "12px")
                .set("borderRadius", "8px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("fontFamily", "monospace")
                .set("fontSize", "var(--lumo-font-size-s)");

        var endpointsTitle = new Paragraph("Endpoints (x402 spec compliant):");
        endpointsTitle.getStyle().set("fontWeight", "bold").set("marginBottom", "8px");

        var endpoint0 = new Paragraph("GET  /api/x402/           → Facilitator info");
        var endpoint1 = new Paragraph("GET  /api/x402/supported  → Supported networks (kinds)");
        var endpoint2 = new Paragraph("POST /api/x402/verify     → Verify payment");
        var endpoint3 = new Paragraph("POST /api/x402/settle     → Settle payment");
        var endpoint4 = new Paragraph("GET  /api/x402/transaction/{txHash} → Transaction status");
        var endpoint5 = new Paragraph("GET  /api/x402/health     → Health check (no auth)");

        for (var p : new Paragraph[] { endpoint0, endpoint1, endpoint2, endpoint3, endpoint4, endpoint5 }) {
            p.getStyle().set("margin", "4px 0").set("fontFamily", "monospace");
        }

        endpointsInfo.add(endpointsTitle, endpoint0, endpoint1, endpoint2, endpoint3, endpoint4, endpoint5);

        card.add(description, apiKeyDisplay, generateBtn, endpointsInfo);
        return card;
    }

    private Component buildGeneralSection() {
        var card = sectionCard("General Settings");

        facilitatorName = new TextField("Facilitator Name");
        facilitatorName.setPlaceholder("My x402 Facilitator");
        facilitatorName.setWidthFull();

        enabledCheckbox = new Checkbox("Enable Facilitator");
        enabledCheckbox.setHelperText("When disabled, all API requests will be rejected");

        card.add(facilitatorName, enabledCheckbox);
        return card;
    }

    private Component buildSolanaSection() {
        var card = sectionCard("Solana Configuration");

        solanaEnabled = new Checkbox("Enable Solana Networks");
        solanaEnabled.setHelperText("Support solana-mainnet and solana-devnet");

        solanaPrivateKey = new PasswordField("Solana Private Key (Base58)");
        solanaPrivateKey.setWidthFull();
        solanaPrivateKey.setPlaceholder("4mSHJ1vegPaThr88c5gAJGq5S2oM9ZD7...");
        solanaPrivateKey.setHelperText("Facilitator's keypair for signing settlement transactions. Must be base58 encoded (e.g., exported from Phantom or solana-keygen).");

        solanaRpcMainnet = new TextField("Solana Mainnet RPC");
        solanaRpcMainnet.setWidthFull();
        solanaRpcMainnet.setPlaceholder("https://api.mainnet-beta.solana.com");
        solanaRpcMainnet.setHelperText("Leave empty to use default public RPC");

        solanaRpcDevnet = new TextField("Solana Devnet RPC");
        solanaRpcDevnet.setWidthFull();
        solanaRpcDevnet.setPlaceholder("https://api.devnet.solana.com");
        solanaRpcDevnet.setHelperText("Leave empty to use default public RPC");

        card.add(solanaEnabled, solanaPrivateKey, solanaRpcMainnet, solanaRpcDevnet);
        return card;
    }

    private Component buildWebhookSection() {
        var card = sectionCard("Webhook Configuration (Optional)");

        var description = new Paragraph(
                "Configure webhooks to receive notifications about settlements and events.");
        description.getStyle().set("marginBottom", "16px");

        webhookUrl = new TextField("Webhook URL");
        webhookUrl.setWidthFull();
        webhookUrl.setPlaceholder("https://your-server.com/x402-webhook");

        webhookSecret = new PasswordField("Webhook Secret");
        webhookSecret.setWidthFull();
        webhookSecret.setHelperText("Used to verify webhook signatures");

        card.add(description, webhookUrl, webhookSecret);
        return card;
    }

    private Component buildActionsSection() {
        var layout = new HorizontalLayout();
        layout.setSpacing(true);
        layout.getStyle().set("marginTop", "16px");

        var saveBtn = new Button("Save Configuration", new Icon(VaadinIcon.CHECK));
        saveBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        saveBtn.addClickListener(e -> saveConfig());

        var cancelBtn = new Button("Reset", e -> loadConfig());

        layout.add(saveBtn, cancelBtn);
        return layout;
    }

    private Div sectionCard(String title) {
        var card = new Div();
        card.getStyle()
                .set("padding", "20px")
                .set("borderRadius", "16px")
                .set("background", "var(--lumo-base-color)")
                .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)")
                .set("marginBottom", "16px");
        card.setWidthFull();

        var header = new H4(title);
        header.getStyle().set("marginTop", "0").set("marginBottom", "16px");
        card.add(header);

        return card;
    }

    private void loadConfig() {
        var user = auth.get().orElse(null);
        if (user == null) {
            return;
        }

        var configOpt = facilitatorService.getConfigForUser(String.valueOf(user.getId()));

        if (configOpt.isPresent()) {
            var config = configOpt.get();

            facilitatorName.setValue(config.getFacilitatorName() != null ? config.getFacilitatorName() : "");
            enabledCheckbox.setValue(config.isEnabled());
            apiKeyDisplay.setValue(config.getApiKeyMasked() != null ? config.getApiKeyMasked() : "No API key generated");

            solanaEnabled.setValue(config.isSolanaEnabled());
            solanaRpcMainnet.setValue(config.getSolanaRpcMainnet() != null ? config.getSolanaRpcMainnet() : "");
            solanaRpcDevnet.setValue(config.getSolanaRpcDevnet() != null ? config.getSolanaRpcDevnet() : "");

            webhookUrl.setValue(config.getWebhookUrl() != null ? config.getWebhookUrl() : "");
        } else {
            // Clear form for new config
            facilitatorName.clear();
            enabledCheckbox.setValue(false);
            apiKeyDisplay.setValue("No API key generated");
            solanaEnabled.setValue(false);
            solanaRpcMainnet.clear();
            solanaRpcDevnet.clear();
            webhookUrl.clear();
        }

        // Clear sensitive fields (never show stored values)
        solanaPrivateKey.clear();
        webhookSecret.clear();
    }

    private void saveConfig() {
        var user = auth.get().orElse(null);
        if (user == null) {
            Notification.show("Not authenticated", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        try {
            X402FacilitatorConfig updates = new X402FacilitatorConfig();
            updates.setFacilitatorName(facilitatorName.getValue());
            updates.setEnabled(enabledCheckbox.getValue());

            updates.setSolanaEnabled(solanaEnabled.getValue());
            if (!solanaPrivateKey.isEmpty()) {
                updates.setSolanaPrivateKey(solanaPrivateKey.getValue());
            }
            updates.setSolanaRpcMainnet(solanaRpcMainnet.getValue());
            updates.setSolanaRpcDevnet(solanaRpcDevnet.getValue());

            updates.setWebhookUrl(webhookUrl.getValue());
            if (!webhookSecret.isEmpty()) {
                updates.setWebhookSecret(webhookSecret.getValue());
            }

            facilitatorService.createOrUpdateConfig(String.valueOf(user.getId()), updates);

            Notification.show("Configuration saved!", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);

            loadConfig(); // Refresh to show updated state

        } catch (Exception e) {
            Notification.show("Error saving configuration: " + e.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void generateApiKey() {
        var user = auth.get().orElse(null);
        if (user == null) {
            Notification.show("Not authenticated", 3000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
            return;
        }

        try {
            String newApiKey = facilitatorService.generateApiKey(String.valueOf(user.getId()));

            // Show the API key in a dialog (only shown once!)
            showApiKeyDialog(newApiKey);

            loadConfig(); // Refresh to show masked key

        } catch (Exception e) {
            Notification.show("Error generating API key: " + e.getMessage(), 5000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_ERROR);
        }
    }

    private void showApiKeyDialog(String apiKey) {
        Dialog dialog = new Dialog();
        dialog.setHeaderTitle("Your New API Key");
        dialog.setWidth("500px");

        var content = new VerticalLayout();
        content.setPadding(false);

        var warning = new Paragraph("⚠️ Copy this API key now. It will not be shown again!");
        warning.getStyle()
                .set("color", "var(--lumo-error-color)")
                .set("fontWeight", "bold");

        var keyField = new TextField();
        keyField.setValue(apiKey);
        keyField.setReadOnly(true);
        keyField.setWidthFull();
        keyField.getStyle()
                .set("fontFamily", "monospace")
                .set("fontSize", "var(--lumo-font-size-s)");

        var copyBtn = new Button("Copy to Clipboard", new Icon(VaadinIcon.COPY));
        copyBtn.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        copyBtn.addClickListener(e -> {
            keyField.getElement().executeJs("navigator.clipboard.writeText($0)", apiKey);
            Notification.show("API key copied to clipboard!", 2000, Notification.Position.MIDDLE)
                    .addThemeVariants(NotificationVariant.LUMO_SUCCESS);
        });

        var closeBtn = new Button("Close", e -> dialog.close());

        content.add(warning, keyField, new HorizontalLayout(copyBtn, closeBtn));

        dialog.add(content);
        dialog.open();
    }
}
