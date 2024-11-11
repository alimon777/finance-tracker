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
import com.finance.identity.repository.UserCredentialRepository;
import com.finance.identity.service.AuthService;
import com.finance.identity.dto.UserResponse;

@RestController
@RequestMapping("/auth")
public class AuthController {
    @Autowired
    private AuthService service;

    @Autowired
    private AuthenticationManager authenticationManager;

//    @PostMapping("/register")
//    public String addNewUser(@RequestBody User user) {
//    	System.out.println("register endpoint...");
//        return service.saveUser(user);
//    }
    
    @PostMapping("/register")
	public ResponseEntity<User> register(@RequestBody User user) {
	User u = service.saveUser(user);
	if(u!=null)
		return ResponseEntity.ok(u);
	else
		return new ResponseEntity("Username already exists", HttpStatus.NOT_FOUND);
}

    @PostMapping("/token")
    public String getToken(@RequestBody AuthRequest authRequest) {
    	System.out.println("token endpoint..." + authRequest.getUsername());
        Authentication authenticate = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword()));
        if (authenticate.isAuthenticated()) {
        	System.out.println(authRequest.getUsername());
            return service.generateToken(authRequest.getUsername());
        } else {
            throw new RuntimeException("invalid access");
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
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}
}