package com.DevanshNewRMS.NewRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class SignUpResponse {
    
    private Long id;
    private String name;
    private String username;
    private String roles;
    private LocalDateTime createdAt;
    private String message;
    
    public static SignUpResponse success(Long id, String name, String username, String roles, LocalDateTime createdAt) {
        return SignUpResponse.builder()
                .id(id)
                .name(name)
                .username(username)
                .roles(roles)
                .createdAt(createdAt)
                .message("User registered successfully")
                .build();
    }
}