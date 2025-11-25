package org.jhely.money.base.ui.view.components;

import com.vaadin.flow.component.ClickEvent;
import com.vaadin.flow.component.ComponentEventListener;
import com.vaadin.flow.component.Unit;
import com.vaadin.flow.component.avatar.Avatar;
import com.vaadin.flow.component.contextmenu.MenuItem;
import com.vaadin.flow.component.menubar.MenuBar;
import com.vaadin.flow.component.menubar.MenuBarVariant;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;

import org.jhely.money.base.domain.UserAccount;

public class UserBadge extends HorizontalLayout {
	public UserBadge(UserAccount user, ComponentEventListener<ClickEvent<MenuItem>> onLogout) {
		setSpacing(true);
		setAlignItems(Alignment.CENTER);

		var avatar = new Avatar(user.getDisplayName());
		avatar.setWidth(28, Unit.PIXELS);
		avatar.setHeight(28, Unit.PIXELS);
		if (user.getAvatarUrl() != null)
			avatar.setImage(user.getAvatarUrl());

		var menu = new MenuBar();
		menu.addThemeVariants(MenuBarVariant.LUMO_TERTIARY_INLINE);
		MenuItem root = menu.addItem(user.getDisplayName());
		root.getSubMenu().addItem("Logout", onLogout);

		add(avatar, menu);
	}
}