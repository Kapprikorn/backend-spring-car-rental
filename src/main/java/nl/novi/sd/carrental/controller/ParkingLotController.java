package nl.novi.sd.carrental.controller;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.dto.ParkingLotDto;
import nl.novi.sd.carrental.model.ParkingLot;
import nl.novi.sd.carrental.service.ParkingLotService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/parking-lots")
@RequiredArgsConstructor
public class ParkingLotController {

    private final ParkingLotService parkingLotService;

    private final ModelMapper mapper = new ModelMapper();

    @ResponseBody
    @GetMapping("/{id}")
    public ParkingLotDto getParkingLotById(@PathVariable Long id) {
        return this.mapToDto(parkingLotService.getParkingLot(id));
    }

    @ResponseBody
    @PostMapping
    public ParkingLotDto createParkingLot(@RequestBody ParkingLotDto parkingLot) {
        return this.mapToDto(parkingLotService.createParkingLot(this.mapToEntity(parkingLot)));
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ParkingLotDto updateParkingLot(@RequestBody ParkingLotDto updatedParkingLot) {
        return this.mapToDto(parkingLotService.updateParkingLot(this.mapToEntity(updatedParkingLot)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteParkingLot(@PathVariable Long id) {
        parkingLotService.deleteParkingLot(id);
    }

    private ParkingLotDto mapToDto(ParkingLot parkingLot) {
        return mapper.map(parkingLot, ParkingLotDto.class);
    }

    private ParkingLot mapToEntity(ParkingLotDto parkingLotDto) {
        return mapper.map(parkingLotDto, ParkingLot.class);
    }
}
