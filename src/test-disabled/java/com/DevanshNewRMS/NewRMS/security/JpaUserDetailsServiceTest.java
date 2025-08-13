package com.DevanshNewRMS.NewRMS.security;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import com.DevanshNewRMS.NewRMS.Service.JpaUserDetailsService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("JPA User Details Service Tests")
class JpaUserDetailsServiceTest {

    @Mock
    private StaffRepository staffRepository;

    @InjectMocks
    private JpaUserDetailsService userDetailsService;

    private Staff testStaff;

    @BeforeEach
    void setUp() {
        testStaff = Staff.builder()
                .id(1L)
                .name("John Doe")
                .username("johndoe")
                .password("encodedPassword")
                .roles("ADMIN")
                .build();
    }

    @Test
    @DisplayName("Should load user by username successfully")
    void shouldLoadUserByUsernameSuccessfully() {
        // Given
        when(staffRepository.findByUsername("johndoe")).thenReturn(Optional.of(testStaff));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("johndoe");

        // Then
        assertNotNull(userDetails);
        assertEquals("johndoe", userDetails.getUsername());
        assertEquals("encodedPassword", userDetails.getPassword());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(userDetails.isEnabled());
        assertTrue(userDetails.isAccountNonExpired());
        assertTrue(userDetails.isAccountNonLocked());
        assertTrue(userDetails.isCredentialsNonExpired());

        verify(staffRepository).findByUsername("johndoe");
    }

    @Test
    @DisplayName("Should load user with multiple roles")
    void shouldLoadUserWithMultipleRoles() {
        // Given
        testStaff.setRoles("ADMIN,MANAGER");
        when(staffRepository.findByUsername("johndoe")).thenReturn(Optional.of(testStaff));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("johndoe");

        // Then
        assertNotNull(userDetails);
        assertEquals(2, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_ADMIN")));
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_MANAGER")));

        verify(staffRepository).findByUsername("johndoe");
    }

    @Test
    @DisplayName("Should throw UsernameNotFoundException when user not found")
    void shouldThrowUsernameNotFoundExceptionWhenUserNotFound() {
        // Given
        when(staffRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        UsernameNotFoundException exception = assertThrows(
                UsernameNotFoundException.class,
                () -> userDetailsService.loadUserByUsername("nonexistent")
        );

        assertEquals("User not found: nonexistent", exception.getMessage());
        verify(staffRepository).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("Should handle null username gracefully")
    void shouldHandleNullUsernameGracefully() {
        // Given
        when(staffRepository.findByUsername(null)).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername(null);
        });

        verify(staffRepository).findByUsername(null);
    }

    @Test
    @DisplayName("Should handle empty username gracefully")
    void shouldHandleEmptyUsernameGracefully() {
        // Given
        when(staffRepository.findByUsername("")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("");
        });

        verify(staffRepository).findByUsername("");
    }

    @Test
    @DisplayName("Should handle staff with single role")
    void shouldHandleStaffWithSingleRole() {
        // Given
        testStaff.setRoles("WAITER");
        when(staffRepository.findByUsername("johndoe")).thenReturn(Optional.of(testStaff));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("johndoe");

        // Then
        assertNotNull(userDetails);
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_WAITER")));

        verify(staffRepository).findByUsername("johndoe");
    }

    @Test
    @DisplayName("Should handle staff with empty roles")
    void shouldHandleStaffWithEmptyRoles() {
        // Given
        testStaff.setRoles("");
        when(staffRepository.findByUsername("johndoe")).thenReturn(Optional.of(testStaff));

        // When
        UserDetails userDetails = userDetailsService.loadUserByUsername("johndoe");

        // Then
        assertNotNull(userDetails);
        assertEquals(1, userDetails.getAuthorities().size());
        assertTrue(userDetails.getAuthorities().stream()
                .anyMatch(auth -> auth.getAuthority().equals("ROLE_")));

        verify(staffRepository).findByUsername("johndoe");
    }

    @Test
    @DisplayName("Should be case sensitive for username")
    void shouldBeCaseSensitiveForUsername() {
        // Given
        when(staffRepository.findByUsername("JohnDoe")).thenReturn(Optional.empty());

        // When & Then
        assertThrows(UsernameNotFoundException.class, () -> {
            userDetailsService.loadUserByUsername("JohnDoe");
        });

        verify(staffRepository).findByUsername("JohnDoe");
        verify(staffRepository, never()).findByUsername("johndoe");
    }
}