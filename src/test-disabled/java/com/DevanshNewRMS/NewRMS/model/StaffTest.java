package com.DevanshNewRMS.NewRMS.model;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.time.LocalDateTime;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Staff Model Tests")
class StaffTest {

    private Validator validator;
    private Staff validStaff;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        validStaff = Staff.builder()
                .name("John Doe")
                .username("johndoe")
                .password("password123")
                .roles("ADMIN")
                .build();
    }

    @Test
    @DisplayName("Should create valid staff with all required fields")
    void shouldCreateValidStaff() {
        Set<ConstraintViolation<Staff>> violations = validator.validate(validStaff);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when name is blank")
    void shouldFailValidationWhenNameIsBlank() {
        validStaff.setName("");
        Set<ConstraintViolation<Staff>> violations = validator.validate(validStaff);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Name is required")));
    }

    @Test
    @DisplayName("Should fail validation when name is too short")
    void shouldFailValidationWhenNameIsTooShort() {
        validStaff.setName("A");
        Set<ConstraintViolation<Staff>> violations = validator.validate(validStaff);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Name must be between 2 and 100 characters")));
    }

    @Test
    @DisplayName("Should fail validation when name is too long")
    void shouldFailValidationWhenNameIsTooLong() {
        validStaff.setName("A".repeat(101));
        Set<ConstraintViolation<Staff>> violations = validator.validate(validStaff);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Name must be between 2 and 100 characters")));
    }

    @Test
    @DisplayName("Should fail validation when username is blank")
    void shouldFailValidationWhenUsernameIsBlank() {
        validStaff.setUsername("");
        Set<ConstraintViolation<Staff>> violations = validator.validate(validStaff);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Username is required")));
    }

    @Test
    @DisplayName("Should fail validation when username contains invalid characters")
    void shouldFailValidationWhenUsernameContainsInvalidCharacters() {
        validStaff.setUsername("john@doe");
        Set<ConstraintViolation<Staff>> violations = validator.validate(validStaff);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Username can only contain letters, numbers, and underscores")));
    }

    @Test
    @DisplayName("Should fail validation when password is blank")
    void shouldFailValidationWhenPasswordIsBlank() {
        validStaff.setPassword("");
        Set<ConstraintViolation<Staff>> violations = validator.validate(validStaff);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Password is required")));
    }

    @Test
    @DisplayName("Should fail validation when roles is invalid")
    void shouldFailValidationWhenRolesIsInvalid() {
        validStaff.setRoles("INVALID_ROLE");
        Set<ConstraintViolation<Staff>> violations = validator.validate(validStaff);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Invalid role format")));
    }

    @Test
    @DisplayName("Should accept valid multiple roles")
    void shouldAcceptValidMultipleRoles() {
        validStaff.setRoles("ADMIN,MANAGER");
        Set<ConstraintViolation<Staff>> violations = validator.validate(validStaff);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should test getRole and setRole methods")
    void shouldTestGetRoleAndSetRoleMethods() {
        validStaff.setRole("WAITER");
        assertEquals("WAITER", validStaff.getRole());
        assertEquals("WAITER", validStaff.getRoles());
    }

    @Test
    @DisplayName("Should set timestamps correctly")
    void shouldSetTimestampsCorrectly() {
        LocalDateTime before = LocalDateTime.now();
        Staff staff = new Staff();
        LocalDateTime after = LocalDateTime.now();
        
        assertNotNull(staff.getCreatedAt());
        assertNotNull(staff.getUpdatedAt());
        assertTrue(staff.getCreatedAt().isAfter(before.minusSeconds(1)));
        assertTrue(staff.getCreatedAt().isBefore(after.plusSeconds(1)));
    }

    @Test
    @DisplayName("Should update timestamp on preUpdate")
    void shouldUpdateTimestampOnPreUpdate() {
        LocalDateTime originalUpdatedAt = validStaff.getUpdatedAt();
        
        // Simulate some delay
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        validStaff.preUpdate();
        assertTrue(validStaff.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    @DisplayName("Should test builder pattern")
    void shouldTestBuilderPattern() {
        Staff staff = Staff.builder()
                .name("Jane Doe")
                .username("janedoe")
                .password("password456")
                .roles("MANAGER")
                .build();
        
        assertEquals("Jane Doe", staff.getName());
        assertEquals("janedoe", staff.getUsername());
        assertEquals("password456", staff.getPassword());
        assertEquals("MANAGER", staff.getRoles());
        assertNotNull(staff.getCreatedAt());
        assertNotNull(staff.getUpdatedAt());
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        Staff staff1 = Staff.builder()
                .id(1L)
                .name("John Doe")
                .username("johndoe")
                .password("password123")
                .roles("ADMIN")
                .build();

        Staff staff2 = Staff.builder()
                .id(1L)
                .name("John Doe")
                .username("johndoe")
                .password("password123")
                .roles("ADMIN")
                .build();

        Staff staff3 = Staff.builder()
                .id(2L)
                .name("Jane Doe")
                .username("janedoe")
                .password("password456")
                .roles("MANAGER")
                .build();

        assertEquals(staff1, staff2);
        assertNotEquals(staff1, staff3);
        assertEquals(staff1.hashCode(), staff2.hashCode());
        assertNotEquals(staff1.hashCode(), staff3.hashCode());
    }
}