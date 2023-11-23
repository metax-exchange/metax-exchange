package org.metax.exchange.binance.websocket;

import org.metax.exchange.binance.BinanceParamAdapter;
import org.metax.exchange.binance.enums.BinanceInstType;
import org.metax.exchange.binance.websocket.client.UMWebsocketClientImpl;
import org.metax.exchange.binance.websocket.client.WebSocketStreamClient;
import org.metax.exchange.binance.websocket.client.WebSocketStreamClientImpl;
import org.metax.exchange.core.InstType;
import org.metax.exchange.core.Interval;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.currency.FuturesCurrencyPair;
import org.metax.exchange.core.websocket.WebSocketService;
import org.metax.exchange.core.websocket.callback.WebSocketCloseCallback;
import org.metax.exchange.core.websocket.callback.WebSocketErrorCallback;
import org.metax.exchange.core.websocket.callback.WebSocketMessageCallback;
import org.metax.exchange.core.websocket.callback.WebSocketOpenCallback;

import java.util.List;
import java.util.Objects;

public class BinanceWebSocketService implements WebSocketService {

    private WebSocketStreamClient webSocketStreamClient = new WebSocketStreamClientImpl();

    private WebSocketStreamClient umWebsocketClient = new UMWebsocketClientImpl();

    private final WebSocketOpenCallback noopOpenCallback = response -> {
    };
    private final WebSocketCloseCallback noopCloseCallback = (i, s, b) -> {
    };
    private final WebSocketErrorCallback noopErrorCallback = (e) -> {
    };

    public WebSocketStreamClient getUmWebsocketClient() {
        return umWebsocketClient;
    }

    public WebSocketStreamClient getWebSocketStreamClient() {
        return webSocketStreamClient;
    }

    public int batchKlineStream(BinanceInstType binanceInstType, List<String> klineStreams, WebSocketMessageCallback messageCallback) {
        return Objects.equals(binanceInstType, BinanceInstType.FUTURES) ? umWebsocketClient.combineStreams(klineStreams, messageCallback) : webSocketStreamClient.combineStreams(klineStreams, messageCallback);
    }

    @Override
    public int klineStream(CurrencyPair currencyPair, Interval interval, WebSocketMessageCallback messageCallback) {
        return this.klineStream(currencyPair, interval, noopOpenCallback, messageCallback, noopCloseCallback, noopErrorCallback);
    }

    @Override
    public int klineStream(CurrencyPair currencyPair, Interval interval, WebSocketOpenCallback openCallback, WebSocketMessageCallback messageCallback, WebSocketCloseCallback closeCallback, WebSocketErrorCallback errorCallback) {
        return currencyPair instanceof FuturesCurrencyPair ? umWebsocketClient.klineStream(BinanceParamAdapter.toStream(currencyPair), interval.getInterval(), openCallback, messageCallback, closeCallback, errorCallback) : webSocketStreamClient.klineStream(BinanceParamAdapter.toSymbol(currencyPair), interval.getInterval(), openCallback, messageCallback, closeCallback, errorCallback);
    }

    @Override
    public void closeConnection(int connectionId, Object... args) {
        InstType instType = args.length == 1 ? (InstType) args[0] : BinanceInstType.SPOT;
        if (Objects.equals(instType.getType(), BinanceInstType.FUTURES.getType())) {
            umWebsocketClient.closeConnection(connectionId);
        } else {
            webSocketStreamClient.closeConnection(connectionId);
        }
    }

    @Override
    public void closeAllConnections(Object... args) {
        InstType instType = args.length == 1 ? (InstType) args[0] : BinanceInstType.SPOT;
        if (Objects.equals(instType.getType(), BinanceInstType.FUTURES.getType())) {
            umWebsocketClient.closeAllConnections();
        } else {
            webSocketStreamClient.closeAllConnections();
        }
    }

}
