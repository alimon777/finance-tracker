package com.finance.goal.exception;

public class GoalServiceException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public GoalServiceException(String message) {
        super(message);
    }

    public GoalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}