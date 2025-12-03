package org.jhely.money.base.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import com.vaadin.flow.spring.security.VaadinWebSecurity;

import org.jhely.money.base.ui.view.LoginRegisterView;

/**
 * Security configuration for Vaadin UI.
 * API security is handled separately in X402SecurityConfig.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig extends VaadinWebSecurity {

    /**
     * Filter chain to permit /error endpoint for API clients.
     * This prevents redirect to /login when API errors occur.
     */
    @Bean
    @Order(0)
    public SecurityFilterChain errorPageSecurityFilterChain(HttpSecurity http) throws Exception {
        http
            .securityMatcher("/error")
            .csrf(AbstractHttpConfigurer::disable)
            .authorizeHttpRequests(auth -> auth.anyRequest().permitAll());
        return http.build();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // Configure CSRF ignores for webhook endpoints and x402 API
        http.csrf(csrf -> csrf.ignoringRequestMatchers(
            new AntPathRequestMatcher("/api/stripe/webhook"),
            new AntPathRequestMatcher("/api/stripe/checkout-session"),
            new AntPathRequestMatcher("/api/bridge/webhook2"),
            new AntPathRequestMatcher("/api/x402/**")
        ));

        // Let VaadinWebSecurity configure sensible defaults
        super.configure(http);
        setLoginView(http, LoginRegisterView.class);
    }
}
