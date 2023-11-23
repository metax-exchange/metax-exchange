package org.metax.exchange.okx.webapi.service;

import com.alibaba.fastjson.JSONArray;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.NonNull;
import org.metax.exchange.core.ExchangeSpecification;
import org.metax.exchange.core.Interval;
import org.metax.exchange.core.currency.Currency;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.currency.FuturesCurrencyPair;
import org.metax.exchange.core.dto.marketdata.Kline;
import org.metax.exchange.core.webapi.MarketDataService;
import org.metax.exchange.core.util.StreamUtils;
import org.metax.exchange.okx.OkxParamAdapter;
import org.metax.exchange.okx.dto.marketdata.OkxKline;
import org.metax.exchange.okx.dto.marketdata.OkxMarkPrice;
import org.metax.exchange.okx.dto.marketdata.OkxTickerPrice;
import org.springframework.util.Assert;

import java.io.IOException;
import java.time.Duration;
import java.util.List;
import java.util.function.Function;

public class OkxMarketDataService extends OkxApiService implements MarketDataService {

    public OkxMarketDataService(@NonNull ExchangeSpecification exchangeSpecification, @NonNull RateLimiterRegistry rateLimiterRegistry) {
        super(exchangeSpecification, rateLimiterRegistry);
    }

    public OkxKline lastIndexCandles(@NonNull CurrencyPair currencyPair, @NonNull Interval interval) throws IOException {
        return indexCandles(currencyPair, interval, null, null, "1").stream().collect(StreamUtils.singletonCollector());
    }

    public List<OkxKline> indexCandles(@NonNull CurrencyPair currencyPair, @NonNull Interval interval) throws IOException {
        return indexCandles(currencyPair, interval, null, null, null);
    }

    public List<OkxKline> indexCandles(@NonNull CurrencyPair currencyPair, @NonNull Interval interval, String after, String before, String limit, Object... ignoredArgs) throws IOException {
        return getJsonKlines(OkxParamAdapter.toSymbol(currencyPair), after, before, interval.getInterval(), limit).stream().map(object -> new OkxKline(currencyPair, interval, object)).toList();
    }

    @Override
    public List<Kline> klines(@NonNull CurrencyPair currencyPair, @NonNull Interval interval, Object... args) throws IOException {
        Assert.isTrue(args.length == 3, "args len is 3");
        String after = (String) args[0];
        String before = (String) args[1];
        String limit = (String) args[2];
        return getJsonKlines(OkxParamAdapter.toSymbol(currencyPair), after, before, interval.getInterval(), limit).stream().map((Function<Object, Kline>) object -> new OkxKline(currencyPair, interval, (JSONArray) object)).toList();
    }

    public List<OkxTickerPrice> getTickerPrice(@NonNull Currency currency) throws IOException {
        return execute(okxApi.indexTickersByQuoteCcy(currency.getCode()));
    }

    @Override
    public OkxTickerPrice getTickerPrice(@NonNull CurrencyPair currencyPair, Object... args) throws IOException {
        return execute(okxApi.indexTickersByInstId(OkxParamAdapter.toSymbol(currencyPair)));
    }

    @Override
    public OkxMarkPrice getMarkPrice(@NonNull FuturesCurrencyPair currencyPair, Object... args) throws IOException {
        return execute("markPrice%s".formatted(OkxParamAdapter.toSymbol(currencyPair)), Duration.ofSeconds(2), 10, Duration.ofSeconds(2), okxApi.markPrice(currencyPair.getInstType(), OkxParamAdapter.toSymbol(currencyPair)));
    }

    private List<JSONArray> getJsonKlines(String instId, String after, String before, String bar, String limit) throws IOException {
        return execute(okxApi.indexCandles(instId, after, before, bar, limit));
    }

}
