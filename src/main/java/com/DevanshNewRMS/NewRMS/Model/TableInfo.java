package com.DevanshNewRMS.NewRMS.Model;

import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "table_info", indexes = {
    @Index(name = "idx_table_number", columnList = "tableNumber", unique = true)
})
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class TableInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "Table number is required")
    @Min(value = 1, message = "Table number must be at least 1")
    @Max(value = 999, message = "Table number cannot exceed 999")
    @Column(nullable = false, unique = true)
    private int tableNumber;
    
    @NotNull(message = "Capacity is required")
    @Min(value = 1, message = "Capacity must be at least 1")
    @Max(value = 50, message = "Capacity cannot exceed 50")
    @Column(nullable = false)
    private int capacity;
}
