package com.finance.identity.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class UserResponse {
    private Long userId; // or appropriate type
    private String username;  // Assuming 'users' is your user class
    private String token;
    private String email;
}
