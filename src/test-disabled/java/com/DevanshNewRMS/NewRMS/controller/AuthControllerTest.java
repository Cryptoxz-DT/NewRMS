package com.DevanshNewRMS.NewRMS.controller;

import com.DevanshNewRMS.NewRMS.Controller.AuthController;
import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(AuthController.class)
@DisplayName("Auth Controller Tests")
class AuthControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StaffRepository staffRepository;

    private Staff testStaff;

    @BeforeEach
    void setUp() {
        testStaff = Staff.builder()
                .id(1L)
                .name("Test Admin")
                .username("admin")
                .password("encodedPassword")
                .roles("ADMIN")
                .build();
    }

    @Test
    @DisplayName("Should get current user info")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldGetCurrentUserInfo() throws Exception {
        // Given
        when(staffRepository.findByUsername("admin")).thenReturn(Optional.of(testStaff));

        // When & Then
        mockMvc.perform(get("/api/auth/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("admin"))
                .andExpect(jsonPath("$.name").value("Test Admin"))
                .andExpect(jsonPath("$.id").value(1));

        verify(staffRepository).findByUsername("admin");
    }

    @Test
    @DisplayName("Should handle user not found in database")
    @WithMockUser(username = "nonexistent", roles = "ADMIN")
    void shouldHandleUserNotFoundInDatabase() throws Exception {
        // Given
        when(staffRepository.findByUsername("nonexistent")).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/auth/user"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("nonexistent"))
                .andExpect(jsonPath("$.name").doesNotExist());

        verify(staffRepository).findByUsername("nonexistent");
    }

    @Test
    @DisplayName("Should require authentication")
    void shouldRequireAuthentication() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/auth/user"))
                .andExpect(status().isUnauthorized());

        verify(staffRepository, never()).findByUsername(any());
    }

    @Test
    @DisplayName("Should handle logout")
    @WithMockUser(username = "admin", roles = "ADMIN")
    void shouldHandleLogout() throws Exception {
        // When & Then
        mockMvc.perform(post("/api/auth/logout"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Logged out successfully"));
    }
}