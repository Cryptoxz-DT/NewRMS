package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler.ResourceNotFoundException;
import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler.BusinessException;
import com.DevanshNewRMS.NewRMS.Model.Reservation;
import com.DevanshNewRMS.NewRMS.Model.TableInfo;
import com.DevanshNewRMS.NewRMS.Repository.ReservationRepository;
import com.DevanshNewRMS.NewRMS.Repository.TableRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class ReservationService {

    private final ReservationRepository reservationRepository;
    private final TableRepository tableRepository;

    @Transactional
    public Reservation createReservation(Reservation reservation) {
        log.debug("Creating reservation for table: {}", reservation.getTableInfo() != null ? reservation.getTableInfo().getId() : "unknown");
        
        TableInfo table = tableRepository.findById(reservation.getTableInfo().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Table not found with id: " + reservation.getTableInfo().getId()));

        // Check for conflicting reservations
        LocalDateTime start = reservation.getReservationTime().minusHours(2);
        LocalDateTime end = reservation.getReservationTime().plusHours(2);
        
        List<Reservation> conflictingReservations = reservationRepository
                .findByTableInfoIdAndReservationTimeBetween(table.getId(), start, end);
        
        if (!conflictingReservations.isEmpty()) {
            log.warn("Reservation conflict detected for table {} at time {}", table.getId(), reservation.getReservationTime());
            throw new BusinessException("Table is already reserved for the selected time. Please choose a different time or table.");
        }

        Reservation savedReservation = reservationRepository.save(reservation);
        log.info("Reservation created successfully with ID: {}", savedReservation.getId());
        return savedReservation;
    }

    public List<Reservation> getAll() {
        log.debug("Fetching all reservations");
        return reservationRepository.findAll();
    }

    @Transactional
    public void delete(Long id){
        log.debug("Attempting to delete reservation with ID: {}", id);
        
        if (!reservationRepository.existsById(id)) {
            throw new ResourceNotFoundException("Reservation not found with id: " + id);
        }
        
        reservationRepository.deleteById(id);
        log.info("Reservation deleted successfully with ID: {}", id);
    }
}
