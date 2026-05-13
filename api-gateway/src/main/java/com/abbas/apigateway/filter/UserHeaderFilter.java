package com.abbas.apigateway.filter;

import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.security.core.context.ReactiveSecurityContextHolder;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.oauth2.server.resource.authentication.JwtAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Component
public class UserHeaderFilter implements GlobalFilter, Ordered {

    // Headers that downstream services use to identify the caller
    private static final String HEADER_USER_ID    = "X-User-Id";
    private static final String HEADER_USER_EMAIL = "X-User-Email";
    private static final String HEADER_USER_NAME  = "X-User-Name";
    private static final String HEADER_USER_ROLES = "X-User-Roles";

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        // Step 1 — strip any X-User-* headers from the incoming request
        // This prevents clients from spoofing user identity
        ServerHttpRequest sanitizedRequest = exchange.getRequest().mutate()
                .headers(headers -> {
                    headers.remove(HEADER_USER_ID);
                    headers.remove(HEADER_USER_EMAIL);
                    headers.remove(HEADER_USER_NAME);
                    headers.remove(HEADER_USER_ROLES);
                })
                .build();

        ServerWebExchange sanitizedExchange = exchange.mutate()
                .request(sanitizedRequest)
                .build();

        // Step 2 — extract user info from the validated JWT and add as headers
        return ReactiveSecurityContextHolder.getContext()
                .map(SecurityContext::getAuthentication)
                .filter(auth -> auth instanceof JwtAuthenticationToken)
                .cast(JwtAuthenticationToken.class)
                .map(jwtAuth -> {
                    Map<String, Object> claims = jwtAuth.getToken().getClaims();

                    String userId   = String.valueOf(claims.getOrDefault("sub", ""));
                    String email    = String.valueOf(claims.getOrDefault("email", ""));
                    String username = String.valueOf(claims.getOrDefault("preferred_username", ""));
                    String roles    = extractRoles(claims);

                    return sanitizedExchange.getRequest().mutate()
                            .header(HEADER_USER_ID,    userId)
                            .header(HEADER_USER_EMAIL, email)
                            .header(HEADER_USER_NAME,  username)
                            .header(HEADER_USER_ROLES, roles)
                            .build();
                })
                .map(request -> sanitizedExchange.mutate().request(request).build())
                .defaultIfEmpty(sanitizedExchange)   // unauthenticated — pass through as-is
                .flatMap(chain::filter);
    }

    /**
     * Extracts realm roles from the JWT claim:
     * "realm_access": { "roles": ["USER", "ADMIN", ...] }
     */
    @SuppressWarnings("unchecked")
    private String extractRoles(Map<String, Object> claims) {
        try {
            Map<String, Object> realmAccess = (Map<String, Object>) claims.get("realm_access");
            if (realmAccess == null) return "";

            List<String> roles = (List<String>) realmAccess.get("roles");
            if (roles == null) return "";

            return String.join(",", roles);
        } catch (ClassCastException e) {
            return "";
        }
    }

    /**
     * Runs after Spring Security has validated the JWT (-1 ensures it runs
     * before default gateway filters but after security context is populated).
     */
    @Override
    public int getOrder() {
        return -1;
    }
}
