package com.finance.mail.controller;

import com.finance.mail.dto.EmailDetails;
import com.finance.mail.exceptions.MailSendingException;
import com.finance.mail.service.EmailService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.ResponseEntity;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class EmailControllerTest {

    @InjectMocks
    private EmailController emailController;

    @Mock
    private EmailService emailService;

    public EmailControllerTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMail_Success() {
        // Arrange
        EmailDetails details = new EmailDetails();
        details.setUserId(1L);
        details.setSubject("Test Mail");
        details.setMsgBody("This is a test message");

        when(emailService.sendMailtoUser(details)).thenReturn("Mail Sent Successfully...");

        // Act
        ResponseEntity<String> response = emailController.sendMail(details);

        // Assert
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("Mail Sent Successfully...", response.getBody());
        verify(emailService, times(1)).sendMailtoUser(details);
    }

    @Test
    void testSendMail_Failure() {
        // Arrange
        EmailDetails details = new EmailDetails();
        details.setUserId(1L);

        when(emailService.sendMailtoUser(details)).thenThrow(new MailSendingException("Error while sending mail"));

        // Act
        ResponseEntity<String> response = emailController.sendMail(details);

        // Assert
        assertEquals(500, response.getStatusCodeValue());
        assertEquals("Error while sending mail", response.getBody());
        verify(emailService, times(1)).sendMailtoUser(details);
    }
}
