package com.finance.transaction.exceptions;

public class DuplicateAccountNumberException extends RuntimeException {
    
	private static final long serialVersionUID = 1L;

	public DuplicateAccountNumberException(String message) {
        super(message);
    }
}
