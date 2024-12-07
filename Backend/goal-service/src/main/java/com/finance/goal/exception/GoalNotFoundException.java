package com.finance.goal.exception;

public class GoalNotFoundException extends RuntimeException {
	private static final long serialVersionUID = 1L;

	public GoalNotFoundException(String message) {
        super(message);
    }
}