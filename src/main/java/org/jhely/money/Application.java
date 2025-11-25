package org.jhely.money;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

import com.vaadin.flow.component.page.AppShellConfigurator;
import com.vaadin.flow.component.page.Push;
import com.vaadin.flow.shared.communication.PushMode;
import com.vaadin.flow.theme.Theme;

@SpringBootApplication
@EnableScheduling
@Theme("default")
@Push(PushMode.AUTOMATIC)
public class Application implements AppShellConfigurator {

    private static final long serialVersionUID = -1217235434565461676L;

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
