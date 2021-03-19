package com.restful.api.advice.exception;

public class CUserNotFoundException extends RuntimeException {

    public CUserNotFoundException(String message, Throwable t) {
        super(message, t);
    }

    public CUserNotFoundException(String message) {
        super(message);
    }

    public CUserNotFoundException() {
        super();
    }
}
