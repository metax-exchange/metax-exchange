package org.metax.exchange.binance.webapi.service;

import com.alibaba.fastjson.JSONArray;
import io.github.resilience4j.ratelimiter.RateLimiterRegistry;
import lombok.NonNull;
import lombok.extern.slf4j.Slf4j;
import org.metax.exchange.binance.BinanceParamAdapter;
import org.metax.exchange.binance.dto.marketdata.BinanceKline;
import org.metax.exchange.binance.dto.marketdata.BinanceMarkPrice;
import org.metax.exchange.binance.dto.marketdata.BinanceTicker24h;
import org.metax.exchange.binance.dto.marketdata.BinanceTickerPrice;
import org.metax.exchange.core.ExchangeSpecification;
import org.metax.exchange.core.Interval;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.currency.FuturesCurrencyPair;
import org.metax.exchange.core.dto.marketdata.Kline;
import org.metax.exchange.core.dto.marketdata.TickerPrice;
import org.metax.exchange.core.webapi.MarketDataService;
import org.metax.exchange.core.util.StreamUtils;
import org.springframework.util.Assert;
import retrofit2.Call;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.function.Function;

@Slf4j
public class BinanceMarketDataService extends BinanceApiService implements MarketDataService {

    public BinanceMarketDataService(@NonNull ExchangeSpecification exchangeSpecification, @NonNull RateLimiterRegistry rateLimiterRegistry) {
        super(exchangeSpecification, rateLimiterRegistry);
    }

    @Override
    public TickerPrice getTickerPrice(@NonNull CurrencyPair currencyPair, Object... args) throws IOException {
        BinanceTickerPrice binanceTickerPrice = execute(currencyPair instanceof FuturesCurrencyPair ? binanceFuturesApi.ticker(BinanceParamAdapter.toSymbol(currencyPair)) : binanceApi.ticker(BinanceParamAdapter.toSymbol(currencyPair)));
        binanceTickerPrice.setCurrencyPair(currencyPair);
        return binanceTickerPrice;
    }

    @Override
    public BinanceMarkPrice getMarkPrice(@NonNull FuturesCurrencyPair currencyPair, Object... args) throws IOException {
        BinanceMarkPrice binanceMarkPrice = execute(binanceFuturesApi.markPrice(BinanceParamAdapter.toSymbol(currencyPair)));
        binanceMarkPrice.setCurrencyPair(currencyPair);
        return binanceMarkPrice;
    }

    public BinanceTicker24h getTicker24h(@NonNull CurrencyPair currencyPair, Object... ignoredArgs) throws IOException {
        BinanceTicker24h binanceTicker24h = execute(currencyPair instanceof FuturesCurrencyPair ? binanceFuturesApi.ticker24h(BinanceParamAdapter.toSymbol(currencyPair)) : binanceApi.ticker24h(BinanceParamAdapter.toSymbol(currencyPair)));
        binanceTicker24h.setCurrencyPair(currencyPair);
        return binanceTicker24h;
    }

    public BinanceKline lastKline(@NonNull CurrencyPair currencyPair, @NonNull Interval interval) throws IOException {
        return klines(currencyPair, interval, null, null, 1).stream().collect(StreamUtils.singletonCollector());
    }

    public List<BinanceKline> klines(@NonNull CurrencyPair currencyPair, @NonNull Interval interval) throws IOException {
        return klines(currencyPair, interval, null, null, null);
    }

    public List<BinanceKline> klines(@NonNull CurrencyPair currencyPair, @NonNull Interval interval, Long startTime, Long endTime, Integer limit, Object... ignoredArgs) throws IOException {
        return getJsonKlines(currencyPair, interval, startTime, endTime, limit).stream().map(object -> new BinanceKline(currencyPair, interval, (JSONArray) object)).toList();
    }


    @Override
    public List<Kline> klines(@NonNull CurrencyPair currencyPair, @NonNull Interval interval, Object... args) throws IOException {
        Assert.isTrue(args.length == 3, "args size error");
        Long startTime = (Long) args[0];
        Long endTime = (Long) args[1];
        Integer limit = (Integer) args[2];
        return getJsonKlines(currencyPair, interval, startTime, endTime, limit).stream().map((Function<Object, Kline>) object -> new BinanceKline(currencyPair, interval, (JSONArray) object)).toList();
    }

    private JSONArray getJsonKlines(@NonNull CurrencyPair currencyPair, @NonNull Interval interval, Long startTime, Long endTime, Integer limit) throws IOException {
        // Check if the limit is specified and less than 1, return an empty JSONArray
        if (limit != null && limit < 1) {
            return new JSONArray();
        }
        // Choose the appropriate API based on the type of currency pair
        Call<JSONArray> klinesApiCall;
        if (currencyPair instanceof FuturesCurrencyPair) {
            klinesApiCall = getFuturesKlinesApiCall(BinanceParamAdapter.toSymbol(currencyPair), interval.getInterval(), startTime, endTime, limit);
        } else {
            klinesApiCall = getSpotKlinesApiCall(BinanceParamAdapter.toSymbol(currencyPair), interval.getInterval(), startTime, endTime, limit);
        }
        // Execute the selected API call and return the result
        return execute(klinesApiCall);
    }

    private Call<JSONArray> getSpotKlinesApiCall(String symbol, String interval, Long startTime, Long endTime, Integer limit) {
        return binanceApi.klines(symbol, interval, startTime, endTime, limit);
    }

    private Call<JSONArray> getFuturesKlinesApiCall(String symbol, String interval, Long startTime, Long endTime, Integer limit) {
        if (Objects.nonNull(limit)) {
            if (limit < 100) {
                return binanceFuturesApi.klines(symbol, interval, startTime, endTime, limit);
            } else if (limit < 500) {
                return binanceFuturesApi.klines1(symbol, interval, startTime, endTime, limit);
            } else if (limit <= 1000) {
                return binanceFuturesApi.klines2(symbol, interval, startTime, endTime, limit);
            } else {
                return binanceFuturesApi.klines3(symbol, interval, startTime, endTime, limit);
            }
        } else {
            return binanceFuturesApi.klines2(symbol, interval, startTime, endTime, 500);
        }
    }

}
