package com.DevanshNewRMS.NewRMS.Service;

import com.DevanshNewRMS.NewRMS.Exception.GlobalExceptionHandler;
import com.DevanshNewRMS.NewRMS.Repository.StaffRepository;
import com.DevanshNewRMS.NewRMS.Model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StaffService {
    @Autowired
    private StaffRepository staffRepository;
    private PasswordEncoder passwordEncoder;

    public Staff save(Staff staff){
        staff.setPassword(passwordEncoder.encode(staff.getPassword()));
        return staffRepository.save(staff);
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
        staffRepository.deleteById(id);
    }
}
