package com.DevanshNewRMS.NewRMS.model;

import com.DevanshNewRMS.NewRMS.Model.Customer;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.Validation;
import jakarta.validation.Validator;
import jakarta.validation.ValidatorFactory;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

@DisplayName("Customer Model Tests")
class CustomerTest {

    private Validator validator;
    private Customer validCustomer;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        validCustomer = Customer.builder()
                .name("John Smith")
                .phone("1234567890")
                .email("john.smith@email.com")
                .build();
    }

    @Test
    @DisplayName("Should create valid customer with all required fields")
    void shouldCreateValidCustomer() {
        Set<ConstraintViolation<Customer>> violations = validator.validate(validCustomer);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when name is blank")
    void shouldFailValidationWhenNameIsBlank() {
        validCustomer.setName("");
        Set<ConstraintViolation<Customer>> violations = validator.validate(validCustomer);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Name is required")));
    }

    @Test
    @DisplayName("Should fail validation when phone is blank")
    void shouldFailValidationWhenPhoneIsBlank() {
        validCustomer.setPhone("");
        Set<ConstraintViolation<Customer>> violations = validator.validate(validCustomer);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Phone number is required")));
    }

    @Test
    @DisplayName("Should fail validation when email is invalid")
    void shouldFailValidationWhenEmailIsInvalid() {
        validCustomer.setEmail("invalid-email");
        Set<ConstraintViolation<Customer>> violations = validator.validate(validCustomer);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Email should be valid")));
    }

    @Test
    @DisplayName("Should fail validation when phone has invalid format")
    void shouldFailValidationWhenPhoneHasInvalidFormat() {
        validCustomer.setPhone("123");
        Set<ConstraintViolation<Customer>> violations = validator.validate(validCustomer);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Phone number must be 10-15 digits")));
    }

    @Test
    @DisplayName("Should accept valid phone formats")
    void shouldAcceptValidPhoneFormats() {
        // Test different valid phone formats
        String[] validPhones = {"1234567890", "12345678901", "123456789012345"};
        
        for (String phone : validPhones) {
            validCustomer.setPhone(phone);
            Set<ConstraintViolation<Customer>> violations = validator.validate(validCustomer);
            assertTrue(violations.isEmpty(), "Phone " + phone + " should be valid");
        }
    }

    @Test
    @DisplayName("Should test builder pattern")
    void shouldTestBuilderPattern() {
        Customer customer = Customer.builder()
                .name("Jane Doe")
                .phone("9876543210")
                .email("jane.doe@email.com")
                .build();
        
        assertEquals("Jane Doe", customer.getName());
        assertEquals("9876543210", customer.getPhone());
        assertEquals("jane.doe@email.com", customer.getEmail());
        assertNotNull(customer.getCreatedAt());
        assertNotNull(customer.getUpdatedAt());
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        Customer customer1 = Customer.builder()
                .id(1L)
                .name("John Smith")
                .phone("1234567890")
                .email("john.smith@email.com")
                .build();

        Customer customer2 = Customer.builder()
                .id(1L)
                .name("John Smith")
                .phone("1234567890")
                .email("john.smith@email.com")
                .build();

        Customer customer3 = Customer.builder()
                .id(2L)
                .name("Jane Doe")
                .phone("9876543210")
                .email("jane.doe@email.com")
                .build();

        assertEquals(customer1, customer2);
        assertNotEquals(customer1, customer3);
        assertEquals(customer1.hashCode(), customer2.hashCode());
        assertNotEquals(customer1.hashCode(), customer3.hashCode());
    }

    @Test
    @DisplayName("Should update timestamp on preUpdate")
    void shouldUpdateTimestampOnPreUpdate() {
        var originalUpdatedAt = validCustomer.getUpdatedAt();
        
        // Simulate some delay
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        validCustomer.preUpdate();
        assertTrue(validCustomer.getUpdatedAt().isAfter(originalUpdatedAt));
    }
}