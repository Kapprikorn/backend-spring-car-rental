package nl.novi.sd.carrental.controller;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.dto.LocationDto;
import nl.novi.sd.carrental.model.Location;
import nl.novi.sd.carrental.service.LocationService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
@RequiredArgsConstructor
public class LocationController {

    private final LocationService locationService;

    private final ModelMapper mapper = new ModelMapper();

    @ResponseBody
    @GetMapping("/{id}")
    public LocationDto getLocationById(@PathVariable Long id) {
        return this.mapToDto(locationService.getLocation(id));
    }

    @ResponseBody
    @PostMapping
    public LocationDto createLocation(@RequestBody LocationDto locationDto) {
        return this.mapToDto(
                locationService.saveLocation(this.mapToEntity(locationDto))
            );
    }

    @ResponseBody
    @PutMapping("/{id}")
    public LocationDto updateLocation(@PathVariable Long id, @RequestBody LocationDto updatedLocation) {
        return this.mapToDto(
                locationService.updateLocation(id, this.mapToEntity(updatedLocation))
        );
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/{id}")
    public void deleteLocation(@PathVariable Long id) {
        locationService.deleteLocation(id);
    }

    private LocationDto mapToDto(Location location) {
        return mapper.map(location, LocationDto.class);
    }

    private Location mapToEntity(LocationDto locationDto) {
        return mapper.map(locationDto, Location.class);
    }
}
