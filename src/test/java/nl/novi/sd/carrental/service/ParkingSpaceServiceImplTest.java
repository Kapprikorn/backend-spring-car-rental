package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.ParkingLot;
import nl.novi.sd.carrental.model.ParkingSpace;
import nl.novi.sd.carrental.model.Vehicle;
import nl.novi.sd.carrental.repository.ParkingSpaceRepository;
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
class ParkingSpaceServiceImplTest {

    @Mock
    private ParkingSpaceRepository parkingSpaceRepository;

    @InjectMocks
    private ParkingSpaceServiceImpl parkingSpaceService;

    private ParkingSpace testParkingSpace;
    private ParkingLot testParkingLot;
    private Vehicle testVehicle;

    @BeforeEach
    void setUp() {
        // Create test parking lot
        testParkingLot = new ParkingLot();
        testParkingLot.setId(1L);
        testParkingLot.setName("Test Parking Lot");

        // Create test vehicle
        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setLicensePlate("ABC-123");
        testVehicle.setMake("Test Make");
        testVehicle.setModel("Test Model");

        // Create test parking space
        testParkingSpace = new ParkingSpace();
        testParkingSpace.setId(1L);
        testParkingSpace.setLocation("Test Location");
        testParkingSpace.setSize("Medium");
        testParkingSpace.setOccupied(false);
        testParkingSpace.setParkingLot(testParkingLot);
        testParkingSpace.setVehicle(null);
    }

    @Test
    void getParkingSpace_WithValidId_ShouldReturnParkingSpace() {
        // Arrange
        when(parkingSpaceRepository.findById(1L)).thenReturn(Optional.of(testParkingSpace));

        // Act
        ParkingSpace result = parkingSpaceService.getParkingSpace(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testParkingSpace.getId(), result.getId());
        assertEquals(testParkingSpace.getLocation(), result.getLocation());
        assertEquals(testParkingSpace.getSize(), result.getSize());
        assertEquals(testParkingSpace.isOccupied(), result.isOccupied());
        assertEquals(testParkingSpace.getParkingLot(), result.getParkingLot());
    }

    @Test
    void getParkingSpace_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(parkingSpaceRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> parkingSpaceService.getParkingSpace(999L)
        );
        assertEquals("Parking space not found", exception.getMessage());
    }

    @Test
    void createParkingSpace_ShouldReturnSavedParkingSpace() {
        // Arrange
        when(parkingSpaceRepository.save(any(ParkingSpace.class))).thenReturn(testParkingSpace);

        // Act
        ParkingSpace result = parkingSpaceService.createParkingSpace(testParkingSpace);

        // Assert
        assertNotNull(result);
        assertEquals(testParkingSpace.getId(), result.getId());
        assertEquals(testParkingSpace.getLocation(), result.getLocation());
        verify(parkingSpaceRepository, times(1)).save(testParkingSpace);
    }

    @Test
    void updateParkingSpace_WithValidId_ShouldReturnUpdatedParkingSpace() {
        // Arrange
        ParkingSpace updatedParkingSpace = new ParkingSpace();
        updatedParkingSpace.setId(1L);
        updatedParkingSpace.setLocation("Updated Location");
        updatedParkingSpace.setSize("Large");
        updatedParkingSpace.setOccupied(true);
        updatedParkingSpace.setParkingLot(testParkingLot);
        updatedParkingSpace.setVehicle(testVehicle);

        when(parkingSpaceRepository.findById(1L)).thenReturn(Optional.of(testParkingSpace));
        when(parkingSpaceRepository.save(any(ParkingSpace.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ParkingSpace result = parkingSpaceService.updateParkingSpace(updatedParkingSpace);

        // Assert
        assertNotNull(result);
        assertEquals(updatedParkingSpace.getId(), result.getId());
        assertEquals(updatedParkingSpace.getLocation(), result.getLocation());
        assertEquals(updatedParkingSpace.getSize(), result.getSize());
        assertEquals(updatedParkingSpace.isOccupied(), result.isOccupied());
        assertEquals(updatedParkingSpace.getParkingLot(), result.getParkingLot());
        assertEquals(updatedParkingSpace.getVehicle(), result.getVehicle());
    }

    @Test
    void updateParkingSpace_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        ParkingSpace nonExistentParkingSpace = new ParkingSpace();
        nonExistentParkingSpace.setId(999L);
        nonExistentParkingSpace.setLocation("Non-existent Location");

        when(parkingSpaceRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> parkingSpaceService.updateParkingSpace(nonExistentParkingSpace)
        );
        assertEquals("Parking space not found", exception.getMessage());
    }

    @Test
    void deleteParkingSpace_ShouldCallRepositoryDeleteById() {
        // Arrange
        doNothing().when(parkingSpaceRepository).deleteById(1L);

        // Act
        parkingSpaceService.deleteParkingSpace(1L);

        // Assert
        verify(parkingSpaceRepository, times(1)).deleteById(1L);
    }
}