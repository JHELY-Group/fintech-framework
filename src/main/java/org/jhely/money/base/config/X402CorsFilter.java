package org.jhely.money.base.config;

import jakarta.servlet.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.boot.web.servlet.FilterRegistrationBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;

import java.io.IOException;

/**
 * CORS filter configuration for x402 API endpoints.
 * Runs at highest priority to handle CORS before any other processing.
 */
@Configuration
public class X402CorsFilter {

    @Bean
    public FilterRegistrationBean<Filter> x402CorsFilterRegistration() {
        FilterRegistrationBean<Filter> registration = new FilterRegistrationBean<>();
        registration.setFilter(new X402CorsServletFilter());
        registration.addUrlPatterns("/api/x402/*");
        registration.setOrder(Ordered.HIGHEST_PRECEDENCE);
        registration.setName("x402CorsFilter");
        return registration;
    }

    /**
     * The actual filter implementation.
     */
    public static class X402CorsServletFilter implements Filter {

        @Override
        public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
                throws IOException, ServletException {
            
            HttpServletRequest request = (HttpServletRequest) req;
            HttpServletResponse response = (HttpServletResponse) res;
            
            String origin = request.getHeader("Origin");
            
            // Set CORS headers for all /api/x402 requests
            if (origin != null && (origin.startsWith("http://localhost:") || origin.startsWith("http://127.0.0.1:"))) {
                response.setHeader("Access-Control-Allow-Origin", origin);
                response.setHeader("Access-Control-Allow-Credentials", "true");
            } else {
                // Allow any origin for API calls (since we use API key auth)
                response.setHeader("Access-Control-Allow-Origin", "*");
            }
            
            response.setHeader("Access-Control-Allow-Methods", "GET, POST, PUT, DELETE, OPTIONS, HEAD");
            response.setHeader("Access-Control-Allow-Headers", "Content-Type, X-API-Key, Authorization, Accept, Origin, X-Requested-With");
            response.setHeader("Access-Control-Expose-Headers", "X-Payment-Requirements, X-Payment-Response");
            response.setHeader("Access-Control-Max-Age", "3600");
            
            // Handle preflight OPTIONS request immediately
            if ("OPTIONS".equalsIgnoreCase(request.getMethod())) {
                response.setStatus(HttpServletResponse.SC_OK);
                return;
            }
            
            chain.doFilter(req, res);
        }
    }
}
