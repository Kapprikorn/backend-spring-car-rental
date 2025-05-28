package nl.novi.sd.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.sd.carrental.dto.ParkingLotDto;
import nl.novi.sd.carrental.model.Location;
import nl.novi.sd.carrental.model.ParkingLot;
import nl.novi.sd.carrental.service.ParkingLotService;
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
class ParkingLotControllerTest {
    // API endpoints
    private static final String PARKING_LOTS_ENDPOINT = "/parking-lots";
    private static final String PARKING_LOT_BY_ID_ENDPOINT = "/parking-lots/1";

    // Test data IDs
    private static final Long PARKING_LOT_ID = 1L;
    private static final Long LOCATION_ID = 1L;

    // Test location data
    private static final String TEST_LOCATION_NAME = "Test Location";
    private static final String TEST_LOCATION_ADDRESS = "123 Test Street";
    private static final String TEST_LOCATION_PHONE = "123-456-7890";

    // Test parking lot data
    private static final String TEST_PARKING_LOT_NAME = "Test Parking Lot";

    // Updated parking lot data
    private static final String UPDATED_PARKING_LOT_NAME = "Updated Parking Lot";

    private MockMvc mockMvc;

    @Mock
    private ParkingLotService parkingLotService;

    @InjectMocks
    private ParkingLotController parkingLotController;

    private ObjectMapper objectMapper;
    private ParkingLot testParkingLot;
    private ParkingLotDto testParkingLotDto;
    private Location testLocation;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(parkingLotController).build();
        objectMapper = new ObjectMapper();

        // Create test location
        testLocation = new Location();
        testLocation.setId(LOCATION_ID);
        testLocation.setName(TEST_LOCATION_NAME);
        testLocation.setAddress(TEST_LOCATION_ADDRESS);
        testLocation.setPhoneNumber(TEST_LOCATION_PHONE);

        // Create test parking lot
        testParkingLot = new ParkingLot();
        testParkingLot.setId(PARKING_LOT_ID);
        testParkingLot.setName(TEST_PARKING_LOT_NAME);
        testParkingLot.setLocation(testLocation);

        // Create test parking lot DTO
        testParkingLotDto = new ParkingLotDto();
        testParkingLotDto.setId(PARKING_LOT_ID);
        testParkingLotDto.setName(TEST_PARKING_LOT_NAME);
        testParkingLotDto.setLocationId(LOCATION_ID);
    }

    @Test
    void getParkingLotById_ShouldReturnParkingLot() throws Exception {
        // Arrange
        when(parkingLotService.getParkingLot(PARKING_LOT_ID)).thenReturn(testParkingLot);

        // Act & Assert
        mockMvc.perform(get(PARKING_LOT_BY_ID_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PARKING_LOT_ID))
                .andExpect(jsonPath("$.name").value(TEST_PARKING_LOT_NAME))
                .andExpect(jsonPath("$.locationId").value(LOCATION_ID));

        verify(parkingLotService, times(1)).getParkingLot(PARKING_LOT_ID);
    }

    @Test
    void createParkingLot_ShouldReturnCreatedParkingLot() throws Exception {
        // Arrange
        when(parkingLotService.createParkingLot(any(ParkingLot.class))).thenReturn(testParkingLot);

        // Act & Assert
        mockMvc.perform(post(PARKING_LOTS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testParkingLotDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PARKING_LOT_ID))
                .andExpect(jsonPath("$.name").value(TEST_PARKING_LOT_NAME))
                .andExpect(jsonPath("$.locationId").value(LOCATION_ID));

        verify(parkingLotService, times(1)).createParkingLot(any(ParkingLot.class));
    }

    @Test
    void updateParkingLot_ShouldReturnUpdatedParkingLot() throws Exception {
        // Arrange
        ParkingLotDto updatedParkingLotDto = new ParkingLotDto();
        updatedParkingLotDto.setId(PARKING_LOT_ID);
        updatedParkingLotDto.setName(UPDATED_PARKING_LOT_NAME);
        updatedParkingLotDto.setLocationId(LOCATION_ID);

        ParkingLot updatedParkingLot = new ParkingLot();
        updatedParkingLot.setId(PARKING_LOT_ID);
        updatedParkingLot.setName(UPDATED_PARKING_LOT_NAME);
        updatedParkingLot.setLocation(testLocation);

        when(parkingLotService.updateParkingLot(any(ParkingLot.class))).thenReturn(updatedParkingLot);

        // Act & Assert
        mockMvc.perform(put(PARKING_LOT_BY_ID_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedParkingLotDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(PARKING_LOT_ID))
                .andExpect(jsonPath("$.name").value(UPDATED_PARKING_LOT_NAME))
                .andExpect(jsonPath("$.locationId").value(LOCATION_ID));

        verify(parkingLotService, times(1)).updateParkingLot(any(ParkingLot.class));
    }

    @Test
    void deleteParkingLot_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(parkingLotService).deleteParkingLot(PARKING_LOT_ID);

        // Act & Assert
        mockMvc.perform(delete(PARKING_LOT_BY_ID_ENDPOINT))
                .andExpect(status().isNoContent());

        verify(parkingLotService, times(1)).deleteParkingLot(PARKING_LOT_ID);
    }
}
