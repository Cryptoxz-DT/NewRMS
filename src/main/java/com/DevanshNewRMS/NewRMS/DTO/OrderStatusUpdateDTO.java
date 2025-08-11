package com.DevanshNewRMS.NewRMS.DTO;

import com.DevanshNewRMS.NewRMS.Model.OrderStatus;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderStatusUpdateDTO {
    private Long orderId;
    private OrderStatus status;
}