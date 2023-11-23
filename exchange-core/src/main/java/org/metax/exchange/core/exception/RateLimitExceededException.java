package org.metax.exchange.core.exception;

public class RateLimitExceededException extends ExchangeException {
    public RateLimitExceededException(String message) {
        super(message);
    }
}
