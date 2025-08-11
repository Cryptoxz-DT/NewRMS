package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Model.Order;
import com.DevanshNewRMS.NewRMS.Model.OrderStatus;
import com.DevanshNewRMS.NewRMS.Repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusService {
    private final OrderRepository orderRepository;

    public Order updateStatus(Long orderId, OrderStatus status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        return orderRepository.save(order);
    }

    public List<OrderStatus> getAllOrderStatuses() {

        return List.of((OrderStatus) orderRepository.findAll());
    }
}