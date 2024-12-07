package com.finance.ai.exception;

public class InvalidDataException extends RuntimeException {
    private static final long serialVersionUID = 3L;

    public InvalidDataException(String message) {
        super(message);
    }

    public InvalidDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
