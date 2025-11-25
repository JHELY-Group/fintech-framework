package org.jhely.money.base.ui.view.payments;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
import com.vaadin.flow.component.radiobutton.RadioButtonGroup;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.component.textfield.BigDecimalField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.service.payments.mock.MockStablecoinAccountsService;
import org.jhely.money.base.service.payments.mock.PaymentModels.*;
import org.jhely.money.base.ui.view.MainLayout;

import java.math.BigDecimal;

@RolesAllowed("USER")
@Route(value = "finance/send", layout = MainLayout.class)
@PageTitle("Finance · Send")
public class SendFundsView extends VerticalLayout {

    private final MockStablecoinAccountsService svc;

    public SendFundsView(MockStablecoinAccountsService svc) {
        this.svc = svc;
        setSizeFull();
        setPadding(true);
        setSpacing(false);

        add(new PaymentsSubnav("/finance/send"));
        add(page());
    }

    private Component page() {
        var card = new Div();
        card.getStyle()
            .set("padding", "20px")
            .set("borderRadius", "16px")
            .set("background", "var(--lumo-base-color)")
            .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)");

        var form = new FormLayout();
        form.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("900px", 2)
        );

        // ---------- Common: Source + Amount + Memo ----------
        var account = new Select<FinancialAccount>();
        account.setLabel("Source account");
        account.setItems(svc.listAccounts());
        account.setItemLabelGenerator(a -> a.name);
        account.setValue(svc.getDefaultAccount());

        BigDecimalField amount = new BigDecimalField("Amount");
        amount.setValue(new BigDecimal("25.00"));
        amount.setClearButtonVisible(true);
        amount.setPlaceholder("Enter amount");
        amount.setHelperText("Positive numbers only");
        amount.addValueChangeListener(e -> {
            BigDecimal v = e.getValue();
            if (v != null && v.signum() < 0) {
                amount.setValue(v.abs());
            }
        });

        TextField memo = new TextField("Memo (optional)");
        memo.setPlaceholder("Vendor payout");

        // ---------- Destination: Wallet or Bank ----------
        RadioButtonGroup<String> destType = new RadioButtonGroup<>();
        destType.setLabel("Destination Type");
        destType.setItems("WALLET", "BANK");
        destType.setValue("WALLET");

        // ===== WALLET SECTION =====
        var asset = new Select<Asset>();
        asset.setLabel("Asset");
        asset.setItems(Asset.USDC, Asset.EURC, Asset.USDT);
        asset.setValue(Asset.USDC);

        var network = new Select<Network>();
        network.setLabel("Network");
        network.setItems(Network.BASE, Network.ETHEREUM, Network.SOLANA, Network.POLYGON, Network.ARBITRUM);
        network.setValue(Network.BASE);

        TextField walletAddress = new TextField("Wallet Address");
        walletAddress.setPlaceholder("0x… for EVM, or Solana address");

        // ===== BANK SECTION =====
        // Which rail?
        RadioButtonGroup<String> bankRail = new RadioButtonGroup<>();
        bankRail.setLabel("Bank Rail");
        bankRail.setItems("EUR/IBAN", "USD/ACH");
        bankRail.setValue("EUR/IBAN");

        // --- EUR/IBAN fields
        FormLayout eurForm = new FormLayout();
        eurForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("900px", 2));
        TextField eurBeneficiary = new TextField("Beneficiary Name");
        TextField eurIban        = new TextField("IBAN");
        TextField eurBic         = new TextField("BIC / SWIFT (optional)");
        TextField eurRef         = new TextField("Payment Reference (optional)");
        eurForm.add(eurBeneficiary, eurIban, eurBic, eurRef);

        // --- USD/ACH fields
        FormLayout usdForm = new FormLayout();
        usdForm.setResponsiveSteps(new FormLayout.ResponsiveStep("0", 1), new FormLayout.ResponsiveStep("900px", 2));
        TextField usdBeneficiary = new TextField("Beneficiary Name");
        TextField usdAccountNum  = new TextField("Account Number");
        TextField usdRoutingNum  = new TextField("Routing Number (ABA)");
        Select<String> usdType = new Select<>();
        usdType.setLabel("Account Type");
        usdType.setItems("Checking", "Savings");

        usdType.setLabel("Account Type");
        TextField usdRef         = new TextField("Payment Reference (optional)");
        usdForm.add(usdBeneficiary, usdAccountNum, usdRoutingNum, usdType, usdRef);

        // Bank container (shown only when BANK)
        var bankBox = new Div();
        bankBox.getStyle()
            .set("marginTop", "8px")
            .set("padding", "12px")
            .set("borderRadius", "12px")
            .set("background", "var(--lumo-contrast-5pct)");
        bankBox.setVisible(false);
        bankBox.add(bankRail, eurForm, usdForm);
        usdForm.setVisible(false); // default rail: EUR/IBAN

        // Rail ↔ asset mapping (for demo: EUR/IBAN -> EURC, USD/ACH -> USDC)
        bankRail.addValueChangeListener(e -> {
            boolean eur = "EUR/IBAN".equals(e.getValue());
            eurForm.setVisible(eur);
            usdForm.setVisible(!eur);
            // Hide WALLET-only fields when BANK selected, but keep asset value aligned
            if ("BANK".equals(destType.getValue())) {
                asset.setValue(eur ? Asset.EURC : Asset.USDC);
            }
        });

        // Toggle sections based on destination type
        destType.addValueChangeListener(e -> {
            boolean wallet = "WALLET".equals(e.getValue());
            walletAddress.setVisible(wallet);
            asset.setVisible(wallet);   // asset selectable for wallet
            network.setVisible(wallet);

            bankBox.setVisible(!wallet);
            if (!wallet) {
                // When BANK, set asset based on rail (read-only behavior in UI)
                asset.setValue("EUR/IBAN".equals(bankRail.getValue()) ? Asset.EURC : Asset.USDC);
            }
        });

        // ---------- Layout composition ----------
        form.add(account, amount,
                 destType, memo,
                 walletAddress, asset, network,
                 bankBox);

        // ---------- Actions ----------
        var sendBtn = new Button("Send");
        sendBtn.addClickListener(ev -> {
            if (amount.getValue() == null || amount.getValue().signum() <= 0) {
                Notification.show("Please enter a positive amount", 2500, Notification.Position.MIDDLE);
                return;
            }

            Transaction tx;
            if ("WALLET".equals(destType.getValue())) {
                // Validate wallet path
                if (walletAddress.getValue().isBlank()) {
                    Notification.show("Please enter a wallet address", 2500, Notification.Position.MIDDLE);
                    return;
                }
                tx = svc.sendTransfer(
                        account.getValue().id,
                        "WALLET",
                        walletAddress.getValue(),
                        asset.getValue(),
                        amount.getValue(),
                        network.getValue(),
                        memo.getValue()
                );
            } else {
                // BANK path
                String rail = bankRail.getValue();
                String destPacked;

                if ("EUR/IBAN".equals(rail)) {
                    if (eurBeneficiary.getValue().isBlank() || eurIban.getValue().isBlank()) {
                        Notification.show("Please enter beneficiary name and IBAN", 2500, Notification.Position.MIDDLE);
                        return;
                    }
                    // Pack details into dest string for mock transport
                    destPacked = "IBAN:" + eurIban.getValue()
                            + (eurBic.getValue().isBlank() ? "" : (";BIC:" + eurBic.getValue()))
                            + ";BENEF:" + eurBeneficiary.getValue()
                            + (eurRef.getValue().isBlank() ? "" : (";REF:" + eurRef.getValue()));
                    tx = svc.sendTransfer(
                            account.getValue().id,
                            "BANK",
                            destPacked,
                            Asset.EURC,              // for demo we map to EURC
                            amount.getValue(),
                            Network.BASE,            // placeholder; not used for bank
                            memo.getValue()
                    );
                } else {
                    // USD/ACH
                    if (usdBeneficiary.getValue().isBlank()
                            || usdAccountNum.getValue().isBlank()
                            || usdRoutingNum.getValue().isBlank()
                            || usdType.getValue() == null) {
                        Notification.show("Please fill beneficiary, account, routing and type", 2500, Notification.Position.MIDDLE);
                        return;
                    }
                    destPacked = "ACH:ACCT=" + usdAccountNum.getValue()
                            + ";ROUT=" + usdRoutingNum.getValue()
                            + ";TYPE=" + usdType.getValue()
                            + ";BENEF=" + usdBeneficiary.getValue()
                            + (usdRef.getValue().isBlank() ? "" : (";REF=" + usdRef.getValue()));
                    tx = svc.sendTransfer(
                            account.getValue().id,
                            "BANK",
                            destPacked,
                            Asset.USDC,              // for demo we map to USDC
                            amount.getValue(),
                            Network.BASE,            // placeholder; not used for bank
                            memo.getValue()
                    );
                }
            }

            Notification.show("Transfer queued: " + tx.id, 3000, Notification.Position.BOTTOM_START);
            UI.getCurrent().navigate("/finance/transactions");
        });

        var cancelBtn = new Button("Cancel", e -> UI.getCurrent().navigate("/finance"));

        var actions = new HorizontalLayout(cancelBtn, sendBtn);
        actions.setSpacing(true);

        card.add(new H3("Send money"),
                new Paragraph("Send to a crypto wallet (selected network) or to a bank account (EUR/IBAN or USD/ACH)."),
                form, actions);
        return card;
    }
}
