package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.DTO.OrderSummary;
import com.DevanshNewRMS.NewRMS.Repository.OrderRepository;
import com.DevanshNewRMS.NewRMS.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

    public OrderService(OrderRepository orderRepository) {
        this.orderRepository = orderRepository;
    }

    public List<OrderSummary> findOrderSummaries() {
        // This line executes the complex query you created.
        return orderRepository.findOrderSummaries();
    }

    public Order save(Order order){
        return orderRepository.save(order);
    }

    public List<Order> getAll(){
        return orderRepository.findAll();
    }

    public Order getById(Long id){
        return orderRepository.findById(id).orElse(null);
    }

    public void delete(Long id){
        orderRepository.deleteById(id);
    }
}
