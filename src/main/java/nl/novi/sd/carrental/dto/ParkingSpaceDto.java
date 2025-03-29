package nl.novi.sd.carrental.dto;

import lombok.Data;

@Data
public class ParkingSpaceDto {
    private Long id;
    private String location;
    private String size;
    private boolean occupied;
    private Long parkingLotId;
    private VehicleDto vehicle;
}