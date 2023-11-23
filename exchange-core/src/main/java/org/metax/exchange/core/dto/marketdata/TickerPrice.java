package org.metax.exchange.core.dto.marketdata;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.metax.exchange.core.currency.CurrencyPair;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TickerPrice {

    private CurrencyPair currencyPair;

    private BigDecimal price;

}
