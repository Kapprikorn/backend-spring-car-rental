package nl.novi.sd.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.dto.ParkingSpaceDto;
import nl.novi.sd.carrental.model.ParkingSpace;
import nl.novi.sd.carrental.service.ParkingSpaceService;
import org.modelmapper.ModelMapper;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking-spaces")
@RequiredArgsConstructor
public class ParkingSpaceController {

    private final ParkingSpaceService parkingSpaceService;

    private final ModelMapper mapper = new ModelMapper();

    @ResponseBody
    @GetMapping("/{id}")
    public ParkingSpaceDto getParkingSpaceById(@PathVariable Long id) {
        return this.mapToDto(parkingSpaceService.getParkingSpace(id));
    }

    @ResponseBody
    @PostMapping
    public ParkingSpaceDto createParkingSpace(@Valid @RequestBody ParkingSpaceDto parkingSpaceDto) {
        return this.mapToDto(
                parkingSpaceService.createParkingSpace(
                        this.mapToEntity(parkingSpaceDto)));
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ParkingSpaceDto updateParkingSpace(@Valid @RequestBody ParkingSpaceDto updatedParkingSpace) {
        return this.mapToDto(parkingSpaceService.updateParkingSpace(this.mapToEntity(updatedParkingSpace)));
    }

    @DeleteMapping("/{id}")
    public void deleteParkingSpace(@PathVariable Long id) {
        parkingSpaceService.deleteParkingSpace(id);
    }

    private ParkingSpaceDto mapToDto(ParkingSpace parkingSpace) {
        return mapper.map(parkingSpace, ParkingSpaceDto.class);
    }

    private ParkingSpace mapToEntity(ParkingSpaceDto parkingSpaceDto) {
        return mapper.map(parkingSpaceDto, ParkingSpace.class);
    }
}
