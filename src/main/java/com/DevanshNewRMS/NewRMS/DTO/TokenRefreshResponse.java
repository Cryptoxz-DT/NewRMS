package com.DevanshNewRMS.NewRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TokenRefreshResponse {
    
    private String accessToken;
    private String refreshToken;
    private String tokenType = "Bearer";
    private Long expiresIn;
    private String message;
    
    public static TokenRefreshResponse success(String accessToken, String refreshToken, Long expiresIn) {
        return TokenRefreshResponse.builder()
                .accessToken(accessToken)
                .refreshToken(refreshToken)
                .tokenType("Bearer")
                .expiresIn(expiresIn)
                .message("Token refreshed successfully")
                .build();
    }
}