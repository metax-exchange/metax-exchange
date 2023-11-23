package org.metax.exchange.binance.dto.marketdata;

import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.metax.exchange.core.currency.CurrencyPair;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
public class BinanceTicker24h {

    private CurrencyPair currencyPair;

    private String symbol;

    private BigDecimal priceChange;

    private BigDecimal priceChangePercent;

    private BigDecimal lastPrice;

    private BigDecimal openPrice;

    private BigDecimal highPrice;

    private BigDecimal lowPrice;

    private BigDecimal volume;

    private long openTime;

    private long closeTime;

}
