package com.DevanshNewRMS.NewRMS.Service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class RateLimitingService {
    
    private final ConcurrentMap<String, AttemptInfo> signUpAttempts = new ConcurrentHashMap<>();
    private static final int MAX_ATTEMPTS = 5;
    private static final int WINDOW_MINUTES = 15;
    
    public boolean isAllowed(String clientIp) {
        String key = "signup:" + clientIp;
        AttemptInfo attemptInfo = signUpAttempts.get(key);
        
        LocalDateTime now = LocalDateTime.now();
        
        if (attemptInfo == null) {
            signUpAttempts.put(key, new AttemptInfo(1, now));
            return true;
        }
        
        // Reset if window has passed
        if (attemptInfo.getFirstAttempt().plusMinutes(WINDOW_MINUTES).isBefore(now)) {
            signUpAttempts.put(key, new AttemptInfo(1, now));
            return true;
        }
        
        // Check if limit exceeded
        if (attemptInfo.getCount() >= MAX_ATTEMPTS) {
            log.warn("Rate limit exceeded for IP: {} - {} attempts in {} minutes", 
                    clientIp, attemptInfo.getCount(), WINDOW_MINUTES);
            return false;
        }
        
        // Increment counter
        attemptInfo.increment();
        return true;
    }
    
    public void recordAttempt(String clientIp) {
        String key = "signup:" + clientIp;
        AttemptInfo attemptInfo = signUpAttempts.get(key);
        
        if (attemptInfo != null) {
            attemptInfo.increment();
        }
    }
    
    public long getRemainingCooldown(String clientIp) {
        String key = "signup:" + clientIp;
        AttemptInfo attemptInfo = signUpAttempts.get(key);
        
        if (attemptInfo == null || attemptInfo.getCount() < MAX_ATTEMPTS) {
            return 0;
        }
        
        LocalDateTime resetTime = attemptInfo.getFirstAttempt().plusMinutes(WINDOW_MINUTES);
        LocalDateTime now = LocalDateTime.now();
        
        if (resetTime.isAfter(now)) {
            return java.time.Duration.between(now, resetTime).toMinutes();
        }
        
        return 0;
    }
    
    private static class AttemptInfo {
        private int count;
        private final LocalDateTime firstAttempt;
        
        public AttemptInfo(int count, LocalDateTime firstAttempt) {
            this.count = count;
            this.firstAttempt = firstAttempt;
        }
        
        public void increment() {
            this.count++;
        }
        
        public int getCount() {
            return count;
        }
        
        public LocalDateTime getFirstAttempt() {
            return firstAttempt;
        }
    }
}