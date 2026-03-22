package com.book.library.entity;


import com.book.library.domain.ReservationStatus;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Builder
public class Reservation {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private  Long id;

    @ManyToOne
    private User user;

    @ManyToOne
    @JoinColumn(name = "book_id")
    private Book book;

    private ReservationStatus status = ReservationStatus.PENDING;
    private LocalDateTime reservedAt;
    private LocalDateTime availableAt;
    private LocalDateTime fulFilledAt;
    private LocalDateTime cancelledAt;
    private LocalDateTime availableUntil;

    @Column(name = "notification_sent")
    private Boolean notificationSent;
    private  int queuePosition;

    @Column(columnDefinition = "TEXT")
    private  String notes;

    @CreationTimestamp
    @Column(name = "created_at", nullable = false,updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    public boolean canBeCancelled(){
        return status == ReservationStatus.PENDING || status ==ReservationStatus.AVAILABLE;
    }

    public boolean hasExpired(){
        return status == ReservationStatus.AVAILABLE &&availableUntil != null && LocalDateTime.now().isAfter(availableUntil);
    }


}
