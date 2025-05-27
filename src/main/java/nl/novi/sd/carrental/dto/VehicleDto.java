package nl.novi.sd.carrental.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;

import java.util.List;

@Data
public class VehicleDto {
    private Long id;

    @NotBlank(message = "License plate is required")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "License plate must contain only uppercase letters, numbers, and hyphens")
    private String licensePlate;

    @NotBlank(message = "Make is required")
    private String make;

    @NotBlank(message = "Model is required")
    private String model;

    @NotBlank(message = "Status is required")
    @Pattern(regexp = "AVAILABLE|RENTED_OUT|MAINTENANCE", message = "Status must be AVAILABLE, RENTED_OUT, or MAINTENANCE")
    private String status;

    @NotNull(message = "Price per day is required")
    @Positive(message = "Price per day must be positive")
    private Double pricePerDay;

    private Long parkingSpaceId;
    private List<ReservationDto> reservations;
    private Long photoId;
}
