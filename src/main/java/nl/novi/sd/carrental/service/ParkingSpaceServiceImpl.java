package nl.novi.sd.carrental.service;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.ParkingSpace;
import nl.novi.sd.carrental.repository.ParkingSpaceRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingSpaceServiceImpl implements ParkingSpaceService {

    private final ParkingSpaceRepository parkingSpaceRepository;

    @Override
    public ParkingSpace getParkingSpace(Long id) {
        return parkingSpaceRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Parking space not found"));
    }

    @Override
    public ParkingSpace createParkingSpace(ParkingSpace parkingSpace) {
        return parkingSpaceRepository.save(parkingSpace);
    }

    @Override
    public ParkingSpace updateParkingSpace(ParkingSpace parkingSpace) {
        ParkingSpace existingParkingSpace = parkingSpaceRepository.findById(parkingSpace.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Parking space not found"));
        updateExistingParkingSpaceFields(existingParkingSpace, parkingSpace);
        return parkingSpaceRepository.save(existingParkingSpace);
    }

    @Override
    public void deleteParkingSpace(Long id) {
        parkingSpaceRepository.deleteById(id);
    }

    private void updateExistingParkingSpaceFields(ParkingSpace existingParkingSpace, ParkingSpace updatedParkingSpace) {
        existingParkingSpace.setLocation(updatedParkingSpace.getLocation());
        existingParkingSpace.setSize(updatedParkingSpace.getSize());
        existingParkingSpace.setOccupied(updatedParkingSpace.isOccupied());
        existingParkingSpace.setParkingLot(updatedParkingSpace.getParkingLot());
        existingParkingSpace.setVehicle(updatedParkingSpace.getVehicle());
    }
}
