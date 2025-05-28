package nl.novi.sd.carrental.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import java.util.List;

@Data
@Entity
public class Vehicle {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "License plate is required")
    @Length(max = 255, message = "LicensePlate can't exceed 255 characters")
    @Pattern(regexp = "^[A-Z0-9-]+$", message = "License plate must contain only uppercase letters, numbers, and hyphens")
    @Column(nullable = false, unique = true)
    private String licensePlate;

    @NotBlank(message = "Make is required")
    @Length(max = 255, message = "Make can't exceed 255 characters")
    @Column(nullable = false)
    private String make;

    @NotBlank(message = "Model is required")
    @Length(max = 255, message = "Model can't exceed 255 characters")
    @Column(nullable = false)
    private String model;

    @NotNull(message = "Status is required")
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusCode status;

    @NotNull(message = "Price per day is required")
    @Positive(message = "Price per day must be positive")
    @Column(nullable = false)
    private Double pricePerDay;

    @OneToOne
    @JoinColumn(name = "parking_space_id", unique = true)
    // One vehicle always has a default parking space reserved.
    private ParkingSpace parkingSpace;

    @OneToMany(mappedBy = "vehicle", cascade = CascadeType.ALL)
    private List<Reservation> reservations;

    @OneToOne
    @JoinColumn(name = "vehicle_photo_id")
    private VehiclePhoto vehiclePhoto;
}
