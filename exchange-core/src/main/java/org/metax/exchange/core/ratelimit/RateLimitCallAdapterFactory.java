package org.metax.exchange.core.ratelimit;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import io.github.resilience4j.ratelimiter.RequestNotPermitted;
import lombok.NonNull;
import org.metax.exchange.core.exception.ExchangeClientException;
import org.metax.exchange.core.exception.RateLimitExceededException;
import org.metax.exchange.core.util.StreamUtils;
import org.springframework.util.Assert;
import org.springframework.util.StringUtils;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

import javax.annotation.Nullable;
import java.lang.annotation.Annotation;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.function.Supplier;

public class RateLimitCallAdapterFactory extends CallAdapter.Factory {

    private final boolean enabledRateLimiter;
    private final RateLimiterRegistry rateLimiterRegistry;

    public RateLimitCallAdapterFactory(boolean enabledRateLimiter, @NonNull RateLimiterRegistry rateLimiterRegistry) {
        this.enabledRateLimiter = enabledRateLimiter;
        this.rateLimiterRegistry = rateLimiterRegistry;
    }

    @Nullable
    @Override
    public CallAdapter<?, ?> get(Type returnType, Annotation[] annotations, Retrofit retrofit) {
        if (!(returnType instanceof ParameterizedType)) {
            throw new IllegalArgumentException("Invalid use of RateLimitCallAdapterFactory. Ensure that your Retrofit interface methods return a parameterized type, for example, CustomCall<ResponseBody>.");
        }
        // 添加对 annotations 的非空检查
        if (enabledRateLimiter && Objects.nonNull(annotations)) {
            Assert.notNull(rateLimiterRegistry, "If rate limiting is enabled, the rateLimiterRegistry cannot be empty.");
            RateLimit rateLimit = (RateLimit) StreamUtils.findSingletonBy(Arrays.stream(annotations).toList(), Annotation::annotationType, RateLimit.class);
            if (Objects.nonNull(rateLimit) && StringUtils.hasText(rateLimit.name())) {
                Type responseType = getParameterUpperBound(0, (ParameterizedType) returnType);
                return new RateLimitCallAdapter<>(responseType, rateLimiterRegistry, rateLimit);
            }
        }
        return null;
    }


    private record RateLimitCallAdapter<R>(Type responseType, RateLimiterRegistry rateLimiterRegistry,
                                           RateLimit rateLimit) implements CallAdapter<R, Call<R>> {

        @Override
        public Call<R> adapt(Call<R> call) {
            try {
                if (rateLimit.permits() <= 0) {
                    return call;
                } else {
                    RateLimiter rateLimiter = StreamUtils.findSingletonBy(rateLimiterRegistry.getAllRateLimiters(), RateLimiter::getName, rateLimit.name());
                    Supplier<Call<R>> supplier = RateLimiter.decorateSupplier(rateLimiter, rateLimit.permits(), () -> call);
                    return supplier.get();
                }
            } catch (Exception e) {
                if (e instanceof RequestNotPermitted) {
                    throw new RateLimitExceededException(e.getMessage());
                }
                throw new ExchangeClientException(e.getMessage());
            }
        }

    }
}