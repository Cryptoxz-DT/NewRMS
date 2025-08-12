package com.DevanshNewRMS.NewRMS.DTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StaffDTO {
    private Long id;
    private String name;
    private String username;
    private String roles;
    
    // Constructor for entity conversion
    public StaffDTO(com.DevanshNewRMS.NewRMS.Model.Staff staff) {
        this.id = staff.getId();
        this.name = staff.getName();
        this.username = staff.getUsername();
        this.roles = staff.getRoles();
    }
}
