package com.finance.budget.exceptions;

public class BudgetCalculationException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public BudgetCalculationException(String message) {
        super(message);
    }
}