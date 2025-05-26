package nl.novi.sd.carrental.security.dto;

import lombok.Data;

@Data
public class AuthenticationRequest {
    private String username;
    private String password;
}

