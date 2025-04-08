package nl.novi.sd.carrental.service;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.Location;
import nl.novi.sd.carrental.repository.LocationRepository;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LocationServiceImpl implements LocationService {
    
    private final LocationRepository locationRepository;
    
    @Override
    public Location getLocation(Long id) {
        return locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("location not found"));
    }

    @Override
    public Location saveLocation(Location location) {
        return locationRepository.save(location);
    }

    @Override
    public Location updateLocation(Long id, Location location) {
        Location existingLocation = locationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("location not found"));
        updateExistingLocationFields(existingLocation, location);
        return locationRepository.save(existingLocation);
    }

    @Override
    public void deleteLocation(Long id) {
        locationRepository.deleteById(id);
    }

    private void updateExistingLocationFields(Location existingLocation, Location updatedLocation) {
        existingLocation.setName(updatedLocation.getName());
        existingLocation.setAddress(updatedLocation.getAddress());
        existingLocation.setPhoneNumber(updatedLocation.getPhoneNumber());
    }
}
