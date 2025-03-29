package nl.novi.sd.carrental.controller;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.model.ParkingLot;
import nl.novi.sd.carrental.repository.ParkingLotRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-lots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotRepository parkingLotRepository;

    @GetMapping
    public List<ParkingLot> getAllParkingLots() {
        return parkingLotRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingLot> getParkingLotById(@PathVariable Long id) {
        return parkingLotRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ParkingLot createParkingLot(@RequestBody ParkingLot parkingLot) {
        return parkingLotRepository.save(parkingLot);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingLot> updateParkingLot(@PathVariable Long id, @RequestBody ParkingLot updatedParkingLot) {
        return parkingLotRepository.findById(id)
                .map(parkingLot -> {
                    // TODO: Setup logic in service layer.
                    return ResponseEntity.ok(parkingLotRepository.save(parkingLot));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingLot(@PathVariable Long id) {
        return parkingLotRepository.findById(id)
                .map(parkingLot -> {
                    parkingLotRepository.delete(parkingLot);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
