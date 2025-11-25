package org.jhely.money.base.ui.view.payments;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import jakarta.annotation.security.RolesAllowed;
import org.jhely.money.base.service.payments.mock.MockStablecoinAccountsService;
import org.jhely.money.base.service.payments.mock.PaymentModels.*;
import org.jhely.money.base.ui.view.MainLayout;

import java.time.ZoneId;
import java.time.format.DateTimeFormatter;

@RolesAllowed("USER")
@Route(value = "finance/transactions", layout = MainLayout.class)
@PageTitle("Finance · Transactions")
public class TransactionsView extends VerticalLayout {

    private final MockStablecoinAccountsService svc;

    public TransactionsView(MockStablecoinAccountsService svc) {
        this.svc = svc;
        setSizeFull();
        setPadding(true);
        setSpacing(false);
        setAlignItems(Alignment.STRETCH);

        add(new PaymentsSubnav("/finance/transactions"));
        add(table());
    }

    private Div table() {
        var card = new Div();
        card.getStyle()
            .set("padding", "16px")
            .set("borderRadius", "16px")
            .set("background", "var(--lumo-base-color)")
            .set("boxShadow", "0 4px 20px rgba(0,0,0,0.06)");
        card.setWidthFull(); // <-- stretch

        var grid = new Grid<Transaction>(Transaction.class, false);
        grid.setWidthFull();          // <-- stretch
//        grid.setHeightByRows(true);   // optional compact height
        // grid.addThemeVariants(GridVariant.LUMO_ROW_STRIPES); // optional

        var fmt = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                .withZone(ZoneId.systemDefault());

        grid.addColumn(t -> fmt.format(t.createdAt))
            .setHeader("Date")
            .setAutoWidth(false)
            .setFlexGrow(1);

        grid.addColumn(t -> t.type.name())
            .setHeader("Type")
            .setAutoWidth(false)
            .setFlexGrow(1);

        grid.addColumn(t -> t.asset.name())
            .setHeader("Asset")
            .setAutoWidth(false)
            .setFlexGrow(1);

        grid.addColumn(t -> (t.isCredit() ? "+" : "-") + t.amount.toPlainString())
            .setHeader("Amount")
            .setAutoWidth(false)
            .setFlexGrow(1);

        grid.addColumn(t -> t.status.name())
            .setHeader("Status")
            .setAutoWidth(false)
            .setFlexGrow(1);

        grid.addColumn(t -> t.network)
            .setHeader("Network")
            .setAutoWidth(false)
            .setFlexGrow(1);

        grid.addColumn(t -> t.memo)
            .setHeader("Memo")
            .setAutoWidth(false)
            .setFlexGrow(2); // give Memo more space

        grid.setItems(svc.listTransactions());
        grid.addItemClickListener(ev -> openDetails(ev.getItem()));

        card.add(new H3("Transactions"), grid);
        return card;
    }


    private void openDetails(Transaction t) {
        Dialog d = new Dialog();
        d.setHeaderTitle("Transaction " + t.id);
        var info = new Paragraph(
            "Type: " + t.type + "\n" +
            "Status: " + t.status + "\n" +
            "Asset: " + t.asset + "  Amount: " + t.amount + "\n" +
            "Network: " + t.network + "\n" +
            "From: " + t.from + "\n" +
            "To: " + t.to + "\n" +
            "Fee: " + t.fee + "\n" +
            "Memo: " + (t.memo == null ? "" : t.memo)
        );
        info.getStyle().set("white-space", "pre-wrap");
        d.add(info);
        d.open();
    }
}
