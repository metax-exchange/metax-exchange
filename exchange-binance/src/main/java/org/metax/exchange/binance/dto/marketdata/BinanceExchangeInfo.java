package org.metax.exchange.binance.dto.marketdata;

import lombok.Data;

import java.util.List;

@Data
public class BinanceExchangeInfo {

    private String timezone;
    private long serverTime;
    private List<RateLimit> rateLimits;
    private List<String> exchangeFilters;
    private List<Symbol> symbols;

    @Data
    public class RateLimit {
        private String rateLimitType;
        private String interval;
        private int intervalNum;
        private int limit;
    }

    @Data
    public class Filter {
        private String filterType;
        private String minPrice;
        private String maxPrice;
        private String tickSize;
    }

    @Data
    public class Symbol {
        private String symbol;
        private String status;
        private String baseAsset;
        private int baseAssetPrecision;
        private String quoteAsset;
        private int quotePrecision;
        private int quoteAssetPrecision;
        private int baseCommissionPrecision;
        private int quoteCommissionPrecision;
        private List<String> orderTypes;
        private boolean icebergAllowed;
        private boolean ocoAllowed;
        private boolean quoteOrderQtyMarketAllowed;
        private boolean allowTrailingStop;
        private boolean cancelReplaceAllowed;
        private boolean isSpotTradingAllowed;
        private boolean isMarginTradingAllowed;
        private List<Filter> filters;
        private List<String> permissions;
        private String defaultSelfTradePreventionMode;
        private List<String> allowedSelfTradePreventionModes;
    }
}
