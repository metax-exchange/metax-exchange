package org.metax.exchange.binance.webapi.service;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.metax.exchange.core.ExchangeSpecification;
import org.metax.exchange.core.webapi.TradeService;

public class BinanceTradeService extends BinanceMarketDataService implements TradeService {
    public BinanceTradeService(ExchangeSpecification exchangeSpecification, RateLimiterRegistry rateLimiterRegistry) {
        super(exchangeSpecification, rateLimiterRegistry);
    }
}
