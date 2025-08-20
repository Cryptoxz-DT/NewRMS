package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.DTO.SignUpRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/test")
@RequiredArgsConstructor
@CrossOrigin(origins = "http://localhost:3000")
public class SignUpTestController {

    @GetMapping("/signup-form")
    public ResponseEntity<Map<String, Object>> getSignUpFormStructure() {
        Map<String, Object> formStructure = new HashMap<>();

        // Field definitions based on Staff.java and SignUpRequest.java
        Map<String, Object> fields = new HashMap<>();

        fields.put("name", Map.of(
                "type", "text",
                "required", true,
                "minLength", 2,
                "maxLength", 100,
                "pattern", "^[a-zA-Z\\s'-]+$",
                "description", "Full name (letters, spaces, hyphens, apostrophes only)"));

        fields.put("username", Map.of(
                "type", "text",
                "required", true,
                "minLength", 3,
                "maxLength", 50,
                "pattern", "^[a-zA-Z0-9_]+$",
                "description", "Username (letters, numbers, underscores only)"));

        fields.put("password", Map.of(
                "type", "password",
                "required", true,
                "minLength", 8,
                "maxLength", 128,
                "pattern", "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d)(?=.*[@$!%*?&])[A-Za-z\\d@$!%*?&]+$",
                "description", "Password must contain uppercase, lowercase, digit, and special character"));

        fields.put("confirmPassword", Map.of(
                "type", "password",
                "required", true,
                "description", "Must match the password field"));

        fields.put("roles", Map.of(
                "type", "select",
                "required", true,
                "options", new String[] { "ADMIN", "MANAGER", "WAITER", "CHEF", "CASHIER" },
                "description", "Staff role (can be comma-separated for multiple roles)"));

        formStructure.put("fields", fields);
        formStructure.put("endpoint", "/api/auth/signup");
        formStructure.put("method", "POST");
        formStructure.put("rateLimiting", Map.of(
                "maxAttempts", 5,
                "windowMinutes", 15,
                "description", "Maximum 5 attempts per 15 minutes per IP"));

        return ResponseEntity.ok(formStructure);
    }

    @PostMapping("/validate-signup")
    public ResponseEntity<Map<String, Object>> validateSignUpData(@RequestBody SignUpRequest signUpRequest) {
        Map<String, Object> validation = new HashMap<>();
        Map<String, String> errors = new HashMap<>();

        // This endpoint just validates without saving - useful for frontend validation
        if (signUpRequest.getName() == null || signUpRequest.getName().trim().isEmpty()) {
            errors.put("name", "Name is required");
        }

        if (signUpRequest.getUsername() == null || signUpRequest.getUsername().trim().isEmpty()) {
            errors.put("username", "Username is required");
        }

        if (signUpRequest.getPassword() == null || signUpRequest.getPassword().isEmpty()) {
            errors.put("password", "Password is required");
        }

        if (!signUpRequest.getPassword().equals(signUpRequest.getConfirmPassword())) {
            errors.put("confirmPassword", "Passwords do not match");
        }

        validation.put("valid", errors.isEmpty());
        validation.put("errors", errors);

        return ResponseEntity.ok(validation);
    }
}