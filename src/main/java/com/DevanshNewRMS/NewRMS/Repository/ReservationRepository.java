package com.DevanshNewRMS.NewRMS.Repository;

import com.DevanshNewRMS.NewRMS.Model.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import java.time.LocalDateTime;
import java.util.List;

public interface ReservationRepository extends JpaRepository<Reservation, Long> {
    List<Reservation> findByTableInfoIdAndReservationTimeBetween(Long tableId, LocalDateTime start, LocalDateTime end);
}