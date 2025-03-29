package nl.novi.sd.carrental.dto;

import lombok.Data;

import java.util.List;

@Data
public class VehicleDto {
    private Long id;
    private String licensePlate;
    private String make;
    private String model;
    private String status;
    private Double pricePerDay;
    private Long parkingSpaceId;
    private List<ReservationDto> reservations;
}