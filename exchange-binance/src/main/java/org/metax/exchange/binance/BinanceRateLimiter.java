package org.metax.exchange.binance;

import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;

import java.time.Duration;

public class BinanceRateLimiter {

    public static final String API_RATE_LIMITER = "apiRateLimiter";
    public static final String SAPI_RATE_LIMITER = "sapiRateLimiter";
    public static final String FAPI_RATE_LIMITER = "fapiRateLimiter";

    public static RateLimiterRegistry createRateLimiterRegistry() {
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.ofDefaults();
        RateLimiterConfig apiRateLimiterConfig = RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofMinutes(1)) // 刷新令牌周期
                .limitForPeriod(6000)// 令牌桶容量
                .timeoutDuration(Duration.ofMinutes(1))// 等待令牌的超时时间 1分钟
                .build();
        RateLimiterConfig sapiRateLimiterConfig = RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofMinutes(1)) // 刷新令牌周期
                .limitForPeriod(12000)// 令牌桶容量
                .timeoutDuration(Duration.ofMinutes(1))// 等待令牌的超时时间 1分钟
                .build();
        RateLimiterConfig fapiRateLimiterConfig = RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofMinutes(1)) // 刷新令牌周期
                .limitForPeriod(1200)// 令牌桶容量
                .timeoutDuration(Duration.ofMinutes(1))// 等待令牌的超时时间 1分钟
                .build();
        rateLimiterRegistry.rateLimiter(BinanceRateLimiter.API_RATE_LIMITER, apiRateLimiterConfig);
        rateLimiterRegistry.rateLimiter(BinanceRateLimiter.SAPI_RATE_LIMITER, sapiRateLimiterConfig);
        rateLimiterRegistry.rateLimiter(BinanceRateLimiter.FAPI_RATE_LIMITER, fapiRateLimiterConfig);
        return rateLimiterRegistry;
    }

}
