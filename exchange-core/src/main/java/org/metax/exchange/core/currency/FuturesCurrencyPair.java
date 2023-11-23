package org.metax.exchange.core.currency;

import lombok.Data;
import lombok.NonNull;
import org.metax.exchange.core.InstType;
import org.metax.exchange.core.ParamAdapter;
import org.springframework.util.StringUtils;

@Data
public class FuturesCurrencyPair extends CurrencyPair {

    private String prompt;

    private String instType;


    public FuturesCurrencyPair(@NonNull Currency base, @NonNull Currency quote, String prompt) {
        super(base, quote);
        this.prompt = prompt;
    }

    public FuturesCurrencyPair(@NonNull final String instId) {
        super(instId);
        String[] currencyArray = ParamAdapter.splitInstId(instId);
        this.prompt = (currencyArray.length == 3) ? currencyArray[2] : "";
    }

    public FuturesCurrencyPair(@NonNull final String instId, @NonNull final InstType instType) {
        super(instId);
        String[] currencyArray = ParamAdapter.splitInstId(instId);
        this.prompt = (currencyArray.length == 3) ? currencyArray[2] : "";
        this.instType = instType.getType();
    }

    public FuturesCurrencyPair(@NonNull String base, @NonNull String quote, String prompt) {
        super(base, quote);
        this.prompt = prompt;
    }

    @Override
    public String toString() {
        return StringUtils.hasText(prompt) ? "%s/%s/%s".formatted(this.getBase(), this.getQuote(), prompt) : "%s/%s".formatted(this.getBase(), this.getQuote());
    }


}
