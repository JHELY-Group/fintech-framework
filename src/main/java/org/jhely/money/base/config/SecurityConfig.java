package org.jhely.money.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import org.jhely.money.base.ui.view.LoginRegisterView;

import java.util.Arrays;
import java.util.List;

@Configuration
public class SecurityConfig extends VaadinWebSecurity {
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		// Enable CORS for x402 API endpoints
		http.cors(cors -> cors.configurationSource(corsConfigurationSource()));

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

	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		CorsConfiguration configuration = new CorsConfiguration();
		// Allow requests from common development origins
		configuration.setAllowedOrigins(Arrays.asList(
			"http://localhost:3000",  // Next.js dev server
			"http://localhost:3001",  // Alternative port
			"http://127.0.0.1:3000",
			"http://localhost:5173",  // Vite dev server
			"http://localhost:8080"
		));
		configuration.setAllowedMethods(Arrays.asList("GET", "POST", "PUT", "DELETE", "OPTIONS"));
		configuration.setAllowedHeaders(Arrays.asList(
			"Authorization",
			"Content-Type",
			"X-API-Key",
			"X-PAYMENT",
			"X-Requested-With"
		));
		configuration.setExposedHeaders(Arrays.asList(
			"X-Payment-Requirements"
		));
		configuration.setAllowCredentials(true);
		configuration.setMaxAge(3600L);

		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		// Apply CORS only to API endpoints
		source.registerCorsConfiguration("/api/**", configuration);
		return source;
	}
}