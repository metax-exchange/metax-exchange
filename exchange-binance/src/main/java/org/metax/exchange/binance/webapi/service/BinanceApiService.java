package org.metax.exchange.binance.webapi.service;

import com.google.common.collect.Lists;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.metax.exchange.binance.BinanceParamAdapter;
import org.metax.exchange.binance.Constant;
import org.metax.exchange.binance.dto.marketdata.BinanceExchangeInfo;
import org.metax.exchange.binance.dto.marketdata.BinanceFuturesExchangeInfo;
import org.metax.exchange.binance.enums.BinanceInstType;
import org.metax.exchange.binance.exception.ExceptionStatus;
import org.metax.exchange.binance.webapi.BinanceApi;
import org.metax.exchange.binance.webapi.BinanceFuturesApi;
import org.metax.exchange.core.ExchangeSpecification;
import org.metax.exchange.core.converter.FastJsonConverterFactory;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.dto.marketdata.ServerTime;
import org.metax.exchange.core.exception.*;
import org.metax.exchange.core.ratelimit.RateLimitCallAdapterFactory;
import org.metax.exchange.core.util.StreamUtils;
import org.metax.exchange.core.webapi.ApiService;
import org.springframework.util.CollectionUtils;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Slf4j
public abstract class BinanceApiService implements ApiService {

    protected final String apiKey;

    protected final String secretKey;

    protected final BinanceApi binanceApi;

    protected final BinanceFuturesApi binanceFuturesApi;


    public BinanceApiService(@NonNull ExchangeSpecification exchangeSpecification, @NonNull RateLimiterRegistry rateLimiterRegistry) {
        // set apiKey
        ExchangeSpecification.ApiSecret apiSecret = exchangeSpecification.getApiSecret();
        this.apiKey = (Objects.nonNull(apiSecret) ? apiSecret.getApiKey() : null);
        this.secretKey = (Objects.nonNull(apiSecret) ? apiSecret.getSecretKey() : null);
        // set rateLimit
        ExchangeSpecification.ResilienceSpecification resilienceSpecification = exchangeSpecification.getResilienceSpecification();
        RateLimitCallAdapterFactory rateLimitCallAdapterFactory = new RateLimitCallAdapterFactory(resilienceSpecification.isEnabledRateLimiter(), rateLimiterRegistry);
        FastJsonConverterFactory fastJsonConverterFactory = FastJsonConverterFactory.create();
        Map parameterMap = exchangeSpecification.getParameterMap();
        this.binanceApi = new Retrofit.Builder().baseUrl(CollectionUtils.isEmpty(parameterMap) || Objects.isNull(parameterMap.get("SPOT_REST_API_BASE_URL")) ? Constant.SPOT_REST_API_BASE_URL : (String) parameterMap.get("SPOT_REST_API_BASE_URL")).addCallAdapterFactory(rateLimitCallAdapterFactory).addConverterFactory(fastJsonConverterFactory).build().create(BinanceApi.class);
        this.binanceFuturesApi = new Retrofit.Builder().baseUrl(CollectionUtils.isEmpty(parameterMap) || Objects.isNull(parameterMap.get("FUTURES_REST_API_BASE_URL")) ? Constant.FUTURES_REST_API_BASE_URL : (String) parameterMap.get("FUTURES_REST_API_BASE_URL")).addCallAdapterFactory(rateLimitCallAdapterFactory).addConverterFactory(fastJsonConverterFactory).build().create(BinanceFuturesApi.class);
    }

    @Override
    public Object ping(Object... args) throws IOException {
        BinanceInstType binanceInstType = (args.length == 0 ? BinanceInstType.SPOT : (BinanceInstType) args[0]);
        return execute(Objects.equals(BinanceInstType.FUTURES, binanceInstType) ? binanceFuturesApi.ping() : binanceApi.ping());
    }

    @Override
    public ServerTime time(Object... args) throws IOException {
        BinanceInstType binanceInstType = (args.length == 0 ? BinanceInstType.SPOT : (BinanceInstType) args[0]);
        return execute(Objects.equals(BinanceInstType.FUTURES, binanceInstType) ? binanceFuturesApi.time() : binanceApi.time());
    }

    public BinanceExchangeInfo.Symbol getSymbolByCurrencyPair(CurrencyPair currencyPair) throws IOException {
        return getSymbolsByCurrencyPairs(List.of(currencyPair)).stream().collect(StreamUtils.singletonCollector());
    }

    public List<BinanceExchangeInfo.Symbol> getSymbolsByCurrencyPairs(List<CurrencyPair> currencyPairs) throws IOException {
        List<String> symbols = currencyPairs.stream().map(currencyPair -> BinanceParamAdapter.toSymbol(currencyPair)).toList();
        BinanceExchangeInfo exchangeInfo = execute(binanceApi.exchangeInfo());
        return StreamUtils.filterByKeys(exchangeInfo.getSymbols(), BinanceExchangeInfo.Symbol::getSymbol, symbols);
    }

    public Object getExchangeInfo(BinanceInstType binanceInstType) throws IOException {
        if (Objects.equals(binanceInstType, BinanceInstType.FUTURES)) {
            return execute(binanceFuturesApi.exchangeInfo());
        } else {
            return execute(binanceApi.exchangeInfo());
        }
    }

    @Override
    public List<CurrencyPair> getCurrencyPairList(Object... args) throws IOException {
        BinanceInstType binanceInstType = (args.length == 0 ? BinanceInstType.SPOT : (BinanceInstType) args[0]);
        Object exchangeInfo = getExchangeInfo(binanceInstType);
        if (exchangeInfo instanceof BinanceExchangeInfo) {
            return ((BinanceExchangeInfo) exchangeInfo).getSymbols().stream().map(symbol -> new CurrencyPair(symbol.getBaseAsset(), symbol.getQuoteAsset())).toList();
        } else if (exchangeInfo instanceof BinanceFuturesExchangeInfo) {
            return ((BinanceFuturesExchangeInfo) exchangeInfo).getSymbols().stream().map(symbol -> new CurrencyPair(symbol.getBaseAsset(), symbol.getQuoteAsset())).toList();
        }
        return Lists.newArrayListWithCapacity(0);
    }

    @Override
    public <T> T execute(Call<?> call) throws IOException {
        try {
            Response<T> response = (Response<T>) call.execute();
            if (response.isSuccessful()) {
                return response.body();
            } else {
                ExceptionStatus exceptionStatus = ExceptionStatus.fromCode(response.code());
                if (exceptionStatus == ExceptionStatus.DEFAULT_ERROR) {
                    throw new ExchangeClientException(response.errorBody().string());
                } else if (exceptionStatus == ExceptionStatus.IP_BLOCKED || exceptionStatus == ExceptionStatus.RATE_LIMIT_WARNING) {
                    throw new RateLimitExceededException(response.errorBody().string());
                } else if (exceptionStatus == ExceptionStatus.SERVER_ERROR) {
                    throw new ExchangeServerExchange(response.errorBody().string());
                }
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
