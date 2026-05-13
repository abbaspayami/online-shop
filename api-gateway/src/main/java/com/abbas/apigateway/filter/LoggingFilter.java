package com.abbas.apigateway.filter;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.Optional;
import java.util.UUID;
import java.util.regex.Pattern;

@Component
public class LoggingFilter implements GlobalFilter, Ordered {

    private static final Logger log = LoggerFactory.getLogger(LoggingFilter.class);

    private static final String CORRELATION_ID_HEADER = "X-Correlation-Id";

    // Simple safe validation (avoid garbage / abuse)
    private static final Pattern VALID_ID_PATTERN =
            Pattern.compile("^[a-zA-Z0-9\\-]{10,100}$");

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        long startTime = System.currentTimeMillis();
        ServerHttpRequest request = exchange.getRequest();

        String correlationId = resolveCorrelationId(request);

        // Mutate request → propagate correlation ID downstream
        ServerWebExchange mutatedExchange = exchange.mutate()
                .request(request.mutate()
                        .header(CORRELATION_ID_HEADER, correlationId)
                        .build())
                .build();

        // Also return it to client
        mutatedExchange.getResponse().getHeaders()
                .add(CORRELATION_ID_HEADER, correlationId);

        log.info(">>> {} {} | correlationId={} | ip={}",
                request.getMethod(),
                buildFullPath(request),
                correlationId,
                getClientIp(request));

        return chain.filter(mutatedExchange)
                .doFinally(signalType -> {
                    long duration = System.currentTimeMillis() - startTime;
                    HttpStatusCode status = exchange.getResponse().getStatusCode();

                    log.info("<<< {} {} | correlationId={} | status={} | duration={}ms",
                            request.getMethod(),
                            buildFullPath(request),
                            correlationId,
                            status != null ? status.value() : "unknown",
                            duration);
                });
    }

    /**
     * Hybrid strategy:
     * - Accept client ID if valid
     * - Otherwise generate new one
     */
    private String resolveCorrelationId(ServerHttpRequest request) {
        return Optional.ofNullable(request.getHeaders().getFirst(CORRELATION_ID_HEADER))
                .filter(id -> !id.isBlank())
                .filter(id -> VALID_ID_PATTERN.matcher(id).matches())
                .orElse(UUID.randomUUID().toString());
    }

    /**
     * Builds path + query string.
     */
    private String buildFullPath(ServerHttpRequest request) {
        String path = request.getURI().getPath();
        String query = request.getURI().getQuery();
        return (query != null && !query.isBlank()) ? path + "?" + query : path;
    }

    /**
     * Extract real client IP (proxy-aware).
     */
    private String getClientIp(ServerHttpRequest request) {

        // 1. Standard header used by some proxies
        String realIp = request.getHeaders().getFirst("X-Real-IP");
        if (realIp != null && !realIp.isBlank()) {
            return realIp;
        }

        // 2. Load balancers / multiple proxies
        String forwarded = request.getHeaders().getFirst("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }

        // 3. Fallback
        return Optional.ofNullable(request.getRemoteAddress())
                .map(addr -> addr.getAddress().getHostAddress())
                .orElse("unknown");
    }

    /**
     * Runs very early in the filter chain.
     */
    @Override
    public int getOrder() {
        return -100;
    }
}