package nl.novi.sd.carrental.model;

import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
@Entity
public class Employee extends User {
    private String employeeId;
    private String position;
}
