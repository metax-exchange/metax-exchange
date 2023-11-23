package org.metax.exchange.core.websocket.callback;

import org.java_websocket.handshake.ServerHandshake;

public interface WebSocketOpenCallback {

    void  onOpen(ServerHandshake serverHandshake);
}
