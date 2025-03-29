package nl.novi.sd.carrental.dto;

import lombok.Data;

import java.util.List;

@Data
public class LocationDto {
    private Long id;
    private String name;
    private String address;
    private String phoneNumber;
    private List<ParkingLotDto> parkingLots;
}