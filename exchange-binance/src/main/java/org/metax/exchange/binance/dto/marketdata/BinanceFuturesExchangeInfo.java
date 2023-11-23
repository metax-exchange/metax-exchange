/**
 * Copyright 2023 bejson.com
 */
package org.metax.exchange.binance.dto.marketdata;

import lombok.Data;

import java.util.List;

/**
 * Auto-generated: 2023-11-22 12:48:12
 *
 * @author bejson.com (i@bejson.com)
 * @website http://www.bejson.com/java2pojo/
 */
@Data
public class BinanceFuturesExchangeInfo {

    private String timezone;
    private long serverTime;
    private String futuresType;
    private List<RateLimit> rateLimits;
    private List<String> exchangeFilters;
    private List<Asset> assets;
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
        private String maxPrice;
        private String tickSize;
        private String minPrice;
        private String filterType;
    }


    @Data
    public class Asset {
        private String asset;
        private boolean marginAvailable;
        private String autoAssetExchange;
    }

    @Data
    public class Symbol {
        private String symbol;
        private String pair;
        private String contractType;
        private long deliveryDate;
        private long onboardDate;
        private String status;
        private String maintMarginPercent;
        private String requiredMarginPercent;
        private String baseAsset;
        private String quoteAsset;
        private String marginAsset;
        private int pricePrecision;
        private int quantityPrecision;
        private int baseAssetPrecision;
        private int quotePrecision;
        private String underlyingType;
        private List<String> underlyingSubType;
        private int settlePlan;
        private String triggerProtect;
        private String liquidationFee;
        private String marketTakeBound;
        private int maxMoveOrderLimit;
        private List<Filter> filters;
        private List<String> orderTypes;
        private List<String> timeInForce;
    }

}