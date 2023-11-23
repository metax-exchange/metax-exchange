package org.metax.exchange.okx;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.NonNull;
import org.metax.exchange.core.BaseExchange;
import org.metax.exchange.core.ExchangeSpecification;
import org.metax.exchange.core.enums.ExchangeAuthority;
import org.metax.exchange.okx.webapi.service.OkxMarketDataService;
import org.metax.exchange.okx.webapi.service.OkxTradeService;
import org.springframework.util.CollectionUtils;

import java.util.Objects;

public class OkxExchange extends BaseExchange {

    private final static RateLimiterRegistry RATE_LIMITER_REGISTRY = OkxRateLimiter.createRateLimiterRegistry();

    public OkxExchange(@NonNull ExchangeSpecification exchangeSpecification) {
        super(exchangeSpecification, RATE_LIMITER_REGISTRY);
    }

    public OkxExchange(@NonNull ExchangeSpecification exchangeSpecification, @NonNull RateLimiterRegistry rateLimiterRegistry) {
        super(exchangeSpecification, rateLimiterRegistry);
    }

    @Override
    protected void initializeService(ExchangeSpecification exchangeSpecification) {
        this.setApiService(Objects.isNull(exchangeSpecification.getApiSecret())
                || CollectionUtils.isEmpty(exchangeSpecification.getParameterMap()) ||
                !exchangeSpecification.getParameterMap().containsKey("passphrase") ? new OkxMarketDataService(exchangeSpecification, getRateLimiterRegistry()) : new OkxTradeService(exchangeSpecification, getRateLimiterRegistry()));
    }
}
