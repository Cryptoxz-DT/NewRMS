package com.DevanshNewRMS.NewRMS.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.DevanshNewRMS.NewRMS.Model.Reservation;
import com.DevanshNewRMS.NewRMS.Service.ReservationService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api/reservations")
 @PreAuthorize("hasAnyRole('ADMIN', 'WAITER')") // COMMENTED OUT FOR DEBUGGING
public class ReservationController {

    @Autowired
    private ReservationService reservationService;

    @PostMapping
    public Reservation create(@Valid @RequestBody Reservation reservation) {
        return reservationService.createReservation(reservation);
    }

    @GetMapping("/{id}")
    public List<Reservation> getAll(Long id) {
        return reservationService.getAll();
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        reservationService.delete(id);
    }
}