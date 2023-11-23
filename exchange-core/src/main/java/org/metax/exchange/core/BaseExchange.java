package org.metax.exchange.core;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.NonNull;
import lombok.Setter;
import org.metax.exchange.core.webapi.ApiService;
import org.metax.exchange.core.websocket.WebSocketService;

public abstract class BaseExchange implements Exchange {

    private ApiService apiService;

    private WebSocketService webSocketService;

    @Setter
    private RateLimiterRegistry rateLimiterRegistry;

    public BaseExchange(@NonNull ExchangeSpecification exchangeSpecification) {
        this.initializeService(exchangeSpecification);
    }
    public BaseExchange(@NonNull ExchangeSpecification exchangeSpecification, @NonNull RateLimiterRegistry rateLimiterRegistry) {
        this.setRateLimiterRegistry(rateLimiterRegistry);
        this.initializeService(exchangeSpecification);
    }

    protected abstract void initializeService(ExchangeSpecification exchangeSpecification);

    protected void setApiService(ApiService apiService) {
        this.apiService = apiService;
    }

    public ApiService getApiService() {
        return apiService;
    }


    protected void setWebSocketService(WebSocketService webSocketService) {
        this.webSocketService = webSocketService;
    }

    @Override
    public WebSocketService getWebSocketService(Object... args) {
        return this.webSocketService;
    }

    @Override
    public RateLimiterRegistry getRateLimiterRegistry() {
        return this.rateLimiterRegistry;
    }
}
