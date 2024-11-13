package com.finance.budget.exceptions;

public class BudgetCalculationException extends RuntimeException {
    public BudgetCalculationException(String message) {
        super(message);
    }
}