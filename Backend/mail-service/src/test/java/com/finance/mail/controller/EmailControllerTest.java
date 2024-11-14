package com.finance.mail.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.mail.exceptions.MailSendingException;
import com.finance.mail.model.EmailDetails;
import com.finance.mail.service.EmailService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;

public class EmailControllerTest {

    private MockMvc mockMvc;

    @Mock
    private EmailService emailService;

    @InjectMocks
    private EmailController emailController;

    private EmailDetails emailDetails;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(emailController).build();
        emailDetails = new EmailDetails("recipient@example.com", "Subject", "Message Body");
    }

    @Test
    public void testSendMailSuccess() throws Exception {
        // Arrange
        when(emailService.sendSimpleMail(emailDetails)).thenReturn("Mail Sent Successfully...");

        // Act & Assert
        mockMvc.perform(post("/api/mails/sendMail")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(emailDetails)))
                .andExpect(status().isOk())
                .andExpect(content().string("Mail Sent Successfully..."));
    }

    @Test
    public void testSendMailFailure() throws Exception {
        // Arrange: Mock the service to throw the exception
        when(emailService.sendSimpleMail(emailDetails)).thenThrow(new MailSendingException("Error sending email"));

        // Act & Assert
        mockMvc.perform(post("/api/mails/sendMail")
                .contentType("application/json")
                .content(new ObjectMapper().writeValueAsString(emailDetails)))
                .andExpect(status().isInternalServerError())  // Expecting 500 status code
                .andExpect(content().string("Error sending email"));  // Expecting the exception message
    }

}
