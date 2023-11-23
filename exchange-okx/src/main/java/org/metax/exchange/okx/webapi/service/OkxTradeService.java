package org.metax.exchange.okx.webapi.service;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.NonNull;
import org.metax.exchange.core.ExchangeSpecification;
import org.metax.exchange.core.webapi.TradeService;

public class OkxTradeService extends OkxMarketDataService implements TradeService {
    public OkxTradeService(@NonNull ExchangeSpecification exchangeSpecification, @NonNull RateLimiterRegistry rateLimiterRegistry) {
        super(exchangeSpecification, rateLimiterRegistry);
    }
}
