package org.metax.exchange.core.exception;

public class ExchangeConnectorException extends ExchangeException {


    public ExchangeConnectorException(Object error, String message) {
        super(error, message);
    }

    public ExchangeConnectorException(String message) {
        super(message);
    }

    public ExchangeConnectorException(Throwable cause) {
        super(cause);
    }

    public ExchangeConnectorException(String message, Throwable cause) {
        super(message, cause);
    }
}
