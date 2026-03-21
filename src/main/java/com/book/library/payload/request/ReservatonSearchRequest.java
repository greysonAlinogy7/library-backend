package com.book.library.payload.request;


import com.book.library.domain.ReservationStatus;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReservatonSearchRequest {
    private Long userId;
    private Long bookId;
    private ReservationStatus status;
    private Boolean activeOnly;

    private  int page = 0;
    private  int size = 20;

    private  String sortBy = "reservedAt";
    private  String sortDirection = "DESC";
}
