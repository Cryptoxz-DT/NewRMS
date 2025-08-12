package com.DevanshNewRMS.NewRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.Set;
import java.util.stream.Collectors;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderDTO {
    private Long id;
    private LocalDateTime orderTime;
    private Long tableId;
    private String tableName;
    private Long staffId;
    private String staffName;
    private Long customerId;
    private String customerName;
    private String statusName;
    private Set<OrderItemDTO> orderItems;
    private Double totalAmount;
    
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class OrderItemDTO {
        private Long id;
        private Long dishId;
        private String dishName;
        private Double dishPrice;
        private Integer quantity;
        private Double subtotal;
    }
    
    // Constructor for entity conversion
    public OrderDTO(com.DevanshNewRMS.NewRMS.Model.Order order) {
        this.id = order.getId();
        this.orderTime = order.getOrderTime();
        
        if (order.getTableInfo() != null) {
            this.tableId = order.getTableInfo().getId();
            this.tableName = "Table " + order.getTableInfo().getTableNumber();
        }
        
        if (order.getStaff() != null) {
            this.staffId = order.getStaff().getId();
            this.staffName = order.getStaff().getName();
        }
        
        if (order.getCustomer() != null) {
            this.customerId = order.getCustomer().getId();
            this.customerName = order.getCustomer().getName();
        }
        
        if (order.getStatus() != null) {
            this.statusName = order.getStatus().getStatusName();
        }
        
        if (order.getOrderItems() != null) {
            this.orderItems = order.getOrderItems().stream()
                .map(item -> OrderItemDTO.builder()
                    .id(item.getId())
                    .dishId(item.getDish() != null ? item.getDish().getId() : null)
                    .dishName(item.getDish() != null ? item.getDish().getName() : null)
                    .dishPrice(item.getDish() != null ? item.getDish().getPrice() : 0.0)
                    .quantity(item.getQuantity())
                    .subtotal(item.getDish() != null ? item.getDish().getPrice() * item.getQuantity() : 0.0)
                    .build())
                .collect(Collectors.toSet());
            
            this.totalAmount = this.orderItems.stream()
                .mapToDouble(OrderItemDTO::getSubtotal)
                .sum();
        }
    }
}
