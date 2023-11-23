package org.metax.exchange.binance.webapi;

import com.alibaba.fastjson.JSONArray;
import org.metax.exchange.binance.BinanceRateLimiter;
import org.metax.exchange.binance.dto.marketdata.BinanceExchangeInfo;
import org.metax.exchange.binance.dto.marketdata.BinanceTicker24h;
import org.metax.exchange.binance.dto.marketdata.BinanceTickerPrice;
import org.metax.exchange.core.dto.marketdata.ServerTime;
import org.metax.exchange.core.ratelimit.RateLimit;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BinanceApi {

    @GET("api/v3/ping")
    @RateLimit(name = BinanceRateLimiter.API_RATE_LIMITER)
    Call<Object> ping();

    @GET("api/v3/time")
    @RateLimit(name = BinanceRateLimiter.API_RATE_LIMITER)
    Call<ServerTime> time();

    @GET("api/v3/exchangeInfo")
    @RateLimit(name = BinanceRateLimiter.API_RATE_LIMITER,permits = 20)
    Call<BinanceExchangeInfo> exchangeInfo();

    @GET("api/v3/ticker/price")
    @RateLimit(name = BinanceRateLimiter.API_RATE_LIMITER, permits = 2)
    Call<BinanceTickerPrice> ticker(@Query("symbol") String symbol);


    @GET("api/v3/klines")
    @RateLimit(name = BinanceRateLimiter.API_RATE_LIMITER, permits = 2)
    Call<JSONArray> klines(@Query("symbol") String symbol, @Query("interval") String interval, @Query("startTime") Long startTime, @Query("endTime") Long endTime, @Query("limit") Integer limit);


    @GET("api/v3/ticker/24hr")
    @RateLimit(name = BinanceRateLimiter.API_RATE_LIMITER, permits = 2)
    Call<BinanceTicker24h> ticker24h(@Query("symbol") String symbol);
}
