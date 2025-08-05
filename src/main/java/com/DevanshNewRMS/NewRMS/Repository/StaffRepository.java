package com.DevanshNewRMS.NewRMS.Repository;

import com.DevanshNewRMS.NewRMS.Model.Staff;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StaffRepository extends JpaRepository<Staff, Long> {
    List<Staff> findByNameContainingIgnoreCase(String name);
    Optional<Staff> findByUsername(String username);
}

