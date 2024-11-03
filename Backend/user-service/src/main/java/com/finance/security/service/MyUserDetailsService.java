package com.finance.security.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.finance.security.model.UserPrincipal;
import com.finance.security.model.users;
import com.finance.security.repo.CredRepo;

@Service
public class MyUserDetailsService implements UserDetailsService{

	
	@Autowired
	private CredRepo crepo;
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		users user = crepo.findByUsername(username);
		if(user==null) {
			System.out.println("user not founnd");
			throw new UsernameNotFoundException("user not found");
		}
			
		return new UserPrincipal(user);
	}

}
