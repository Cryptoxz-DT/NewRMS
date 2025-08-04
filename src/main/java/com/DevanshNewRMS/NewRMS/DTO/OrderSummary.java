package com.DevanshNewRMS.NewRMS.DTO;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderSummary {

    private Long orderId;
    private LocalDateTime orderTime;
    private String staffName;
    private Integer tableNumber;
    private Double totalOrderAmount;

    public OrderSummary(Long orderId, LocalDateTime orderTime, String staffName, Integer tableNumber, Double totalOrderAmount) {
        this.orderId = orderId;
        this.orderTime = orderTime;
        this.staffName = staffName;
        this.tableNumber = tableNumber;
        this.totalOrderAmount = totalOrderAmount;
    }

}
