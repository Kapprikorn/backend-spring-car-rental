package nl.novi.sd.carrental.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String address;
    private String phoneNumber;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<ParkingLot> parkingLots;

    @OneToMany
    @JoinColumn(name = "location_id")
    private List<Employee> employees;
}
