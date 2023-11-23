package org.metax.exchange.core.websocket;

import org.metax.exchange.core.Interval;
import org.metax.exchange.core.currency.CurrencyPair;
import org.metax.exchange.core.websocket.callback.WebSocketCloseCallback;
import org.metax.exchange.core.websocket.callback.WebSocketErrorCallback;
import org.metax.exchange.core.websocket.callback.WebSocketMessageCallback;
import org.metax.exchange.core.websocket.callback.WebSocketOpenCallback;

public interface WebSocketService {

    int klineStream(CurrencyPair currencyPair, Interval interval, WebSocketMessageCallback messageCallback);

    int klineStream(CurrencyPair currencyPair, Interval interval, WebSocketOpenCallback openCallback, WebSocketMessageCallback messageCallback, WebSocketCloseCallback closeCallback, WebSocketErrorCallback errorCallback);

    void closeConnection(int connectionId, Object... args);

    void closeAllConnections(Object... args);
}
