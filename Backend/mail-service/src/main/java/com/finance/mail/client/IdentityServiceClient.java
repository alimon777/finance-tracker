package com.finance.mail.client;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import com.finance.mail.dto.UserResponse;

@FeignClient(name = "IDENTITY-SERVICE")
public interface IdentityServiceClient {
	
	@GetMapping("/userDetails/{userId}")
    ResponseEntity<UserResponse> fetchUserDetails(@PathVariable Long userId);
}

