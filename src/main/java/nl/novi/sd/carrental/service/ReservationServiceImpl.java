package nl.novi.sd.carrental.service;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.exception.ResourceNotFoundException;
import nl.novi.sd.carrental.model.Reservation;
import nl.novi.sd.carrental.repository.ReservationRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReservationServiceImpl implements ReservationService {

    private final ReservationRepository reservationRepository;

    @Override
    public List<Reservation> getReservationsByUserId(Long userId) {
        List<Reservation> reservations = reservationRepository.findAll().stream()
                .filter(reservation -> reservation.getUser().getId().equals(userId))
                .toList();
        if (reservations.isEmpty()) {
            throw new ResourceNotFoundException("No reservations found for user with ID: " + userId);
        }
        return reservations;
    }

    @Override
    public List<Reservation> getActiveReservationsByUserId(Long userId) {
        List<Reservation> reservations = reservationRepository.findAll().stream()
                .filter(reservation ->
                        reservation
                            .getUser()
                            .getId()
                            .equals(userId))
                .filter(reservation ->
                        reservation
                            .getEndDate()
                            .toInstant()
                            .isAfter(java.time.Instant.now()))
                .toList();
        if (reservations.isEmpty()) {
            throw new ResourceNotFoundException("No active reservations found for user with ID: " + userId);
        }
        return reservations;
    }

    @Override
    public Reservation getReservation(Long id) {
        return reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));
    }

    @Override
    public Reservation createReservation(Reservation reservation) {
        Reservation newReservation = reservationRepository.save(reservation);
        return reservationRepository.save(newReservation);
    }

    @Override
    public Reservation updateReservation(Long id, Reservation reservation) {
        Reservation existingReservation = reservationRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Reservation not found"));

        updateReservationFields(existingReservation, reservation);
        return reservationRepository.save(existingReservation);
    }

    @Override
    public void deleteReservation(Long id) {
        reservationRepository.deleteById(id);
    }

    private void updateReservationFields(Reservation existingReservation, Reservation updatedReservation) {
        existingReservation.setStartDate(updatedReservation.getStartDate());
        existingReservation.setEndDate(updatedReservation.getEndDate());
        existingReservation.setTotalPrice(updatedReservation.getTotalPrice());
        existingReservation.setUser(updatedReservation.getUser());
        existingReservation.setVehicle(updatedReservation.getVehicle());
    }
}
