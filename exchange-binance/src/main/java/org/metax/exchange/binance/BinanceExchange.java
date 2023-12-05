package org.metax.exchange.binance;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.NonNull;
import org.metax.exchange.binance.webapi.service.BinanceAccountService;
import org.metax.exchange.binance.webapi.service.BinanceMarketDataService;
import org.metax.exchange.binance.websocket.BinanceWebSocketService;
import org.metax.exchange.core.BaseExchange;
import org.metax.exchange.core.ExchangeSpecification;

import java.util.Objects;

public class BinanceExchange extends BaseExchange {

    public final static RateLimiterRegistry RATE_LIMITER_REGISTRY = BinanceRateLimiter.createRateLimiterRegistry();

    public BinanceExchange(@NonNull ExchangeSpecification exchangeSpecification) {
        super(exchangeSpecification, RATE_LIMITER_REGISTRY);
    }

    public BinanceExchange(@NonNull ExchangeSpecification exchangeSpecification, @NonNull RateLimiterRegistry rateLimiterRegistry) {
        super(exchangeSpecification, rateLimiterRegistry);
    }

    @Override
    protected void initializeService(@NonNull ExchangeSpecification exchangeSpecification) {
        this.setApiService(Objects.isNull(exchangeSpecification.getApiSecret()) ? new BinanceMarketDataService(exchangeSpecification, getRateLimiterRegistry()) : new BinanceAccountService(exchangeSpecification, getRateLimiterRegistry()));
        this.setWebSocketService(new BinanceWebSocketService());
    }

}
