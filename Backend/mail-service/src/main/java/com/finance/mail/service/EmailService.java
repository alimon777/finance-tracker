package com.finance.mail.service;

import com.finance.mail.model.EmailDetails;

public interface EmailService {
	
    String sendSimpleMail(EmailDetails details);

}
