package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.model.Location;

public interface LocationService {

    Location getLocation(Long id);

    Location saveLocation(Location location);

    Location updateLocation(Long id, Location location);

    void deleteLocation(Long id);
}
