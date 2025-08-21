package com.DevanshNewRMS.NewRMS.Config;

import com.DevanshNewRMS.NewRMS.Service.JwtService;
import com.DevanshNewRMS.NewRMS.Service.SecurityAuditService;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;
    private final SecurityAuditService securityAuditService;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {

        // Skip JWT validation for public endpoints
        if (isPublicEndpoint(request.getServletPath())) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authHeader = request.getHeader("Authorization");
        final String jwt;
        final String userEmail;

        // Check if Authorization header is present and starts with "Bearer "
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        // Extract JWT token from Authorization header
        jwt = authHeader.substring(7);
        
        try {
            // Extract username from JWT token
            userEmail = jwtService.extractUsername(jwt);

            // If username is present and user is not already authenticated
            if (userEmail != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                
                // Load user details from database
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);
                
                // Validate JWT token
                if (jwtService.isTokenValid(jwt, userDetails)) {
                    
                    // Extract authorities from token
                    List<String> authorities = jwtService.extractAuthorities(jwt);
                    List<SimpleGrantedAuthority> grantedAuthorities = authorities.stream()
                            .map(SimpleGrantedAuthority::new)
                            .toList();

                    // Create authentication token
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            grantedAuthorities
                    );
                    
                    // Set authentication details
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    
                    // Set authentication in security context
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                    
                    log.debug("JWT authentication successful for user: {}", userEmail);
                    
                } else {
                    log.warn("Invalid JWT token for user: {}", userEmail);
                    securityAuditService.logSuspiciousActivity(
                            userEmail, 
                            getClientIpAddress(request), 
                            "INVALID_JWT_TOKEN", 
                            "Invalid JWT token used"
                    );
                }
            }
            
        } catch (ExpiredJwtException e) {
            log.warn("JWT token expired: {}", e.getMessage());
            String username = e.getClaims().getSubject();
            securityAuditService.logSuspiciousActivity(
                    username != null ? username : "UNKNOWN", 
                    getClientIpAddress(request), 
                    "EXPIRED_JWT_TOKEN", 
                    "Expired JWT token used"
            );
            
            // Set error response for expired token
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Token expired\",\"message\":\"JWT token has expired\"}");
            return;
            
        } catch (JwtException | IllegalArgumentException e) {
            log.error("JWT token validation error: {}", e.getMessage());
            securityAuditService.logSuspiciousActivity(
                    "UNKNOWN", 
                    getClientIpAddress(request), 
                    "MALFORMED_JWT_TOKEN", 
                    "Malformed JWT token: " + e.getMessage()
            );
            
            // Set error response for invalid token
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.setContentType("application/json");
            response.getWriter().write("{\"error\":\"Invalid token\",\"message\":\"JWT token is invalid\"}");
            return;
        }

        // Continue with the filter chain
        filterChain.doFilter(request, response);
    }

    /**
     * Check if the endpoint is public and doesn't require authentication
     */
    private boolean isPublicEndpoint(String path) {
        return path.startsWith("/api/auth/login") ||
               path.startsWith("/api/auth/signup") ||
               path.startsWith("/api/auth/refresh") ||
               path.startsWith("/api/auth/check-username") ||
               path.startsWith("/actuator") ||
               path.startsWith("/swagger-ui") ||
               path.startsWith("/v3/api-docs") ||
               path.equals("/favicon.ico") ||
               path.equals("/error");
    }

    /**
     * Extract client IP address from request
     */
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