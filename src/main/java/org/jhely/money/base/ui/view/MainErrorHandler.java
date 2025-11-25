package org.jhely.money.base.ui.view;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.vaadin.flow.server.VaadinServiceInitListener;

@Configuration
class MainErrorHandler {

    private static final Logger log = LoggerFactory.getLogger(MainErrorHandler.class);

    @Bean
    public VaadinServiceInitListener errorHandlerInitializer() {
        return event -> event.getSource().addSessionInitListener(
            sessionInitEvent -> sessionInitEvent.getSession().setErrorHandler(new MainUIErrorHandler())
        );
    }
}
