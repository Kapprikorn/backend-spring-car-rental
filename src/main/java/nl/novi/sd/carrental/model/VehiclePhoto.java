package nl.novi.sd.carrental.model;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
public class VehiclePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String url;
    private String originalFileName;
    private String contentType;

    @Lob
    private byte[] contents;

    @OneToOne
    private Vehicle vehicle;
}
