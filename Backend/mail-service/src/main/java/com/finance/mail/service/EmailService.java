package com.finance.mail.service;

import com.finance.mail.model.EmailDetails;

public interface EmailService {
	
	// Method
    // To send a simple email
    String sendSimpleMail(EmailDetails details);

}
