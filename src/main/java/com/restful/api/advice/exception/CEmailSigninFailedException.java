package com.restful.api.advice.exception;

public class CEmailSigninFailedException extends RuntimeException {

    public CEmailSigninFailedException(String message, Throwable t) {
        super(message, t);
    }

    public CEmailSigninFailedException(String message) {
        super(message);
    }

    public CEmailSigninFailedException() {
        super();
    }
}
