package nl.novi.sd.carrental.service;


import nl.novi.sd.carrental.model.ParkingSpace;

public interface ParkingSpaceService {

    ParkingSpace getParkingSpace(Long id);

    ParkingSpace createParkingSpace(ParkingSpace parkingSpace);

    ParkingSpace updateParkingSpace(ParkingSpace parkingSpace);

    void deleteParkingSpace(Long id);
}
