package nl.novi.sd.carrental.controller;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.model.Reservation;
import nl.novi.sd.carrental.repository.ReservationRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationRepository reservationRepository;

    @GetMapping
    public List<Reservation> getAllReservations() {
        return reservationRepository.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Reservation> getReservationById(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public Reservation createReservation(@RequestBody Reservation reservation) {
        return reservationRepository.save(reservation);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Reservation> updateReservation(@PathVariable Long id, @RequestBody Reservation updatedReservation) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    // TODO: Setup logic in service layer.
                    return ResponseEntity.ok(reservationRepository.save(reservation));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Object> deleteReservation(@PathVariable Long id) {
        return reservationRepository.findById(id)
                .map(reservation -> {
                    reservationRepository.delete(reservation);
                    return ResponseEntity.noContent().build();
                })
                .orElse(ResponseEntity.notFound().build());
    }
}