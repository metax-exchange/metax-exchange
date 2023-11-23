package org.metax.exchange.binance.websocket.client;

import lombok.extern.slf4j.Slf4j;
import org.metax.exchange.binance.BinanceParamAdapter;
import org.metax.exchange.binance.Constant;
import org.metax.exchange.binance.util.URIUtil;
import org.metax.exchange.binance.websocket.WebSocketConnection;
import org.metax.exchange.core.websocket.callback.WebSocketCloseCallback;
import org.metax.exchange.core.websocket.callback.WebSocketErrorCallback;
import org.metax.exchange.core.websocket.callback.WebSocketMessageCallback;
import org.metax.exchange.core.websocket.callback.WebSocketOpenCallback;
import org.springframework.util.Assert;

import java.net.URI;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


@Slf4j
public class WebSocketStreamClientImpl implements WebSocketStreamClient {

    private final Map<Integer, WebSocketConnection> connections = new ConcurrentHashMap<>();

    private final WebSocketOpenCallback noopOpenCallback = response -> {
    };
    private final WebSocketCloseCallback noopCloseCallback = (i, s, b) -> {
    };
    private final WebSocketErrorCallback noopErrorCallback = (e) -> {
    };
    private int maxStreamsSize;

    private String baseUrl;


    public WebSocketStreamClientImpl() {
        this(Constant.SPOT_WS_URL, 1024);
    }

    public WebSocketStreamClientImpl(String baseUrl, int maxStreamsSize) {
        this.baseUrl = baseUrl;
        this.maxStreamsSize = maxStreamsSize;
    }

    @Override
    public int klineStream(String symbol, String interval, WebSocketMessageCallback messageCallback) {
        return this.klineStream(symbol, interval, noopOpenCallback, messageCallback, noopCloseCallback, noopErrorCallback);
    }

    @Override
    public int klineStream(String symbol, String interval, WebSocketOpenCallback openCallback, WebSocketMessageCallback messageCallback, WebSocketCloseCallback closeCallback, WebSocketErrorCallback errorCallback) {
        Assert.hasText(symbol, "symbol it must not be null or empty");
        Assert.hasText(interval, "interval it must not be null or empty");
        return this.combineStreams(List.of("%s@kline_%s".formatted(BinanceParamAdapter.toStream(symbol), interval)), openCallback, messageCallback, closeCallback, errorCallback);
    }

    /**
     * <p>批量订阅，返回多个connectionId</p>
     *
     * @param streams
     * @param messageCallback
     * @return
     */
    @Override
    public List<Integer> batchCombineStreams(List<String> streams, WebSocketMessageCallback messageCallback) {
        return this.batchCombineStreams(streams, noopOpenCallback, messageCallback, noopCloseCallback, noopErrorCallback);
    }

    @Override
    public List<Integer> batchCombineStreams(List<String> streams, WebSocketOpenCallback openCallback, WebSocketMessageCallback messageCallback, WebSocketCloseCallback closeCallback, WebSocketErrorCallback errorCallback) {
        Assert.notEmpty(streams, "streams require not empty");
        int batchSize = maxStreamsSize;
        List<Integer> connectionIds = new ArrayList<>();
        for (int i = 0; i < streams.size(); i += batchSize) {
            int endIndex = Math.min(i + batchSize, streams.size());
            List<String> batchStreams = streams.subList(i, endIndex);
            connectionIds.add(this.combineStreams(batchStreams, openCallback, messageCallback, closeCallback, errorCallback));
        }
        return connectionIds;
    }

    @Override
    public int combineStreams(List<String> streams, WebSocketMessageCallback messageCallback) {
        return this.combineStreams(streams, noopOpenCallback, messageCallback, noopCloseCallback, noopErrorCallback);
    }


    @Override
    public int combineStreams(List<String> streams, WebSocketOpenCallback openCallback, WebSocketMessageCallback messageCallback, WebSocketCloseCallback closeCallback, WebSocketErrorCallback errorCallback) {
        Assert.notEmpty(streams, "streams require not empty");
        Assert.isTrue(streams.size() <= maxStreamsSize, "streams size require le " + maxStreamsSize);
        URI uri = URIUtil.create(baseUrl, streams);
        return this.createConnection(uri, openCallback, messageCallback, closeCallback, errorCallback);
    }

    @Override
    public void closeConnection(int connectionId) {
        if (connections.containsKey(connectionId)) {
            log.info("Close ConnectionId {}", connectionId);
            connections.get(connectionId).close();
            connections.remove(connectionId);
        } else {
            log.warn("ConnectionId: {} does not exist!", connectionId);
        }
    }

    @Override
    public void closeAllConnections() {
        if (!connections.isEmpty()) {
            log.info("Close {} Connections(s)", connections.size());
            Iterator<Map.Entry<Integer, WebSocketConnection>> iter = connections.entrySet().iterator();
            while (iter.hasNext()) {
                WebSocketConnection connection = iter.next().getValue();
                connection.close();
                iter.remove();
            }
        }
        if (connections.isEmpty()) {
            log.info("All Connections are closed!");
        }
    }


    private int createConnection(URI uri, WebSocketOpenCallback openCallback, WebSocketMessageCallback messageCallback, WebSocketCloseCallback closeCallback, WebSocketErrorCallback errorCallback) {
        WebSocketConnection connection = new WebSocketConnection(uri, openCallback, messageCallback, closeCallback, errorCallback);
        connection.setConnectionLostTimeout(0);
        connection.connect();
        int connectionId = connection.getConnectionId();
        connections.put(connectionId, connection);
        return connectionId;
    }


}
