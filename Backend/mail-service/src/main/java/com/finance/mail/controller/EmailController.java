package com.finance.mail.controller;

import com.finance.mail.dto.EmailDetails;
import com.finance.mail.exceptions.MailSendingException;
import com.finance.mail.service.EmailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("api/mails")
public class EmailController {

 @Autowired 
 private EmailService emailService;
 
 @PostMapping("/sendMail")
 public ResponseEntity<String> sendMail(@RequestBody EmailDetails details)
 {
	 try {
         String status = emailService.sendMailtoUser(details);
         return ResponseEntity.ok(status);
     } catch (MailSendingException e) {
         return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
     }
  }
}
