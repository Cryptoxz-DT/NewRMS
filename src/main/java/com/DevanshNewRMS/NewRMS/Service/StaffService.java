package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import com.DevanshNewRMS.NewRMS.Model.Staff;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class StaffService {
    
    private final StaffRepository staffRepository;
    private final PasswordEncoder passwordEncoder;

    public Staff save(Staff staff){
        // Encode password before saving
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        return staffRepository.save(staff);
    }

    public Staff update(Long id, Staff staff) {
        Staff existingStaff = getById(id);
        existingStaff.setName(staff.getName());
        existingStaff.setUsername(staff.getUsername());
        existingStaff.setRoles(staff.getRoles());
        
        // Only encode password if it's being changed
        if (staff.getPassword() != null && !staff.getPassword().isEmpty()) {
            existingStaff.setPassword(passwordEncoder.encode(staff.getPassword()));
        }
        
        return staffRepository.save(existingStaff);
    }

    public List<Staff> getAll(){
        return staffRepository.findAll();
    }

    public Staff getById(Long id){
        return staffRepository.findById(id).orElseThrow(
                () -> new GlobalExceptionHandler.ResourceNotFoundException("Staff not Found with id:" + id)
        );
    }

    public List<Staff> getStaffName(String name){
        return staffRepository.findByNameContainingIgnoreCase(name);
    }

    public void delete(long id){
        if (!staffRepository.existsById(id)) {
            throw new GlobalExceptionHandler.ResourceNotFoundException("Staff not found with id: " + id);
        }
        staffRepository.deleteById(id);
    }

    public Staff update(Long id, Staff staff) {
        Staff existingStaff = getById(id);
        existingStaff.setName(staff.getName());
        existingStaff.setUsername(staff.getUsername());
        existingStaff.setRoles(staff.getRoles());
        
        // Only encode password if it's being changed
        if (staff.getPassword() != null && !staff.getPassword().isEmpty()) {
            existingStaff.setPassword(passwordEncoder.encode(staff.getPassword()));
        }
        
        return staffRepository.save(existingStaff);
    }

    public String encodePassword(String rawPassword) {
        return passwordEncoder.encode(rawPassword);
    }
}