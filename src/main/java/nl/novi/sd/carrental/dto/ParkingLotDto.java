package nl.novi.sd.carrental.dto;

import lombok.Data;

import java.util.List;

@Data
public class ParkingLotDto {
    private Long id;
    private String name;
    private Long locationId;
    private List<ParkingSpaceDto> parkingSpaces;
}