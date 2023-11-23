package org.metax.exchange.core;

import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.NonNull;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.reflect.ConstructorUtils;

@Slf4j
public enum ExchangeFactory {

    INSTANCE;

    @SneakyThrows
    public <T extends Exchange> T createExchange(@NonNull Class<T> exchangeClass) {
        return createExchange(exchangeClass, ExchangeSpecification.builder().build());
    }

    @SneakyThrows
    public <T extends Exchange> T createExchange(@NonNull Class<T> exchangeClass, @NonNull ExchangeSpecification exchangeSpecification) {
        return ConstructorUtils.invokeConstructor(exchangeClass, exchangeSpecification);
    }

    @SneakyThrows
    public <T extends Exchange> T createExchange(@NonNull Class<T> exchangeClass, @NonNull ExchangeSpecification exchangeSpecification, @NonNull RateLimiterRegistry rateLimiterRegistry) {
        return ConstructorUtils.invokeConstructor(exchangeClass, exchangeSpecification, rateLimiterRegistry);

    }

}
