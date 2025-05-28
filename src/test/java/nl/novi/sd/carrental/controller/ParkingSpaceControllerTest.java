package nl.novi.sd.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.sd.carrental.dto.ParkingSpaceDto;
import nl.novi.sd.carrental.dto.VehicleDto;
import nl.novi.sd.carrental.model.ParkingLot;
import nl.novi.sd.carrental.model.ParkingSpace;
import nl.novi.sd.carrental.model.Vehicle;
import nl.novi.sd.carrental.service.ParkingSpaceService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ParkingSpaceControllerTest {
    // API endpoints
    private static final String PARKING_SPACES_ENDPOINT = "/parking-spaces";
    private static final String PARKING_SPACE_BY_ID_ENDPOINT = "/parking-spaces/1";

    // Test data IDs
    private static final Long PARKING_SPACE_ID = 1L;
    private static final Long PARKING_LOT_ID = 1L;
    private static final Long VEHICLE_ID = 1L;

    // Test parking lot data
    private static final String TEST_PARKING_LOT_NAME = "Test Parking Lot";

    // Test vehicle data
    private static final String TEST_VEHICLE_LICENSE_PLATE = "ABC-123";
    private static final String TEST_VEHICLE_MAKE = "Test Make";
    private static final String TEST_VEHICLE_MODEL = "Test Model";

    // Test parking space data
    private static final String TEST_PARKING_SPACE_LOCATION = "Test Location";
    private static final String TEST_PARKING_SPACE_SIZE = "Medium";
    private static final boolean TEST_PARKING_SPACE_OCCUPIED = false;

    // Updated parking space data
    private static final String UPDATED_PARKING_SPACE_LOCATION = "Updated Location";
    private static final String UPDATED_PARKING_SPACE_SIZE = "Large";
    private static final boolean UPDATED_PARKING_SPACE_OCCUPIED = true;

    private MockMvc mockMvc;

    @Mock
    private ParkingSpaceService parkingSpaceService;

    @InjectMocks
    private ParkingSpaceController parkingSpaceController;

    private ObjectMapper objectMapper;
    private ParkingSpace testParkingSpace;
    private ParkingSpaceDto testParkingSpaceDto;
    private ParkingLot testParkingLot;
    private Vehicle testVehicle;
    private VehicleDto testVehicleDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(parkingSpaceController).build();
        objectMapper = new ObjectMapper();

        // Create test parking lot
        testParkingLot = new ParkingLot();
        testParkingLot.setId(PARKING_LOT_ID);
        testParkingLot.setName(TEST_PARKING_LOT_NAME);

        // Create test vehicle
        testVehicle = new Vehicle();
        testVehicle.setId(VEHICLE_ID);
        testVehicle.setLicensePlate(TEST_VEHICLE_LICENSE_PLATE);
        testVehicle.setMake(TEST_VEHICLE_MAKE);
        testVehicle.setModel(TEST_VEHICLE_MODEL);

        // Create test vehicle DTO
        testVehicleDto = new VehicleDto();
        testVehicleDto.setId(VEHICLE_ID);
        testVehicleDto.setLicensePlate(TEST_VEHICLE_LICENSE_PLATE);
        testVehicleDto.setMake(TEST_VEHICLE_MAKE);
        testVehicleDto.setModel(TEST_VEHICLE_MODEL);

        // Create test parking space
        testParkingSpace = new ParkingSpace();
        testParkingSpace.setId(PARKING_SPACE_ID);
        testParkingSpace.setLocation(TEST_PARKING_SPACE_LOCATION);
        testParkingSpace.setSize(TEST_PARKING_SPACE_SIZE);
        testParkingSpace.setOccupied(TEST_PARKING_SPACE_OCCUPIED);
        testParkingSpace.setParkingLot(testParkingLot);
        testParkingSpace.setVehicle(null);

        // Create test parking space DTO
        testParkingSpaceDto = new ParkingSpaceDto();
        testParkingSpaceDto.setId(PARKING_SPACE_ID);
        testParkingSpaceDto.setLocation(TEST_PARKING_SPACE_LOCATION);
        testParkingSpaceDto.setSize(TEST_PARKING_SPACE_SIZE);
        testParkingSpaceDto.setOccupied(TEST_PARKING_SPACE_OCCUPIED);
        testParkingSpaceDto.setParkingLotId(PARKING_LOT_ID);
        testParkingSpaceDto.setVehicle(null);
    }

    @Test
    void getParkingSpaceById_ShouldReturnParkingSpace() throws Exception {
        // Arrange
        when(parkingSpaceService.getParkingSpace(PARKING_SPACE_ID)).thenReturn(testParkingSpace);

        // Act & Assert
        mockMvc.perform(get(PARKING_SPACE_BY_ID_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PARKING_SPACE_ID))
                .andExpect(jsonPath("$.location").value(TEST_PARKING_SPACE_LOCATION))
                .andExpect(jsonPath("$.size").value(TEST_PARKING_SPACE_SIZE))
                .andExpect(jsonPath("$.occupied").value(TEST_PARKING_SPACE_OCCUPIED))
                .andExpect(jsonPath("$.parkingLotId").value(PARKING_LOT_ID))
                .andExpect(jsonPath("$.vehicle").isEmpty());

        verify(parkingSpaceService, times(1)).getParkingSpace(PARKING_SPACE_ID);
    }

    @Test
    void createParkingSpace_ShouldReturnCreatedParkingSpace() throws Exception {
        // Arrange
        when(parkingSpaceService.createParkingSpace(any(ParkingSpace.class))).thenReturn(testParkingSpace);

        // Act & Assert
        mockMvc.perform(post(PARKING_SPACES_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testParkingSpaceDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PARKING_SPACE_ID))
                .andExpect(jsonPath("$.location").value(TEST_PARKING_SPACE_LOCATION))
                .andExpect(jsonPath("$.size").value(TEST_PARKING_SPACE_SIZE))
                .andExpect(jsonPath("$.occupied").value(TEST_PARKING_SPACE_OCCUPIED))
                .andExpect(jsonPath("$.parkingLotId").value(PARKING_LOT_ID))
                .andExpect(jsonPath("$.vehicle").isEmpty());

        verify(parkingSpaceService, times(1)).createParkingSpace(any(ParkingSpace.class));
    }

    @Test
    void updateParkingSpace_ShouldReturnUpdatedParkingSpace() throws Exception {
        // Arrange
        ParkingSpaceDto updatedParkingSpaceDto = new ParkingSpaceDto();
        updatedParkingSpaceDto.setId(PARKING_SPACE_ID);
        updatedParkingSpaceDto.setLocation(UPDATED_PARKING_SPACE_LOCATION);
        updatedParkingSpaceDto.setSize(UPDATED_PARKING_SPACE_SIZE);
        updatedParkingSpaceDto.setOccupied(UPDATED_PARKING_SPACE_OCCUPIED);
        updatedParkingSpaceDto.setParkingLotId(PARKING_LOT_ID);
        updatedParkingSpaceDto.setVehicle(testVehicleDto);

        ParkingSpace updatedParkingSpace = new ParkingSpace();
        updatedParkingSpace.setId(PARKING_SPACE_ID);
        updatedParkingSpace.setLocation(UPDATED_PARKING_SPACE_LOCATION);
        updatedParkingSpace.setSize(UPDATED_PARKING_SPACE_SIZE);
        updatedParkingSpace.setOccupied(UPDATED_PARKING_SPACE_OCCUPIED);
        updatedParkingSpace.setParkingLot(testParkingLot);
        updatedParkingSpace.setVehicle(testVehicle);

        when(parkingSpaceService.updateParkingSpace(any(ParkingSpace.class))).thenReturn(updatedParkingSpace);

        // Act & Assert
        mockMvc.perform(put(PARKING_SPACE_BY_ID_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedParkingSpaceDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PARKING_SPACE_ID))
                .andExpect(jsonPath("$.location").value(UPDATED_PARKING_SPACE_LOCATION))
                .andExpect(jsonPath("$.size").value(UPDATED_PARKING_SPACE_SIZE))
                .andExpect(jsonPath("$.occupied").value(UPDATED_PARKING_SPACE_OCCUPIED))
                .andExpect(jsonPath("$.parkingLotId").value(PARKING_LOT_ID))
                .andExpect(jsonPath("$.vehicle.id").value(VEHICLE_ID))
                .andExpect(jsonPath("$.vehicle.licensePlate").value(TEST_VEHICLE_LICENSE_PLATE))
                .andExpect(jsonPath("$.vehicle.make").value(TEST_VEHICLE_MAKE))
                .andExpect(jsonPath("$.vehicle.model").value(TEST_VEHICLE_MODEL));

        verify(parkingSpaceService, times(1)).updateParkingSpace(any(ParkingSpace.class));
    }

    @Test
    void deleteParkingSpace_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(parkingSpaceService).deleteParkingSpace(PARKING_SPACE_ID);

        // Act & Assert
        mockMvc.perform(delete(PARKING_SPACE_BY_ID_ENDPOINT))
                .andExpect(status().isOk());

        verify(parkingSpaceService, times(1)).deleteParkingSpace(PARKING_SPACE_ID);
    }
}
