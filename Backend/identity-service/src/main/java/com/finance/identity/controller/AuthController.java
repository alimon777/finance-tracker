package com.finance.identity.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import com.finance.identity.dto.AuthRequest;
import com.finance.identity.entity.User;
import com.finance.identity.service.AuthService;
import com.finance.identity.dto.UserResponse;
import com.finance.identity.exceptions.UserAlreadyExistsException;
import com.finance.identity.exceptions.AuthenticationFailedException;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

    @PostMapping("/register")
    public ResponseEntity<User> register(@RequestBody User user) {
        if (service.usernameExists(user.getUsername())) {
            throw new UserAlreadyExistsException("Username already exists");
        }
        User u = service.saveUser(user);
        return ResponseEntity.ok(u);
    }

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            return service.generateToken(authRequest.getUsername());
        } else {
            throw new AuthenticationFailedException("Invalid credentials");
        }
    }

    @GetMapping("/validate")
    public String validateToken(@RequestParam("token") String token) {
        service.validateToken(token);
        return "Token is valid";
    }

    @PostMapping("/login")
    public ResponseEntity<UserResponse> login(@RequestBody AuthRequest authRequest) {
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
            UserResponse userResponse = service.getLoginnedUserDetails(authRequest.getUsername());
            userResponse.setToken(service.generateToken(authRequest.getUsername()));
            return ResponseEntity.ok(userResponse);
        } else {
            throw new AuthenticationFailedException("Invalid credentials");
        }
    }
}
