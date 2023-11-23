package org.metax.exchange.binance.websocket;


import lombok.extern.slf4j.Slf4j;
import org.java_websocket.client.WebSocketClient;
import org.java_websocket.handshake.ServerHandshake;
import org.metax.exchange.core.websocket.callback.WebSocketCloseCallback;
import org.metax.exchange.core.websocket.callback.WebSocketErrorCallback;
import org.metax.exchange.core.websocket.callback.WebSocketMessageCallback;
import org.metax.exchange.core.websocket.callback.WebSocketOpenCallback;

import java.net.URI;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
public class WebSocketConnection extends WebSocketClient {

    private static final AtomicInteger connectionCounter = new AtomicInteger(0);

    private WebSocketMessageCallback webSocketMessageCallback;

    private WebSocketCloseCallback webSocketCloseCallback;

    private WebSocketOpenCallback webSocketOpenCallback;

    private WebSocketErrorCallback webSocketErrorCallback;

    private final int connectionId;

    public WebSocketConnection(URI serverUri, WebSocketOpenCallback webSocketOpenCallback, WebSocketMessageCallback webSocketMessageCallback, WebSocketCloseCallback webSocketCloseCallback, WebSocketErrorCallback webSocketErrorCallback) {
        super(serverUri);
        this.webSocketOpenCallback = webSocketOpenCallback;
        this.webSocketMessageCallback = webSocketMessageCallback;
        this.webSocketCloseCallback = webSocketCloseCallback;
        this.webSocketErrorCallback = webSocketErrorCallback;
        this.connectionId = WebSocketConnection.connectionCounter.incrementAndGet();
    }

    @Override
    public void onOpen(ServerHandshake serverHandshake) {
        webSocketOpenCallback.onOpen(serverHandshake);
    }

    @Override
    public void onMessage(String s) {
        webSocketMessageCallback.onMessage(s);
    }

    @Override
    public void onClose(int i, String s, boolean b) {
        log.info("Close ConnectionId:{}, code: {}, reason:{}, remote:{}", connectionId, i, s, b);
        webSocketCloseCallback.onClose(i, s, b);
    }

    @Override
    public void onError(Exception e) {
        log.error("[ConnectionId:{} ] Failure:", connectionId, e);
        webSocketErrorCallback.onError(e);
    }

    public int getConnectionId() {
        return connectionId;
    }

}
