package com.andi.common.auth.exception;

public class Oauth2InitializationException extends RuntimeException {
    public Oauth2InitializationException() {
    }

    public Oauth2InitializationException(String message) {
        super(message);
    }

    public Oauth2InitializationException(String message, Throwable cause) {
        super(message, cause);
    }

    public Oauth2InitializationException(Throwable cause) {
        super(cause);
    }

    public Oauth2InitializationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
