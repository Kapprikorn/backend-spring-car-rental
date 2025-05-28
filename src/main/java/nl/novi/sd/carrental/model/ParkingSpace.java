package nl.novi.sd.carrental.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Length(max = 255, message = "Location can't exceed 255 characters")
    private String location;
    @Length(max = 255, message = "Size can't exceed 255 characters")
    private String size;
    private boolean occupied;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;

    @OneToOne(mappedBy = "parkingSpace")
    private Vehicle vehicle;

}
