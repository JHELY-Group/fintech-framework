package org.jhely.money.base.ui.view.events;


import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentEvent;

/** UI-scoped event to signal that the current user's balance changed. */
public class BalanceChangedEvent extends ComponentEvent<Component> {
  public BalanceChangedEvent(Component source, boolean fromClient) {
    super(source, fromClient);
  }
}

