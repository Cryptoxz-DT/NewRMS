package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.Model.OrderStatus;
import com.DevanshNewRMS.NewRMS.Service.OrderStatusService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/order-statuses")
@RequiredArgsConstructor
public class OrderStatusController {

    private final OrderStatusService orderStatusService;

    @GetMapping
    public ResponseEntity<List<OrderStatus>> getAllOrderStatuses() {
        return ResponseEntity.ok(orderStatusService.getAllOrderStatuses());
    }

    @GetMapping("/{id}")
    public ResponseEntity<OrderStatus> getOrderStatusById(@PathVariable Long id) {
        return ResponseEntity.ok(orderStatusService.getOrderStatusById(id));
    }

    @PostMapping
    public ResponseEntity<OrderStatus> saveOrderStatus(@RequestBody OrderStatus orderStatus) {
        return ResponseEntity.ok(orderStatusService.saveOrderStatus(orderStatus));
    }

    @PutMapping("/{id}")
    public ResponseEntity<OrderStatus> updateOrderStatus(@PathVariable Long id, @RequestBody OrderStatus orderStatus) {
        return ResponseEntity.ok(orderStatusService.updateOrderStatus(id, orderStatus));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteOrderStatus(@PathVariable Long id) {
        orderStatusService.deleteOrderStatus(id);
        return ResponseEntity.noContent().build();
    }
}
