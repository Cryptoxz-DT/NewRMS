package com.DevanshNewRMS.NewRMS.controller;

import com.DevanshNewRMS.NewRMS.Service.StaffService;
import com.DevanshNewRMS.NewRMS.model.Staff;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/staff")
public class StaffController {
    @Autowired
    private StaffService staffService;

    @PostMapping
    public Staff create(@RequestBody Staff staff){
        return staffService.save(staff);
    }

    @GetMapping
    public List<Staff> getAll(){
        return staffService.getAll();
    }

    @GetMapping("/{id}")
    public Staff getById(Long id){
        return staffService.getById(id);
    }

    @DeleteMapping("/{id}")
    public void delete(Long id){
        staffService.delete(id);
    }
}
