package org.metax.exchange.okx.dto.marketdata;

import com.alibaba.fastjson.JSONArray;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.metax.exchange.core.Interval;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.dto.marketdata.Kline;
import org.metax.exchange.okx.OkxParamAdapter;

@Data
@ToString(callSuper = true)
@NoArgsConstructor
public class OkxKline extends Kline {

    private long openTime;

    private String instId;

    public OkxKline(CurrencyPair currencyPair, Interval interval, JSONArray jsonArray) {
        this.setCurrencyPair(currencyPair);
        this.setInstId(OkxParamAdapter.toSymbol(currencyPair));
        this.setInterval(interval);
        this.setOpenTime(jsonArray.getLong(0));
        this.setOpen(jsonArray.getBigDecimal(1));
        this.setHigh(jsonArray.getBigDecimal(2));
        this.setLow(jsonArray.getBigDecimal(3));
        this.setClose(jsonArray.getBigDecimal(4));
        this.setVolume(jsonArray.getBigDecimal(5));
    }
}
