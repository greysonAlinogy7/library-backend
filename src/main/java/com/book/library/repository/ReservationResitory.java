package com.book.library.repository;

import com.book.library.entity.Reservation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReservationResitory extends JpaRepository<Reservation, Long> {
}
