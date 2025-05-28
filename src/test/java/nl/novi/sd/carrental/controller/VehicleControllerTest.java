package nl.novi.sd.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.sd.carrental.dto.VehicleDto;
import nl.novi.sd.carrental.model.ParkingSpace;
import nl.novi.sd.carrental.model.StatusCode;
import nl.novi.sd.carrental.model.Vehicle;
import nl.novi.sd.carrental.model.VehiclePhoto;
import nl.novi.sd.carrental.service.VehiclePhotoService;
import nl.novi.sd.carrental.service.VehicleService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class VehicleControllerTest {

    private MockMvc mockMvc;

    @Mock
    private VehicleService vehicleService;

    @Mock
    private VehiclePhotoService vehiclePhotoService;

    @InjectMocks
    private VehicleController vehicleController;

    private ObjectMapper objectMapper;
    private Vehicle testVehicle;
    private VehicleDto testVehicleDto;
    private ParkingSpace testParkingSpace;
    private VehiclePhoto testPhoto;
    private MultipartFile testFile;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(vehicleController).build();
        objectMapper = new ObjectMapper();

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

        // Create test vehicle DTO
        testVehicleDto = new VehicleDto();
        testVehicleDto.setId(1L);
        testVehicleDto.setLicensePlate("ABC-123");
        testVehicleDto.setMake("Test Make");
        testVehicleDto.setModel("Test Model");
        testVehicleDto.setStatus("AVAILABLE");
        testVehicleDto.setPricePerDay(50.0);
        testVehicleDto.setParkingSpaceId(1L);

        // Create test photo
        testPhoto = new VehiclePhoto();
        testPhoto.setId(1L);
        testPhoto.setUrl("http://example.com/photo.jpg");
        testPhoto.setOriginalFileName("photo.jpg");
        testPhoto.setContentType("image/jpeg");
        testPhoto.setContents(new byte[]{1, 2, 3});

        // Create test file
        testFile = new MockMultipartFile(
                "file",
                "photo.jpg",
                "image/jpeg",
                new byte[]{1, 2, 3}
        );
    }

    @Test
    void getAllVehicles_ShouldReturnAllVehicles() throws Exception {
        // Arrange
        List<Vehicle> vehicles = List.of(testVehicle);
        when(vehicleService.getVehicles()).thenReturn(vehicles);

        // Act & Assert
        mockMvc.perform(get("/vehicles"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].licensePlate").value("ABC-123"))
                .andExpect(jsonPath("$[0].make").value("Test Make"))
                .andExpect(jsonPath("$[0].model").value("Test Model"))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$[0].pricePerDay").value(50.0))
                .andExpect(jsonPath("$[0].parkingSpaceId").value(1));

        verify(vehicleService, times(1)).getVehicles();
    }

    @Test
    void getVehicleById_ShouldReturnVehicle() throws Exception {
        // Arrange
        when(vehicleService.getVehicle(1L)).thenReturn(testVehicle);

        // Act & Assert
        mockMvc.perform(get("/vehicles/1"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.licensePlate").value("ABC-123"))
                .andExpect(jsonPath("$.make").value("Test Make"))
                .andExpect(jsonPath("$.model").value("Test Model"))
                .andExpect(jsonPath("$.status").value("AVAILABLE"))
                .andExpect(jsonPath("$.pricePerDay").value(50.0))
                .andExpect(jsonPath("$.parkingSpaceId").value(1));

        verify(vehicleService, times(1)).getVehicle(1L);
    }

    @Test
    void createVehicle_ShouldReturnCreatedVehicles() throws Exception {
        // Arrange
        List<VehicleDto> vehicleDtos = List.of(testVehicleDto);
        when(vehicleService.createVehicle(any(Vehicle.class))).thenReturn(testVehicle);

        // Act & Assert
        mockMvc.perform(post("/vehicles")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(vehicleDtos)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].licensePlate").value("ABC-123"))
                .andExpect(jsonPath("$[0].make").value("Test Make"))
                .andExpect(jsonPath("$[0].model").value("Test Model"))
                .andExpect(jsonPath("$[0].status").value("AVAILABLE"))
                .andExpect(jsonPath("$[0].pricePerDay").value(50.0))
                .andExpect(jsonPath("$[0].parkingSpaceId").value(1));

        verify(vehicleService, times(1)).createVehicle(any(Vehicle.class));
    }

    @Test
    void updateVehicle_ShouldReturnUpdatedVehicle() throws Exception {
        // Arrange
        VehicleDto updatedVehicleDto = new VehicleDto();
        updatedVehicleDto.setId(1L);
        updatedVehicleDto.setLicensePlate("DEF-456");
        updatedVehicleDto.setMake("Updated Make");
        updatedVehicleDto.setModel("Updated Model");
        updatedVehicleDto.setStatus("MAINTENANCE");
        updatedVehicleDto.setPricePerDay(60.0);
        updatedVehicleDto.setParkingSpaceId(1L);

        Vehicle updatedVehicle = new Vehicle();
        updatedVehicle.setId(1L);
        updatedVehicle.setLicensePlate("DEF-456");
        updatedVehicle.setMake("Updated Make");
        updatedVehicle.setModel("Updated Model");
        updatedVehicle.setStatus(StatusCode.MAINTENANCE);
        updatedVehicle.setPricePerDay(60.0);
        updatedVehicle.setParkingSpace(testParkingSpace);

        when(vehicleService.updateVehicle(eq(1L), any(Vehicle.class))).thenReturn(updatedVehicle);

        // Act & Assert
        mockMvc.perform(put("/vehicles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedVehicleDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.licensePlate").value("DEF-456"))
                .andExpect(jsonPath("$.make").value("Updated Make"))
                .andExpect(jsonPath("$.model").value("Updated Model"))
                .andExpect(jsonPath("$.status").value("MAINTENANCE"))
                .andExpect(jsonPath("$.pricePerDay").value(60.0))
                .andExpect(jsonPath("$.parkingSpaceId").value(1));

        verify(vehicleService, times(1)).updateVehicle(eq(1L), any(Vehicle.class));
    }

    @Test
    void deleteVehicle_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(vehicleService).deleteVehicle(1L);

        // Act & Assert
        mockMvc.perform(delete("/vehicles/1"))
                .andExpect(status().isNoContent());

        verify(vehicleService, times(1)).deleteVehicle(1L);
    }

    @Test
    void getVehiclePhoto_ShouldReturnPhoto() throws Exception {
        // Arrange
        when(vehicleService.getPhotoFromVehicle(1L)).thenReturn(testPhoto);

        // Act & Assert
        mockMvc.perform(get("/vehicles/1/photo"))
                .andExpect(status().isOk())
                .andExpect(header().string(HttpHeaders.CONTENT_DISPOSITION, "inline; filename=photo.jpg"))
                .andExpect(content().contentType(MediaType.IMAGE_JPEG))
                .andExpect(content().bytes(new byte[]{1, 2, 3}));

        verify(vehicleService, times(1)).getPhotoFromVehicle(1L);
    }

    // Note: Testing file upload with MockMvc is complex and might require a different approach
    // This test is a simplified version and might not work as expected
    @Test
    void addPhotoToVehicle_ShouldReturnVehicleWithPhoto() throws Exception {
        // Arrange
        when(vehiclePhotoService.storePhoto(any(MultipartFile.class), anyString())).thenReturn(testPhoto);
        when(vehicleService.addPhotoToVehicle(eq(1L), any(VehiclePhoto.class))).thenReturn(testVehicle);

        // Act & Assert
        mockMvc.perform(multipart("/vehicles/1/photo")
                .file((MockMultipartFile) testFile))
                .andExpect(status().isCreated())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.licensePlate").value("ABC-123"));

        verify(vehiclePhotoService, times(1)).storePhoto(any(MultipartFile.class), anyString());
        verify(vehicleService, times(1)).addPhotoToVehicle(eq(1L), any(VehiclePhoto.class));
    }
}
