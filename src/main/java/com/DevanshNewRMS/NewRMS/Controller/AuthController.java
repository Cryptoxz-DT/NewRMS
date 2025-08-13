package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class AuthController {

    private final StaffRepository staffRepository;

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

    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout() {
        Map<String, String> response = new HashMap<>();
        response.put("message", "Logged out successfully");
        return ResponseEntity.ok(response);
    }
}