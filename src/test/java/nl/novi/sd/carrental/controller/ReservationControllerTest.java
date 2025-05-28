package nl.novi.sd.carrental.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import nl.novi.sd.carrental.dto.ReservationDto;
import nl.novi.sd.carrental.model.Reservation;
import nl.novi.sd.carrental.model.User;
import nl.novi.sd.carrental.model.UserRole;
import nl.novi.sd.carrental.model.Vehicle;
import nl.novi.sd.carrental.service.ReservationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ReservationControllerTest {
    // API endpoints
    private static final String RESERVATIONS_ENDPOINT = "/reservations";
    private static final String RESERVATION_BY_ID_ENDPOINT = "/reservations/1";
    private static final String RESERVATIONS_BY_USER_ID_ENDPOINT = "/reservations/users/1";
    private static final String ACTIVE_RESERVATIONS_BY_USER_ID_ENDPOINT = "/reservations/users/1/active";

    // Test data IDs
    private static final Long RESERVATION_ID = 1L;
    private static final Long ACTIVE_RESERVATION_ID = 2L;
    private static final Long USER_ID = 1L;
    private static final Long VEHICLE_ID = 1L;

    // Test user data
    private static final String TEST_USER_NAME = "Test User";
    private static final String TEST_USERNAME = "testuser";
    private static final String TEST_USER_PASSWORD = "Password1@";
    private static final String TEST_USER_EMAIL = "test@example.com";

    // Test vehicle data
    private static final String TEST_VEHICLE_LICENSE_PLATE = "ABC-123";
    private static final String TEST_VEHICLE_MAKE = "Test Make";
    private static final String TEST_VEHICLE_MODEL = "Test Model";

    // Test reservation data
    private static final double TEST_RESERVATION_PRICE = 250.0;
    private static final double TEST_ACTIVE_RESERVATION_PRICE = 300.0;

    private MockMvc mockMvc;

    @Mock
    private ReservationService reservationService;

    @InjectMocks
    private ReservationController reservationController;

    private ObjectMapper objectMapper;
    private Reservation testReservation;
    private Reservation testActiveReservation;
    private ReservationDto testReservationDto;
    private User testUser;
    private Vehicle testVehicle;
    private Date startDate;
    private Date endDate;
    private Date activeStartDate;
    private Date activeEndDate;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(reservationController).build();
        objectMapper = new ObjectMapper();

        // Create dates
        Calendar startCalendar = Calendar.getInstance();
        startCalendar.add(Calendar.DAY_OF_MONTH, -10);
        startDate = startCalendar.getTime();

        Calendar endCalendar = Calendar.getInstance();
        endCalendar.add(Calendar.DAY_OF_MONTH, -5);
        endDate = endCalendar.getTime();

        Calendar activeStartCalendar = Calendar.getInstance();
        activeStartCalendar.add(Calendar.DAY_OF_MONTH, -2);
        activeStartDate = activeStartCalendar.getTime();

        Calendar activeEndCalendar = Calendar.getInstance();
        activeEndCalendar.add(Calendar.DAY_OF_MONTH, 3);
        activeEndDate = activeEndCalendar.getTime();

        // Create test user
        testUser = new User();
        testUser.setId(USER_ID);
        testUser.setName(TEST_USER_NAME);
        testUser.setUsername(TEST_USERNAME);
        testUser.setPassword(TEST_USER_PASSWORD);
        testUser.setEmail(TEST_USER_EMAIL);
        testUser.setRole(UserRole.USER);

        // Create test vehicle
        testVehicle = new Vehicle();
        testVehicle.setId(VEHICLE_ID);
        testVehicle.setLicensePlate(TEST_VEHICLE_LICENSE_PLATE);
        testVehicle.setMake(TEST_VEHICLE_MAKE);
        testVehicle.setModel(TEST_VEHICLE_MODEL);

        // Create test reservation (past reservation)
        testReservation = new Reservation();
        testReservation.setId(RESERVATION_ID);
        testReservation.setStartDate(startDate);
        testReservation.setEndDate(endDate);
        testReservation.setTotalPrice(TEST_RESERVATION_PRICE);
        testReservation.setUser(testUser);
        testReservation.setVehicle(testVehicle);

        // Create test active reservation (current reservation)
        testActiveReservation = new Reservation();
        testActiveReservation.setId(ACTIVE_RESERVATION_ID);
        testActiveReservation.setStartDate(activeStartDate);
        testActiveReservation.setEndDate(activeEndDate);
        testActiveReservation.setTotalPrice(TEST_ACTIVE_RESERVATION_PRICE);
        testActiveReservation.setUser(testUser);
        testActiveReservation.setVehicle(testVehicle);

        // Create test reservation DTO
        testReservationDto = new ReservationDto();
        testReservationDto.setId(RESERVATION_ID);
        testReservationDto.setStartDate(startDate);
        testReservationDto.setEndDate(endDate);
        testReservationDto.setTotalPrice(TEST_RESERVATION_PRICE);
        testReservationDto.setUserId(USER_ID);
        testReservationDto.setVehicleId(VEHICLE_ID);
    }

    @Test
    void getReservationsByUserId_ShouldReturnReservations() throws Exception {
        // Arrange
        List<Reservation> reservations = List.of(testReservation, testActiveReservation);
        when(reservationService.getReservationsByUserId(USER_ID)).thenReturn(reservations);

        // Act & Assert
        mockMvc.perform(get(RESERVATIONS_BY_USER_ID_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(RESERVATION_ID))
                .andExpect(jsonPath("$[0].totalPrice").value(TEST_RESERVATION_PRICE))
                .andExpect(jsonPath("$[0].userId").value(USER_ID))
                .andExpect(jsonPath("$[0].vehicleId").value(VEHICLE_ID))
                .andExpect(jsonPath("$[1].id").value(ACTIVE_RESERVATION_ID))
                .andExpect(jsonPath("$[1].totalPrice").value(TEST_ACTIVE_RESERVATION_PRICE))
                .andExpect(jsonPath("$[1].userId").value(USER_ID))
                .andExpect(jsonPath("$[1].vehicleId").value(VEHICLE_ID));

        verify(reservationService, times(1)).getReservationsByUserId(USER_ID);
    }

    @Test
    void getActiveReservationsByUserId_ShouldReturnActiveReservations() throws Exception {
        // Arrange
        List<Reservation> activeReservations = List.of(testActiveReservation);
        when(reservationService.getActiveReservationsByUserId(USER_ID)).thenReturn(activeReservations);

        // Act & Assert
        mockMvc.perform(get(ACTIVE_RESERVATIONS_BY_USER_ID_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(ACTIVE_RESERVATION_ID))
                .andExpect(jsonPath("$[0].totalPrice").value(TEST_ACTIVE_RESERVATION_PRICE))
                .andExpect(jsonPath("$[0].userId").value(USER_ID))
                .andExpect(jsonPath("$[0].vehicleId").value(VEHICLE_ID));

        verify(reservationService, times(1)).getActiveReservationsByUserId(USER_ID);
    }

    @Test
    void getReservationById_ShouldReturnReservation() throws Exception {
        // Arrange
        when(reservationService.getReservation(RESERVATION_ID)).thenReturn(testReservation);

        // Act & Assert
        mockMvc.perform(get(RESERVATION_BY_ID_ENDPOINT))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(RESERVATION_ID))
                .andExpect(jsonPath("$.totalPrice").value(TEST_RESERVATION_PRICE))
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.vehicleId").value(VEHICLE_ID));

        verify(reservationService, times(1)).getReservation(RESERVATION_ID);
    }

    @Test
    void createReservation_ShouldReturnCreatedReservation() throws Exception {
        // Arrange
        when(reservationService.createReservation(any(Reservation.class))).thenReturn(testReservation);

        // Act & Assert
        mockMvc.perform(post(RESERVATIONS_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(testReservationDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(RESERVATION_ID))
                .andExpect(jsonPath("$.totalPrice").value(TEST_RESERVATION_PRICE))
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.vehicleId").value(VEHICLE_ID));

        verify(reservationService, times(1)).createReservation(any(Reservation.class));
    }

    @Test
    void updateReservation_ShouldReturnUpdatedReservation() throws Exception {
        // Arrange
        ReservationDto updatedReservationDto = new ReservationDto();
        updatedReservationDto.setId(RESERVATION_ID);
        updatedReservationDto.setStartDate(activeStartDate);
        updatedReservationDto.setEndDate(activeEndDate);
        updatedReservationDto.setTotalPrice(TEST_ACTIVE_RESERVATION_PRICE);
        updatedReservationDto.setUserId(USER_ID);
        updatedReservationDto.setVehicleId(VEHICLE_ID);

        Reservation updatedReservation = new Reservation();
        updatedReservation.setId(RESERVATION_ID);
        updatedReservation.setStartDate(activeStartDate);
        updatedReservation.setEndDate(activeEndDate);
        updatedReservation.setTotalPrice(TEST_ACTIVE_RESERVATION_PRICE);
        updatedReservation.setUser(testUser);
        updatedReservation.setVehicle(testVehicle);

        when(reservationService.updateReservation(eq(RESERVATION_ID), any(Reservation.class))).thenReturn(updatedReservation);

        // Act & Assert
        mockMvc.perform(put(RESERVATION_BY_ID_ENDPOINT)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(updatedReservationDto)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(RESERVATION_ID))
                .andExpect(jsonPath("$.totalPrice").value(TEST_ACTIVE_RESERVATION_PRICE))
                .andExpect(jsonPath("$.userId").value(USER_ID))
                .andExpect(jsonPath("$.vehicleId").value(VEHICLE_ID));

        verify(reservationService, times(1)).updateReservation(eq(RESERVATION_ID), any(Reservation.class));
    }

    @Test
    void deleteReservation_ShouldReturnNoContent() throws Exception {
        // Arrange
        doNothing().when(reservationService).deleteReservation(RESERVATION_ID);

        // Act & Assert
        mockMvc.perform(delete(RESERVATION_BY_ID_ENDPOINT))
                .andExpect(status().isNoContent());

        verify(reservationService, times(1)).deleteReservation(RESERVATION_ID);
    }
}
