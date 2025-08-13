package com.DevanshNewRMS.NewRMS.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "reservations")
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Customer name is required")
    @Size(min = 2, max = 100, message = "Customer name must be between 2 and 100 characters")
    @Column(nullable = false)
    private String customerName;
    
    @NotBlank(message = "Customer phone is required")
    @Pattern(regexp = "^[+]?[0-9]{10,15}$", message = "Phone number must be 10-15 digits")
    @Column(nullable = false)
    private String customerPhone;
    
    @NotNull(message = "Reservation time is required")
    @Future(message = "Reservation time must be in the future")
    @Column(nullable = false)
    private LocalDateTime reservationTime;
    
    @NotNull(message = "Number of people is required")
    @Min(value = 1, message = "At least 1 person is required")
    @Max(value = 50, message = "Maximum 50 people allowed")
    @Column(nullable = false)
    private int numberOfPeople;

    @NotNull(message = "Table selection is required")
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "table_id", nullable = false)
    private TableInfo tableInfo;

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
