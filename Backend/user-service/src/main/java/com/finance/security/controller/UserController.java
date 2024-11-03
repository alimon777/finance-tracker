package com.finance.security.controller;

import com.finance.security.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.finance.security.model.User;
import com.finance.security.service.UserService;

@CrossOrigin
@RestController
public class UserController {
	
	@Autowired
	private UserService service;
	@PostMapping("/register")
		public ResponseEntity<User> register(@RequestBody User user) {
		User u = service.register(user);
		if(u!=null)
			return ResponseEntity.ok(u);
		else
			return new ResponseEntity("Username already exists", HttpStatus.NOT_FOUND);
	}
	@PostMapping("/login")
	public ResponseEntity<UserResponse> login(@RequestBody User user) {
		System.out.print(user);
		UserResponse userResponse = service.verify(user);
		if (userResponse != null) {
			return ResponseEntity.ok(userResponse);
		} else {
			return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(null);
		}
	}

}
