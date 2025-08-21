package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class AuthenticationService {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final SecurityAuditService securityAuditService;

    @Transactional
    public Authentication authenticate(String usernameOrEmail, String password, String clientIp) {
        log.info("Authentication attempt for user: {}", usernameOrEmail);
        
        // Find user by username or email
        Optional<Staff> staffOptional = staffRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        
        if (staffOptional.isEmpty()) {
            log.warn("Authentication failed - user not found: {}", usernameOrEmail);
            throw new BadCredentialsException("Invalid username/email or password");
        }
        
        Staff staff = staffOptional.get();
        
        // Check if account is locked
        if (staff.getAccountLocked()) {
            log.warn("Authentication failed - account locked: {}", usernameOrEmail);
            throw new DisabledException("Account is locked due to multiple failed login attempts");
        }
        
        try {
            // Attempt authentication
            Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(staff.getUsername(), password)
            );
            
            // Reset failed login attempts on successful authentication
            staff.resetFailedLoginAttempts();
            staffRepository.save(staff);
            
            log.info("Authentication successful for user: {}", usernameOrEmail);
            return authentication;
            
        } catch (AuthenticationException e) {
            // Increment failed login attempts
            staff.incrementFailedLoginAttempts();
            staffRepository.save(staff);
            
            log.warn("Authentication failed for user: {} - Failed attempts: {}", 
                    usernameOrEmail, staff.getFailedLoginAttempts());
            
            if (staff.getAccountLocked()) {
                securityAuditService.logAccountLockout(staff.getUsername(), clientIp, staff.getFailedLoginAttempts());
                throw new DisabledException("Account has been locked due to multiple failed login attempts");
            }
            
            throw new BadCredentialsException("Invalid username/email or password");
        }
    }
    
    @Transactional
    public void unlockAccount(String usernameOrEmail) {
        Optional<Staff> staffOptional = staffRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        
        if (staffOptional.isPresent()) {
            Staff staff = staffOptional.get();
            staff.resetFailedLoginAttempts();
            staffRepository.save(staff);
            log.info("Account unlocked for user: {}", usernameOrEmail);
        }
    }
    
    public boolean isAccountLocked(String usernameOrEmail) {
        Optional<Staff> staffOptional = staffRepository.findByUsernameOrEmail(usernameOrEmail, usernameOrEmail);
        return staffOptional.map(Staff::getAccountLocked).orElse(false);
    }
    
    @Transactional
    public void changePassword(String username, String oldPassword, String newPassword) {
        Optional<Staff> staffOptional = staffRepository.findByUsername(username);
        
        if (staffOptional.isEmpty()) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("User not found");
        }
        
        Staff staff = staffOptional.get();
        
        // Verify old password
        if (!passwordEncoder.matches(oldPassword, staff.getPassword())) {
            throw new BadCredentialsException("Current password is incorrect");
        }
        
        // Validate new password strength
        validatePasswordStrength(newPassword);
        
        // Update password
        staff.setPassword(passwordEncoder.encode(newPassword));
        staff.setPasswordChangedAt(LocalDateTime.now());
        staffRepository.save(staff);
        
        log.info("Password changed successfully for user: {}", username);
    }
    
    private void validatePasswordStrength(String password) {
        if (password.length() < 8) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must be at least 8 characters long");
        }
        
        if (password.length() > 128) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must not exceed 128 characters");
        }
        
        if (!password.matches(".*[a-z].*")) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must contain at least one lowercase letter");
        }
        
        if (!password.matches(".*[A-Z].*")) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must contain at least one uppercase letter");
        }
        
        if (!password.matches(".*\\d.*")) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must contain at least one digit");
        }
        
        if (!password.matches(".*[@$!%*?&].*")) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must contain at least one special character (@$!%*?&)");
        }
    }
}