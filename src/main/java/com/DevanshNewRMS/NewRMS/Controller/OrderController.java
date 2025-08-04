package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.DTO.OrderSummary;
import com.DevanshNewRMS.NewRMS.Service.OrderService;
import com.DevanshNewRMS.NewRMS.Model.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order")
public class OrderController {
    @Autowired
    private OrderService orderService;

    @PostMapping
    public Order create(@RequestBody Order order){
        return orderService.save(order);
    }

    @GetMapping
    public List<Order> getAll(){
        return orderService.getAll();
    }

    @GetMapping("/summaries")
    public ResponseEntity<List<OrderSummary>> getOrderSummaries() {
        List<OrderSummary> summaries = orderService.findOrderSummaries();
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/{id}")
    public Order getById(@PathVariable Long id){
        return orderService.getById(id);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        orderService.delete(id);
    }
}
