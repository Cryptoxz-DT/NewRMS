package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.DTO.SignUpRequest;
import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import com.DevanshNewRMS.NewRMS.Model.Staff;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
@Slf4j
public class StaffService {
    
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public Staff save(Staff staff){
        // Encode password before saving
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        return staffRepository.save(staff);
    }

    public Staff update(Long id, Staff staff) {
        Staff existingStaff = getById(id);
        existingStaff.setFirstName(staff.getFirstName());
        existingStaff.setLastName(staff.getLastName());
        existingStaff.setEmail(staff.getEmail());
        existingStaff.setUsername(staff.getUsername());
        existingStaff.setRoles(staff.getRoles());
        
        // Only encode password if it's being changed
        if (staff.getPassword() != null && !staff.getPassword().isEmpty()) {
            existingStaff.setPassword(passwordEncoder.encode(staff.getPassword()));
            existingStaff.setPasswordChangedAt(LocalDateTime.now());
        }
        
        return staffRepository.save(existingStaff);
    }

    public List<Staff> getAll(){
        return staffRepository.findAll();
    }

    public Staff getById(Long id){
        return staffRepository.findById(id).orElseThrow(
                () -> new GlobalExceptionHandler.ResourceNotFoundException("Staff not Found with id:" + id)
        );
    }

    public List<Staff> getStaffName(String name){
        return staffRepository.findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(name, name);
    }

    public void delete(long id){
        if (!staffRepository.existsById(id)) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("Staff not found with id: " + id);
        }
        staffRepository.deleteById(id);
    }


    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }

    @Transactional
    public Staff registerUser(SignUpRequest signUpRequest) {
        log.info("Attempting to register user with username: {} and email: {}", 
                signUpRequest.getUsername(), signUpRequest.getEmail());
        
        // Validate password confirmation
        validatePasswordConfirmation(signUpRequest.getPassword(), signUpRequest.getConfirmPassword());
        
        // Additional password strength validation
        validatePasswordStrength(signUpRequest.getPassword());
        
        // Check if username already exists
        if (staffRepository.findByUsername(signUpRequest.getUsername()).isPresent()) {
            log.warn("Registration attempt failed - username already exists: {}", signUpRequest.getUsername());
            throw new GlobalExceptionHandler.UserAlreadyExistsException("Username is already taken");
        }
        
        // Check if email already exists
        if (staffRepository.findByEmail(signUpRequest.getEmail()).isPresent()) {
            log.warn("Registration attempt failed - email already exists: {}", signUpRequest.getEmail());
            throw new GlobalExceptionHandler.UserAlreadyExistsException("Email is already registered");
        }
        
        // Sanitize and validate input
        String sanitizedFirstName = sanitizeInput(signUpRequest.getFirstName());
        String sanitizedLastName = sanitizeInput(signUpRequest.getLastName());
        String sanitizedEmail = sanitizeInput(signUpRequest.getEmail().toLowerCase());
        String sanitizedUsername = sanitizeInput(signUpRequest.getUsername());
        String sanitizedRoles = sanitizeInput(signUpRequest.getRoles());
        
        // Validate required fields for new registrations
        if (sanitizedFirstName == null || sanitizedFirstName.trim().isEmpty()) {
            throw new GlobalExceptionHandler.BusinessException("First name is required");
        }
        if (sanitizedLastName == null || sanitizedLastName.trim().isEmpty()) {
            throw new GlobalExceptionHandler.BusinessException("Last name is required");
        }
        if (sanitizedEmail == null || sanitizedEmail.trim().isEmpty()) {
            throw new GlobalExceptionHandler.BusinessException("Email is required");
        }
        
        // Create new staff member
        Staff staff = Staff.builder()
                .firstName(sanitizedFirstName)
                .lastName(sanitizedLastName)
                .email(sanitizedEmail)
                .username(sanitizedUsername)
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .roles(sanitizedRoles)
                .accountLocked(false)
                .failedLoginAttempts(0)
                .passwordChangedAt(LocalDateTime.now())
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        
        try {
            Staff savedStaff = staffRepository.save(staff);
            log.info("User registered successfully with ID: {}, username: {}, and email: {}", 
                    savedStaff.getId(), savedStaff.getUsername(), savedStaff.getEmail());
            return savedStaff;
        } catch (Exception e) {
            log.error("Failed to register user with username: {} and email: {}", 
                    signUpRequest.getUsername(), signUpRequest.getEmail(), e);
            throw new GlobalExceptionHandler.BusinessException("Registration failed due to system error");
        }
    }
    
    private void validatePasswordConfirmation(String password, String confirmPassword) {
        if (!password.equals(confirmPassword)) {
            throw new GlobalExceptionHandler.PasswordMismatchException("Password and confirmation password do not match");
        }
    }
    
    private void validatePasswordStrength(String password) {
        // Check minimum length
        if (password.length() < 8) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must be at least 8 characters long");
        }
        
        // Check maximum length
        if (password.length() > 128) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must not exceed 128 characters");
        }
        
        // Check for at least one lowercase letter
        if (!Pattern.compile("[a-z]").matcher(password).find()) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must contain at least one lowercase letter");
        }
        
        // Check for at least one uppercase letter
        if (!Pattern.compile("[A-Z]").matcher(password).find()) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must contain at least one uppercase letter");
        }
        
        // Check for at least one digit
        if (!Pattern.compile("\\d").matcher(password).find()) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must contain at least one digit");
        }
        
        // Check for at least one special character
        if (!Pattern.compile("[@$!%*?&]").matcher(password).find()) {
            throw new GlobalExceptionHandler.WeakPasswordException("Password must contain at least one special character (@$!%*?&)");
        }
        
        // Check for common weak passwords
        String[] commonPasswords = {"password", "123456", "password123", "admin", "qwerty", "letmein"};
        String lowerPassword = password.toLowerCase();
        for (String common : commonPasswords) {
            if (lowerPassword.contains(common)) {
                throw new GlobalExceptionHandler.WeakPasswordException("Password contains common patterns and is not secure");
            }
        }
    }
    
    private String sanitizeInput(String input) {
        if (input == null) {
            return null;
        }
        // Remove potential XSS characters and trim whitespace
        return input.trim()
                .replaceAll("<", "&lt;")
                .replaceAll(">", "&gt;")
                .replaceAll("\"", "&quot;")
                .replaceAll("'", "&#x27;")
                .replaceAll("/", "&#x2F;");
    }
    
    public boolean isUsernameAvailable(String username) {
        return staffRepository.findByUsername(username).isEmpty();
    }
}