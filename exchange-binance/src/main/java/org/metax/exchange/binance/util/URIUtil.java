package org.metax.exchange.binance.util;

import java.net.URI;
import java.util.List;

public class URIUtil {


    public static URI create(String baseUrl, List<String> streams) {
        return URI.create(buildStreamUrl(baseUrl, streams));
    }


    public static String buildStreamUrl(String baseUrl, List<String> streams) {
        StringBuilder sb = new StringBuilder(baseUrl).append("/stream");
        if (streams != null && !streams.isEmpty()) {
            sb.append("?streams=");
            sb.append(joinStreamUrls(streams));
        }
        return sb.toString();
    }


    /**
     * <p>Joins streams from an ArrayList into a StringBuilder representation. </p>
     *
     * @param streams
     * @return
     */
    public static String joinStreamUrls(List<String> streams) {
        return joinStreamUrls(new StringBuilder(), streams).toString();
    }

    /**
     * Joins streams from an ArrayList into a StringBuilder representation.
     *
     * @param sb The StringBuilder to append the streams to.
     * @return The StringBuilder representation of the joined streams.
     */
    public static StringBuilder joinStreamUrls(StringBuilder sb, List<String> streams) {
        if (streams != null && !streams.isEmpty()) {
            for (String stream : streams) {
                sb.append(stream);
                sb.append("/");
            }
            sb.deleteCharAt(sb.length() - 1);
        }
        return sb;
    }


}
