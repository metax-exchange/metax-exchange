package org.metax.exchange.okx.webapi.service;

import io.github.resilience4j.ratelimiter.RateLimiter;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.NonNull;
import org.metax.exchange.core.ExchangeSpecification;
import org.metax.exchange.core.converter.FastJsonConverterFactory;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.currency.FuturesCurrencyPair;
import org.metax.exchange.core.exception.ExchangeClientException;
import org.metax.exchange.core.exception.ExchangeConnectorException;
import org.metax.exchange.core.exception.ExchangeException;
import org.metax.exchange.core.ratelimit.RateLimitAdapter;
import org.metax.exchange.core.ratelimit.RateLimitCallAdapterFactory;
import org.metax.exchange.core.webapi.ApiService;
import org.metax.exchange.okx.Constant;
import org.metax.exchange.okx.dto.OkxServerTime;
import org.metax.exchange.okx.dto.marketdata.OkxInstrument;
import org.metax.exchange.okx.enums.OkxInstType;
import org.metax.exchange.okx.webapi.OkxApi;
import org.metax.exchange.okx.webapi.OkxApiResponse;
import org.springframework.util.Assert;
import org.springframework.util.CollectionUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.time.Duration;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.function.Supplier;
import java.util.stream.Collectors;

public abstract class OkxApiService implements ApiService {

    protected final String apiKey;

    protected final String secretKey;

    protected final String passphrase;

    protected final OkxApi okxApi;

    protected RateLimiterRegistry rateLimiterRegistry;


    public OkxApiService(@NonNull ExchangeSpecification exchangeSpecification, @NonNull RateLimiterRegistry rateLimiterRegistry) {
        ExchangeSpecification.ApiSecret apiSecret = exchangeSpecification.getApiSecret();
        this.apiKey = Objects.nonNull(apiSecret) ? apiSecret.getApiKey() : null;
        this.secretKey = Objects.nonNull(apiSecret) ? apiSecret.getSecretKey() : null;
        this.passphrase = CollectionUtils.isEmpty(exchangeSpecification.getParameterMap()) ? null : (String) exchangeSpecification.getParameterMap().get("passphrase");
        this.rateLimiterRegistry = rateLimiterRegistry;
        ExchangeSpecification.ResilienceSpecification resilienceSpecification = exchangeSpecification.getResilienceSpecification();
        RateLimitCallAdapterFactory rateLimitCallAdapterFactory = new RateLimitCallAdapterFactory(resilienceSpecification.isEnabledRateLimiter(), rateLimiterRegistry);
        FastJsonConverterFactory fastJsonConverterFactory = FastJsonConverterFactory.create();
        this.okxApi = new Retrofit.Builder().baseUrl(Constant.REST_API_BASE_URL).addCallAdapterFactory(rateLimitCallAdapterFactory).addConverterFactory(fastJsonConverterFactory).build().create(OkxApi.class);
    }

    @Override
    public Object ping(Object... args) throws IOException {
        return execute(okxApi.time());
    }

    @Override
    public OkxServerTime time(Object... args) throws IOException {
        return execute(okxApi.time());
    }

    @Override
    public List<CurrencyPair> getCurrencyPairList(Object... args) throws IOException {
        Assert.isTrue(Objects.nonNull(args) && args.length == 1 && args[0] instanceof OkxInstType, "args len is 1 and type is OkxInstType");
        OkxInstType okxInstType = (OkxInstType) args[0];
        List<OkxInstrument> okxInstrumentList = execute(okxInstType.name(), Duration.ofSeconds(2), 20, Duration.ofSeconds(2), okxApi.instruments(okxInstType.name()));
        return okxInstrumentList.stream().map(okxInstrument -> {
            if (Objects.equals(okxInstrument.getInstType(), OkxInstType.SPOT.name())) {
                return new CurrencyPair(okxInstrument.getBaseCcy(), okxInstrument.getQuoteCcy());
            } else {
                return new FuturesCurrencyPair(okxInstrument.getInstId());
            }
        }).toList();
    }

    /**
     * <p>执行需要动态适配限流器的方法</p>
     *
     * @param name               限流器名称
     * @param limitRefreshPeriod 刷新周期
     * @param limitForPeriod     容量
     * @param timeoutDuration    超时时间
     * @param call               apiCall
     * @param <T>
     * @return
     */
    public <T> T execute(String name, final Duration limitRefreshPeriod, final int limitForPeriod, final Duration timeoutDuration, Call<?> call) {
        Supplier<T> supplier = RateLimiter.decorateSupplier(RateLimitAdapter.rateLimiter(rateLimiterRegistry, name, limitRefreshPeriod, limitForPeriod, timeoutDuration), () -> {
            try {
                return execute(call);
            } catch (IOException e) {
                throw new ExchangeClientException(e.getMessage());
            }
        });
        return supplier.get();
    }

    /**
     * <p>执行采用注解试限流器或者无需限流器的方法</p>
     *
     * @param call
     * @param <T>
     * @return
     * @throws IOException
     */
    @Override
    public <T> T execute(Call<?> call) throws IOException {
        try {
            Response<OkxApiResponse<T>> response = (Response<OkxApiResponse<T>>) call.execute();
            if (response.isSuccessful()) {
                if (response.body().getCode() == 0) {
                    if (!CollectionUtils.isEmpty(response.body().getData()) && response.body().getData().size() == 1) {
                        Object data = response.body().getData().get(0);
                        if (!(data instanceof Collection<?>)) {
                            return (T) data;
                        }
                    }
                    return (T) response.body().getData().stream().collect(Collectors.toList());
                } else {
                    throw new ExchangeClientException(response.body(), response.body().getMsg());
                }

            } else {
                throw new ExchangeClientException(response.errorBody().string());
            }
        } catch (Exception e) {
            if (e instanceof ExchangeException) {
                throw e;
            }
            throw new ExchangeConnectorException(e.getMessage());
        }
    }

}
