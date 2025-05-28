package nl.novi.sd.carrental.model;

import jakarta.persistence.*;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

@Data
@Entity
public class VehiclePhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Length(max = 255, message = "URL can't exceed 255 characters")
    private String url;
    @Length(max = 255, message = "OriginalFileName can't exceed 255 characters")
    private String originalFileName;
    @Length(max = 255, message = "ContentType can't exceed 255 characters")
    private String contentType;

    @Lob
    private byte[] contents;

    @OneToOne
    private Vehicle vehicle;
}
