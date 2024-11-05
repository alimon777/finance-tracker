package com.finance.goal.exception;

public class GoalServiceException extends RuntimeException {

    public GoalServiceException(String message) {
        super(message);
    }

    public GoalServiceException(String message, Throwable cause) {
        super(message, cause);
    }
}