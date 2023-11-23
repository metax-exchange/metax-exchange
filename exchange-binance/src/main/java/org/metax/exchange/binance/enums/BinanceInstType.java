package org.metax.exchange.binance.enums;

import org.metax.exchange.core.InstType;

public enum BinanceInstType  implements InstType {

    SPOT, // 现货

    DELIVERY, // 交割合约
    FUTURES,  // 永续合约

    OPTION; // 期权


    @Override
    public String getType() {
        return this.name();
    }
}
