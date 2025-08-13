package com.DevanshNewRMS.NewRMS.model;

import com.DevanshNewRMS.NewRMS.Model.*;
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

@DisplayName("Order Model Tests")
class OrderTest {

    private Validator validator;
    private Order validOrder;
    private Staff testStaff;
    private Customer testCustomer;
    private TableInfo testTable;
    private OrderStatus testStatus;

    @BeforeEach
    void setUp() {
        ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
        validator = factory.getValidator();
        
        testStaff = Staff.builder()
                .id(1L)
                .name("John Waiter")
                .username("johnwaiter")
                .password("password")
                .roles("WAITER")
                .build();

        testCustomer = Customer.builder()
                .id(1L)
                .name("Jane Customer")
                .phone("1234567890")
                .email("jane@email.com")
                .build();

        testTable = TableInfo.builder()
                .id(1L)
                .tableNumber(5)
                .capacity(4)
                .build();

        testStatus = OrderStatus.builder()
                .id(1L)
                .name("PENDING")
                .build();
        
        validOrder = Order.builder()
                .orderTime(LocalDateTime.now())
                .staff(testStaff)
                .customer(testCustomer)
                .tableInfo(testTable)
                .status(testStatus)
                .build();
    }

    @Test
    @DisplayName("Should create valid order with all required fields")
    void shouldCreateValidOrder() {
        Set<ConstraintViolation<Order>> violations = validator.validate(validOrder);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should fail validation when order time is null")
    void shouldFailValidationWhenOrderTimeIsNull() {
        validOrder.setOrderTime(null);
        Set<ConstraintViolation<Order>> violations = validator.validate(validOrder);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Order time is required")));
    }

    @Test
    @DisplayName("Should fail validation when staff is null")
    void shouldFailValidationWhenStaffIsNull() {
        validOrder.setStaff(null);
        Set<ConstraintViolation<Order>> violations = validator.validate(validOrder);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Staff is required")));
    }

    @Test
    @DisplayName("Should fail validation when status is null")
    void shouldFailValidationWhenStatusIsNull() {
        validOrder.setStatus(null);
        Set<ConstraintViolation<Order>> violations = validator.validate(validOrder);
        assertFalse(violations.isEmpty());
        assertTrue(violations.stream().anyMatch(v -> v.getMessage().contains("Order status is required")));
    }

    @Test
    @DisplayName("Should allow null customer for walk-in orders")
    void shouldAllowNullCustomerForWalkInOrders() {
        validOrder.setCustomer(null);
        Set<ConstraintViolation<Order>> violations = validator.validate(validOrder);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should allow null table for takeaway orders")
    void shouldAllowNullTableForTakeawayOrders() {
        validOrder.setTableInfo(null);
        Set<ConstraintViolation<Order>> violations = validator.validate(validOrder);
        assertTrue(violations.isEmpty());
    }

    @Test
    @DisplayName("Should test builder pattern")
    void shouldTestBuilderPattern() {
        Order order = Order.builder()
                .orderTime(LocalDateTime.now())
                .staff(testStaff)
                .customer(testCustomer)
                .tableInfo(testTable)
                .status(testStatus)
                .build();
        
        assertNotNull(order.getOrderTime());
        assertEquals(testStaff, order.getStaff());
        assertEquals(testCustomer, order.getCustomer());
        assertEquals(testTable, order.getTableInfo());
        assertEquals(testStatus, order.getStatus());
        assertNotNull(order.getCreatedAt());
        assertNotNull(order.getUpdatedAt());
    }

    @Test
    @DisplayName("Should test equals and hashCode")
    void shouldTestEqualsAndHashCode() {
        Order order1 = Order.builder()
                .id(1L)
                .orderTime(LocalDateTime.of(2024, 1, 1, 12, 0))
                .staff(testStaff)
                .customer(testCustomer)
                .tableInfo(testTable)
                .status(testStatus)
                .build();

        Order order2 = Order.builder()
                .id(1L)
                .orderTime(LocalDateTime.of(2024, 1, 1, 12, 0))
                .staff(testStaff)
                .customer(testCustomer)
                .tableInfo(testTable)
                .status(testStatus)
                .build();

        Order order3 = Order.builder()
                .id(2L)
                .orderTime(LocalDateTime.of(2024, 1, 2, 12, 0))
                .staff(testStaff)
                .customer(testCustomer)
                .tableInfo(testTable)
                .status(testStatus)
                .build();

        assertEquals(order1, order2);
        assertNotEquals(order1, order3);
        assertEquals(order1.hashCode(), order2.hashCode());
        assertNotEquals(order1.hashCode(), order3.hashCode());
    }

    @Test
    @DisplayName("Should update timestamp on preUpdate")
    void shouldUpdateTimestampOnPreUpdate() {
        LocalDateTime originalUpdatedAt = validOrder.getUpdatedAt();
        
        // Simulate some delay
        try {
            Thread.sleep(10);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
        
        validOrder.preUpdate();
        assertTrue(validOrder.getUpdatedAt().isAfter(originalUpdatedAt));
    }

    @Test
    @DisplayName("Should handle order time in the past")
    void shouldHandleOrderTimeInThePast() {
        validOrder.setOrderTime(LocalDateTime.of(2020, 1, 1, 12, 0));
        Set<ConstraintViolation<Order>> violations = validator.validate(validOrder);
        assertTrue(violations.isEmpty()); // Past orders should be allowed for historical data
    }

    @Test
    @DisplayName("Should handle order time in the future")
    void shouldHandleOrderTimeInTheFuture() {
        validOrder.setOrderTime(LocalDateTime.now().plusDays(1));
        Set<ConstraintViolation<Order>> violations = validator.validate(validOrder);
        assertTrue(violations.isEmpty()); // Future orders should be allowed for reservations
    }
}