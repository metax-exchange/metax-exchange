package org.metax.exchange.binance.webapi;

import com.alibaba.fastjson.JSONArray;
import org.metax.exchange.binance.BinanceRateLimiter;
import org.metax.exchange.binance.dto.marketdata.*;
import org.metax.exchange.core.dto.marketdata.ServerTime;
import org.metax.exchange.core.ratelimit.RateLimit;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface BinanceFuturesApi {

    @GET("fapi/v1/ping")
    @RateLimit(name = BinanceRateLimiter.FAPI_RATE_LIMITER)
    Call<Object> ping();

    @GET("fapi/v1/time")
    @RateLimit(name = BinanceRateLimiter.FAPI_RATE_LIMITER)
    Call<ServerTime> time();

    @GET("fapi/v1/exchangeInfo")
    @RateLimit(name = BinanceRateLimiter.FAPI_RATE_LIMITER)
    Call<BinanceFuturesExchangeInfo> exchangeInfo();

    @GET("fapi/v2/ticker/price")
    @RateLimit(name = BinanceRateLimiter.FAPI_RATE_LIMITER, permits = 0)
    Call<BinanceTickerPrice> ticker(@Query("symbol") String symbol);

    @GET("fapi/v1/premiumIndex")
    @RateLimit(name = BinanceRateLimiter.FAPI_RATE_LIMITER)
    Call<BinanceMarkPrice> markPrice(@Query("symbol") String symbol);

    /**
     * <p>适用于  1 <= limit < 100 </p>
     */
    @GET("fapi/v1/klines")
    @RateLimit(name = BinanceRateLimiter.FAPI_RATE_LIMITER)
    Call<JSONArray> klines(@Query("symbol") String symbol, @Query("interval") String interval, @Query("startTime") Long startTime, @Query("endTime") Long endTime, @Query("limit") Integer limit);

    /**
     * <p>适用于  100 <= limit < 500 </p>
     */
    @GET("fapi/v1/klines")
    @RateLimit(name = BinanceRateLimiter.FAPI_RATE_LIMITER, permits = 2)
    Call<JSONArray> klines1(@Query("symbol") String symbol, @Query("interval") String interval, @Query("startTime") Long startTime, @Query("endTime") Long endTime, @Query("limit") Integer limit);

    /**
     * <p>适用于  500 <= limit <= 1000 </p>
     */
    @GET("fapi/v1/klines")
    @RateLimit(name = BinanceRateLimiter.FAPI_RATE_LIMITER, permits = 5)
    Call<JSONArray> klines2(@Query("symbol") String symbol, @Query("interval") String interval, @Query("startTime") Long startTime, @Query("endTime") Long endTime, @Query("limit") Integer limit);

    /**
     * <p>适用于  1000 < limit </p>
     */
    @GET("fapi/v1/klines")
    @RateLimit(name = BinanceRateLimiter.FAPI_RATE_LIMITER, permits = 10)
    Call<JSONArray> klines3(@Query("symbol") String symbol, @Query("interval") String interval, @Query("startTime") Long startTime, @Query("endTime") Long endTime, @Query("limit") Integer limit);


    @GET("fapi/v1/ticker/24hr")
    @RateLimit(name = BinanceRateLimiter.FAPI_RATE_LIMITER)
    Call<BinanceTicker24h> ticker24h(@Query("symbol") String symbol);
}
