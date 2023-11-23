package org.metax.exchange.okx;

import org.metax.exchange.core.currency.CurrencyPair;

public class OkxParamAdapter {

    public static String toSymbol(CurrencyPair currencyPair) {
        return currencyPair.toString().replace("/", "-");
    }
}
