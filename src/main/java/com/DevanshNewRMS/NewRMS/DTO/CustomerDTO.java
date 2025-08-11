package com.DevanshNewRMS.NewRMS.DTO;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CustomerDTO {
    private String name;
    private String contactNumber;
    private String email;
}