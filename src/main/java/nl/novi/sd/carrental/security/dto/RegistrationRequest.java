package nl.novi.sd.carrental.security.dto;

import lombok.Data;

@Data
public class RegistrationRequest {
    private String name;
    private String username;
    private String password;
    private String email;
}
