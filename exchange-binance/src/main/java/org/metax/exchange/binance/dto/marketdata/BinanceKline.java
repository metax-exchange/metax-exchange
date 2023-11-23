package org.metax.exchange.binance.dto.marketdata;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.metax.exchange.binance.BinanceInterval;
import org.metax.exchange.binance.BinanceParamAdapter;
import org.metax.exchange.core.Interval;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.dto.marketdata.Kline;
import org.springframework.util.Assert;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class BinanceKline extends Kline {

    public BinanceKline(JSONObject klineData) {
        Assert.notNull(klineData, "klineData is not null");
        this.setOpenTime(klineData.getLong("t"));
        this.setCloseTime(klineData.getLong("T"));
        this.setHigh(klineData.getBigDecimal("h"));
        this.setLow(klineData.getBigDecimal("l"));
        this.setVolume(klineData.getBigDecimal("v"));
        this.setOpen(klineData.getBigDecimal("o"));
        this.setClose(klineData.getBigDecimal("c"));
        this.setSymbol(klineData.getString("s"));
        this.setInterval(BinanceInterval.fromCode(klineData.getString("i")));
    }

    public BinanceKline(CurrencyPair currencyPair, Interval interval, JSONArray jsonArray) {
        this.setCurrencyPair(currencyPair);
        this.setSymbol(BinanceParamAdapter.toSymbol(currencyPair));
        this.setInterval(interval);
        this.setOpenTime(jsonArray.getLong(0));
        this.setOpen(jsonArray.getBigDecimal(1));
        this.setHigh(jsonArray.getBigDecimal(2));
        this.setLow(jsonArray.getBigDecimal(3));
        this.setClose(jsonArray.getBigDecimal(4));
        this.setVolume(jsonArray.getBigDecimal(5));
        this.setCloseTime(jsonArray.getLong(6));
    }

    private String symbol;

    private long openTime;

    private long closeTime;
}
