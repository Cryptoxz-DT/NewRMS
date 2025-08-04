package com.DevanshNewRMS.NewRMS.Repository;

import com.DevanshNewRMS.NewRMS.Model.Order;
import com.DevanshNewRMS.NewRMS.DTO.OrderSummary;
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
            "t.tableNumber, " +
            "SUM(oi.quantity * d.price)) " +
            "FROM Order o " +
            "JOIN o.staff s " +
            "JOIN o.tableInfo t " +
            "JOIN o.orderItems oi " +
            "JOIN oi.dish d " +
            "GROUP BY o.id, o.orderTime, s.name, t.tableNumber " +
            "ORDER BY o.orderTime DESC")
    List<OrderSummary> findOrderSummaries();
}
