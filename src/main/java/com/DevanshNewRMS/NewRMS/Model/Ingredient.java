package com.DevanshNewRMS.NewRMS.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "ingredient")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Ingredient name is required")
    @Size(min = 2, max = 100, message = "Ingredient name must be between 2 and 100 characters")
    @Column(nullable = false, unique = true)
    private String name;

    @NotNull(message = "Quantity in stock is required")
    @DecimalMin(value = "0.0", message = "Quantity cannot be negative")
    @Column(name = "quantity_in_stock", nullable = false)
    @Builder.Default
    private double quantityInStock = 0.0;

    @NotBlank(message = "Unit is required")
    @Size(max = 20, message = "Unit cannot exceed 20 characters")
    @Column(nullable = false)
    private String unit;

    @Column(name = "created_at", nullable = false, updatable = false)
    @Builder.Default
    private LocalDateTime createdAt = LocalDateTime.now();

    @Column(name = "updated_at")
    @Builder.Default
    private LocalDateTime updatedAt = LocalDateTime.now();

    @PreUpdate
    public void preUpdate() {
        this.updatedAt = LocalDateTime.now();
    }
}
