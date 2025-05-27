package nl.novi.sd.carrental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Data;

import java.util.List;

@Data
@Entity
public class Location {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "Name is required")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "Address is required")
    @Column(nullable = false)
    private String address;

    @NotBlank(message = "Phone number is required")
    @Pattern(regexp = "^[0-9+\\-\\s()]*$", message = "Phone number must contain only digits, +, -, spaces, and parentheses")
    @Column(nullable = false)
    private String phoneNumber;

    @OneToMany(mappedBy = "location", cascade = CascadeType.ALL)
    private List<ParkingLot> parkingLots;

    @OneToMany
    @JoinColumn(name = "location_id")
    private List<User> users;
}
