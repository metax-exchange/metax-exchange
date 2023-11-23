package org.metax.exchange.okx.dto.marketdata;

import lombok.Data;
import lombok.ToString;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.dto.marketdata.MarkPrice;

@Data
@ToString(callSuper = true)
public class OkxMarkPrice extends MarkPrice {

    private String instType;

    private String instId;

    private String markPx;

    private String ts;


    public void setMarkPx(String markPx) {
        this.markPx = markPx;
        setMarkPrice(markPx);
    }

    public void setInstId(String instId) {
        this.instId = instId;
        setCurrencyPair(new CurrencyPair(instId));
    }
}
