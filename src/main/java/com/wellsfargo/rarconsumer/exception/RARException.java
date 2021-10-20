package com.wellsfargo.rarconsumer.exception;

public class RARException extends RuntimeException {

    private static final long serialVersionUID = 1L;

    public RARException(String message) {
        super(message);
    }

    public RARException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
