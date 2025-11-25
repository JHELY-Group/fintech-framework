package org.jhely.money.base.security;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.vaadin.flow.server.VaadinSession;

import org.jhely.money.base.domain.UserAccount;
import org.jhely.money.base.service.UserService;

@Component
public class AuthenticatedUser {
	private static final String KEY = "auth.user.email";
	private final UserService userService;

	public AuthenticatedUser(UserService userService) {
		this.userService = userService;
	}

	public void set(String email) {
		VaadinSession.getCurrent().setAttribute(KEY, email.toLowerCase());
	}

	public Optional<UserAccount> get() {
		var s = VaadinSession.getCurrent();
		if (s == null)
			return Optional.empty();
		var email = (String) s.getAttribute(KEY);
		if (email == null)
			return Optional.empty();
		return userService.findByEmail(email);
	}

	public void logout() {
		var session = VaadinSession.getCurrent();
		if (session != null) {
			session.getSession().invalidate();
			session.close();
		}
	}
}