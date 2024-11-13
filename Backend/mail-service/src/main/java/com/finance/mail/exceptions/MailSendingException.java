package com.finance.mail.exceptions;

public class MailSendingException extends RuntimeException {

    public MailSendingException(String message) {
        super(message);
    }
    
    public MailSendingException(String message, Throwable cause) {
        super(message, cause);
    }
}
