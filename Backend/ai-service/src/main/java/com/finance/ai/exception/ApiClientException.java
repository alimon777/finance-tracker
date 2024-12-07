package com.finance.ai.exception;

public class ApiClientException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public ApiClientException(String message) {
        super(message);
    }

    public ApiClientException(String message, Throwable cause) {
        super(message, cause);
    }
}
