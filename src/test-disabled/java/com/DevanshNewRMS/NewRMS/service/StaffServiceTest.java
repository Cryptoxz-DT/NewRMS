package com.DevanshNewRMS.NewRMS.service;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import com.DevanshNewRMS.NewRMS.Service.StaffService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("Staff Service Tests")
class StaffServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private StaffService staffService;

    private Staff testStaff;

    @BeforeEach
    void setUp() {
        testStaff = Staff.builder()
                .id(1L)
                .name("John Doe")
                .username("johndoe")
                .password("password123")
                .roles("ADMIN")
                .build();
    }

    @Test
    @DisplayName("Should get all staff")
    void shouldGetAllStaff() {
        // Given
        List<Staff> staffList = Arrays.asList(testStaff);
        when(staffRepository.findAll()).thenReturn(staffList);

        // When
        List<Staff> result = staffService.getAllStaff();

        // Then
        assertEquals(1, result.size());
        assertEquals(testStaff.getName(), result.get(0).getName());
        verify(staffRepository).findAll();
    }

    @Test
    @DisplayName("Should get staff by id")
    void shouldGetStaffById() {
        // Given
        when(staffRepository.findById(1L)).thenReturn(Optional.of(testStaff));

        // When
        Optional<Staff> result = staffService.getStaffById(1L);

        // Then
        assertTrue(result.isPresent());
        assertEquals(testStaff.getName(), result.get().getName());
        verify(staffRepository).findById(1L);
    }

    @Test
    @DisplayName("Should return empty when staff not found by id")
    void shouldReturnEmptyWhenStaffNotFoundById() {
        // Given
        when(staffRepository.findById(999L)).thenReturn(Optional.empty());

        // When
        Optional<Staff> result = staffService.getStaffById(999L);

        // Then
        assertFalse(result.isPresent());
        verify(staffRepository).findById(999L);
    }

    @Test
    @DisplayName("Should create staff with encoded password")
    void shouldCreateStaffWithEncodedPassword() {
        // Given
        Staff newStaff = Staff.builder()
                .name("Jane Smith")
                .username("janesmith")
                .password("plainPassword")
                .roles("MANAGER")
                .build();

        when(passwordEncoder.encode("plainPassword")).thenReturn("encodedPassword");
        when(staffRepository.save(any(Staff.class))).thenReturn(newStaff);

        // When
        Staff result = staffService.createStaff(newStaff);

        // Then
        assertNotNull(result);
        verify(passwordEncoder).encode("plainPassword");
        verify(staffRepository).save(argThat(staff -> 
            staff.getPassword().equals("encodedPassword")
        ));
    }

    @Test
    @DisplayName("Should update existing staff")
    void shouldUpdateExistingStaff() {
        // Given
        Staff updatedStaff = Staff.builder()
                .id(1L)
                .name("Updated Name")
                .username("johndoe")
                .password("newPassword")
                .roles("MANAGER")
                .build();

        when(staffRepository.existsById(1L)).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("encodedNewPassword");
        when(staffRepository.save(any(Staff.class))).thenReturn(updatedStaff);

        // When
        Staff result = staffService.updateStaff(1L, updatedStaff);

        // Then
        assertNotNull(result);
        verify(staffRepository).existsById(1L);
        verify(passwordEncoder).encode("newPassword");
        verify(staffRepository).save(argThat(staff -> 
            staff.getId().equals(1L) && 
            staff.getPassword().equals("encodedNewPassword")
        ));
    }

    @Test
    @DisplayName("Should throw exception when updating non-existent staff")
    void shouldThrowExceptionWhenUpdatingNonExistentStaff() {
        // Given
        when(staffRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            staffService.updateStaff(999L, testStaff);
        });
        
        verify(staffRepository).existsById(999L);
        verify(staffRepository, never()).save(any());
    }

    @Test
    @DisplayName("Should delete staff by id")
    void shouldDeleteStaffById() {
        // Given
        when(staffRepository.existsById(1L)).thenReturn(true);

        // When
        staffService.deleteStaff(1L);

        // Then
        verify(staffRepository).existsById(1L);
        verify(staffRepository).deleteById(1L);
    }

    @Test
    @DisplayName("Should throw exception when deleting non-existent staff")
    void shouldThrowExceptionWhenDeletingNonExistentStaff() {
        // Given
        when(staffRepository.existsById(999L)).thenReturn(false);

        // When & Then
        assertThrows(RuntimeException.class, () -> {
            staffService.deleteStaff(999L);
        });
        
        verify(staffRepository).existsById(999L);
        verify(staffRepository, never()).deleteById(any());
    }

    @Test
    @DisplayName("Should search staff by name")
    void shouldSearchStaffByName() {
        // Given
        List<Staff> staffList = Arrays.asList(testStaff);
        when(staffRepository.findByNameContainingIgnoreCase("john")).thenReturn(staffList);

        // When
        List<Staff> result = staffService.searchStaffByName("john");

        // Then
        assertEquals(1, result.size());
        assertEquals(testStaff.getName(), result.get(0).getName());
        verify(staffRepository).findByNameContainingIgnoreCase("john");
    }

    @Test
    @DisplayName("Should find staff by username")
    void shouldFindStaffByUsername() {
        // Given
        when(staffRepository.findByUsername("johndoe")).thenReturn(Optional.of(testStaff));

        // When
        Optional<Staff> result = staffService.findByUsername("johndoe");

        // Then
        assertTrue(result.isPresent());
        assertEquals(testStaff.getUsername(), result.get().getUsername());
        verify(staffRepository).findByUsername("johndoe");
    }

    @Test
    @DisplayName("Should handle null password gracefully")
    void shouldHandleNullPasswordGracefully() {
        // Given
        Staff staffWithNullPassword = Staff.builder()
                .name("Test User")
                .username("testuser")
                .password(null)
                .roles("WAITER")
                .build();

        when(staffRepository.save(any(Staff.class))).thenReturn(staffWithNullPassword);

        // When
        Staff result = staffService.createStaff(staffWithNullPassword);

        // Then
        assertNotNull(result);
        verify(passwordEncoder, never()).encode(any());
        verify(staffRepository).save(staffWithNullPassword);
    }

    @Test
    @DisplayName("Should handle empty password gracefully")
    void shouldHandleEmptyPasswordGracefully() {
        // Given
        Staff staffWithEmptyPassword = Staff.builder()
                .name("Test User")
                .username("testuser")
                .password("")
                .roles("WAITER")
                .build();

        when(staffRepository.save(any(Staff.class))).thenReturn(staffWithEmptyPassword);

        // When
        Staff result = staffService.createStaff(staffWithEmptyPassword);

        // Then
        assertNotNull(result);
        verify(passwordEncoder, never()).encode(any());
        verify(staffRepository).save(staffWithEmptyPassword);
    }
}