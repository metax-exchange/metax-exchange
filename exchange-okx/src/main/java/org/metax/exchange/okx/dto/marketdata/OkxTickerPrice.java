package org.metax.exchange.okx.dto.marketdata;

import lombok.Data;
import lombok.ToString;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.dto.marketdata.TickerPrice;

import java.math.BigDecimal;

@Data
@ToString(callSuper = true)
public class OkxTickerPrice extends TickerPrice {

    private String instId;

    private String idxPx;

    private String high24h;

    private String sodUtc0;

    private String open24h;

    private String low24h;

    private String sodUtc8;

    private String ts;


    public void setInstId(String instId) {
        this.instId = instId;
        setCurrencyPair(new CurrencyPair(instId));
    }

    public void setIdxPx(String idxPx) {
        this.idxPx = idxPx;
        setPrice(new BigDecimal(idxPx));
    }
}
