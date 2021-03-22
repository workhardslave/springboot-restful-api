package com.restful.api.advice.exception;

public class CAuthenticationEntryPointException extends RuntimeException {

    public CAuthenticationEntryPointException(String message, Throwable t) {
        super(message, t);
    }

    public CAuthenticationEntryPointException(String message) {
        super(message);
    }

    public CAuthenticationEntryPointException() {
        super();
    }
}
