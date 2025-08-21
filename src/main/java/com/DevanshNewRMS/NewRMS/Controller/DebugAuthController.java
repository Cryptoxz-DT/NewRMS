package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/debug")
@RequiredArgsConstructor
public class DebugAuthController {

    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/staff")
    public List<Staff> getAllStaff() {
        return staffRepository.findAll();
    }

    @GetMapping("/staff/{username}")
    public Optional<Staff> getStaffByUsername(@PathVariable String username) {
        return staffRepository.findByUsername(username);
    }

    @PostMapping("/encode-password")
    public Map<String, String> encodePassword(@RequestBody Map<String, String> request) {
        String rawPassword = request.get("password");
        String encodedPassword = passwordEncoder.encode(rawPassword);
        
        Map<String, String> response = new HashMap<>();
        response.put("rawPassword", rawPassword);
        response.put("encodedPassword", encodedPassword);
        return response;
    }

    @PostMapping("/verify-password")
    public Map<String, Object> verifyPassword(@RequestBody Map<String, String> request) {
        String username = request.get("username");
        String rawPassword = request.get("password");
        
        Optional<Staff> staffOpt = staffRepository.findByUsername(username);
        Map<String, Object> response = new HashMap<>();
        
        if (staffOpt.isPresent()) {
            Staff staff = staffOpt.get();
            boolean matches = passwordEncoder.matches(rawPassword, staff.getPassword());
            
            response.put("userFound", true);
            response.put("username", staff.getUsername());
            response.put("storedPasswordHash", staff.getPassword());
            response.put("providedPassword", rawPassword);
            response.put("passwordMatches", matches);
            response.put("roles", staff.getRoles());
        } else {
            response.put("userFound", false);
            response.put("message", "User not found: " + username);
        }
        
        return response;
    }

    @PostMapping("/create-test-user")
    public Staff createTestUser(@RequestBody Map<String, String> request) {
        String username = request.getOrDefault("username", "tester");
        String password = request.getOrDefault("password", "test pass");
        String name = request.getOrDefault("name", "Test User");
        String roles = request.getOrDefault("roles", "USER");

        Staff staff = Staff.builder()
                .firstName(name)
                .username(username)
                .password(passwordEncoder.encode(password))
                .roles(roles)
                .build();

        return staffRepository.save(staff);
    }
}