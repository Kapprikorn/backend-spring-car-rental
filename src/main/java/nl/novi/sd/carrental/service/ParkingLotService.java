package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.model.ParkingLot;

public interface ParkingLotService {

    ParkingLot getParkingLot(Long id);

    ParkingLot createParkingLot(ParkingLot parkingLot);

    ParkingLot updateParkingLot(ParkingLot parkingLot);

    void deleteParkingLot(Long id);
}
