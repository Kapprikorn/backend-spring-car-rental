package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.Location;
import nl.novi.sd.carrental.repository.LocationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class LocationServiceImplTest {

    @Mock
    private LocationRepository locationRepository;

    @InjectMocks
    private LocationServiceImpl locationService;

    private Location testLocation;

    @BeforeEach
    void setUp() {
        // Create test location
        testLocation = new Location();
        testLocation.setId(1L);
        testLocation.setName("Test Location");
        testLocation.setAddress("123 Test Street");
        testLocation.setPhoneNumber("123-456-7890");
    }

    @Test
    void getLocation_WithValidId_ShouldReturnLocation() {
        // Arrange
        when(locationRepository.findById(1L)).thenReturn(Optional.of(testLocation));

        // Act
        Location result = locationService.getLocation(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testLocation.getId(), result.getId());
        assertEquals(testLocation.getName(), result.getName());
        assertEquals(testLocation.getAddress(), result.getAddress());
        assertEquals(testLocation.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void getLocation_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(locationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> locationService.getLocation(999L)
        );
        assertEquals("location not found", exception.getMessage());
    }

    @Test
    void saveLocation_ShouldReturnSavedLocation() {
        // Arrange
        when(locationRepository.save(any(Location.class))).thenReturn(testLocation);

        // Act
        Location result = locationService.saveLocation(testLocation);

        // Assert
        assertNotNull(result);
        assertEquals(testLocation.getId(), result.getId());
        assertEquals(testLocation.getName(), result.getName());
        verify(locationRepository, times(1)).save(testLocation);
    }

    @Test
    void updateLocation_WithValidId_ShouldReturnUpdatedLocation() {
        // Arrange
        Location updatedLocation = new Location();
        updatedLocation.setName("Updated Location");
        updatedLocation.setAddress("456 Updated Street");
        updatedLocation.setPhoneNumber("987-654-3210");

        when(locationRepository.findById(1L)).thenReturn(Optional.of(testLocation));
        when(locationRepository.save(any(Location.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Location result = locationService.updateLocation(1L, updatedLocation);

        // Assert
        assertNotNull(result);
        assertEquals(testLocation.getId(), result.getId());
        assertEquals(updatedLocation.getName(), result.getName());
        assertEquals(updatedLocation.getAddress(), result.getAddress());
        assertEquals(updatedLocation.getPhoneNumber(), result.getPhoneNumber());
    }

    @Test
    void updateLocation_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(locationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> locationService.updateLocation(999L, testLocation)
        );
        assertEquals("location not found", exception.getMessage());
    }

    @Test
    void deleteLocation_ShouldCallRepositoryDeleteById() {
        // Arrange
        doNothing().when(locationRepository).deleteById(1L);

        // Act
        locationService.deleteLocation(1L);

        // Assert
        verify(locationRepository, times(1)).deleteById(1L);
    }
}