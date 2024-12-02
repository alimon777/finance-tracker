package com.finance.mail.service;

import com.finance.mail.client.IdentityServiceClient;
import com.finance.mail.dto.EmailDetails;
import com.finance.mail.dto.UserResponse;
import com.finance.mail.exceptions.MailSendingException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class EmailServiceTest {

    @InjectMocks
    private EmailService emailService;

    @Mock
    private IdentityServiceClient identityServiceClient;

    @Mock
    private JavaMailSender javaMailSender;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendMailtoUser_Success() {
        // Arrange
        EmailDetails details = new EmailDetails();
        details.setUserId(1L);
        details.setSubject("Test Subject");
        details.setMsgBody("Test Message");

        UserResponse userResponse = new UserResponse();
        userResponse.setEmail("test@example.com");
        userResponse.setUsername("Test User");

        when(identityServiceClient.fetchUserDetails(1L)).thenReturn(ResponseEntity.ok(userResponse));

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        // Act
        String result = emailService.sendMailtoUser(details);

        // Assert
        assertEquals("Mail Sent Successfully...", result);
        verify(identityServiceClient, times(1)).fetchUserDetails(1L);
        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class));
    }

    @Test
    void testSendMailtoUser_Failure() {
        // Arrange
        EmailDetails details = new EmailDetails();
        details.setUserId(1L);

        when(identityServiceClient.fetchUserDetails(1L)).thenThrow(new RuntimeException("User service not available"));

        // Act & Assert
        MailSendingException exception = assertThrows(MailSendingException.class, () -> {
            emailService.sendMailtoUser(details);
        });

        assertEquals("Error while sending mail to 1", exception.getMessage());
        verify(identityServiceClient, times(1)).fetchUserDetails(1L);
        verifyNoInteractions(javaMailSender);
    }
}
