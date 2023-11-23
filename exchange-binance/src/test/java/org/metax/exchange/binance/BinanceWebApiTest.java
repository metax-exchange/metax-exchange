package org.metax.exchange.binance;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.metax.exchange.binance.dto.marketdata.BinanceKline;
import org.metax.exchange.binance.enums.BinanceInstType;
import org.metax.exchange.binance.webapi.service.BinanceMarketDataService;
import org.metax.exchange.binance.websocket.BinanceWebSocketService;
import org.metax.exchange.core.ExchangeFactory;
import org.metax.exchange.core.ExchangeSpecification;
import org.metax.exchange.core.InstType;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.currency.FuturesCurrencyPair;
import org.metax.exchange.core.websocket.callback.WebSocketMessageCallback;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Slf4j
public class BinanceWebApiTest {

    private static final BinanceExchange binanceExchange;

    static {
        ExchangeSpecification exchangeSpecification = ExchangeSpecification.builder().resilienceSpecification(new ExchangeSpecification.ResilienceSpecification(false, true)).build();
        binanceExchange = ExchangeFactory.INSTANCE.createExchange(BinanceExchange.class, exchangeSpecification);
    }

    @Test
    public void ping() throws IOException {
        BinanceMarketDataService binanceMarketDataService = (BinanceMarketDataService) binanceExchange.getApiService();
        System.out.println(binanceMarketDataService.ping());
    }

    @Test
    public void exchangeInfo() throws IOException {
        BinanceMarketDataService binanceMarketDataService = (BinanceMarketDataService) binanceExchange.getApiService();
        System.out.println(binanceMarketDataService.getExchangeInfo(BinanceInstType.SPOT));
    }

    @Test
    public void currencyPairList() throws IOException {
        BinanceMarketDataService binanceMarketDataService = (BinanceMarketDataService) binanceExchange.getApiService();
        System.out.println(binanceMarketDataService.getCurrencyPairList());
        System.out.println(binanceMarketDataService.getCurrencyPairList(BinanceInstType.FUTURES));
    }


    @Test
    public void tickerPrice() throws IOException {
        BinanceMarketDataService binanceMarketDataService = (BinanceMarketDataService) binanceExchange.getApiService();
        System.out.println(binanceMarketDataService.getTickerPrice(new CurrencyPair("BTC/USDT")));
    }

    @Test
    public void klines() throws IOException {
        BinanceMarketDataService binanceMarketDataService = (BinanceMarketDataService) binanceExchange.getApiService();
        System.out.println(binanceMarketDataService.lastKline(new CurrencyPair("BTC/USDT"), BinanceInterval.m5));
    }

    @Test
    public void ticker24h() throws IOException {
        BinanceMarketDataService binanceMarketDataService = (BinanceMarketDataService) binanceExchange.getApiService();
        System.out.println(binanceMarketDataService.getTicker24h(new CurrencyPair("BTC/USDT")));
    }

    @Test
    public void time() throws IOException {
        BinanceMarketDataService binanceMarketDataService = (BinanceMarketDataService) binanceExchange.getApiService();
        System.out.println(binanceMarketDataService.time());
    }


    @Test
    public void markPrice() throws IOException {
        BinanceMarketDataService binanceMarketDataService = (BinanceMarketDataService) binanceExchange.getApiService();
        System.out.println(binanceMarketDataService.getMarkPrice(new FuturesCurrencyPair("BTC/USDT")));
    }

    @Test
    public void klineStream() throws InterruptedException {
        BinanceWebSocketService binanceWebSocketService = (BinanceWebSocketService) binanceExchange.getWebSocketService();
        binanceWebSocketService.klineStream(new CurrencyPair("BTC/USDT"), BinanceInterval.m5, new WebSocketMessageCallback() {
            @Override
            public void onMessage(String data) {
                System.out.println(data);
            }
        });
        Thread.sleep(10_000);
    }


    @Test
    public void batchKlineStream() throws InterruptedException {
        BinanceWebSocketService binanceWebSocketService = (BinanceWebSocketService) binanceExchange.getWebSocketService();
        List<CurrencyPair> currencyPairList = List.of(new CurrencyPair("BTC/USDT"), new CurrencyPair("ETH/USDT"));
        Map<String, CurrencyPair> map = currencyPairList.stream().collect(Collectors.toMap(o -> BinanceParamAdapter.toSymbol(o), currencyPair -> currencyPair));
        binanceWebSocketService.batchKlineStream(BinanceInstType.SPOT, BinanceParamAdapter.toKlineStreams(currencyPairList), data -> {
            BinanceKline binanceKline = BinanceResponseAdapter.fromKlineStream(data);
            binanceKline.setCurrencyPair(map.get(binanceKline.getSymbol()));
            System.out.println(binanceKline);
        });
        Thread.sleep(10_000);
        binanceWebSocketService.closeAllConnections((InstType) () -> "SPOT");
        Thread.sleep(10_000);
    }

}
