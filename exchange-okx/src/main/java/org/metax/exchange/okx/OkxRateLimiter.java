package org.metax.exchange.okx;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.metax.exchange.core.util.StreamUtils;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Objects;

public class OkxRateLimiter {

    public static RateLimiterRegistry createRateLimiterRegistry() {
        RateLimiterRegistry rateLimiterRegistry = RateLimiterRegistry.ofDefaults();
        rateLimiterRegistry.rateLimiter("time", RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofSeconds(3)) // 刷新令牌周期
                .limitForPeriod(10)// 令牌桶容量
                .timeoutDuration(Duration.ofSeconds(3))// 等待令牌的超时时间
                .build());
        rateLimiterRegistry.rateLimiter("indexCandles", RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofSeconds(3)) // 刷新令牌周期
                .limitForPeriod(20)// 令牌桶容量
                .timeoutDuration(Duration.ofSeconds(2))// 等待令牌的超时时间
                .build());
        rateLimiterRegistry.rateLimiter("indexTickers", RateLimiterConfig.custom().limitRefreshPeriod(Duration.ofSeconds(3)) // 刷新令牌周期
                .limitForPeriod(20)// 令牌桶容量
                .timeoutDuration(Duration.ofSeconds(2))// 等待令牌的超时时间
                .build());
        return rateLimiterRegistry;
    }

}
