package org.jhely.money.base.config;

import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import org.jhely.money.base.ui.view.LoginRegisterView;

@Configuration
public class SecurityConfig extends VaadinWebSecurity {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Configure CSRF ignores and anonymous access for machine-to-machine endpoints BEFORE Vaadin defaults
		http.csrf(csrf -> csrf.ignoringRequestMatchers(
			new AntPathRequestMatcher("/api/stripe/webhook"),
			new AntPathRequestMatcher("/api/stripe/checkout-session"),
			new AntPathRequestMatcher("/api/bridge/webhook2"),
			new AntPathRequestMatcher("/api/x402/**")
		));

		http.authorizeHttpRequests(auth -> auth
			.requestMatchers(new AntPathRequestMatcher("/api/bridge/webhook2")).permitAll()
			.requestMatchers(new AntPathRequestMatcher("/api/x402/**")).permitAll()
		);

		// Let VaadinWebSecurity configure sensible defaults (routes, anyRequest, etc.)
		super.configure(http);
		setLoginView(http, LoginRegisterView.class); // ensures redirect instead of 401

		// Authorization: avoid adding requestMatchers after super (would conflict).
		// Use method-level annotations on controllers for exceptions (e.g., @PermitAll on webhook).
	}
}