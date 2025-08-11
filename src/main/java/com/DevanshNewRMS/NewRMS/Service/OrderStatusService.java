package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Model.OrderStatus;
import java.util.List;

public interface OrderStatusService {
    List<OrderStatus> getAllOrderStatuses();
    OrderStatus getOrderStatusById(Long id);
    OrderStatus saveOrderStatus(OrderStatus orderStatus);
    OrderStatus updateOrderStatus(Long id, OrderStatus orderStatus);
    void deleteOrderStatus(Long id);
}