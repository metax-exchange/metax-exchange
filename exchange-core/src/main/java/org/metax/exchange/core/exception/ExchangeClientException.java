package org.metax.exchange.core.exception;

public class ExchangeClientException extends ExchangeException{


    public ExchangeClientException(Object error, String message) {
        super(error, message);
    }

    public ExchangeClientException(String message) {
        super(message);
    }

    public ExchangeClientException(Throwable cause) {
        super(cause);
    }

    public ExchangeClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
