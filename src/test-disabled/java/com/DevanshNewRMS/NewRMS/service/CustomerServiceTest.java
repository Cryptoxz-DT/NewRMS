package com.DevanshNewRMS.NewRMS.service;

import com.DevanshNewRMS.NewRMS.Model.Customer;
import com.DevanshNewRMS.NewRMS.Repository.CustomerRepository;
import com.DevanshNewRMS.NewRMS.Service.CustomerService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Customer Service Tests")
class CustomerServiceTest {

    @Mock
    private CustomerRepository customerRepository;

    @InjectMocks
    private CustomerService customerService;

    private Customer testCustomer;

    @BeforeEach
    void setUp() {
        testCustomer = Customer.builder()
                .id(1L)
                .name("John Smith")
                .phone("1234567890")
                .email("john.smith@email.com")
                .build();
    }

    @Test
    @DisplayName("Should get all customers")
    void shouldGetAllCustomers() {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findAll()).thenReturn(customers);

        // When
        List<Customer> result = customerService.getAllCustomers();

        // Then
        assertEquals(1, result.size());
        assertEquals(testCustomer.getName(), result.get(0).getName());
        verify(customerRepository).findAll();
    }

    @Test
    @DisplayName("Should get customer by id")
    void shouldGetCustomerById() {
        // Given
        when(customerRepository.findById(1L)).thenReturn(Optional.of(testCustomer));

        // When
        Optional<Customer> result = customerService.getCustomerById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testCustomer.getName(), result.get().getName());
        verify(customerRepository).findById(1L);
    }

    @Test
    @DisplayName("Should create new customer")
    void shouldCreateNewCustomer() {
        // Given
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        Customer result = customerService.createCustomer(testCustomer);

        // Then
        assertNotNull(result);
        assertEquals(testCustomer.getName(), result.getName());
        verify(customerRepository).save(testCustomer);
    }

    @Test
    @DisplayName("Should update existing customer")
    void shouldUpdateExistingCustomer() {
        // Given
        when(customerRepository.existsById(1L)).thenReturn(true);
        when(customerRepository.save(any(Customer.class))).thenReturn(testCustomer);

        // When
        Customer result = customerService.updateCustomer(1L, testCustomer);

        // Then
        assertNotNull(result);
        verify(customerRepository).existsById(1L);
        verify(customerRepository).save(argThat(customer -> customer.getId().equals(1L)));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent customer")
    void shouldThrowExceptionWhenUpdatingNonExistentCustomer() {
        // Given
        when(customerRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            customerService.updateCustomer(999L, testCustomer);
        });
        
        verify(customerRepository).existsById(999L);
        verify(customerRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete customer")
    void shouldDeleteCustomer() {
        // Given
        when(customerRepository.existsById(1L)).thenReturn(true);

        // When
        customerService.deleteCustomer(1L);

        // Then
        verify(customerRepository).existsById(1L);
        verify(customerRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should search customers by name")
    void shouldSearchCustomersByName() {
        // Given
        List<Customer> customers = Arrays.asList(testCustomer);
        when(customerRepository.findByNameContainingIgnoreCase("john")).thenReturn(customers);

        // When
        List<Customer> result = customerService.searchCustomersByName("john");

        // Then
        assertEquals(1, result.size());
        assertEquals(testCustomer.getName(), result.get(0).getName());
        verify(customerRepository).findByNameContainingIgnoreCase("john");
    }
}