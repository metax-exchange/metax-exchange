package org.metax.exchange.binance.websocket.client;

import lombok.extern.slf4j.Slf4j;
import org.metax.exchange.binance.Constant;

@Slf4j
public class UMWebsocketClientImpl extends WebSocketStreamClientImpl {


    public UMWebsocketClientImpl() {
        this(Constant.UM_WS_URL, 200);
    }

    public UMWebsocketClientImpl(String baseUrl, int maxStreamsSize) {
        super(baseUrl, maxStreamsSize);
    }

}
