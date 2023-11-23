package org.metax.exchange.okx.enums;

import org.metax.exchange.core.InstType;

public enum OkxInstType implements InstType {

    SPOT,

    MARGIN,

    SWAP,

    FUTURES,

    OPTION;

    @Override
    public String getType() {
        return this.name();
    }
}
