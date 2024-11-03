package com.finance.security.service;

import com.finance.security.dto.UserResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.finance.security.model.User;
import com.finance.security.repo.CredRepo;
@Service
public class UserService {

	@Autowired
	private CredRepo repo;
	
	@Autowired
	AuthenticationManager authManager;
	
	@Autowired
	JWTServices service;
	
	private BCryptPasswordEncoder encoder = new BCryptPasswordEncoder(12);
	
	public User register(User user) {
		if(repo.findByUsername(user.getUsername())!=null)
			return null;
		user.setPassword(encoder.encode(user.getPassword()));
		return repo.save(user);
	}

	public UserResponse verify(User user) {
		Authentication authentication =
				authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

		if (authentication.isAuthenticated()) {
			String username=user.getUsername();
			System.out.println(username+" verified");
			String token = service.generateToken(username);
			return new UserResponse(repo.findByUsername(username).getId(), username, token); // Assuming user has a getId() method
		}

		return null; // or throw an exception, or return an error response object
	}


}
