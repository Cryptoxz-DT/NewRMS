package com.DevanshNewRMS.NewRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor  // This will generate the constructor we need
public class OrderSummary {

    private Long orderId;
    private LocalDateTime orderTime;
    private String staffName;
    private Integer tableNumber;
    private Double totalOrderAmount;


}