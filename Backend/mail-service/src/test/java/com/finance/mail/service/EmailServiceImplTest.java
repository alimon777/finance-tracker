//package com.finance.mail.service;
//import com.finance.mail.dto.EmailDetails;
//import com.finance.mail.exceptions.MailSendingException;
//import com.finance.mail.service.EmailServiceImpl;
//import jakarta.mail.MessagingException;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.mockito.MockitoAnnotations;
//import org.springframework.mail.javamail.JavaMailSender;
//import org.springframework.mail.SimpleMailMessage;
//
//import static org.mockito.Mockito.*;
//
//import static org.junit.jupiter.api.Assertions.*;
//
//public class EmailServiceImplTest {
//
//    @Mock
//    private JavaMailSender javaMailSender;
//
//    @InjectMocks
//    private EmailServiceImpl emailService;
//
//    private EmailDetails emailDetails;
//
//    @BeforeEach
//    void setUp() {
//        MockitoAnnotations.openMocks(this);
//        emailDetails = new EmailDetails();
//        emailDetails.setRecipient("test@example.com");
//        emailDetails.setMsgBody("Test Body");
//        emailDetails.setSubject("Test Subject");
//    }
//
//    @Test
//    void testSendSimpleMailSuccess() {
//        // Arrange: Mock the behavior of javaMailSender
//        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));
//
//        // Act
//        String response = emailService.sendSimpleMail(emailDetails);
//
//        // Assert
//        assertEquals("Mail Sent Successfully...", response);
//        verify(javaMailSender, times(1)).send(any(SimpleMailMessage.class)); // Ensure send() was called once
//    }
//
//    @Test
//    void testSendSimpleMailFailure() {
//        // Arrange: Mock the behavior of javaMailSender to throw exception
//        doThrow(new RuntimeException("Mail sending failed")).when(javaMailSender).send(any(SimpleMailMessage.class));
//
//        // Act & Assert
//        MailSendingException exception = assertThrows(MailSendingException.class, () -> {
//            emailService.sendSimpleMail(emailDetails);
//        });
//        assertEquals("Error while sending mail to test@example.com", exception.getMessage());
//    }
//}
