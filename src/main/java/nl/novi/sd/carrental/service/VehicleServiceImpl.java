package nl.novi.sd.carrental.service;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.Vehicle;
import nl.novi.sd.carrental.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    private final VehicleRepository VehicleRepository;

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        return VehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getVehicle(Long id) {
        return VehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));
    }

    @Override
    public List<Vehicle> getVehicles() {
        return VehicleRepository.findAll();
    }

    @Override
    public Vehicle updateVehicle(Long vehicleId, Vehicle updatedVehicle) {
        Vehicle existingVehicle = VehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException("Vehicle not found"));

        updateExistingVehicle(existingVehicle, updatedVehicle);
        return VehicleRepository.save(existingVehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        VehicleRepository.deleteById(id);
    }

    private void updateExistingVehicle(Vehicle existingVehicle, Vehicle updatedVehicle) {
        if (updatedVehicle.getLicensePlate() != null) existingVehicle.setLicensePlate(updatedVehicle.getLicensePlate());
        if (updatedVehicle.getMake() != null) existingVehicle.setMake(updatedVehicle.getMake());
        if (updatedVehicle.getModel() != null) existingVehicle.setModel(updatedVehicle.getModel());
        if (updatedVehicle.getStatus() != null) existingVehicle.setStatus(updatedVehicle.getStatus());
        if (updatedVehicle.getPricePerDay() != null) existingVehicle.setPricePerDay(updatedVehicle.getPricePerDay());
        if (updatedVehicle.getParkingSpace() != null) existingVehicle.setParkingSpace(updatedVehicle.getParkingSpace());

    }
}
