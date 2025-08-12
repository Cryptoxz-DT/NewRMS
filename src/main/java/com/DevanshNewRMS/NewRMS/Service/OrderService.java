package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.DTO.OrderSummary;
import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Repository.OrderRepository;
import com.DevanshNewRMS.NewRMS.Model.Order;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class OrderService {
    
    private final OrderRepository orderRepository;

    public List<OrderSummary> findOrderSummaries() {
        return orderRepository.findOrderSummaries();
    }

    @Transactional
    public Order save(Order order){
        log.debug("Creating new order for table: {}", order.getTableInfo() != null ? order.getTableInfo().getId() : "unknown");
        
        if (order.getOrderTime() == null) {
            order.setOrderTime(LocalDateTime.now());
        }
        
        Order savedOrder = orderRepository.save(order);
        log.info("Order created successfully with ID: {}", savedOrder.getId());
        return savedOrder;
    }

    public List<Order> getAll(){
        log.debug("Fetching all orders");
        return orderRepository.findAll();
    }

    public Order getById(Long id){
        log.debug("Fetching order with ID: {}", id);
        return orderRepository.findById(id).orElseThrow(
                ()-> new GlobalExceptionHandler.ResourceNotFoundException("Order not found with id: " + id));
    }

    @Transactional
    public void delete(Long id){
        log.debug("Attempting to delete order with ID: {}", id);
        
        if (!orderRepository.existsById(id)) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("Order not found with id: " + id);
        }
        
        orderRepository.deleteById(id);
        log.info("Order deleted successfully with ID: {}", id);
    }
}
