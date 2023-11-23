package org.metax.exchange.core.exception;

public class ExchangeServerExchange extends  ExchangeException {


    public ExchangeServerExchange(Object error, String message) {
        super(error, message);
    }

    public ExchangeServerExchange(String message) {
        super(message);
    }

    public ExchangeServerExchange(Throwable cause) {
        super(cause);
    }

    public ExchangeServerExchange(String message, Throwable cause) {
        super(message, cause);
    }
}
