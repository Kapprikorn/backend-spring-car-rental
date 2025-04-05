package nl.novi.sd.carrental.controller;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.dto.VehicleDto;
import nl.novi.sd.carrental.model.Vehicle;
import nl.novi.sd.carrental.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Validated
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    private final ModelMapper mapper = new ModelMapper();

    @ResponseBody
    @GetMapping
    public List<VehicleDto> getAllVehicles() {
        return vehicleService.getVehicles().stream().map(this::mapToDto).toList();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public VehicleDto getVehicleById(@PathVariable Long id) {
        return this.mapToDto(vehicleService.getVehicle(id));
    }

    @ResponseBody
    @PostMapping
    public List<VehicleDto> createVehicle(@RequestBody List<VehicleDto> vehicleDtos) {
        return vehicleDtos.stream().map(vehicleDto ->
                this.mapToDto(
                        vehicleService.createVehicle(
                                this.mapToEntity(vehicleDto)
                        )
                )
        ).toList();
    }

    @ResponseBody
    @PutMapping("/{id}")
    public VehicleDto updateVehicle(@PathVariable Long id, @RequestBody VehicleDto updatedVehicleDto) {
        return this.mapToDto(vehicleService.updateVehicle(id, this.mapToEntity(updatedVehicleDto)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
    }

    private VehicleDto mapToDto(Vehicle vehicle) {
        return mapper.map(vehicle, VehicleDto.class);
    }

    private Vehicle mapToEntity(VehicleDto vehicleDto) {
        return mapper.map(vehicleDto, Vehicle.class);
    }
}
