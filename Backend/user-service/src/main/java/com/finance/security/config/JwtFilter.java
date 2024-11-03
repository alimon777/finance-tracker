package com.finance.security.config;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.finance.security.service.JWTServices;
import com.finance.security.service.MyUserDetailsService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
@Component
public class JwtFilter extends OncePerRequestFilter{

	@Autowired
	private JWTServices jwtservice;
	@Autowired
	private ApplicationContext context;
	
	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
			throws ServletException, IOException {
		String authHeader  = request.getHeader("Authorization");
		String token = null;
		String username = null;
		if(authHeader !=null && authHeader.startsWith("Bearer ")) {
			token = authHeader.substring(7);
			username = jwtservice.extractUserName(token);
		}
		
		
		if(username!=null && SecurityContextHolder.getContext().getAuthentication()==null) {
			UserDetails userdetails = context.getBean(MyUserDetailsService.class).loadUserByUsername(username);
			if(jwtservice.validateToken(token,userdetails)) {
				UsernamePasswordAuthenticationToken authtoken = new UsernamePasswordAuthenticationToken(userdetails, null,userdetails.getAuthorities());
				authtoken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
				SecurityContextHolder.getContext().setAuthentication(authtoken);
			}
		}
		filterChain.doFilter(request, response);
	}

}
