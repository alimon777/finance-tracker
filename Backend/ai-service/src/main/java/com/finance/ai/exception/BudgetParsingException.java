package com.finance.ai.exception;

public class BudgetParsingException extends RuntimeException {
    private static final long serialVersionUID = 2L;

    public BudgetParsingException(String message) {
        super(message);
    }

    public BudgetParsingException(String message, Throwable cause) {
        super(message, cause);
    }
}
