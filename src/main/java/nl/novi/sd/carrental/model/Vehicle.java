package nl.novi.sd.carrental.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String licensePlate;
    private String make;
    private String model;

    @Enumerated(EnumType.STRING)
    private StatusCode status;

    private Double pricePerDay;

    @OneToOne
    @JoinColumn(name = "parking_space_id", unique = true)
    // One vehicle always has a default parking space reserved.
    private ParkingSpace parkingSpace;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Reservation> reservations;
}