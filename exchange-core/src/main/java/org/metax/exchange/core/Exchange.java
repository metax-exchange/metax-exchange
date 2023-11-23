package org.metax.exchange.core;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.metax.exchange.core.webapi.ApiService;
import org.metax.exchange.core.websocket.WebSocketService;

public interface Exchange {

    ApiService getApiService();

    default WebSocketService getWebSocketService(Object... args) {
        return null;
    }

    RateLimiterRegistry getRateLimiterRegistry();
}
