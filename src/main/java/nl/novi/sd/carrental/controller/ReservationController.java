package nl.novi.sd.carrental.controller;

import lombok.RequiredArgsConstructor;
import nl.novi.sd.carrental.dto.ReservationDto;
import nl.novi.sd.carrental.model.Reservation;
import nl.novi.sd.carrental.service.ReservationService;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reservations")
@RequiredArgsConstructor
public class ReservationController {

    private final ReservationService reservationService;

    private final ModelMapper mapper = new ModelMapper();

    @ResponseBody
    @GetMapping
    public List<ReservationDto> getReservationsByUserId(@RequestParam Long userId) {
        return reservationService
                .getReservationsByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @ResponseBody
    @GetMapping
    public List<ReservationDto> getActiveReservationsByUserId(@RequestParam Long userId) {
        return reservationService
                .getActiveReservationsByUserId(userId)
                .stream()
                .map(this::mapToDto)
                .toList();
    }

    @ResponseBody
    @GetMapping("/{id}")
    public ReservationDto getReservationById(@PathVariable Long id) {
        return this.mapToDto(reservationService.getReservation(id));
    }

    @ResponseBody
    @PostMapping
    public ReservationDto createReservation(
            @RequestBody ReservationDto reservationDto
    ) {
        return this.mapToDto(reservationService.createReservation(
                this.mapToEntity(reservationDto)
        ));
    }

    @ResponseBody
    @PutMapping("/{id}")
    public ReservationDto updateReservation(@PathVariable Long id, @RequestBody ReservationDto updatedReservation) {
        return this.mapToDto(reservationService.updateReservation(id, this.mapToEntity(updatedReservation)));
    }

    @ResponseStatus(HttpStatus.NO_CONTENT)
    @ResponseBody
    @DeleteMapping("/{id}")
    public void deleteReservation(@PathVariable Long id) {
        reservationService.deleteReservation(id);
    }

    private ReservationDto mapToDto(Reservation reservation) {
        return mapper.map(reservation, ReservationDto.class);
    }

    private Reservation mapToEntity(ReservationDto reservationDto) {
        return mapper.map(reservationDto, Reservation.class);
    }
}
