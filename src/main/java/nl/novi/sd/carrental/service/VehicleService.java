package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.model.Vehicle;

import java.util.List;

public interface VehicleService {
    Vehicle createVehicle(Vehicle vehicle);

    Vehicle getVehicle(Long id);

    List<Vehicle> getVehicles();

    Vehicle updateVehicle(Long id, Vehicle vehicle);

    void deleteVehicle(Long id);
}
