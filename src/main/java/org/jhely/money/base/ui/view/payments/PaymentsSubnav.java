package org.jhely.money.base.ui.view.payments;

import com.vaadin.flow.component.html.Anchor;
import com.vaadin.flow.component.orderedlayout.FlexComponent;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

public class PaymentsSubnav extends HorizontalLayout {

	private static final long serialVersionUID = -3290567523751645373L;

	public PaymentsSubnav(String activePath) {
        setWidthFull();
        setPadding(false);
        setSpacing(true);
        setAlignItems(FlexComponent.Alignment.CENTER);
        getStyle().set("margin", "0 0 12px 0");

        add(link("Overview", "/finance", activePath));
        add(link("Receive", "/finance/receive", activePath));
        add(link("Send", "/finance/send", activePath));
        add(link("Transactions", "/finance/transactions", activePath));
    }

    private Anchor link(String label, String href, String activePath) {
        Anchor a = new Anchor(href, label);
        a.getStyle()
            .set("padding", "8px 12px")
            .set("borderRadius", "999px")
            .set("textDecoration", "none")
            .set("color", "var(--lumo-primary-text-color)");
        if (activePath != null && activePath.equalsIgnoreCase(href)) {
            a.getStyle()
                .set("background", "var(--lumo-primary-color-10pct)")
                .set("fontWeight", "600");
        }
        return a;
    }
}

