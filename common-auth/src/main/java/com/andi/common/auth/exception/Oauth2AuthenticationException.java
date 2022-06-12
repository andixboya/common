package com.andi.common.auth.exception;

public class Oauth2AuthenticationException extends RuntimeException {
    public Oauth2AuthenticationException() {
    }

    public Oauth2AuthenticationException(String message) {
        super(message);
    }

    public Oauth2AuthenticationException(String message, Throwable cause) {
        super(message, cause);
    }

    public Oauth2AuthenticationException(Throwable cause) {
        super(cause);
    }

    public Oauth2AuthenticationException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace) {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
