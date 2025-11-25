package org.jhely.money.base.ui.view;

import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.auth.AnonymousAllowed;

import jakarta.annotation.security.PermitAll;
import org.jhely.money.base.ui.view.MainLayout;

@Route(value = "", layout = MainLayout.class)
@PageTitle("Home")
@PermitAll
@AnonymousAllowed
public class HomeView extends Paragraph {
	public HomeView() {
		super("Welcome! This is the home page.");
	}
}