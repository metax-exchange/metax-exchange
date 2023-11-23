package org.metax.exchange.binance;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.metax.exchange.core.Interval;

import java.util.Objects;

import static java.util.concurrent.TimeUnit.*;


@AllArgsConstructor
public enum BinanceInterval implements Interval {

    m1("1m", MINUTES.toSeconds(1), IntervalGroup.SHORT, "分钟"),
    m3("3m", MINUTES.toSeconds(3), IntervalGroup.SHORT, "三分钟"),
    m5("5m", MINUTES.toSeconds(5), IntervalGroup.SHORT, "五分钟"),
    m15("15m", MINUTES.toSeconds(15), IntervalGroup.SHORT, "十五分钟"),
    m30("30m", MINUTES.toSeconds(30), IntervalGroup.SHORT, "三十分钟"),

    h1("1h", HOURS.toSeconds(1), IntervalGroup.MIDDLE, "小时"),
    h2("2h", HOURS.toSeconds(2), IntervalGroup.MIDDLE, "二小时"),
    h4("4h", HOURS.toSeconds(4), IntervalGroup.MIDDLE, "四小时"),
    h6("6h", HOURS.toSeconds(6), IntervalGroup.MIDDLE, "六小时"),
    h8("8h", HOURS.toSeconds(8), IntervalGroup.MIDDLE, "八小时"),
    h12("12h", HOURS.toSeconds(12), IntervalGroup.MIDDLE, "十二小时"),

    d1("1d", DAYS.toSeconds(1), IntervalGroup.LONG, "日"),
    d3("3d", DAYS.toSeconds(3), IntervalGroup.LONG, "三日"),
    w1("1w", DAYS.toSeconds(7), IntervalGroup.LONG, "周"),
    M1("1M", DAYS.toSeconds(30), IntervalGroup.LONG, "月");


    @Getter
    private final String code;

    @Getter
    private final long seconds;

    @Getter
    private final IntervalGroup group;

    @Getter
    private final String chinese;

    @Override
    public String getInterval() {
        return code;
    }


    public enum IntervalGroup {
        SHORT, MIDDLE, LONG;
    }


    public static BinanceInterval fromCode(String code) {
        for (BinanceInterval binanceInterval : BinanceInterval.values()) {
            if (Objects.equals(code, binanceInterval.code)) {
                return binanceInterval;
            }
        }
        throw new IllegalArgumentException("Invalid code: " + code);
    }
}
