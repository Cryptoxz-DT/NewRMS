package com.DevanshNewRMS.NewRMS.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "staff", indexes = {
    @Index(name = "idx_staff_username", columnList = "username"),
    @Index(name = "idx_staff_email", columnList = "email")
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Staff {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Size(min = 2, max = 50, message = "First name must be between 2 and 50 characters")
    @Column(name = "first_name")
    private String firstName;

    @Size(min = 2, max = 50, message = "Last name must be between 2 and 50 characters")
    @Column(name = "last_name")
    private String lastName;

    @Email(message = "Email should be valid")
    @Size(max = 100, message = "Email must not exceed 100 characters")
    @Column(unique = true)
    private String email;
    
    @NotBlank(message = "Username is required")
    @Size(min = 3, max = 50, message = "Username must be between 3 and 50 characters")
    @Pattern(regexp = "^[a-zA-Z0-9_]+$", message = "Username can only contain letters, numbers, and underscores")
    @Column(nullable = false, unique = true)
    private String username;
    
    @NotBlank(message = "Password is required")
    @Column(nullable = false)
    private String password;
    
    @NotBlank(message = "Role is required")
    @Pattern(regexp = "^(ADMIN|MANAGER|STAFF|WAITER|CHEF|CASHIER)(,(ADMIN|MANAGER|STAFF|WAITER|CHEF|CASHIER))*$", 
             message = "Invalid role format")
    @Column(nullable = false)
    private String roles;

    @Column(name = "account_locked")
    @Builder.Default
    private Boolean accountLocked = false;

    @Column(name = "failed_login_attempts")
    @Builder.Default
    private Integer failedLoginAttempts = 0;

    @Column(name = "last_login_attempt")
    private LocalDateTime lastLoginAttempt;

    @Column(name = "password_changed_at")
    @Builder.Default
    private LocalDateTime passwordChangedAt = LocalDateTime.now();

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    public String getRole() {
        return this.roles;
    }

    public void setRole(String role) {
        this.roles = role;
    }

    public String getName() {
        if (this.firstName != null && this.lastName != null) {
            return this.firstName + " " + this.lastName;
        } else if (this.firstName != null) {
            return this.firstName;
        } else if (this.lastName != null) {
            return this.lastName;
        } else {
            return this.username; // fallback to username
        }
    }

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    public void incrementFailedLoginAttempts() {
        this.failedLoginAttempts++;
        this.lastLoginAttempt = LocalDateTime.now();
        if (this.failedLoginAttempts >= 5) {
            this.accountLocked = true;
        }
    }

    public void resetFailedLoginAttempts() {
        this.failedLoginAttempts = 0;
        this.accountLocked = false;
        this.lastLoginAttempt = LocalDateTime.now();
    }
}