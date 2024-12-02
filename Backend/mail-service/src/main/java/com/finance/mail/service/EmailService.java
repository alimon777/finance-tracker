package com.finance.mail.service;

import com.finance.mail.client.IdentityServiceClient;
import com.finance.mail.dto.EmailDetails;
import com.finance.mail.dto.UserResponse;
import com.finance.mail.exceptions.MailSendingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;


@Service
public class EmailService {

 @Autowired private IdentityServiceClient identityServiceClient;

 @Autowired private JavaMailSender javaMailSender;

 @Value("${spring.mail.username}") private String sender;

 public String sendSimpleMail(EmailDetails details)
 {
     try {
         SimpleMailMessage mailMessage
             = new SimpleMailMessage();

         mailMessage.setFrom(sender);
         UserResponse user = identityServiceClient.fetchUserDetails(details.getUserId()).getBody();
         
         
         mailMessage.setTo(user.getEmail());
         mailMessage.setText("Dear "+user.getUsername()+",\n\n"+details.getMsgBody());
         mailMessage.setSubject(details.getSubject());

         javaMailSender.send(mailMessage);
         return "Mail Sent Successfully...";
     }
     catch (Exception e) {
    	 throw new MailSendingException("Error while sending mail to " + details.getUserId(), e);
     }
 }
}
