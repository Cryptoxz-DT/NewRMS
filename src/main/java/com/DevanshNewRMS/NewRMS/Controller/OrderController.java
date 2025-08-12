package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.DTO.OrderSummary;
import com.DevanshNewRMS.NewRMS.Service.OrderService;
import com.DevanshNewRMS.NewRMS.Model.Order;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;

    @PostMapping
    public ResponseEntity<Order> createOrder(@Valid @RequestBody Order order){
        Order savedOrder = orderService.save(order);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedOrder);
    }

    @GetMapping
    public ResponseEntity<List<Order>> getAllOrders(){
        return ResponseEntity.ok(orderService.getAll());
    }

    @GetMapping("/summaries")
    public ResponseEntity<List<OrderSummary>> getOrderSummaries() {
        List<OrderSummary> summaries = orderService.findOrderSummaries();
        return ResponseEntity.ok(summaries);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Order> getOrderById(@PathVariable Long id){
        return ResponseEntity.ok(orderService.getById(id));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrder(@PathVariable Long id){
        orderService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
