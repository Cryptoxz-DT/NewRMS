package com.DevanshNewRMS.NewRMS.repository;

import com.DevanshNewRMS.NewRMS.Model.Customer;
import com.DevanshNewRMS.NewRMS.Repository.CustomerRepository;
import com.DevanshNewRMS.NewRMS.config.TestConfig;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@Import(TestConfig.class)
@DisplayName("Customer Repository Tests")
class CustomerRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CustomerRepository customerRepository;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .name("John Smith")
                .phone("1234567890")
                .email("john.smith@email.com")
                .build();
    }

    @Test
    @DisplayName("Should save and find customer by id")
    void shouldSaveAndFindCustomerById() {
        // Given
        Customer savedCustomer = entityManager.persistAndFlush(testCustomer);
        
        // When
        Optional<Customer> foundCustomer = customerRepository.findById(savedCustomer.getId());
        
        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals(testCustomer.getName(), foundCustomer.get().getName());
        assertEquals(testCustomer.getPhone(), foundCustomer.get().getPhone());
        assertEquals(testCustomer.getEmail(), foundCustomer.get().getEmail());
    }

    @Test
    @DisplayName("Should find customer by phone")
    void shouldFindCustomerByPhone() {
        // Given
        entityManager.persistAndFlush(testCustomer);
        
        // When
        Optional<Customer> foundCustomer = customerRepository.findByPhone("1234567890");
        
        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals(testCustomer.getName(), foundCustomer.get().getName());
        assertEquals(testCustomer.getPhone(), foundCustomer.get().getPhone());
    }

    @Test
    @DisplayName("Should find customer by email")
    void shouldFindCustomerByEmail() {
        // Given
        entityManager.persistAndFlush(testCustomer);
        
        // When
        Optional<Customer> foundCustomer = customerRepository.findByEmail("john.smith@email.com");
        
        // Then
        assertTrue(foundCustomer.isPresent());
        assertEquals(testCustomer.getName(), foundCustomer.get().getName());
        assertEquals(testCustomer.getEmail(), foundCustomer.get().getEmail());
    }

    @Test
    @DisplayName("Should find customers by name containing ignore case")
    void shouldFindCustomersByNameContainingIgnoreCase() {
        // Given
        Customer customer1 = Customer.builder()
                .name("John Smith")
                .phone("1234567890")
                .email("john.smith@email.com")
                .build();
        
        Customer customer2 = Customer.builder()
                .name("Jane Johnson")
                .phone("9876543210")
                .email("jane.johnson@email.com")
                .build();
        
        Customer customer3 = Customer.builder()
                .name("Bob Wilson")
                .phone("5555555555")
                .email("bob.wilson@email.com")
                .build();
        
        entityManager.persistAndFlush(customer1);
        entityManager.persistAndFlush(customer2);
        entityManager.persistAndFlush(customer3);
        
        // When
        List<Customer> foundCustomers = customerRepository.findByNameContainingIgnoreCase("john");
        
        // Then
        assertEquals(2, foundCustomers.size());
        assertTrue(foundCustomers.stream().anyMatch(c -> c.getName().equals("John Smith")));
        assertTrue(foundCustomers.stream().anyMatch(c -> c.getName().equals("Jane Johnson")));
    }

    @Test
    @DisplayName("Should return empty when customer not found by phone")
    void shouldReturnEmptyWhenCustomerNotFoundByPhone() {
        // When
        Optional<Customer> foundCustomer = customerRepository.findByPhone("9999999999");
        
        // Then
        assertFalse(foundCustomer.isPresent());
    }

    @Test
    @DisplayName("Should return empty when customer not found by email")
    void shouldReturnEmptyWhenCustomerNotFoundByEmail() {
        // When
        Optional<Customer> foundCustomer = customerRepository.findByEmail("nonexistent@email.com");
        
        // Then
        assertFalse(foundCustomer.isPresent());
    }

    @Test
    @DisplayName("Should enforce unique phone constraint")
    void shouldEnforceUniquePhoneConstraint() {
        // Given
        Customer customer1 = Customer.builder()
                .name("John Smith")
                .phone("1234567890")
                .email("john.smith@email.com")
                .build();
        
        Customer customer2 = Customer.builder()
                .name("Jane Doe")
                .phone("1234567890") // Same phone
                .email("jane.doe@email.com")
                .build();
        
        entityManager.persistAndFlush(customer1);
        
        // When & Then
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(customer2);
        });
    }

    @Test
    @DisplayName("Should delete customer by id")
    void shouldDeleteCustomerById() {
        // Given
        Customer savedCustomer = entityManager.persistAndFlush(testCustomer);
        Long customerId = savedCustomer.getId();
        
        // When
        customerRepository.deleteById(customerId);
        entityManager.flush();
        
        // Then
        Optional<Customer> deletedCustomer = customerRepository.findById(customerId);
        assertFalse(deletedCustomer.isPresent());
    }

    @Test
    @DisplayName("Should update customer")
    void shouldUpdateCustomer() {
        // Given
        Customer savedCustomer = entityManager.persistAndFlush(testCustomer);
        
        // When
        savedCustomer.setName("Updated Name");
        savedCustomer.setEmail("updated@email.com");
        Customer updatedCustomer = customerRepository.save(savedCustomer);
        entityManager.flush();
        
        // Then
        assertEquals("Updated Name", updatedCustomer.getName());
        assertEquals("updated@email.com", updatedCustomer.getEmail());
        assertNotNull(updatedCustomer.getUpdatedAt());
    }

    @Test
    @DisplayName("Should find all customers")
    void shouldFindAllCustomers() {
        // Given
        Customer customer1 = Customer.builder()
                .name("John Smith")
                .phone("1234567890")
                .email("john.smith@email.com")
                .build();
        
        Customer customer2 = Customer.builder()
                .name("Jane Doe")
                .phone("9876543210")
                .email("jane.doe@email.com")
                .build();
        
        entityManager.persistAndFlush(customer1);
        entityManager.persistAndFlush(customer2);
        
        // When
        List<Customer> allCustomers = customerRepository.findAll();
        
        // Then
        assertEquals(2, allCustomers.size());
    }
}