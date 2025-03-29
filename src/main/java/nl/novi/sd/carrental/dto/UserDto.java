package nl.novi.sd.carrental.dto;

import lombok.Data;

import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String name;
    private String email;
    private String role;
    private List<ReservationDto> reservations;
}