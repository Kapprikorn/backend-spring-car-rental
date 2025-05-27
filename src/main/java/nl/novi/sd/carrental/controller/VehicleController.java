package nl.novi.sd.carrental.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.dto.VehicleDto;
import nl.novi.sd.carrental.model.Vehicle;
import nl.novi.sd.carrental.model.VehiclePhoto;
import nl.novi.sd.carrental.service.VehiclePhotoService;
import nl.novi.sd.carrental.service.VehicleService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.IOException;
import java.net.URI;
import java.util.List;
import java.util.Objects;

@Validated
@RestController
@RequestMapping("/vehicles")
@RequiredArgsConstructor
public class VehicleController {

    private final VehicleService vehicleService;

    private final VehiclePhotoService vehiclePhotoService;

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
    public List<VehicleDto> createVehicle(@Valid @RequestBody List<VehicleDto> vehicleDtos) {
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
    public VehicleDto updateVehicle(@PathVariable Long id,
                                    @Valid @RequestBody VehicleDto updatedVehicleDto) {
        return this.mapToDto(vehicleService.updateVehicle(id, this.mapToEntity(updatedVehicleDto)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteVehicle(@PathVariable Long id) {
        vehicleService.deleteVehicle(id);
    }

    @GetMapping("/{vehicleId}/photo")
    public ResponseEntity<byte[]> getVehiclePhoto(@PathVariable Long vehicleId) {
        VehiclePhoto photo = vehicleService.getPhotoFromVehicle(vehicleId);

        MediaType mediaType;
        try {
            mediaType = MediaType.parseMediaType(photo.getContentType());
        } catch (IllegalArgumentException e) {
            mediaType = MediaType.APPLICATION_OCTET_STREAM;
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(mediaType);
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=" + photo.getOriginalFileName());

        return new ResponseEntity<>(photo.getContents(), headers, HttpStatus.OK);
    }

    @PostMapping("/{id}/photo")
    public ResponseEntity<VehicleDto> addPhotoToVehicle(@PathVariable Long id,
                                        @RequestBody MultipartFile file) throws IOException {
        String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/vehicles/")
                .path(Objects.requireNonNull(id.toString()))
                .path("/photo")
                .toUriString();

        VehiclePhoto photo = vehiclePhotoService.storePhoto(file, url);

        VehicleDto vehicleDto = this.mapToDto(vehicleService.addPhotoToVehicle(id, photo));

        return ResponseEntity.created(URI.create(url)).body(vehicleDto);
    }

    private VehicleDto mapToDto(Vehicle vehicle) {
        return mapper.map(vehicle, VehicleDto.class);
    }

    private Vehicle mapToEntity(VehicleDto vehicleDto) {
        return mapper.map(vehicleDto, Vehicle.class);
    }
}
