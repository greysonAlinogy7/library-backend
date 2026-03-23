package com.book.library.repository;

import com.book.library.domain.ReservationStatus;
import com.book.library.entity.Reservation;
import jakarta.validation.constraints.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface ReservationRepository extends JpaRepository<Reservation, Long> {



    @Query("""
              SELECT r
              FROM Reservation r
              WHERE r.book.id = :bookId
              AND r.status = com.book.library.domain.ReservationStatus.PENDING
              ORDER BY r.reservedAt ASC
          """)
    Optional<Reservation> findNextPendingReservation(@Param("bookId") Long bookId);

    @Query("""
              SELECT CASE WHEN COUNT(r) > 0 THEN true ELSE false END
              FROM Reservation r
              WHERE r.user.id = :userId
              AND r.book.id = :bookId
              AND (r.status = 'PENDING' OR r.status = 'AVAILABLE')
            """)
    boolean hasActiveReservation(Long userId, Long bookId);

    @Query("""
    SELECT r FROM Reservation r
    WHERE r.user.id = :userId
      AND (:bookId IS NULL OR r.book.id = :bookId)
      AND (:status IS NULL OR r.status = :status)
    ORDER BY r.reservedAt DESC
""")
    Page<Reservation> findMyReservations(
            @Param("userId") Long userId,
            @Param("bookId") Long bookId,     // can be null
            @Param("status") ReservationStatus status,  // can be null
            Pageable pageable
    );

    @Query("""
    SELECT COUNT(r) 
    FROM Reservation r 
    WHERE r.user.id = :userId 
    AND r.book.id = :bookId 
    AND (r.status = 'PENDING' OR r.status = 'AVAILABLE')
""")
    long countActiveReservationByUser(
            @Param("userId") Long userId,
            @Param("bookId") Long bookId
    );

    @Query("SELECT COUNT(r) FROM Reservation r WHERE r.book.id = :bookId AND r.status = 'PENDING'")
    long countPendingReservationsByBook(@Param("bookId") Long bookId);

    @Query("SELECT r FROM Reservation r" +
            " WHERE r.status = :status AND r.availableUntil < :currentDateTime")
    List<Reservation> findExpiredReservation(
            @Param("status") ReservationStatus status,
            @Param("currentDateTime") LocalDateTime currentDateTime
    );


    @Query("SELECT r FROM Reservation r " +
            "WHERE r.book.id = :bookId " +
            "AND r.user.id = :userId " +
            "AND r.status = :status "
            )
    Page<Reservation> searchReservationWithFilters(
            @Param("userId") Long userId,
            @Param("bookId") Long bookId,
            @Param("status") ReservationStatus status,
            Pageable pageable);



}
