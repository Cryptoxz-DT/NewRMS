package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.DTO.*;
import com.DevanshNewRMS.NewRMS.Model.RefreshToken;
import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import com.DevanshNewRMS.NewRMS.Service.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
@Slf4j
public class AuthController {

    private final StaffRepository staffRepository;
    private final StaffService staffService;
    private final AuthenticationService authenticationService;
    private final RateLimitingService rateLimitingService;
    private final SecurityAuditService securityAuditService;
    private final JwtService jwtService;
    private final RefreshTokenService refreshTokenService;

    @GetMapping("/user")
    public ResponseEntity<Map<String, Object>> getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        
        if (authentication == null || !authentication.isAuthenticated()) {
            return ResponseEntity.status(401).build();
        }

        String username = authentication.getName();
        Staff staff = staffRepository.findByUsername(username)
                .orElse(null);

        Map<String, Object> response = new HashMap<>();
        response.put("username", username);
        response.put("roles", authentication.getAuthorities());
        
        if (staff != null) {
            response.put("name", staff.getName());
            response.put("id", staff.getId());
        }

        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@Valid @RequestBody LoginRequest loginRequest, 
                                  HttpServletRequest request) {
        
        String clientIp = getClientIpAddress(request);
        log.info("Login attempt from IP: {} for user: {}", clientIp, loginRequest.getUsernameOrEmail());
        
        // Rate limiting check
        if (!rateLimitingService.isAllowed(clientIp)) {
            long cooldownMinutes = rateLimitingService.getRemainingCooldown(clientIp);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Too Many Requests");
            errorResponse.put("message", "Too many login attempts. Please try again in " + cooldownMinutes + " minutes.");
            errorResponse.put("retryAfterMinutes", cooldownMinutes);
            
            log.warn("Rate limit exceeded for IP: {} during login attempt", clientIp);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
        }
        
        try {
            // Record the attempt for rate limiting
            rateLimitingService.recordAttempt(clientIp);
            
            // Authenticate user and get JWT tokens
            AuthenticationService.AuthenticationResult authResult = authenticationService.authenticate(
                loginRequest.getUsernameOrEmail(), 
                loginRequest.getPassword(),
                clientIp,
                request.getHeader("User-Agent")
            );
            
            Staff staff = authResult.getStaff();
            
            // Create success response with JWT tokens
            LoginResponse response = LoginResponse.success(
                staff.getId(),
                staff.getFirstName(),
                staff.getLastName(),
                staff.getEmail(),
                staff.getUsername(),
                java.util.Arrays.asList(staff.getRoles().split(",")),
                staff.getLastLoginAttempt(),
                authResult.getAccessToken(),
                authResult.getRefreshToken(),
                authResult.getExpiresIn()
            );
            
            securityAuditService.logLoginAttempt(staff.getUsername(), clientIp, true, "Login successful");
            log.info("User successfully logged in with username: {}", staff.getUsername());
            return ResponseEntity.ok(response);
            
        } catch (org.springframework.security.authentication.DisabledException e) {
            securityAuditService.logLoginAttempt(loginRequest.getUsernameOrEmail(), clientIp, false, "Account locked");
            log.warn("Login failed - account locked for user: {}", loginRequest.getUsernameOrEmail());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Account Locked");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            
        } catch (org.springframework.security.authentication.BadCredentialsException e) {
            securityAuditService.logLoginAttempt(loginRequest.getUsernameOrEmail(), clientIp, false, "Invalid credentials");
            log.warn("Login failed - invalid credentials for user: {}", loginRequest.getUsernameOrEmail());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Invalid Credentials");
            errorResponse.put("message", "Invalid username/email or password");
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(errorResponse);
            
        } catch (Exception e) {
            log.error("Unexpected error during login for user: {}", loginRequest.getUsernameOrEmail(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", "Login failed due to system error");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/refresh")
    public ResponseEntity<?> refreshToken(@Valid @RequestBody TokenRefreshRequest request,
                                         HttpServletRequest httpRequest) {
        
        String clientIp = getClientIpAddress(httpRequest);
        String userAgent = httpRequest.getHeader("User-Agent");
        
        log.info("Token refresh attempt from IP: {}", clientIp);
        
        try {
            // Find and verify refresh token
            RefreshToken refreshToken = refreshTokenService.findByToken(request.getRefreshToken())
                    .orElseThrow(() -> new GlobalExceptionHandler.TokenRefreshException("Refresh token not found"));
            
            refreshToken = refreshTokenService.verifyExpiration(refreshToken);
            
            // Generate new access token
            Staff staff = refreshToken.getStaff();
            org.springframework.security.core.userdetails.UserDetails userDetails = 
                org.springframework.security.core.userdetails.User.builder()
                    .username(staff.getUsername())
                    .password(staff.getPassword())
                    .authorities(staff.getRoles().split(","))
                    .build();
            
            String newAccessToken = jwtService.generateToken(userDetails);
            
            // Rotate refresh token for better security
            RefreshToken newRefreshToken = refreshTokenService.rotateRefreshToken(refreshToken, clientIp, userAgent);
            
            TokenRefreshResponse response = TokenRefreshResponse.success(
                newAccessToken,
                newRefreshToken.getToken(),
                jwtService.getExpirationTime()
            );
            
            securityAuditService.logSuspiciousActivity(
                    staff.getUsername(), 
                    clientIp, 
                    "TOKEN_REFRESHED", 
                    "Access token refreshed successfully"
            );
            
            log.info("Token refreshed successfully for user: {}", staff.getUsername());
            return ResponseEntity.ok(response);
            
        } catch (GlobalExceptionHandler.TokenRefreshException e) {
            log.warn("Token refresh failed from IP: {} - {}", clientIp, e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Token Refresh Failed");
            errorResponse.put("message", e.getMessage());
            
            return ResponseEntity.status(HttpStatus.FORBIDDEN).body(errorResponse);
            
        } catch (Exception e) {
            log.error("Unexpected error during token refresh from IP: {}", clientIp, e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", "Token refresh failed due to system error");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(HttpServletRequest request) {
        try {
            // Extract JWT token from Authorization header
            String authHeader = request.getHeader("Authorization");
            if (authHeader != null && authHeader.startsWith("Bearer ")) {
                String jwt = authHeader.substring(7);
                String username = jwtService.extractUsername(jwt);
                
                // Find staff and revoke all refresh tokens
                Optional<Staff> staffOptional = staffRepository.findByUsername(username);
                if (staffOptional.isPresent()) {
                    refreshTokenService.revokeAllTokensForStaff(staffOptional.get());
                    
                    securityAuditService.logSuspiciousActivity(
                            username, 
                            getClientIpAddress(request), 
                            "USER_LOGOUT", 
                            "User logged out successfully"
                    );
                    
                    log.info("User logged out successfully: {}", username);
                }
            }
        } catch (Exception e) {
            log.warn("Error during logout process: {}", e.getMessage());
        }
        
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }

    @PostMapping("/signup")
    public ResponseEntity<?> signUp(@Valid @RequestBody SignUpRequest signUpRequest, 
                                   HttpServletRequest request) {
        
        String clientIp = getClientIpAddress(request);
        log.info("Sign-up attempt from IP: {} for username: {}", clientIp, signUpRequest.getUsername());
        
        // Rate limiting check
        if (!rateLimitingService.isAllowed(clientIp)) {
            long cooldownMinutes = rateLimitingService.getRemainingCooldown(clientIp);
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Too Many Requests");
            errorResponse.put("message", "Too many sign-up attempts. Please try again in " + cooldownMinutes + " minutes.");
            errorResponse.put("retryAfterMinutes", cooldownMinutes);
            
            log.warn("Rate limit exceeded for IP: {} during sign-up attempt", clientIp);
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS).body(errorResponse);
        }
        
        try {
            // Record the attempt for rate limiting
            rateLimitingService.recordAttempt(clientIp);
            
            // Register the user
            Staff registeredStaff = staffService.registerUser(signUpRequest);
            
            // Create success response (excluding sensitive information)
            SignUpResponse response = SignUpResponse.success(
                registeredStaff.getId(),
                registeredStaff.getFirstName(),
                registeredStaff.getLastName(),
                registeredStaff.getEmail(),
                registeredStaff.getUsername(),
                registeredStaff.getRoles(),
                registeredStaff.getCreatedAt()
            );
            
            securityAuditService.logSignupAttempt(registeredStaff.getUsername(), registeredStaff.getEmail(), 
                    clientIp, true, "User registration successful");
            log.info("User successfully registered with ID: {} and username: {}", 
                    registeredStaff.getId(), registeredStaff.getUsername());
            
            return ResponseEntity.status(HttpStatus.CREATED).body(response);
            
        } catch (GlobalExceptionHandler.UserAlreadyExistsException | 
                 GlobalExceptionHandler.PasswordMismatchException | 
                 GlobalExceptionHandler.WeakPasswordException e) {
            
            securityAuditService.logSignupAttempt(signUpRequest.getUsername(), signUpRequest.getEmail(), 
                    clientIp, false, e.getMessage());
            log.warn("Sign-up validation failed for username: {} - {}", signUpRequest.getUsername(), e.getMessage());
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", e.getClass().getSimpleName().replace("Exception", ""));
            errorResponse.put("message", e.getMessage());
            
            HttpStatus status = e instanceof GlobalExceptionHandler.UserAlreadyExistsException ? 
                               HttpStatus.CONFLICT : HttpStatus.BAD_REQUEST;
            
            return ResponseEntity.status(status).body(errorResponse);
            
        } catch (Exception e) {
            log.error("Unexpected error during sign-up for username: {}", signUpRequest.getUsername(), e);
            
            Map<String, Object> errorResponse = new HashMap<>();
            errorResponse.put("error", "Internal Server Error");
            errorResponse.put("message", "Registration failed due to system error");
            
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(errorResponse);
        }
    }

    @GetMapping("/check-username")
    public ResponseEntity<Map<String, Boolean>> checkUsernameAvailability(@RequestParam String username) {
        boolean isAvailable = staffService.isUsernameAvailable(username);
        Map<String, Boolean> response = new HashMap<>();
        response.put("available", isAvailable);
        
        log.debug("Username availability check for '{}': {}", username, isAvailable);
        return ResponseEntity.ok(response);
    }
    
    private String getClientIpAddress(HttpServletRequest request) {
        String xForwardedFor = request.getHeader("X-Forwarded-For");
        if (xForwardedFor != null && !xForwardedFor.isEmpty()) {
            return xForwardedFor.split(",")[0].trim();
        }
        
        String xRealIp = request.getHeader("X-Real-IP");
        if (xRealIp != null && !xRealIp.isEmpty()) {
            return xRealIp;
        }
        
        return request.getRemoteAddr();
    }
}