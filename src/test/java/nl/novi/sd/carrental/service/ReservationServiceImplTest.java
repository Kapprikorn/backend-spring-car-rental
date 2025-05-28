package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.*;
import nl.novi.sd.carrental.repository.ReservationRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Calendar;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ReservationServiceImplTest {

    @Mock
    private ReservationRepository reservationRepository;

    @InjectMocks
    private ReservationServiceImpl reservationService;

    private User testUser;
    private Vehicle testVehicle;
    private Reservation testReservation;
    private Reservation testActiveReservation;

    @BeforeEach
    void setUp() {
        // Create test user
        testUser = new User();
        testUser.setId(1L);
        testUser.setName("Test User");
        testUser.setUsername("testuser");
        testUser.setPassword("Password1@");
        testUser.setEmail("test@example.com");
        testUser.setRole(UserRole.USER);

        // Create test vehicle
        testVehicle = new Vehicle();
        testVehicle.setId(1L);
        testVehicle.setLicensePlate("ABC-123");
        testVehicle.setMake("Test Make");
        testVehicle.setModel("Test Model");
        testVehicle.setStatus(StatusCode.AVAILABLE);
        testVehicle.setPricePerDay(50.0);

        // Create test reservation (past reservation)
        testReservation = new Reservation();
        testReservation.setId(1L);
        Calendar pastStart = Calendar.getInstance();
        pastStart.add(Calendar.DAY_OF_MONTH, -10);
        testReservation.setStartDate(pastStart.getTime());
        Calendar pastEnd = Calendar.getInstance();
        pastEnd.add(Calendar.DAY_OF_MONTH, -5);
        testReservation.setEndDate(pastEnd.getTime());
        testReservation.setTotalPrice(250.0);
        testReservation.setUser(testUser);
        testReservation.setVehicle(testVehicle);

        // Create test active reservation (current reservation)
        testActiveReservation = new Reservation();
        testActiveReservation.setId(2L);
        Calendar activeStart = Calendar.getInstance();
        activeStart.add(Calendar.DAY_OF_MONTH, -2);
        testActiveReservation.setStartDate(activeStart.getTime());
        Calendar activeEnd = Calendar.getInstance();
        activeEnd.add(Calendar.DAY_OF_MONTH, 3);
        testActiveReservation.setEndDate(activeEnd.getTime());
        testActiveReservation.setTotalPrice(250.0);
        testActiveReservation.setUser(testUser);
        testActiveReservation.setVehicle(testVehicle);
    }

    @Test
    void getReservationsByUserId_WithValidId_ShouldReturnReservations() {
        // Arrange
        List<Reservation> reservations = List.of(testReservation, testActiveReservation);
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Act
        List<Reservation> result = reservationService.getReservationsByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals(testReservation.getId(), result.get(0).getId());
        assertEquals(testActiveReservation.getId(), result.get(1).getId());
    }

    @Test
    void getReservationsByUserId_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.getReservationsByUserId(999L)
        );
        assertEquals("No reservations found for user with ID: 999", exception.getMessage());
    }

    @Test
    void getActiveReservationsByUserId_WithValidId_ShouldReturnActiveReservations() {
        // Arrange
        List<Reservation> reservations = List.of(testReservation, testActiveReservation);
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Act
        List<Reservation> result = reservationService.getActiveReservationsByUserId(1L);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testActiveReservation.getId(), result.getFirst().getId());
    }

    @Test
    void getActiveReservationsByUserId_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(reservationRepository.findAll()).thenReturn(Collections.emptyList());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.getActiveReservationsByUserId(999L)
        );
        assertEquals("No active reservations found for user with ID: 999", exception.getMessage());
    }

    @Test
    void getActiveReservationsByUserId_WithValidIdButNoActiveReservations_ShouldThrowResourceNotFoundException() {
        // Arrange
        List<Reservation> reservations = Collections.singletonList(testReservation); // Only past reservation
        when(reservationRepository.findAll()).thenReturn(reservations);

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.getActiveReservationsByUserId(1L)
        );
        assertEquals("No active reservations found for user with ID: 1", exception.getMessage());
    }

    @Test
    void getReservation_WithValidId_ShouldReturnReservation() {
        // Arrange
        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));

        // Act
        Reservation result = reservationService.getReservation(1L);

        // Assert
        assertNotNull(result);
        assertEquals(testReservation.getId(), result.getId());
        assertEquals(testReservation.getStartDate(), result.getStartDate());
        assertEquals(testReservation.getEndDate(), result.getEndDate());
    }

    @Test
    void getReservation_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.getReservation(999L)
        );
        assertEquals("Reservation not found", exception.getMessage());
    }

    @Test
    void createReservation_ShouldReturnSavedReservation() {
        // Arrange
        when(reservationRepository.save(any(Reservation.class))).thenReturn(testReservation);

        // Act
        Reservation result = reservationService.createReservation(testReservation);

        // Assert
        assertNotNull(result);
        assertEquals(testReservation.getId(), result.getId());
        assertEquals(testReservation.getStartDate(), result.getStartDate());
        assertEquals(testReservation.getEndDate(), result.getEndDate());
        // Verify save is called twice (as per implementation)
        verify(reservationRepository, times(2)).save(any(Reservation.class));
    }

    @Test
    void updateReservation_WithValidId_ShouldReturnUpdatedReservation() {
        // Arrange
        Reservation updatedReservation = new Reservation();
        Calendar updatedStart = Calendar.getInstance();
        updatedStart.add(Calendar.DAY_OF_MONTH, 5);
        updatedReservation.setStartDate(updatedStart.getTime());
        Calendar updatedEnd = Calendar.getInstance();
        updatedEnd.add(Calendar.DAY_OF_MONTH, 10);
        updatedReservation.setEndDate(updatedEnd.getTime());
        updatedReservation.setTotalPrice(300.0);
        updatedReservation.setUser(testUser);
        updatedReservation.setVehicle(testVehicle);

        when(reservationRepository.findById(1L)).thenReturn(Optional.of(testReservation));
        when(reservationRepository.save(any(Reservation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        Reservation result = reservationService.updateReservation(1L, updatedReservation);

        // Assert
        assertNotNull(result);
        assertEquals(testReservation.getId(), result.getId());
        assertEquals(updatedReservation.getStartDate(), result.getStartDate());
        assertEquals(updatedReservation.getEndDate(), result.getEndDate());
        assertEquals(updatedReservation.getTotalPrice(), result.getTotalPrice());
        assertEquals(updatedReservation.getUser(), result.getUser());
        assertEquals(updatedReservation.getVehicle(), result.getVehicle());
    }

    @Test
    void updateReservation_WithInvalidId_ShouldThrowResourceNotFoundException() {
        // Arrange
        when(reservationRepository.findById(999L)).thenReturn(Optional.empty());

        // Act & Assert
        ResourceNotFoundException exception = assertThrows(
                ResourceNotFoundException.class,
                () -> reservationService.updateReservation(999L, testReservation)
        );
        assertEquals("Reservation not found", exception.getMessage());
    }

    @Test
    void deleteReservation_ShouldCallRepositoryDeleteById() {
        // Arrange
        doNothing().when(reservationRepository).deleteById(1L);

        // Act
        reservationService.deleteReservation(1L);

        // Assert
        verify(reservationRepository, times(1)).deleteById(1L);
    }
}