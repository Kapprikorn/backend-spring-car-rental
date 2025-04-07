package nl.novi.sd.carrental.service;

import nl.novi.sd.carrental.model.Reservation;

import java.util.List;

public interface ReservationService {

    List<Reservation> getReservationsByUserId(Long userId);

    Reservation getReservation(Long id);

    Reservation createReservation(Reservation reservation);

    Reservation updateReservation(Long id, Reservation reservation);

    void deleteReservation(Long id);
}
