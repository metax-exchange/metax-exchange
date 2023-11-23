package org.metax.exchange.binance.dto.marketdata;

import lombok.Data;
import lombok.ToString;
import org.metax.exchange.core.dto.marketdata.MarkPrice;

@Data
@ToString(callSuper = true)
public class BinanceMarkPrice extends MarkPrice {

    private String symbol;

    private String indexPrice;

    private String estimatedSettlePrice;

    private String lastFundingRate;

    private long nextFundingTime;

    private String interestRate;

    private long time;


}
