package org.metax.exchange.core.currency;

import lombok.Data;
import lombok.NonNull;
import org.metax.exchange.core.ParamAdapter;


@Data
public class CurrencyPair {

    private Currency base;

    private Currency quote;

    public CurrencyPair(){

    }


    public CurrencyPair(@NonNull Currency base, @NonNull Currency quote) {
        this.base = base;
        this.quote = quote;
    }

    public CurrencyPair(@NonNull final String instId) {
        String[] currencyArray = ParamAdapter.splitInstId(instId);
        this.base = new Currency(currencyArray[0]);
        this.quote = new Currency(currencyArray[1]);
    }

    public CurrencyPair(@NonNull final String base, @NonNull final String quote) {
        this.base = new Currency(base);
        this.quote = new Currency(quote);
    }

    @Override
    public String toString() {
        return "%s/%s".formatted(base, quote);
    }

}
