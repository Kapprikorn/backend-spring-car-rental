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
        existingVehicle.setLicensePlate(updatedVehicle.getLicensePlate());
        existingVehicle.setMake(updatedVehicle.getMake());
        existingVehicle.setModel(updatedVehicle.getModel());
        existingVehicle.setStatus(updatedVehicle.getStatus());
        existingVehicle.setPricePerDay(updatedVehicle.getPricePerDay());
        existingVehicle.setParkingSpace(updatedVehicle.getParkingSpace());
    }
}
