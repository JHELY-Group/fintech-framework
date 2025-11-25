package org.jhely.money.base.ui.view;

import java.io.Serializable;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.notification.NotificationVariant;
import com.vaadin.flow.server.ErrorEvent;
import com.vaadin.flow.server.ErrorHandler;

public class MainUIErrorHandler implements ErrorHandler, Serializable {

    private static final long serialVersionUID = 1L;
	private static final Logger log = LoggerFactory.getLogger(MainUIErrorHandler.class);

    @Override
    public void error(ErrorEvent event) {
        log.error("An unexpected error occurred", event.getThrowable());

        event.getComponent().flatMap(c -> c.getUI()).ifPresent(ui -> {
            ui.access(() -> {
                var notification = new Notification(
                        "An unexpected error has occurred. Please try again later.");
                notification.addThemeVariants(NotificationVariant.LUMO_ERROR);
                notification.setPosition(Notification.Position.TOP_CENTER);
                notification.setDuration(3000);
                notification.open();
            });
        });
    }
}
