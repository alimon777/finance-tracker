package com.finance.mail.service;

import com.finance.mail.exceptions.MailSendingException;
import com.finance.mail.model.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import java.io.File;


@Service
public class EmailServiceImpl implements EmailService {

 @Autowired private JavaMailSender javaMailSender;

 @Value("${spring.mail.username}") private String sender;

 public String sendSimpleMail(EmailDetails details)
 {
     try {
         SimpleMailMessage mailMessage
             = new SimpleMailMessage();

         mailMessage.setFrom(sender);
         mailMessage.setTo(details.getRecipient());
         mailMessage.setText(details.getMsgBody());
         mailMessage.setSubject(details.getSubject());

         javaMailSender.send(mailMessage);
         return "Mail Sent Successfully...";
     }
     catch (Exception e) {
    	 throw new MailSendingException("Error while sending mail to " + details.getRecipient(), e);
     }
 }
}
