package com.finance.security.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class UserResponse {
    private int userId; // or appropriate type
    private String username;  // Assuming 'users' is your user class
    private String token;
}
