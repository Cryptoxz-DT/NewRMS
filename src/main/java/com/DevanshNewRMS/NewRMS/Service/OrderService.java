package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Repository.OrderRepository;
import com.DevanshNewRMS.NewRMS.model.Order;
import org.aspectj.weaver.ast.Or;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class OrderService {
    @Autowired
    private OrderRepository orderRepository;

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
