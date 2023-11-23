package org.metax.exchange.core.dto.marketdata;

import lombok.Data;
import org.metax.exchange.core.currency.CurrencyPair;

@Data
public class MarkPrice {

    private CurrencyPair currencyPair;

    private String markPrice;


}
