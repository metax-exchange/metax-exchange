package org.metax.exchange.binance;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.metax.exchange.binance.dto.marketdata.BinanceKline;

import java.util.Objects;

public class BinanceResponseAdapter {

    public static BinanceKline fromKlineStream(String e) {
        JSONObject jsonObject = JSON.parseObject(e);
        JSONObject data = jsonObject.getJSONObject("data");
        if (Objects.nonNull(data)) {
            JSONObject k = data.getJSONObject("k");
            return new BinanceKline(k);
        }
        return null;
    }
}
