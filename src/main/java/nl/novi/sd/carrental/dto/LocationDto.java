package nl.novi.sd.carrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
public class LocationDto {
    private Long id;

    @NotBlank(message = "Name is required")
    private String name;

    @NotBlank(message = "Address is required")
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Phone number must contain only digits, +, -, spaces, and parentheses")
    private String phoneNumber;

    private List<ParkingLotDto> parkingLots;
}
