package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Model.OrderStatus;
import com.DevanshNewRMS.NewRMS.Repository.OrderStatusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderStatusServiceImpl implements OrderStatusService {

    private final OrderStatusRepository orderStatusRepository;

    @Override
    public List<OrderStatus> getAllOrderStatuses() {

        return orderStatusRepository.findAll();
    }

    @Override
    public OrderStatus getOrderStatusById(Long id) {
        return orderStatusRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("OrderStatus not found with id: " + id));
    }

    @Override
    public OrderStatus saveOrderStatus(OrderStatus orderStatus) {

        return orderStatusRepository.save(orderStatus);
    }

    @Override
    public OrderStatus updateOrderStatus(Long id, OrderStatus orderStatus) {
        OrderStatus existing = getOrderStatusById(id);
        existing.setStatusName(orderStatus.getStatusName());
        return orderStatusRepository.save(existing);
    }

    @Override
    public void deleteOrderStatus(Long id) {

        orderStatusRepository.deleteById(id);
    }
}