package com.finance.identity.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.finance.identity.dto.AuthRequest;
import com.finance.identity.entity.User;
import com.finance.identity.exceptions.AuthenticationFailedException;
import com.finance.identity.service.AuthService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AuthControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AuthService authService;

    @Mock
    private AuthenticationManager authenticationManager;

    @InjectMocks
    private AuthController authController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(authController).build();
    }

    @Test
    void testRegisterUserAlreadyExists() throws Exception {
        User user = new User();
        user.setUsername("existingUser");
        when(authService.usernameExists(user.getUsername())).thenReturn(true);

        mockMvc.perform(post("/auth/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(user)))
                .andExpect(status().isBadRequest())
                .andExpect(content().string("Username already exists"));
    }

    @Test
    void testGetTokenInvalidCredentials() throws Exception {
        AuthRequest authRequest = new AuthRequest("user", "wrongPassword");
        when(authenticationManager.authenticate(any(Authentication.class)))
                .thenThrow(new AuthenticationFailedException("Invalid credentials"));

        mockMvc.perform(post("/auth/token")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(authRequest)))
                .andExpect(status().isUnauthorized())
                .andExpect(content().string("Invalid credentials"));
    }

    @Test
    void testValidateToken() throws Exception {
        String token = "validToken";
        doNothing().when(authService).validateToken(token);

        mockMvc.perform(get("/auth/validate").param("token", token))
                .andExpect(status().isOk())
                .andExpect(content().string("Token is valid"));
    }
}

