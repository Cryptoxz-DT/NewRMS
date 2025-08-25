package com.DevanshNewRMS.NewRMS.DTO;

import lombok.Data;

@Data
public class SimpleLoginRequest {
    private String username;
    private String password;
}