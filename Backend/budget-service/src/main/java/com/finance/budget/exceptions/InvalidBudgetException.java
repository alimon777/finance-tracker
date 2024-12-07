package com.finance.budget.exceptions;

public class InvalidBudgetException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public InvalidBudgetException(String message) {
        super(message);
    }
}