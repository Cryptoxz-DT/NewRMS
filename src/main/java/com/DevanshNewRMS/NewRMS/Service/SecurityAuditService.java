package com.DevanshNewRMS.NewRMS.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Service
@Slf4j
public class SecurityAuditService {

    private static final String AUDIT_LOG_FORMAT = "[SECURITY_AUDIT] {} | User: {} | IP: {} | Action: {} | Status: {} | Details: {}";
    private static final DateTimeFormatter TIMESTAMP_FORMAT = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    public void logLoginAttempt(String username, String ipAddress, boolean successful, String details) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String status = successful ? "SUCCESS" : "FAILURE";

        log.info(AUDIT_LOG_FORMAT, timestamp, username, ipAddress, "LOGIN_ATTEMPT", status, details);

        if (!successful) {
            log.warn("Failed login attempt for user: {} from IP: {} - {}", username, ipAddress, details);
        }
    }

    public void logSignupAttempt(String username, String email, String ipAddress, boolean successful, String details) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String status = successful ? "SUCCESS" : "FAILURE";
        String userInfo = username + " (" + email + ")";

        log.info(AUDIT_LOG_FORMAT, timestamp, userInfo, ipAddress, "SIGNUP_ATTEMPT", status, details);

        if (!successful) {
            log.warn("Failed signup attempt for user: {} from IP: {} - {}", userInfo, ipAddress, details);
        }
    }

    public void logAccountLockout(String username, String ipAddress, int failedAttempts) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String details = "Account locked after " + failedAttempts + " failed attempts";

        log.warn(AUDIT_LOG_FORMAT, timestamp, username, ipAddress, "ACCOUNT_LOCKOUT", "LOCKED", details);
    }

    public void logAccountUnlock(String username, String adminUser, String ipAddress) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String details = "Account unlocked by admin: " + adminUser;

        log.info(AUDIT_LOG_FORMAT, timestamp, username, ipAddress, "ACCOUNT_UNLOCK", "UNLOCKED", details);
    }

    public void logPasswordChange(String username, String ipAddress, boolean successful) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String status = successful ? "SUCCESS" : "FAILURE";
        String details = successful ? "Password changed successfully" : "Password change failed";

        log.info(AUDIT_LOG_FORMAT, timestamp, username, ipAddress, "PASSWORD_CHANGE", status, details);
    }

    public void logSuspiciousActivity(String username, String ipAddress, String activity, String details) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);

        log.warn(AUDIT_LOG_FORMAT, timestamp, username, ipAddress, "SUSPICIOUS_ACTIVITY", "DETECTED",
                activity + " - " + details);
    }

    public void logRateLimitExceeded(String ipAddress, String endpoint, int attempts) {
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMAT);
        String details = "Rate limit exceeded with " + attempts + " attempts on " + endpoint;

        log.warn(AUDIT_LOG_FORMAT, timestamp, "UNKNOWN", ipAddress, "RATE_LIMIT_EXCEEDED", "BLOCKED", details);
    }
}