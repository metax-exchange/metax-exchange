package org.metax.exchange.binance;

import com.google.common.collect.Lists;
import org.metax.exchange.core.currency.CurrencyPair;

import java.util.List;

public class BinanceParamAdapter {

    public static String toSymbol(CurrencyPair currencyPair) {
        return currencyPair.toString().replace("/", "");
    }

    public static String toStream(String symbol) {
        return symbol.toLowerCase();
    }

    public static String toStream(CurrencyPair currencyPair) {
        return currencyPair.toString().replace("/", "").toLowerCase();
    }

    public static List<String> toKlineStreams(List<CurrencyPair> currencyPairList, List<BinanceInterval> binanceIntervals) {
        List<String> klineStreams = Lists.newArrayListWithCapacity(currencyPairList.size() * BinanceInterval.values().length);
        for (CurrencyPair currencyPair : currencyPairList) {
            for (BinanceInterval binanceInterval : binanceIntervals) {
                klineStreams.add("%s@kline_%s".formatted(BinanceParamAdapter.toStream(currencyPair), binanceInterval.getInterval()));
            }
        }
        return klineStreams;
    }

    public static List<String> toKlineStreams(List<CurrencyPair> currencyPairList) {
        return toKlineStreams(currencyPairList, List.of(BinanceInterval.values()));
    }

    public static List<String> toKlineStreams(CurrencyPair currencyPair, List<BinanceInterval> binanceIntervals) {
        return toKlineStreams(List.of(currencyPair), binanceIntervals);
    }

    public static List<String> toKlineStreams(CurrencyPair currencyPair) {
        return toKlineStreams(currencyPair, List.of(BinanceInterval.values()));
    }

}
