package nl.novi.sd.carrental.service;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.Vehicle;
import nl.novi.sd.carrental.model.VehiclePhoto;
import nl.novi.sd.carrental.repository.VehicleRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class VehicleServiceImpl implements VehicleService {

    public static final String VEHICLE_NOT_FOUND_MESSAGE = "Vehicle not found";
    private final VehicleRepository VehicleRepository;
    private final VehicleRepository vehicleRepository;

    @Override
    public Vehicle createVehicle(Vehicle vehicle) {
        return VehicleRepository.save(vehicle);
    }

    @Override
    public Vehicle getVehicle(Long id) {
        return VehicleRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_MESSAGE));
    }

    @Override
    public List<Vehicle> getVehicles() {
        return VehicleRepository.findAll();
    }

    @Override
    public Vehicle updateVehicle(Long vehicleId, Vehicle updatedVehicle) {
        Vehicle existingVehicle = VehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_MESSAGE));

        updateExistingVehicle(existingVehicle, updatedVehicle);
        return VehicleRepository.save(existingVehicle);
    }

    @Override
    public void deleteVehicle(Long id) {
        VehicleRepository.deleteById(id);
    }

    @Override
    @Transactional
    public Vehicle addPhotoToVehicle(Long vehicleId, VehiclePhoto photo) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_MESSAGE));

        vehicle.setVehiclePhoto(photo);
        return vehicleRepository.save(vehicle);
    }

    @Override
    @Transactional
    public VehiclePhoto getPhotoFromVehicle(Long vehicleId) {
        Vehicle vehicle = vehicleRepository.findById(vehicleId)
                .orElseThrow(() -> new ResourceNotFoundException(VEHICLE_NOT_FOUND_MESSAGE));

        return vehicle.getVehiclePhoto();
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
