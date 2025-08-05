package com.DevanshNewRMS.NewRMS.Model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String customerName;
    private String customerPhone;
    private LocalDateTime reservationTime;
    private int numberOfPeople;

    @ManyToOne
    @JoinColumn(name = "table_id")
    private TableInfo tableInfo;
}