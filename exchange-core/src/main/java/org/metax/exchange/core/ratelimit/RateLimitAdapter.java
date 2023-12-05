package org.metax.exchange.core.ratelimit;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterConfig;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import org.metax.exchange.core.util.StreamUtils;
import org.springframework.util.Assert;

import java.time.Duration;
import java.util.Objects;

public class RateLimitAdapter {

    /**
     * <p>将限流器注入工厂并返回，如果已经存在该名称的限流器则直接返回已经存在的</p>
     *
     * @param rateLimiterRegistry 限流器工厂
     * @param name                限流器名称
     * @param limitRefreshPeriod  刷新周期
     * @param limitForPeriod      容量
     * @param timeoutDuration     等待时间
     * @return
     */
    public static RateLimiter rateLimiter(RateLimiterRegistry rateLimiterRegistry, String name, final Duration limitRefreshPeriod, final int limitForPeriod, final Duration timeoutDuration) {
        Assert.notNull(rateLimiterRegistry, "rateLimiterRegistry is not null");
        RateLimiter rateLimiter = StreamUtils.findSingleByKey(rateLimiterRegistry.getAllRateLimiters(), RateLimiter::getName, name);
        if (Objects.isNull(rateLimiter)) {
            return rateLimiterRegistry.rateLimiter(name, RateLimiterConfig.custom().limitRefreshPeriod(limitRefreshPeriod) // 刷新令牌周期
                    .limitForPeriod(limitForPeriod)// 令牌桶容量
                    .timeoutDuration(timeoutDuration)// 等待令牌的超时时间
                    .build());
        }
        return rateLimiter;
    }


}
