package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Model.RefreshToken;
import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.RefreshTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RefreshTokenService {

    private final RefreshTokenRepository refreshTokenRepository;
    private final SecurityAuditService securityAuditService;

    @Value("${app.security.jwt.refresh-expiration}")
    private long refreshTokenDurationMs;

    private static final int MAX_ACTIVE_TOKENS_PER_USER = 5;

    /**
     * Create a new refresh token for a staff member
     */
    @Transactional
    public RefreshToken createRefreshToken(Staff staff, String clientIp, String userAgent) {
        // Check if user has too many active tokens
        long activeTokenCount = refreshTokenRepository.countActiveTokensByStaff(staff);
        if (activeTokenCount >= MAX_ACTIVE_TOKENS_PER_USER) {
            // Revoke oldest tokens to make room for new one
            List<RefreshToken> activeTokens = refreshTokenRepository.findByStaffAndRevokedFalse(staff);
            activeTokens.stream()
                    .limit(activeTokenCount - MAX_ACTIVE_TOKENS_PER_USER + 1)
                    .forEach(token -> {
                        token.setRevoked(true);
                        refreshTokenRepository.save(token);
                    });
            
            log.info("Revoked old refresh tokens for user: {} due to token limit", staff.getUsername());
        }

        RefreshToken refreshToken = RefreshToken.builder()
                .staff(staff)
                .token(UUID.randomUUID().toString())
                .expiresAt(LocalDateTime.now().plusSeconds(refreshTokenDurationMs / 1000))
                .clientIp(clientIp)
                .userAgent(userAgent)
                .build();

        RefreshToken savedToken = refreshTokenRepository.save(refreshToken);
        
        securityAuditService.logSuspiciousActivity(
                staff.getUsername(), 
                clientIp, 
                "REFRESH_TOKEN_CREATED", 
                "New refresh token created"
        );
        
        log.info("Created refresh token for user: {}", staff.getUsername());
        return savedToken;
    }

    /**
     * Find refresh token by token string
     */
    public Optional<RefreshToken> findByToken(String token) {
        return refreshTokenRepository.findByToken(token);
    }

    /**
     * Verify refresh token and return it if valid
     */
    @Transactional
    public RefreshToken verifyExpiration(RefreshToken token) {
        if (token.isExpired()) {
            refreshTokenRepository.delete(token);
            securityAuditService.logSuspiciousActivity(
                    token.getStaff().getUsername(), 
                    token.getClientIp(), 
                    "EXPIRED_REFRESH_TOKEN", 
                    "Expired refresh token used"
            );
            throw new GlobalExceptionHandler.TokenRefreshException(
                    "Refresh token was expired. Please make a new signin request"
            );
        }

        if (token.getRevoked()) {
            securityAuditService.logSuspiciousActivity(
                    token.getStaff().getUsername(), 
                    token.getClientIp(), 
                    "REVOKED_REFRESH_TOKEN", 
                    "Revoked refresh token used"
            );
            throw new GlobalExceptionHandler.TokenRefreshException(
                    "Refresh token was revoked. Please make a new signin request"
            );
        }

        return token;
    }

    /**
     * Revoke a specific refresh token
     */
    @Transactional
    public void revokeToken(RefreshToken token) {
        token.setRevoked(true);
        refreshTokenRepository.save(token);
        
        securityAuditService.logSuspiciousActivity(
                token.getStaff().getUsername(), 
                token.getClientIp(), 
                "REFRESH_TOKEN_REVOKED", 
                "Refresh token manually revoked"
        );
        
        log.info("Revoked refresh token for user: {}", token.getStaff().getUsername());
    }

    /**
     * Revoke all refresh tokens for a staff member
     */
    @Transactional
    public void revokeAllTokensForStaff(Staff staff) {
        refreshTokenRepository.revokeAllByStaff(staff);
        
        securityAuditService.logSuspiciousActivity(
                staff.getUsername(), 
                "SYSTEM", 
                "ALL_REFRESH_TOKENS_REVOKED", 
                "All refresh tokens revoked for user"
        );
        
        log.info("Revoked all refresh tokens for user: {}", staff.getUsername());
    }

    /**
     * Clean up expired tokens (scheduled task)
     */
    @Scheduled(fixedRate = 3600000) // Run every hour
    @Transactional
    public void cleanupExpiredTokens() {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime cutoff = now.minusDays(7); // Delete tokens older than 7 days
        
        // First revoke expired tokens
        refreshTokenRepository.revokeExpiredTokens(now);
        
        // Then delete old expired tokens
        refreshTokenRepository.deleteExpiredTokens(cutoff);
        
        log.info("Cleaned up expired refresh tokens");
    }

    /**
     * Get active token count for a staff member
     */
    public long getActiveTokenCount(Staff staff) {
        return refreshTokenRepository.countActiveTokensByStaff(staff);
    }

    /**
     * Create a new refresh token and revoke the old one
     */
    @Transactional
    public RefreshToken rotateRefreshToken(RefreshToken oldToken, String clientIp, String userAgent) {
        // Revoke the old token
        revokeToken(oldToken);
        
        // Create a new token
        return createRefreshToken(oldToken.getStaff(), clientIp, userAgent);
    }
}