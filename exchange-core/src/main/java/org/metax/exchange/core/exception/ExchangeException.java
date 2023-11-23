package org.metax.exchange.core.exception;


public class ExchangeException extends RuntimeException {


    private Object error;

    public ExchangeException(Object error, String message) {
        super(message);
        this.error = error;
    }

    public ExchangeException(String message) {

        super(message);
    }

    public ExchangeException(Throwable cause) {

        super(cause);
    }

    public ExchangeException(String message, Throwable cause) {

        super(message, cause);
    }
}
