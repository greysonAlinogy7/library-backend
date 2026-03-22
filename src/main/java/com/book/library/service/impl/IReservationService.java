package com.book.library.service.impl;

import com.book.library.payload.dto.ReservationDTO;
import com.book.library.payload.request.ReservationRequest;
import com.book.library.payload.request.ReservatonSearchRequest;
import com.book.library.payload.response.PageResponse;

public interface IReservationService {
    ReservationDTO createReservation(ReservationRequest reservationRequest);
    ReservationDTO createReservationForUser(ReservationRequest reservationRequest ,Long userId) throws Exception;
    ReservationDTO cancelReservation(Long reservationId) throws Exception;
    ReservationDTO fulfillReservation(Long reservationId) throws Exception;
    PageResponse<ReservationDTO> getMyReservations(ReservatonSearchRequest searchRequest);
    PageResponse<ReservationDTO> searchReservations(ReservatonSearchRequest searchRequest);

}
