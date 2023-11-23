package org.metax.exchange.core.webapi;

import lombok.NonNull;
import org.metax.exchange.core.Interval;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.currency.FuturesCurrencyPair;
import org.metax.exchange.core.dto.marketdata.Kline;
import org.metax.exchange.core.dto.marketdata.MarkPrice;
import org.metax.exchange.core.dto.marketdata.TickerPrice;

import java.io.IOException;
import java.util.List;

public interface MarketDataService extends ApiService {

    /**
     * Get K-line data.
     *
     * @param currencyPair
     * @param interval
     * @param args
     * @return
     * @throws IOException
     */
    List<Kline> klines(@NonNull CurrencyPair currencyPair, @NonNull Interval interval, Object... args) throws IOException;

    /**
     * Get the latest price of a trading pair.
     *
     * @param currencyPair
     * @param args
     * @return
     * @throws IOException
     */
    TickerPrice getTickerPrice(@NonNull CurrencyPair currencyPair, Object... args) throws IOException;


    /**
     * Get the latest mark price; return the latest spot price by default if unavailable.
     *
     * @param currencyPair
     * @param args
     * @return
     * @throws IOException
     */
    MarkPrice getMarkPrice(@NonNull FuturesCurrencyPair currencyPair, Object... args) throws IOException;


}
