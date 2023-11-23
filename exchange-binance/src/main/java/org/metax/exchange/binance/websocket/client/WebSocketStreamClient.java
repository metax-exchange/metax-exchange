package org.metax.exchange.binance.websocket.client;

import org.metax.exchange.core.websocket.callback.WebSocketCloseCallback;
import org.metax.exchange.core.websocket.callback.WebSocketErrorCallback;
import org.metax.exchange.core.websocket.callback.WebSocketMessageCallback;
import org.metax.exchange.core.websocket.callback.WebSocketOpenCallback;

import java.util.List;

public interface WebSocketStreamClient {

    int klineStream(String symbol, String interval, WebSocketMessageCallback messageCallback);

    int klineStream(String symbol, String interval, WebSocketOpenCallback openCallback, WebSocketMessageCallback messageCallback, WebSocketCloseCallback closeCallback, WebSocketErrorCallback errorCallback);

    List<Integer> batchCombineStreams(List<String> streams, WebSocketMessageCallback messageCallback);

    List<Integer> batchCombineStreams(List<String> streams, WebSocketOpenCallback openCallback, WebSocketMessageCallback messageCallback, WebSocketCloseCallback closeCallback, WebSocketErrorCallback errorCallback);

    int combineStreams(List<String> streams, WebSocketMessageCallback messageCallback);

    /**
     * <p>批量订阅消息</p>
     * <p>注意：所有的websocket连接方法都是最后一步都应该是通过combineStreams去实现订阅的</p>
     *
     * @param streams
     * @param openCallback
     * @param messageCallback
     * @param closeCallback
     * @param errorCallback
     * @return
     */
    int combineStreams(List<String> streams, WebSocketOpenCallback openCallback, WebSocketMessageCallback messageCallback, WebSocketCloseCallback closeCallback, WebSocketErrorCallback errorCallback);

    void closeConnection(int connectionId);

    void closeAllConnections();
}
