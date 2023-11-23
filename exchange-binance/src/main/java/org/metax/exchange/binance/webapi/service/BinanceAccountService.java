package org.metax.exchange.binance.webapi.service;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.metax.exchange.core.ExchangeSpecification;
import org.metax.exchange.core.webapi.AccountService;

public class BinanceAccountService extends BinanceTradeService implements AccountService {
    public BinanceAccountService(ExchangeSpecification exchangeSpecification, RateLimiterRegistry rateLimiterRegistry) {
        super(exchangeSpecification, rateLimiterRegistry);
    }
}
