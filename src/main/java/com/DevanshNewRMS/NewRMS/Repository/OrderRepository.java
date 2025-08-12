package com.DevanshNewRMS.NewRMS.Repository;

import com.DevanshNewRMS.NewRMS.Model.Order;
import com.DevanshNewRMS.NewRMS.DTO.OrderSummary;
import com.DevanshNewRMS.NewRMS.Model.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT new com.DevanshNewRMS.NewRMS.DTO.OrderSummary(" +
            "o.id, " +
            "o.orderTime, " +
            "s.name, " +
            "COALESCE(t.tableNumber, 0), " +
            "COALESCE(c.name, 'Walk-in'), " +
            "os.statusName, " +
            "COALESCE(SUM(oi.quantity * d.price), 0.0)) " +
            "FROM Order o " +
            "JOIN o.staff s " +
            "LEFT JOIN o.tableInfo t " +
            "LEFT JOIN o.customer c " +
            "JOIN o.status os " +
            "LEFT JOIN o.orderItems oi " +
            "LEFT JOIN oi.dish d " +
            "GROUP BY o.id, o.orderTime, s.name, t.tableNumber, c.name, os.statusName " +
            "ORDER BY o.orderTime DESC")
    List<OrderSummary> findOrderSummaries();

    // These methods might be used in future features, keeping them for now
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByCustomerId(Long customerId);
}