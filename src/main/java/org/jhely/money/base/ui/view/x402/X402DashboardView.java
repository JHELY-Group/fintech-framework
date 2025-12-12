package org.jhely.money.base.ui.view.x402;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.tabs.Tab;
import com.vaadin.flow.component.tabs.Tabs;
import com.vaadin.flow.data.renderer.ComponentRenderer;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouterLink;
import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.domain.X402ApiRequestLog;
import org.jhely.money.base.domain.X402FacilitatorConfig;
import org.jhely.money.base.security.AuthenticatedUser;
import org.jhely.money.base.service.x402.X402FacilitatorService;
import org.jhely.money.base.service.x402.X402FacilitatorService.DashboardStats;
import org.jhely.money.base.ui.view.MainLayout;
import org.springframework.data.domain.PageRequest;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.List;

/**
 * Dashboard view for x402 API activity.
 * Shows API request logs, transaction history, and statistics.
 */
@RolesAllowed("USER")
@Route(value = "x402", layout = MainLayout.class)
@PageTitle("x402 Dashboard")
public class X402DashboardView extends VerticalLayout {

    private static final long serialVersionUID = 1L;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
            .withZone(ZoneId.systemDefault());

    private final X402FacilitatorService facilitatorService;
    private final AuthenticatedUser auth;

    private Grid<X402ApiRequestLog> requestLogGrid;
    private Grid<X402ApiRequestLog> transactionGrid;
    private Div statsContainer;
    private X402FacilitatorConfig config;

    public X402DashboardView(X402FacilitatorService facilitatorService, AuthenticatedUser auth) {
        this.facilitatorService = facilitatorService;
        this.auth = auth;

        setSizeFull();
        setPadding(true);
        setSpacing(true);

        add(buildHeader());
        
        // Load config first
        loadConfig();
        
        if (config != null) {
            add(buildNetworkStatusSection());
            add(buildStatsSection());
            add(buildTabsSection());
        } else {
            add(buildNoConfigMessage());
        }
    }

    private void loadConfig() {
        var user = auth.get().orElse(null);
        if (user == null) return;
        
        config = facilitatorService.getConfigForUser(String.valueOf(user.getId())).orElse(null);
    }

    private Component buildHeader() {
        var header = new HorizontalLayout();
        header.setWidthFull();
        header.setAlignItems(Alignment.CENTER);
        header.setJustifyContentMode(JustifyContentMode.BETWEEN);

        var left = new VerticalLayout();
        left.setPadding(false);
        left.setSpacing(false);

        var title = new H2("x402 Dashboard");
        var subtitle = new Paragraph("Monitor your API requests and transactions");
        subtitle.getStyle().set("color", "var(--lumo-secondary-text-color)");

        left.add(title, subtitle);

        var configLink = new RouterLink("Configuration →", X402FacilitatorView.class);
        configLink.getStyle()
                .set("textDecoration", "none")
                .set("color", "var(--lumo-primary-color)");

        var refreshBtn = new Button("Refresh", new Icon(VaadinIcon.REFRESH));
        refreshBtn.addClickListener(e -> refreshData());

        var rightLayout = new HorizontalLayout(configLink, refreshBtn);
        rightLayout.setSpacing(true);
        rightLayout.setAlignItems(Alignment.CENTER);

        header.add(left, rightLayout);
        return header;
    }

    private Component buildNoConfigMessage() {
        var card = new Div();
        card.getStyle()
                .set("padding", "40px")
                .set("borderRadius", "16px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("textAlign", "center");

        var icon = new Icon(VaadinIcon.WARNING);
        icon.setSize("48px");
        icon.getStyle().set("color", "var(--lumo-secondary-text-color)");

        var message = new H3("No Facilitator Configuration Found");
        var description = new Paragraph("You need to configure your x402 facilitator before viewing the dashboard.");

        var configLink = new RouterLink("Configure Facilitator →", X402FacilitatorView.class);
        configLink.getStyle()
                .set("display", "inline-block")
                .set("marginTop", "16px")
                .set("padding", "12px 24px")
                .set("background", "var(--lumo-primary-color)")
                .set("color", "white")
                .set("borderRadius", "8px")
                .set("textDecoration", "none");

        card.add(icon, message, description, configLink);
        return card;
    }

    private Component buildNetworkStatusSection() {
        var container = new Div();
        container.getStyle()
                .set("display", "flex")
                .set("flexWrap", "wrap")
                .set("gap", "16px")
                .set("marginBottom", "16px")
                .set("padding", "16px")
                .set("borderRadius", "12px")
                .set("background", "var(--lumo-contrast-5pct)");

        // Check which networks are configured
        boolean hasDevnet = config.getSolanaRpcDevnet() != null && !config.getSolanaRpcDevnet().isBlank();
        boolean hasMainnet = config.getSolanaRpcMainnet() != null && !config.getSolanaRpcMainnet().isBlank();

        // Network status header
        var headerDiv = new Div();
        headerDiv.getStyle()
                .set("width", "100%")
                .set("marginBottom", "8px");
        var headerSpan = new Span("🌐 Network Configuration");
        headerSpan.getStyle()
                .set("fontWeight", "bold")
                .set("fontSize", "14px");
        headerDiv.add(headerSpan);
        container.add(headerDiv);

        // Devnet status
        var devnetBadge = createNetworkBadge(
                "DEVNET",
                hasDevnet,
                hasDevnet ? config.getSolanaRpcDevnet() : "Not configured",
                "#ff9800" // orange
        );
        container.add(devnetBadge);

        // Mainnet status
        var mainnetBadge = createNetworkBadge(
                "MAINNET",
                hasMainnet,
                hasMainnet ? config.getSolanaRpcMainnet() : "Not configured",
                "#4caf50" // green
        );
        container.add(mainnetBadge);

        // Add warning if no networks configured
        if (!hasDevnet && !hasMainnet) {
            var warning = new Div();
            warning.getStyle()
                    .set("width", "100%")
                    .set("padding", "12px")
                    .set("background", "#fff3cd")
                    .set("color", "#856404")
                    .set("borderRadius", "8px")
                    .set("marginTop", "8px");
            warning.add(new Span("⚠️ No RPC endpoints configured. Configure them in the Configuration page."));
            container.add(warning);
        }

        return container;
    }

    private Div createNetworkBadge(String networkName, boolean isEnabled, String rpcUrl, String enabledColor) {
        var badge = new Div();
        badge.getStyle()
                .set("display", "flex")
                .set("flexDirection", "column")
                .set("gap", "4px")
                .set("padding", "12px 16px")
                .set("borderRadius", "8px")
                .set("background", isEnabled ? enabledColor : "var(--lumo-contrast-20pct)")
                .set("color", isEnabled ? "white" : "var(--lumo-secondary-text-color)")
                .set("minWidth", "200px")
                .set("flex", "1");

        var nameSpan = new Span((isEnabled ? "✓ " : "✗ ") + networkName);
        nameSpan.getStyle()
                .set("fontWeight", "bold")
                .set("fontSize", "16px");

        var statusSpan = new Span(isEnabled ? "Enabled" : "Disabled");
        statusSpan.getStyle()
                .set("fontSize", "12px")
                .set("opacity", "0.9");

        var rpcSpan = new Span(truncateRpcUrl(rpcUrl));
        rpcSpan.getStyle()
                .set("fontSize", "11px")
                .set("opacity", "0.8")
                .set("fontFamily", "monospace")
                .set("wordBreak", "break-all");
        rpcSpan.getElement().setAttribute("title", rpcUrl);

        badge.add(nameSpan, statusSpan, rpcSpan);
        return badge;
    }

    private String truncateRpcUrl(String url) {
        if (url == null) return "—";
        if (url.length() <= 40) return url;
        return url.substring(0, 37) + "...";
    }

    private Component buildStatsSection() {
        statsContainer = new Div();
        statsContainer.getStyle()
                .set("display", "flex")
                .set("flexWrap", "wrap")
                .set("gap", "16px")
                .set("marginBottom", "24px");

        refreshStats();
        return statsContainer;
    }

    private void refreshStats() {
        if (config == null) return;

        statsContainer.removeAll();

        DashboardStats stats = facilitatorService.getDashboardStats(config);

        statsContainer.add(
                buildStatCard("Total Requests", String.valueOf(stats.totalRequests), VaadinIcon.ENVELOPE_O, "var(--lumo-primary-color)"),
                buildStatCard("Successful", String.valueOf(stats.successfulRequests), VaadinIcon.CHECK_CIRCLE, "var(--lumo-success-color)"),
                buildStatCard("Verify Calls", String.valueOf(stats.verifyRequests), VaadinIcon.SEARCH, "var(--lumo-contrast-70pct)"),
                buildStatCard("Settlements", String.valueOf(stats.settleRequests), VaadinIcon.DOLLAR, "var(--lumo-success-color)"),
                buildStatCard("Success Rate", String.format("%.1f%%", stats.getSuccessRate()), VaadinIcon.CHART, "var(--lumo-primary-color)"),
                buildStatCard("Total Settled", formatAmount(stats.totalSettledAmount), VaadinIcon.WALLET, "var(--lumo-success-color)")
        );
    }

    private Component buildStatCard(String label, String value, VaadinIcon iconType, String color) {
        var card = new Div();
        card.getStyle()
                .set("padding", "20px")
                .set("borderRadius", "12px")
                .set("background", "var(--lumo-contrast-5pct)")
                .set("display", "flex")
                .set("flexDirection", "column")
                .set("alignItems", "flex-start")
                .set("minWidth", "160px")
                .set("flex", "1 1 160px")
                .set("maxWidth", "220px");

        var icon = new Icon(iconType);
        icon.setSize("24px");
        icon.getStyle().set("color", color);

        var valueText = new Span(value);
        valueText.getStyle()
                .set("fontSize", "24px")
                .set("fontWeight", "bold")
                .set("marginTop", "8px");

        var labelText = new Span(label);
        labelText.getStyle()
                .set("color", "var(--lumo-secondary-text-color)")
                .set("fontSize", "14px");

        card.add(icon, valueText, labelText);
        return card;
    }

    private String formatAmount(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) == 0) {
            return "$0.00";
        }
        // Assuming USDC with 6 decimals
        BigDecimal usdAmount = amount.divide(new BigDecimal("1000000"), 2, RoundingMode.HALF_UP);
        return "$" + usdAmount.toPlainString();
    }

    private Component buildTabsSection() {
        var container = new VerticalLayout();
        container.setPadding(false);
        container.setSpacing(false);
        container.setSizeFull();

        var allRequestsTab = new Tab("All Requests");
        var transactionsTab = new Tab("Transactions");

        var tabs = new Tabs(allRequestsTab, transactionsTab);
        tabs.setWidthFull();

        var requestLogContainer = new Div();
        requestLogContainer.setSizeFull();
        requestLogContainer.add(buildRequestLogGrid());

        var transactionContainer = new Div();
        transactionContainer.setSizeFull();
        transactionContainer.add(buildTransactionGrid());
        transactionContainer.setVisible(false);

        tabs.addSelectedChangeListener(event -> {
            requestLogContainer.setVisible(event.getSelectedTab() == allRequestsTab);
            transactionContainer.setVisible(event.getSelectedTab() == transactionsTab);
        });

        container.add(tabs, requestLogContainer, transactionContainer);
        return container;
    }

    private Component buildRequestLogGrid() {
        requestLogGrid = new Grid<>();
        requestLogGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        requestLogGrid.setWidthFull();
        requestLogGrid.setHeight("500px");
        requestLogGrid.setColumnReorderingAllowed(true);

        requestLogGrid.addColumn(log -> DATE_FORMATTER.format(log.getTimestamp()))
                .setHeader("Timestamp")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);

        requestLogGrid.addColumn(X402ApiRequestLog::getEndpoint)
                .setHeader("Endpoint")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);

        requestLogGrid.addColumn(X402ApiRequestLog::getMethod)
                .setHeader("Method")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);

        requestLogGrid.addColumn(new ComponentRenderer<>(log -> {
            var badge = new Span(log.isSuccess() ? "Success" : "Failed");
            badge.getStyle()
                    .set("padding", "4px 8px")
                    .set("borderRadius", "4px")
                    .set("fontSize", "12px")
                    .set("fontWeight", "bold")
                    .set("background", log.isSuccess() ? "var(--lumo-success-color-10pct)" : "var(--lumo-error-color-10pct)")
                    .set("color", log.isSuccess() ? "var(--lumo-success-color)" : "var(--lumo-error-color)");
            return badge;
        })).setHeader("Status").setAutoWidth(true).setResizable(true).setFlexGrow(0);

        requestLogGrid.addColumn(X402ApiRequestLog::getNetwork)
                .setHeader("Network")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);

        requestLogGrid.addColumn(log -> formatAmountForGrid(log.getAmount()))
                .setHeader("Amount")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);

        requestLogGrid.addColumn(log -> log.getDurationMs() != null ? log.getDurationMs() + "ms" : "-")
                .setHeader("Duration")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);

        requestLogGrid.addColumn(X402ApiRequestLog::getErrorMessage)
                .setHeader("Error")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(1);

        refreshRequestLogs();
        return requestLogGrid;
    }

    private Component buildTransactionGrid() {
        transactionGrid = new Grid<>();
        transactionGrid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES);
        transactionGrid.setWidthFull();
        transactionGrid.setHeight("500px");
        transactionGrid.setColumnReorderingAllowed(true);

        transactionGrid.addColumn(log -> DATE_FORMATTER.format(log.getTimestamp()))
                .setHeader("Timestamp")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);

        transactionGrid.addColumn(X402ApiRequestLog::getNetwork)
                .setHeader("Network")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);

        transactionGrid.addColumn(log -> formatAmountForGrid(log.getAmount()))
                .setHeader("Amount")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);

        transactionGrid.addColumn(new ComponentRenderer<>(log -> {
            if (log.getPayer() == null) return new Span("-");
            var link = new Anchor(getSolanaAccountUrl(log.getPayer(), log.getNetwork()), log.getPayer());
            link.setTarget("_blank");
            link.getStyle().set("fontFamily", "monospace").set("fontSize", "12px");
            return link;
        })).setHeader("Payer").setAutoWidth(true).setResizable(true).setFlexGrow(1);

        transactionGrid.addColumn(new ComponentRenderer<>(log -> {
            if (log.getRecipient() == null) return new Span("-");
            var link = new Anchor(getSolanaAccountUrl(log.getRecipient(), log.getNetwork()), log.getRecipient());
            link.setTarget("_blank");
            link.getStyle().set("fontFamily", "monospace").set("fontSize", "12px");
            return link;
        })).setHeader("Recipient").setAutoWidth(true).setResizable(true).setFlexGrow(1);

        transactionGrid.addColumn(new ComponentRenderer<>(log -> {
            if (log.getTxHash() == null) return new Span("-");
            
            var link = new Anchor(getSolanaExplorerUrl(log.getTxHash(), log.getNetwork()), 
                    log.getTxHash());
            link.setTarget("_blank");
            link.getStyle()
                    .set("fontFamily", "monospace")
                    .set("fontSize", "12px");
            return link;
        })).setHeader("Transaction").setAutoWidth(true).setResizable(true).setFlexGrow(1);

        transactionGrid.addColumn(log -> log.getSlot() != null ? log.getSlot().toString() : "-")
                .setHeader("Slot")
                .setAutoWidth(true)
                .setResizable(true)
                .setFlexGrow(0);

        refreshTransactions();
        return transactionGrid;
    }

    private String formatAmountForGrid(BigDecimal amount) {
        if (amount == null) return "-";
        // Assuming USDC with 6 decimals
        BigDecimal usdAmount = amount.divide(new BigDecimal("1000000"), 6, RoundingMode.HALF_UP);
        return usdAmount.stripTrailingZeros().toPlainString() + " USDC";
    }

    private String truncate(String str, int maxLen) {
        if (str == null) return "-";
        if (str.length() <= maxLen) return str;
        return str.substring(0, maxLen - 3) + "...";
    }

    private String getSolanaExplorerUrl(String txHash, String network) {
        if ("solana-devnet".equals(network) || "solana-devnet".equalsIgnoreCase(network)) {
            return "https://explorer.solana.com/tx/" + txHash + "?cluster=devnet";
        }
        return "https://explorer.solana.com/tx/" + txHash;
    }

    private String getSolanaAccountUrl(String address, String network) {
        if ("solana-devnet".equals(network) || "solana-devnet".equalsIgnoreCase(network)) {
            return "https://explorer.solana.com/address/" + address + "?cluster=devnet";
        }
        return "https://explorer.solana.com/address/" + address;
    }

    private void refreshData() {
        loadConfig();
        if (config != null) {
            refreshStats();
            refreshRequestLogs();
            refreshTransactions();
        }
    }

    private void refreshRequestLogs() {
        if (config == null) return;
        List<X402ApiRequestLog> logs = facilitatorService.getRecentApiRequestLogs(config);
        requestLogGrid.setItems(logs);
    }

    private void refreshTransactions() {
        if (config == null) return;
        var page = facilitatorService.getSuccessfulSettlements(config, PageRequest.of(0, 50));
        transactionGrid.setItems(page.getContent());
    }
}
