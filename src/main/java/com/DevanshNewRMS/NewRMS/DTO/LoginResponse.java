package com.DevanshNewRMS.NewRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class LoginResponse {
    
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
    private List<String> roles;
    private LocalDateTime lastLogin;
    private String message;
    
    public static LoginResponse success(Long id, String firstName, String lastName, String email, 
                                      String username, List<String> roles, LocalDateTime lastLogin) {
        return LoginResponse.builder()
                .id(id)
                .firstName(firstName)
                .lastName(lastName)
                .email(email)
                .username(username)
                .roles(roles)
                .lastLogin(lastLogin)
                .message("Login successful")
                .build();
    }
}