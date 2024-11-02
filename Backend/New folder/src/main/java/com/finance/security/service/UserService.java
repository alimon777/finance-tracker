package com.finance.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import com.finance.security.model.users;
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
	
	public users register(users user) {
		if(repo.findByUsername(user.getUsername())!=null)
			return null;
		user.setPassword(encoder.encode(user.getPassword()));
		return repo.save(user);
	}

	public String verify(users user) {
		Authentication authentication =
				authManager
				.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));
		if(authentication.isAuthenticated())
			return service.generateToken(user.getUsername());
		return "false";
	}

}
