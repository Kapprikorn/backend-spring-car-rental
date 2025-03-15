package nl.novi.sd.carrental.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class ParkingSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String location;
    private String size;
    private boolean occupied;

    @ManyToOne
    @JoinColumn(name = "parking_lot_id")
    private ParkingLot parkingLot;

    @OneToOne(mappedBy = "parkingSpace")
    private Vehicle vehicle;

}
