package com.DevanshNewRMS.NewRMS.controller;

import com.DevanshNewRMS.NewRMS.Controller.StaffController;
import com.DevanshNewRMS.NewRMS.Model.Staff;
import com.DevanshNewRMS.NewRMS.Service.StaffService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(StaffController.class)
@DisplayName("Staff Controller Tests")
class StaffControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private StaffService staffService;

    @Autowired
    private ObjectMapper objectMapper;

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
    @WithMockUser(roles = "ADMIN")
    void shouldGetAllStaff() throws Exception {
        // Given
        List<Staff> staffList = Arrays.asList(testStaff);
        when(staffService.getAllStaff()).thenReturn(staffList);

        // When & Then
        mockMvc.perform(get("/api/staff"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("John Doe"))
                .andExpect(jsonPath("$[0].username").value("johndoe"));

        verify(staffService).getAllStaff();
    }

    @Test
    @DisplayName("Should get staff by id")
    @WithMockUser(roles = "ADMIN")
    void shouldGetStaffById() throws Exception {
        // Given
        when(staffService.getStaffById(1L)).thenReturn(Optional.of(testStaff));

        // When & Then
        mockMvc.perform(get("/api/staff/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("John Doe"))
                .andExpect(jsonPath("$.username").value("johndoe"));

        verify(staffService).getStaffById(1L);
    }

    @Test
    @DisplayName("Should return 404 when staff not found")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn404WhenStaffNotFound() throws Exception {
        // Given
        when(staffService.getStaffById(999L)).thenReturn(Optional.empty());

        // When & Then
        mockMvc.perform(get("/api/staff/999"))
                .andExpect(status().isNotFound());

        verify(staffService).getStaffById(999L);
    }

    @Test
    @DisplayName("Should create new staff")
    @WithMockUser(roles = "ADMIN")
    void shouldCreateNewStaff() throws Exception {
        // Given
        Staff newStaff = Staff.builder()
                .name("Jane Smith")
                .username("janesmith")
                .password("password456")
                .roles("MANAGER")
                .build();

        when(staffService.createStaff(any(Staff.class))).thenReturn(newStaff);

        // When & Then
        mockMvc.perform(post("/api/staff")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newStaff)))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Jane Smith"))
                .andExpect(jsonPath("$.username").value("janesmith"));

        verify(staffService).createStaff(any(Staff.class));
    }

    @Test
    @DisplayName("Should update existing staff")
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateExistingStaff() throws Exception {
        // Given
        Staff updatedStaff = Staff.builder()
                .id(1L)
                .name("Updated Name")
                .username("johndoe")
                .password("newPassword")
                .roles("MANAGER")
                .build();

        when(staffService.updateStaff(eq(1L), any(Staff.class))).thenReturn(updatedStaff);

        // When & Then
        mockMvc.perform(put("/api/staff/1")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updatedStaff)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.roles").value("MANAGER"));

        verify(staffService).updateStaff(eq(1L), any(Staff.class));
    }

    @Test
    @DisplayName("Should delete staff")
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteStaff() throws Exception {
        // Given
        doNothing().when(staffService).deleteStaff(1L);

        // When & Then
        mockMvc.perform(delete("/api/staff/1")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(staffService).deleteStaff(1L);
    }

    @Test
    @DisplayName("Should search staff by name")
    @WithMockUser(roles = "ADMIN")
    void shouldSearchStaffByName() throws Exception {
        // Given
        List<Staff> staffList = Arrays.asList(testStaff);
        when(staffService.searchStaffByName("john")).thenReturn(staffList);

        // When & Then
        mockMvc.perform(get("/api/staff/search")
                        .param("name", "john"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("John Doe"));

        verify(staffService).searchStaffByName("john");
    }

    @Test
    @DisplayName("Should return 400 for invalid staff data")
    @WithMockUser(roles = "ADMIN")
    void shouldReturn400ForInvalidStaffData() throws Exception {
        // Given
        Staff invalidStaff = Staff.builder()
                .name("") // Invalid: empty name
                .username("invalid@username") // Invalid: contains @
                .password("")
                .roles("INVALID_ROLE")
                .build();

        // When & Then
        mockMvc.perform(post("/api/staff")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidStaff)))
                .andExpect(status().isBadRequest());

        verify(staffService, never()).createStaff(any());
    }

    @Test
    @DisplayName("Should require authentication")
    void shouldRequireAuthentication() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/staff"))
                .andExpect(status().isUnauthorized());

        verify(staffService, never()).getAllStaff();
    }

    @Test
    @DisplayName("Should require admin role for staff management")
    @WithMockUser(roles = "WAITER")
    void shouldRequireAdminRoleForStaffManagement() throws Exception {
        // When & Then
        mockMvc.perform(get("/api/staff"))
                .andExpect(status().isForbidden());

        verify(staffService, never()).getAllStaff();
    }

    @Test
    @DisplayName("Should handle service exceptions")
    @WithMockUser(roles = "ADMIN")
    void shouldHandleServiceExceptions() throws Exception {
        // Given
        when(staffService.getStaffById(1L)).thenThrow(new RuntimeException("Database error"));

        // When & Then
        mockMvc.perform(get("/api/staff/1"))
                .andExpect(status().isInternalServerError());

        verify(staffService).getStaffById(1L);
    }

    @Test
    @DisplayName("Should handle update of non-existent staff")
    @WithMockUser(roles = "ADMIN")
    void shouldHandleUpdateOfNonExistentStaff() throws Exception {
        // Given
        when(staffService.updateStaff(eq(999L), any(Staff.class)))
                .thenThrow(new RuntimeException("Staff not found"));

        // When & Then
        mockMvc.perform(put("/api/staff/999")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testStaff)))
                .andExpect(status().isInternalServerError());

        verify(staffService).updateStaff(eq(999L), any(Staff.class));
    }
}