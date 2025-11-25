package org.jhely.money.base.config;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Lightweight request/response logger focused on Stripe endpoints to aid debugging.
 * Logs method, URI, status and principal for /api/stripe/** requests.
 */
@Component
@Order(Ordered.HIGHEST_PRECEDENCE + 20)
public class RequestResponseLoggingFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(RequestResponseLoggingFilter.class);

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        boolean track = uri != null && (uri.startsWith("/api/stripe/"));
        long start = System.currentTimeMillis();
        try {
            filterChain.doFilter(request, response);
        } finally {
            if (track) {
                long ms = System.currentTimeMillis() - start;
                int status = response.getStatus();
                Authentication auth = SecurityContextHolder.getContext().getAuthentication();
                String principal = (auth != null && auth.isAuthenticated()) ? String.valueOf(auth.getName()) : "anonymous";
                String location = response.getHeader("Location");
                if (status >= 300 && status < 400 && location != null) {
                    log.info("{} {} -> {} ({} ms) principal={} location={}",
                            request.getMethod(), uri, status, ms, principal, location);
                } else {
                    log.info("{} {} -> {} ({} ms) principal={}",
                            request.getMethod(), uri, status, ms, principal);
                }
            }
        }
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        String uri = request.getRequestURI();
        return uri == null || !(uri.startsWith("/api/stripe/"));
    }
}
