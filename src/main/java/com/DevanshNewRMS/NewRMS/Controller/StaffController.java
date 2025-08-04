package com.DevanshNewRMS.NewRMS.Controller;

import com.DevanshNewRMS.NewRMS.Service.StaffService;
import com.DevanshNewRMS.NewRMS.Model.Staff;
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

    @GetMapping("/staffName/{staffName}")
    public List<Staff> getStaffName(@PathVariable("staffName") String name){

        return staffService.getStaffName(name);
    }

    @DeleteMapping("/{id}")
    public void delete(Long id){
        staffService.delete(id);
    }
}
