package nl.novi.sd.carrental.dto;

import lombok.Data;

import java.util.Date;

@Data
public class ReservationDto {
    private Long id;
    private Date startDate;
    private Date endDate;
    private Double totalPrice;
    private Long userId;
    private Long vehicleId;
}