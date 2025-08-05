package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.DevanshNewRMS.NewRMS.Model.Reservation;
import com.DevanshNewRMS.NewRMS.Model.TableInfo;
import com.DevanshNewRMS.NewRMS.Repository.ReservationRepository;
import com.DevanshNewRMS.NewRMS.Repository.TableRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ReservationService {

    @Autowired
    private ReservationRepository reservationRepository;

    @Autowired
    private TableRepository tableRepository;

    public Reservation createReservation(Reservation reservation) {
        TableInfo table = tableRepository.findById(reservation.getTableInfo().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found"));

        // Check for conflicting reservations
        LocalDateTime start = reservation.getReservationTime().minusHours(1);
        LocalDateTime end = reservation.getReservationTime().plusHours(1);
        List<Reservation> conflictingReservations = reservationRepository.findByTableInfoIdAndReservationTimeBetween(table.getId(), start, end);
        if (!conflictingReservations.isEmpty()) {
            throw new IllegalStateException("Table is already reserved for the selected time.");
        }

        return reservationRepository.save(reservation);
    }

    public List<Reservation> getAll() {
        return reservationRepository.findAll();
    }

    public void delete(Long id){
        reservationRepository.deleteById(id);
    }
}