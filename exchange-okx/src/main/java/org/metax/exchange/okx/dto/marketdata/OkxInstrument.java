package org.metax.exchange.okx.dto.marketdata;

import lombok.Data;

@Data
public class OkxInstrument {

    private String alias;
    private String baseCcy;
    private String category;
    private String ctMult;
    private String ctType;
    private String ctVal;
    private String ctValCcy;
    private String expTime;
    private String instFamily;
    private String instId;
    private String instType;
    private String lever;
    private String listTime;
    private String lotSz;
    private String maxIcebergSz;
    private String maxLmtSz;
    private String maxMktSz;
    private String maxStopSz;
    private String maxTriggerSz;
    private String maxTwapSz;
    private String minSz;
    private String optType;
    private String quoteCcy;
    private String settleCcy;
    private String state;
    private String stk;
    private String tickSz;
    private String uly;
}
