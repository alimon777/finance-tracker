package com.finance.identity.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.finance.identity.dto.UserResponse;
import com.finance.identity.entity.User;
import com.finance.identity.repository.UserCredentialRepository;

@Service
public class AuthService {

    @Autowired
    private UserCredentialRepository repository;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtService jwtService;

    public User saveUser(User newUser) {
        newUser.setPassword(passwordEncoder.encode(newUser.getPassword()));
        return repository.save(newUser);
    }

    public String generateToken(String username) {
        return jwtService.generateToken(username);
    }

    public void validateToken(String token) {
        jwtService.validateToken(token);
    }

    public UserResponse getLoginnedUserDetails(String username) {
    	User user = repository.findByUsername(username).get();
    	UserResponse userDetails = new UserResponse();
    	userDetails.setUserId(user.getId());
    	userDetails.setUsername(user.getUsername());
    	userDetails.setEmail(user.getEmail());
    	return userDetails;
    }
}
