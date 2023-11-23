package org.metax.exchange.binance.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Objects;

@AllArgsConstructor
public enum ExceptionStatus {

    DEFAULT_ERROR("DEFAULT_ERROR"), IP_BLOCKED("418"), RATE_LIMIT_WARNING("429"), SERVER_ERROR("5XX");

    @Getter
    private String code;

    public static ExceptionStatus fromCode(int code) {
        if (code >= 500) {
            return SERVER_ERROR;
        }
        for (ExceptionStatus value : ExceptionStatus.values()) {
            if (Objects.equals(String.valueOf(code), value.code)) {
                return value;
            }
        }
        return DEFAULT_ERROR;
    }


}
