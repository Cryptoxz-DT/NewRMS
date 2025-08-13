package com.DevanshNewRMS.NewRMS.repository;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
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
@DisplayName("Staff Repository Tests")
class StaffRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private StaffRepository staffRepository;

    private Staff testStaff;

    @BeforeEach
    void setUp() {
        testStaff = Staff.builder()
                .name("John Doe")
                .username("johndoe")
                .password("password123")
                .roles("ADMIN")
                .build();
    }

    @Test
    @DisplayName("Should save and find staff by id")
    void shouldSaveAndFindStaffById() {
        // Given
        Staff savedStaff = entityManager.persistAndFlush(testStaff);
        
        // When
        Optional<Staff> foundStaff = staffRepository.findById(savedStaff.getId());
        
        // Then
        assertTrue(foundStaff.isPresent());
        assertEquals(testStaff.getName(), foundStaff.get().getName());
        assertEquals(testStaff.getUsername(), foundStaff.get().getUsername());
        assertEquals(testStaff.getRoles(), foundStaff.get().getRoles());
    }

    @Test
    @DisplayName("Should find staff by username")
    void shouldFindStaffByUsername() {
        // Given
        entityManager.persistAndFlush(testStaff);
        
        // When
        Optional<Staff> foundStaff = staffRepository.findByUsername("johndoe");
        
        // Then
        assertTrue(foundStaff.isPresent());
        assertEquals(testStaff.getName(), foundStaff.get().getName());
        assertEquals(testStaff.getUsername(), foundStaff.get().getUsername());
    }

    @Test
    @DisplayName("Should return empty when staff not found by username")
    void shouldReturnEmptyWhenStaffNotFoundByUsername() {
        // When
        Optional<Staff> foundStaff = staffRepository.findByUsername("nonexistent");
        
        // Then
        assertFalse(foundStaff.isPresent());
    }

    @Test
    @DisplayName("Should find staff by name containing ignore case")
    void shouldFindStaffByNameContainingIgnoreCase() {
        // Given
        Staff staff1 = Staff.builder()
                .name("John Doe")
                .username("johndoe")
                .password("password123")
                .roles("ADMIN")
                .build();
        
        Staff staff2 = Staff.builder()
                .name("Jane Smith")
                .username("janesmith")
                .password("password456")
                .roles("MANAGER")
                .build();
        
        Staff staff3 = Staff.builder()
                .name("Bob Johnson")
                .username("bobjohnson")
                .password("password789")
                .roles("WAITER")
                .build();
        
        entityManager.persistAndFlush(staff1);
        entityManager.persistAndFlush(staff2);
        entityManager.persistAndFlush(staff3);
        
        // When
        List<Staff> foundStaff = staffRepository.findByNameContainingIgnoreCase("john");
        
        // Then
        assertEquals(2, foundStaff.size());
        assertTrue(foundStaff.stream().anyMatch(s -> s.getName().equals("John Doe")));
        assertTrue(foundStaff.stream().anyMatch(s -> s.getName().equals("Bob Johnson")));
    }

    @Test
    @DisplayName("Should find all staff")
    void shouldFindAllStaff() {
        // Given
        Staff staff1 = Staff.builder()
                .name("John Doe")
                .username("johndoe")
                .password("password123")
                .roles("ADMIN")
                .build();
        
        Staff staff2 = Staff.builder()
                .name("Jane Smith")
                .username("janesmith")
                .password("password456")
                .roles("MANAGER")
                .build();
        
        entityManager.persistAndFlush(staff1);
        entityManager.persistAndFlush(staff2);
        
        // When
        List<Staff> allStaff = staffRepository.findAll();
        
        // Then
        assertEquals(2, allStaff.size());
    }

    @Test
    @DisplayName("Should delete staff by id")
    void shouldDeleteStaffById() {
        // Given
        Staff savedStaff = entityManager.persistAndFlush(testStaff);
        Long staffId = savedStaff.getId();
        
        // When
        staffRepository.deleteById(staffId);
        entityManager.flush();
        
        // Then
        Optional<Staff> deletedStaff = staffRepository.findById(staffId);
        assertFalse(deletedStaff.isPresent());
    }

    @Test
    @DisplayName("Should update staff")
    void shouldUpdateStaff() {
        // Given
        Staff savedStaff = entityManager.persistAndFlush(testStaff);
        
        // When
        savedStaff.setName("Updated Name");
        savedStaff.setRoles("MANAGER");
        Staff updatedStaff = staffRepository.save(savedStaff);
        entityManager.flush();
        
        // Then
        assertEquals("Updated Name", updatedStaff.getName());
        assertEquals("MANAGER", updatedStaff.getRoles());
        assertNotNull(updatedStaff.getUpdatedAt());
    }

    @Test
    @DisplayName("Should enforce unique username constraint")
    void shouldEnforceUniqueUsernameConstraint() {
        // Given
        Staff staff1 = Staff.builder()
                .name("John Doe")
                .username("johndoe")
                .password("password123")
                .roles("ADMIN")
                .build();
        
        Staff staff2 = Staff.builder()
                .name("Jane Smith")
                .username("johndoe") // Same username
                .password("password456")
                .roles("MANAGER")
                .build();
        
        entityManager.persistAndFlush(staff1);
        
        // When & Then
        assertThrows(Exception.class, () -> {
            entityManager.persistAndFlush(staff2);
        });
    }
}