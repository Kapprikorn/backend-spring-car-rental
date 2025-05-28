package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.ParkingSpace;
import nl.novi.sd.carrental.model.StatusCode;
import nl.novi.sd.carrental.model.Vehicle;
import nl.novi.sd.carrental.model.VehiclePhoto;
import nl.novi.sd.carrental.repository.VehicleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class VehicleServiceImplTest {

    @Mock
    private VehicleRepository vehicleRepository;

    @InjectMocks
    private VehicleServiceImpl vehicleService;

    private Vehicle testVehicle;
    private VehiclePhoto testPhoto;
    private ParkingSpace testParkingSpace;

    @BeforeEach
    void setUp() {
        // Create test parking space
        testParkingSpace = new ParkingSpace();
        testParkingSpace.setId(1L);
        testParkingSpace.setLocation("Test Location");
        testParkingSpace.setSize("Medium");
        testParkingSpace.setOccupied(false);

        // Create test vehicle
        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setLicensePlate("ABC-123");
        testVehicle.setMake("Test Make");
        testVehicle.setModel("Test Model");
        testVehicle.setStatus(StatusCode.AVAILABLE);
        testVehicle.setPricePerDay(50.0);
        testVehicle.setParkingSpace(testParkingSpace);

        // Create test photo
        testPhoto = new VehiclePhoto();
        testPhoto.setId(1L);
        testPhoto.setUrl("http://example.com/photo.jpg");
        testPhoto.setOriginalFileName("photo.jpg");
        testPhoto.setContentType("image/jpeg");
        testPhoto.setContents(new byte[]{1, 2, 3});
    }

    @Test
    void createVehicle_ShouldReturnSavedVehicle() {
        // Arrange
        when(vehicleRepository.save(any(Vehicle.class))).thenReturn(testVehicle);

        // Act
        Vehicle result = vehicleService.createVehicle(testVehicle);

        // Assert
        assertNotNull(result);
        assertEquals(testVehicle.getId(), result.getId());
        assertEquals(testVehicle.getLicensePlate(), result.getLicensePlate());
        verify(vehicleRepository, times(1)).save(testVehicle);
    }

    @Test
    void getVehicle_WithValidId_ShouldReturnVehicle() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));

        // Act
        Vehicle result = vehicleService.getVehicle(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testVehicle.getId(), result.getId());
        assertEquals(testVehicle.getLicensePlate(), result.getLicensePlate());
    }

    @Test
    void getVehicle_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vehicleService.getVehicle(999L)
        );
        assertEquals("Vehicle not found", exception.getMessage());
    }

    @Test
    void getVehicles_ShouldReturnAllVehicles() {
        // Arrange
        Vehicle secondVehicle = new Vehicle();
        secondVehicle.setId(2L);
        secondVehicle.setLicensePlate("XYZ-789");
        secondVehicle.setMake("Another Make");
        secondVehicle.setModel("Another Model");
        secondVehicle.setStatus(StatusCode.AVAILABLE);
        secondVehicle.setPricePerDay(75.0);

        List<Vehicle> vehicles = List.of(testVehicle, secondVehicle);
        when(vehicleRepository.findAll()).thenReturn(vehicles);

        // Act
        List<Vehicle> result = vehicleService.getVehicles();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testVehicle.getId(), result.get(0).getId());
        assertEquals(secondVehicle.getId(), result.get(1).getId());
    }

    @Test
    void updateVehicle_WithValidId_ShouldReturnUpdatedVehicle() {
        // Arrange
        Vehicle updatedVehicle = new Vehicle();
        updatedVehicle.setLicensePlate("DEF-456");
        updatedVehicle.setMake("Updated Make");
        updatedVehicle.setModel("Updated Model");
        updatedVehicle.setStatus(StatusCode.MAINTENANCE);
        updatedVehicle.setPricePerDay(60.0);
        updatedVehicle.setParkingSpace(testParkingSpace);

        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Vehicle result = vehicleService.updateVehicle(1L, updatedVehicle);

        // Assert
        assertNotNull(result);
        assertEquals(testVehicle.getId(), result.getId());
        assertEquals(updatedVehicle.getLicensePlate(), result.getLicensePlate());
        assertEquals(updatedVehicle.getMake(), result.getMake());
        assertEquals(updatedVehicle.getModel(), result.getModel());
        assertEquals(updatedVehicle.getStatus(), result.getStatus());
        assertEquals(updatedVehicle.getPricePerDay(), result.getPricePerDay());
        assertEquals(updatedVehicle.getParkingSpace(), result.getParkingSpace());
    }

    @Test
    void updateVehicle_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vehicleService.updateVehicle(999L, testVehicle)
        );
        assertEquals("Vehicle not found", exception.getMessage());
    }

    @Test
    void deleteVehicle_ShouldCallRepositoryDeleteById() {
        // Arrange
        doNothing().when(vehicleRepository).deleteById(1L);

        // Act
        vehicleService.deleteVehicle(1L);

        // Assert
        verify(vehicleRepository, times(1)).deleteById(1L);
    }

    @Test
    void addPhotoToVehicle_WithValidId_ShouldReturnVehicleWithPhoto() {
        // Arrange
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));
        when(vehicleRepository.save(any(Vehicle.class))).thenAnswer(invocation -> {
            Vehicle vehicle = invocation.getArgument(0);
            vehicle.setVehiclePhoto(testPhoto);
            return vehicle;
        });

        // Act
        Vehicle result = vehicleService.addPhotoToVehicle(1L, testPhoto);

        // Assert
        assertNotNull(result);
        assertNotNull(result.getVehiclePhoto());
        assertEquals(testPhoto.getId(), result.getVehiclePhoto().getId());
        assertEquals(testPhoto.getUrl(), result.getVehiclePhoto().getUrl());
    }

    @Test
    void addPhotoToVehicle_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vehicleService.addPhotoToVehicle(999L, testPhoto)
        );
        assertEquals("Vehicle not found", exception.getMessage());
    }

    @Test
    void getPhotoFromVehicle_WithValidId_ShouldReturnPhoto() {
        // Arrange
        testVehicle.setVehiclePhoto(testPhoto);
        when(vehicleRepository.findById(1L)).thenReturn(Optional.of(testVehicle));

        // Act
        VehiclePhoto result = vehicleService.getPhotoFromVehicle(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testPhoto.getId(), result.getId());
        assertEquals(testPhoto.getUrl(), result.getUrl());
    }

    @Test
    void getPhotoFromVehicle_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(vehicleRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> vehicleService.getPhotoFromVehicle(999L)
        );
        assertEquals("Vehicle not found", exception.getMessage());
    }
}