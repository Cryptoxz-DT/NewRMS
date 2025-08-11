package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/debug")
public class DebugController {

    @Autowired
    private StaffRepository staffRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @PostMapping("/create-admin")
    public ResponseEntity<String> createAdmin() {
        try {

            if (staffRepository.findByUsername("admin").isPresent()) {
                return ResponseEntity.ok("Admin already exists");
            }

            Staff admin = Staff.builder()
                    .name("Test Admin")
                    .username("admin")
                    .password(passwordEncoder.encode("admin123"))
                    .roles("ADMIN")
                    .build();

            staffRepository.save(admin);
            return ResponseEntity.ok("Admin created successfully");
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    @GetMapping("/user/{username}")
    public ResponseEntity<String> checkUser(@PathVariable String username) {
        try {
            Optional<Staff> staff = staffRepository.findByUsername(username);
            return staff.map(value -> ResponseEntity.ok("User found: " + value.getName() +
                    ", Roles: " + value.getRoles())).orElseGet(() -> ResponseEntity.ok("User not found"));
        } catch (Exception e) {
            return ResponseEntity.ok("Error: " + e.getMessage());
        }
    }

    // Simple test endpoint
    @GetMapping("/test")
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Debug controller is working!");
    }
}