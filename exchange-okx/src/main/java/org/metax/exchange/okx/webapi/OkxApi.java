package org.metax.exchange.okx.webapi;

import com.alibaba.fastjson.JSONArray;
import org.metax.exchange.core.ratelimit.RateLimit;
import org.metax.exchange.okx.dto.OkxServerTime;
import org.metax.exchange.okx.dto.marketdata.OkxInstrument;
import org.metax.exchange.okx.dto.marketdata.OkxMarkPrice;
import org.metax.exchange.okx.dto.marketdata.OkxTickerPrice;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

import java.util.List;

public interface OkxApi {

    @GET("api/v5/public/time")
    @RateLimit(name = "time")
    Call<OkxApiResponse<OkxServerTime>> time();

    @GET("api/v5/public/instruments")
    Call<OkxApiResponse<OkxInstrument>> instruments(@Query("instType") String instType);

    @GET("api/v5/market/index-candles")
    @RateLimit(name = "indexCandles")
    Call<OkxApiResponse<JSONArray>> indexCandles(@Query("instId") String instId, @Query("after") String after, @Query("before") String before, @Query("bar") String bar, @Query("limit") String limit);


    @GET("api/v5/market/index-tickers")
    @RateLimit(name = "indexTickers")
    Call<OkxApiResponse<List<OkxTickerPrice>>> indexTickersByQuoteCcy(@Query("quoteCcy") String quoteCcy);


    @GET("api/v5/market/index-tickers")
    @RateLimit(name = "indexTickers")
    Call<OkxApiResponse<OkxTickerPrice>> indexTickersByInstId(@Query("instId") String instId);

    @GET("api/v5/public/mark-price")
    Call<OkxApiResponse<OkxMarkPrice>> markPrice(@Query("instType") String instType, @Query("instId") String instId);
}
