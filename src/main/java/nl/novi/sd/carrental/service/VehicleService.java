package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.model.Vehicle;

import java.util.List;

public interface VehicleService {
    public Vehicle createVehicle(Vehicle vehicle);

    public Vehicle getVehicle(Long id);

    public List<Vehicle> getVehicles();

    public Vehicle updateVehicle(Long id, Vehicle vehicle);

    public void deleteVehicle(Long id);
}
