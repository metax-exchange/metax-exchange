package org.metax.exchange.binance.dto.marketdata;

import lombok.Data;
import lombok.ToString;
import org.metax.exchange.core.dto.marketdata.TickerPrice;

@Data
@ToString(callSuper = true)
public class BinanceTickerPrice extends TickerPrice {

    private String symbol;

}
