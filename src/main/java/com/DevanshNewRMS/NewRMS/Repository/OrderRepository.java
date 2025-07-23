package com.DevanshNewRMS.NewRMS.Repository;
import com.DevanshNewRMS.NewRMS.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order,Long> {
}
