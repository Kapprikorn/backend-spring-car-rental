package nl.novi.sd.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.sd.carrental.dto.LocationDto;
import nl.novi.sd.carrental.model.Location;
import nl.novi.sd.carrental.service.LocationService;
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
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class LocationControllerTest {
    // API endpoints
    private static final String LOCATIONS_ENDPOINT = "/locations";
    private static final String LOCATION_BY_ID_ENDPOINT = "/locations/1";

    // Test data
    private static final Long LOCATION_ID = 1L;
    private static final String TEST_LOCATION_NAME = "Test Location";
    private static final String TEST_LOCATION_ADDRESS = "123 Test Street";
    private static final String TEST_LOCATION_PHONE = "123-456-7890";

    // Updated test data
    private static final String UPDATED_LOCATION_NAME = "Updated Location";
    private static final String UPDATED_LOCATION_ADDRESS = "456 Updated Street";
    private static final String UPDATED_LOCATION_PHONE = "987-654-3210";

    private MockMvc mockMvc;

    @Mock
    private LocationService locationService;

    @InjectMocks
    private LocationController locationController;

    private ObjectMapper objectMapper;
    private Location testLocation;
    private LocationDto testLocationDto;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(locationController).build();
        objectMapper = new ObjectMapper();

        // Create test location
        testLocation = new Location();
        testLocation.setId(LOCATION_ID);
        testLocation.setName(TEST_LOCATION_NAME);
        testLocation.setAddress(TEST_LOCATION_ADDRESS);
        testLocation.setPhoneNumber(TEST_LOCATION_PHONE);

        // Create test location DTO
        testLocationDto = new LocationDto();
        testLocationDto.setId(LOCATION_ID);
        testLocationDto.setName(TEST_LOCATION_NAME);
        testLocationDto.setAddress(TEST_LOCATION_ADDRESS);
        testLocationDto.setPhoneNumber(TEST_LOCATION_PHONE);
    }

    @Test
    void getLocationById_ShouldReturnLocation() throws Exception {
        // Arrange
        when(locationService.getLocation(LOCATION_ID)).thenReturn(testLocation);

        // Act & Assert
        mockMvc.perform(get(LOCATION_BY_ID_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(LOCATION_ID))
                .andExpect(jsonPath("$.name").value(TEST_LOCATION_NAME))
                .andExpect(jsonPath("$.address").value(TEST_LOCATION_ADDRESS))
                .andExpect(jsonPath("$.phoneNumber").value(TEST_LOCATION_PHONE));

        verify(locationService, times(1)).getLocation(LOCATION_ID);
    }

    @Test
    void createLocation_ShouldReturnCreatedLocation() throws Exception {
        // Arrange
        when(locationService.saveLocation(any(Location.class))).thenReturn(testLocation);

        // Act & Assert
        mockMvc.perform(post(LOCATIONS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testLocationDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(LOCATION_ID))
                .andExpect(jsonPath("$.name").value(TEST_LOCATION_NAME))
                .andExpect(jsonPath("$.address").value(TEST_LOCATION_ADDRESS))
                .andExpect(jsonPath("$.phoneNumber").value(TEST_LOCATION_PHONE));

        verify(locationService, times(1)).saveLocation(any(Location.class));
    }

    @Test
    void updateLocation_ShouldReturnUpdatedLocation() throws Exception {
        // Arrange
        LocationDto updatedLocationDto = new LocationDto();
        updatedLocationDto.setId(LOCATION_ID);
        updatedLocationDto.setName(UPDATED_LOCATION_NAME);
        updatedLocationDto.setAddress(UPDATED_LOCATION_ADDRESS);
        updatedLocationDto.setPhoneNumber(UPDATED_LOCATION_PHONE);

        Location updatedLocation = new Location();
        updatedLocation.setId(LOCATION_ID);
        updatedLocation.setName(UPDATED_LOCATION_NAME);
        updatedLocation.setAddress(UPDATED_LOCATION_ADDRESS);
        updatedLocation.setPhoneNumber(UPDATED_LOCATION_PHONE);

        when(locationService.updateLocation(eq(LOCATION_ID), any(Location.class))).thenReturn(updatedLocation);

        // Act & Assert
        mockMvc.perform(put(LOCATION_BY_ID_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedLocationDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(LOCATION_ID))
                .andExpect(jsonPath("$.name").value(UPDATED_LOCATION_NAME))
                .andExpect(jsonPath("$.address").value(UPDATED_LOCATION_ADDRESS))
                .andExpect(jsonPath("$.phoneNumber").value(UPDATED_LOCATION_PHONE));

        verify(locationService, times(1)).updateLocation(eq(LOCATION_ID), any(Location.class));
    }

    @Test
    void deleteLocation_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(locationService).deleteLocation(LOCATION_ID);

        // Act & Assert
        mockMvc.perform(delete(LOCATION_BY_ID_ENDPOINT))
                .andExpect(status().isNoContent());

        verify(locationService, times(1)).deleteLocation(LOCATION_ID);
    }
}
