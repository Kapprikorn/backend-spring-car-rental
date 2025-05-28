package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.Location;
import nl.novi.sd.carrental.model.ParkingLot;
import nl.novi.sd.carrental.repository.ParkingLotRepository;
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
class ParkingLotServiceImplTest {

    @Mock
    private ParkingLotRepository parkingLotRepository;

    @InjectMocks
    private ParkingLotServiceImpl parkingLotService;

    private ParkingLot testParkingLot;
    private Location testLocation;

    @BeforeEach
    void setUp() {
        // Create test location
        testLocation = new Location();
        testLocation.setId(1L);
        testLocation.setName("Test Location");
        testLocation.setAddress("123 Test Street");
        testLocation.setPhoneNumber("123-456-7890");

        // Create test parking lot
        testParkingLot = new ParkingLot();
        testParkingLot.setId(1L);
        testParkingLot.setName("Test Parking Lot");
        testParkingLot.setLocation(testLocation);
    }

    @Test
    void getParkingLot_WithValidId_ShouldReturnParkingLot() {
        // Arrange
        when(parkingLotRepository.findById(1L)).thenReturn(Optional.of(testParkingLot));

        // Act
        ParkingLot result = parkingLotService.getParkingLot(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testParkingLot.getId(), result.getId());
        assertEquals(testParkingLot.getName(), result.getName());
        assertEquals(testParkingLot.getLocation(), result.getLocation());
    }

    @Test
    void getParkingLot_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(parkingLotRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> parkingLotService.getParkingLot(999L)
        );
        assertEquals("Parking lot not found", exception.getMessage());
    }

    @Test
    void createParkingLot_ShouldReturnSavedParkingLot() {
        // Arrange
        when(parkingLotRepository.save(any(ParkingLot.class))).thenReturn(testParkingLot);

        // Act
        ParkingLot result = parkingLotService.createParkingLot(testParkingLot);

        // Assert
        assertNotNull(result);
        assertEquals(testParkingLot.getId(), result.getId());
        assertEquals(testParkingLot.getName(), result.getName());
        verify(parkingLotRepository, times(1)).save(testParkingLot);
    }

    @Test
    void updateParkingLot_WithValidId_ShouldReturnUpdatedParkingLot() {
        // Arrange
        ParkingLot updatedParkingLot = new ParkingLot();
        updatedParkingLot.setId(1L);
        updatedParkingLot.setName("Updated Parking Lot");
        updatedParkingLot.setLocation(testLocation);

        when(parkingLotRepository.findById(1L)).thenReturn(Optional.of(testParkingLot));
        when(parkingLotRepository.save(any(ParkingLot.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ParkingLot result = parkingLotService.updateParkingLot(updatedParkingLot);

        // Assert
        assertNotNull(result);
        assertEquals(updatedParkingLot.getId(), result.getId());
        assertEquals(updatedParkingLot.getName(), result.getName());
        assertEquals(updatedParkingLot.getLocation(), result.getLocation());
    }

    @Test
    void updateParkingLot_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        ParkingLot nonExistentParkingLot = new ParkingLot();
        nonExistentParkingLot.setId(999L);
        nonExistentParkingLot.setName("Non-existent Parking Lot");

        when(parkingLotRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> parkingLotService.updateParkingLot(nonExistentParkingLot)
        );
        assertEquals("Parking lot not found", exception.getMessage());
    }

    @Test
    void deleteParkingLot_ShouldCallRepositoryDeleteById() {
        // Arrange
        doNothing().when(parkingLotRepository).deleteById(1L);

        // Act
        parkingLotService.deleteParkingLot(1L);

        // Assert
        verify(parkingLotRepository, times(1)).deleteById(1L);
    }
}