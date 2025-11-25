package org.jhely.money.base.ui.view.payments;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.*;
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
@Route(value = "finance/receive", layout = MainLayout.class)
@PageTitle("Finance · Receive")
public class ReceiveFundsView extends VerticalLayout {

    private final MockStablecoinAccountsService svc;

    public ReceiveFundsView(MockStablecoinAccountsService svc) {
        this.svc = svc;
        setSizeFull();
        setPadding(true);
        setSpacing(false);

        add(new PaymentsSubnav("/finance/receive"));
        add(page());
    }

    private Component page() {
        var wrapper = new VerticalLayout();
        wrapper.setPadding(false);
        wrapper.setSpacing(false);
        wrapper.setWidthFull();

        // ============ CRYPTO CARD ===========================================
        var cryptoCard = new Div();
        cryptoCard.getStyle()
            .set("padding", "20px")
            .set("borderRadius", "16px")
            .set("background", "var(--lumo-base-color)")
            .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)");
        cryptoCard.setWidthFull();

        var cryptoForm = new FormLayout();
        cryptoForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );

        var account = new Select<FinancialAccount>();
        account.setLabel("Account");
        account.setItems(svc.listAccounts());
        account.setItemLabelGenerator(a -> a.name);
        account.setValue(svc.getDefaultAccount());

        var asset = new Select<Asset>();
        asset.setLabel("Asset");
        asset.setItems(Asset.USDC, Asset.EURC, Asset.USDT);
        asset.setValue(Asset.USDC);

        var network = new Select<Network>();
        network.setLabel("Network");
        network.setItems(Network.BASE, Network.ETHEREUM, Network.SOLANA, Network.POLYGON, Network.ARBITRUM);
        network.setValue(Network.BASE);

        var addressField = new TextField("Deposit Address");
        addressField.setReadOnly(true);
        addressField.setWidthFull();

        var ensureAddrBtn = new Button("Generate / Load Address", e -> {
            String addr = svc.ensureDepositAddress(account.getValue().id, asset.getValue(), network.getValue());
            addressField.setValue(addr);
            UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", addr);
            Notification.show("Address ready & copied to clipboard", 2500, Notification.Position.BOTTOM_START);
        });

        var linkBtn = new Button("Create Payment Link", e -> openPaymentLinkDialog(account.getValue(), asset.getValue()));
        var toTx = new Button("View Transactions", ev -> UI.getCurrent().navigate("/finance/transactions"));

        cryptoForm.add(account, asset, network, addressField);
        var cryptoBtns = new HorizontalLayout(ensureAddrBtn, linkBtn, toTx);
        cryptoCard.add(new H3("Receive crypto"), new Paragraph("Create a deposit address or payment link."), cryptoForm, cryptoBtns);

        // ============ FIAT CARD (EUR/IBAN + USD/ACH) ========================
        var fiatCard = new Div();
        fiatCard.getStyle()
            .set("marginTop", "16px")
            .set("padding", "20px")
            .set("borderRadius", "16px")
            .set("background", "var(--lumo-base-color)")
            .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)");
        fiatCard.setWidthFull();

        // --- EUR / IBAN form ---
        var eurForm = new FormLayout();
        eurForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );
        var eurBeneficiary = roField("Beneficiary Name");
        var eurIban        = roField("IBAN");
        var eurBic         = roField("BIC / SWIFT");
        var eurBank        = roField("Bank Name");
        var eurRef         = roField("Payment Reference (optional)");
        Button eurBtn = new Button("Generate / Load EUR IBAN", e -> {
            BankDetails d = svc.ensureBankDetails(account.getValue().id, BankDetails.Rail.EUR_IBAN);
            eurBeneficiary.setValue(d.beneficiaryName);
            eurIban.setValue(d.iban);
            eurBic.setValue(d.bicSwift);
            eurBank.setValue(d.bankName);
            eurRef.setValue(d.reference);
            UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", d.iban);
            Notification.show("EUR/IBAN details ready (IBAN copied)", 2500, Notification.Position.BOTTOM_START);
        });

        eurForm.add(eurBeneficiary, eurIban, eurBic, eurBank, eurRef);

        // --- USD / ACH form ---
        var usdForm = new FormLayout();
        usdForm.setResponsiveSteps(
                new FormLayout.ResponsiveStep("0", 1),
                new FormLayout.ResponsiveStep("600px", 2)
        );
        var usdBeneficiary = roField("Beneficiary Name");
        var usdAccountNum  = roField("Account Number");
        var usdRoutingNum  = roField("Routing Number (ABA)");
        var usdBank        = roField("Bank Name");
        var usdType        = roField("Account Type");
        var usdRef         = roField("Payment Reference (optional)");
        Button usdBtn = new Button("Generate / Load USD ACH", e -> {
            BankDetails d = svc.ensureBankDetails(account.getValue().id, BankDetails.Rail.USD_ACH);
            usdBeneficiary.setValue(d.beneficiaryName);
            usdAccountNum.setValue(d.accountNumber);
            usdRoutingNum.setValue(d.routingNumber);
            usdBank.setValue(d.bankName);
            usdType.setValue(d.accountType);
            usdRef.setValue(d.reference);
            UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", d.accountNumber);
            Notification.show("USD/ACH details ready (Account Number copied)", 2500, Notification.Position.BOTTOM_START);
        });

        usdForm.add(usdBeneficiary, usdAccountNum, usdRoutingNum, usdBank, usdType, usdRef);

        var eurBox = sectionBox("EUR / IBAN (SEPA)", eurForm, eurBtn);
        var usdBox = sectionBox("USD / ACH", usdForm, usdBtn);

        fiatCard.add(new H3("Receive fiat to bank"),
                new Paragraph("Generate once and reuse. We’ll show existing details if already generated."),
                eurBox, usdBox);

        wrapper.add(cryptoCard, fiatCard);
        return wrapper;
    }

    private static TextField roField(String label) {
        TextField tf = new TextField(label);
        tf.setReadOnly(true);
        tf.setWidthFull();
        return tf;
    }

    private static Component sectionBox(String title, Component form, Button actionBtn) {
        var box = new Div();
        box.getStyle()
            .set("marginTop", "12px")
            .set("padding", "12px")
            .set("borderRadius", "12px")
            .set("background", "var(--lumo-contrast-5pct)");
        var head = new HorizontalLayout(new H3(title), actionBtn);
        head.setWidthFull();
        head.setAlignItems(Alignment.CENTER);
        head.expand(head.getComponentAt(0));
        box.add(head, form);
        return box;
    }

    private void openPaymentLinkDialog(FinancialAccount acc, Asset asset) {
        Dialog d = new Dialog();
        d.setHeaderTitle("Create payment link");

        BigDecimalField amount = new BigDecimalField("Amount");
        amount.setValue(new BigDecimal("100.00"));
        amount.setClearButtonVisible(true);
        amount.setPlaceholder("Enter amount");
        amount.setHelperText("Positive numbers only");
        amount.addValueChangeListener(e -> {
            BigDecimal v = e.getValue();
            if (v != null && v.signum() < 0) {
                amount.setValue(v.abs());
            }
        });

        TextField memo = new TextField("Memo / Reference");
        memo.setPlaceholder("Invoice 3021");

        var create = new Button("Create", ev -> {
            var pl = svc.createPaymentLink(acc.id, asset, amount.getValue(), memo.getValue());
            Notification.show("Payment link created", 2500, Notification.Position.BOTTOM_START);
            UI.getCurrent().getPage().executeJs("navigator.clipboard.writeText($0)", pl.url);
            Notification.show("Copied link: " + pl.url, 3000, Notification.Position.BOTTOM_START);
            d.close();
        });
        var cancel = new Button("Cancel", ev -> d.close());

        var layout = new VerticalLayout(amount, memo);
        layout.setPadding(false);

        d.add(layout);
        d.getFooter().add(cancel, create);
        d.open();
    }
}
