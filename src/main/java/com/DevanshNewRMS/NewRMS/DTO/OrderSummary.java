package com.DevanshNewRMS.NewRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class OrderSummary {
    private Long orderId;
    private LocalDateTime orderTime;
    private String staffName;
    private Integer tableNumber;
    private String customerName;
    private String statusName;
    private Double totalAmount;
}