package com.book.library.controller;

import com.book.library.domain.ReservationStatus;
import com.book.library.payload.dto.ReservationDTO;
import com.book.library.payload.request.ReservationRequest;
import com.book.library.payload.request.ReservatonSearchRequest;
import com.book.library.payload.response.PageResponse;
import com.book.library.service.ReservationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/reservation")
public class ReservationController {
    private  final ReservationService reservationService;

    @PostMapping()
    public ResponseEntity<?> createReservation(@Valid @RequestBody ReservationRequest reservationRequest) throws Exception {
        ReservationDTO reservationDTO = reservationService.createReservation(reservationRequest);
        return ResponseEntity.ok(reservationDTO);
    }

    @PostMapping("/user/{userId}")
    public ResponseEntity<?> createReservationForUser(@PathVariable Long userId ,@Valid @RequestBody ReservationRequest reservationRequest) throws Exception {
        ReservationDTO reservation = reservationService.createReservationForUser(reservationRequest, userId);
        return new ResponseEntity<>(reservation, HttpStatus.CREATED);

    }


    @PostMapping("/{id}/fulfill")
    public ResponseEntity<?> fulfillReservation(@PathVariable Long id) throws Exception {
        ReservationDTO reservation = reservationService.fulfillReservation(id);
        return  ResponseEntity.ok(reservation);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> cancelReservation(@PathVariable Long id) throws Exception {
        ReservationDTO reservation = reservationService.cancelReservation(id);
        return ResponseEntity.ok(reservation);
    }

    @GetMapping
    public ResponseEntity<PageResponse<ReservationDTO>> getMyReservations(
            @RequestParam(required = false)ReservationStatus status,
            @RequestParam(required = false)Boolean activeOnly,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "20") int size,
            @RequestParam(defaultValue = "reservedAt") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) throws Exception {

        ReservatonSearchRequest searchRequest = new ReservatonSearchRequest();
        searchRequest.setStatus(status);


        searchRequest.setActiveOnly(activeOnly);
        searchRequest.setPage(page);

        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);

        PageResponse<ReservationDTO> reservation = reservationService.getMyReservations(searchRequest);
        return ResponseEntity.ok(reservation);
    }

}
