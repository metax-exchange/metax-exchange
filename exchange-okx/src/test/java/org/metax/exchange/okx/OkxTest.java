package org.metax.exchange.okx;

import org.junit.Test;
import org.metax.exchange.core.ExchangeFactory;
import org.metax.exchange.core.ExchangeSpecification;
import org.metax.exchange.core.currency.Currency;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.currency.FuturesCurrencyPair;
import org.metax.exchange.core.webapi.MarketDataService;
import org.metax.exchange.okx.enums.OkxInstType;
import org.metax.exchange.okx.webapi.service.OkxMarketDataService;

import java.io.IOException;

public class OkxTest {

    private static final OkxExchange okxExchange;

    static {
        ExchangeSpecification exchangeSpecification = ExchangeSpecification.builder().resilienceSpecification(new ExchangeSpecification.ResilienceSpecification(false, true)).build();
        okxExchange = ExchangeFactory.INSTANCE.createExchange(OkxExchange.class, exchangeSpecification);
    }

    @Test
    public void symbol() {
        System.out.println(OkxParamAdapter.toSymbol(new CurrencyPair("BTC-USDT")));
    }

    @Test
    public void currencyPairList() throws IOException {
        OkxMarketDataService marketDataService = (OkxMarketDataService) okxExchange.getApiService();
        System.out.println(marketDataService.getCurrencyPairList(OkxInstType.FUTURES));
    }

    @Test
    public void time() throws IOException {
        OkxMarketDataService marketDataService = (OkxMarketDataService) okxExchange.getApiService();
        System.out.println(marketDataService.time());
    }

    @Test
    public void klines() throws IOException {
        OkxMarketDataService marketDataService = (OkxMarketDataService) okxExchange.getApiService();
        System.out.println(marketDataService.lastIndexCandles(new CurrencyPair("BTC-USDT"), () -> "5m"));
    }

    @Test
    public void ticker() throws IOException {
        MarketDataService marketDataService = (MarketDataService) okxExchange.getApiService();
        System.out.println(marketDataService.getTickerPrice(new CurrencyPair("BTC-USDT")));

    }

    @Test
    public void tickers() throws IOException {
        OkxMarketDataService marketDataService = (OkxMarketDataService) okxExchange.getApiService();
        System.out.println(marketDataService.getTickerPrice(new Currency("USDT")));
    }

    @Test
    public void markPrice() throws IOException {
        OkxMarketDataService marketDataService = (OkxMarketDataService) okxExchange.getApiService();
        FuturesCurrencyPair futuresCurrencyPair = new FuturesCurrencyPair("BTC-USD-231124", OkxInstType.FUTURES);
        System.out.println(marketDataService.getMarkPrice(futuresCurrencyPair));
    }

}
