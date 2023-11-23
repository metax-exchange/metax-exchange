package org.metax.exchange.core.dto.marketdata;

import lombok.Data;
import org.metax.exchange.core.Interval;
import org.metax.exchange.core.currency.CurrencyPair;

import java.math.BigDecimal;

@Data
public class Kline {

    private CurrencyPair currencyPair;

    private Interval interval;

    private BigDecimal open;

    private BigDecimal high;

    private BigDecimal low;

    private BigDecimal close;

    private BigDecimal volume;
}
