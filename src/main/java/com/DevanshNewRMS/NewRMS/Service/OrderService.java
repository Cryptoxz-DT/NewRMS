package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.DTO.OrderSummary;
import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Repository.OrderRepository;
import com.DevanshNewRMS.NewRMS.Model.Order;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {
    
    private final OrderRepository orderRepository;

    public List<OrderSummary> findOrderSummaries() {
        return orderRepository.findOrderSummaries();
    }

    public Order save(Order order){
        if (order.getOrderTime() == null) {
            order.setOrderTime(LocalDateTime.now());
        }
        return orderRepository.save(order);
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

    public Order getById(Long id){
        return orderRepository.findById(id).orElseThrow(
                ()-> new GlobalExceptionHandler.ResourceNotFoundException("Order not found with id: " + id));
    }

    public void delete(Long id){
        if (!orderRepository.existsById(id)) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("Order not found with id: " + id);
        }
        orderRepository.deleteById(id);
    }
}
