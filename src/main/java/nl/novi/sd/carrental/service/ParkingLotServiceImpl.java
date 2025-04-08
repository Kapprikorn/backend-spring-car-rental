package nl.novi.sd.carrental.service;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.ParkingLot;
import nl.novi.sd.carrental.repository.ParkingLotRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ParkingLotServiceImpl implements ParkingLotService {

    private final ParkingLotRepository parkingLotRepository;

    @Override
    public ParkingLot getParkingLot(Long id) {
        return parkingLotRepository.findById(id).
                orElseThrow(() -> new ResourceNotFoundException("Parking lot not found"));
    }

    @Override
    public ParkingLot createParkingLot(ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }

    @Override
    public ParkingLot updateParkingLot(ParkingLot parkingLot) {
        ParkingLot existingParkingLot = parkingLotRepository.findById(parkingLot.getId())
                .orElseThrow(() -> new ResourceNotFoundException("Parking lot not found"));
        updateParkingLotFields(existingParkingLot, parkingLot);
        return parkingLotRepository.save(existingParkingLot);
    }

    @Override
    public void deleteParkingLot(Long id) {
        parkingLotRepository.deleteById(id);
    }

    private void updateParkingLotFields(ParkingLot existingParkingLot, ParkingLot updatedParkingLot) {
        existingParkingLot.setName(updatedParkingLot.getName());
        existingParkingLot.setLocation(updatedParkingLot.getLocation());
        existingParkingLot.setParkingSpaces(updatedParkingLot.getParkingSpaces());
    }
}
