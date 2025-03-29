package nl.novi.sd.carrental.controller;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.model.ParkingSpace;
import nl.novi.sd.carrental.repository.ParkingSpaceRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/parking-spaces")
@RequiredArgsConstructor
public class ParkingSpaceController {

    private final ParkingSpaceRepository parkingSpaceRepository;

    @GetMapping
    public List<ParkingSpace> getAllParkingSpaces() {
        return parkingSpaceRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<ParkingSpace> getParkingSpaceById(@PathVariable Long id) {
        return parkingSpaceRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ParkingSpace createParkingSpace(@RequestBody ParkingSpace parkingSpace) {
        return parkingSpaceRepository.save(parkingSpace);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ParkingSpace> updateParkingSpace(@PathVariable Long id, @RequestBody ParkingSpace updatedParkingSpace) {
        return parkingSpaceRepository.findById(id)
                .map(parkingSpace -> {
                    // TODO: Setup logic in service layer.
                    return ResponseEntity.ok(parkingSpaceRepository.save(parkingSpace));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteParkingSpace(@PathVariable Long id) {
        return parkingSpaceRepository.findById(id)
                .map(parkingSpace -> {
                    parkingSpaceRepository.delete(parkingSpace);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
