package com.book.library.entity;

import com.book.library.domain.FineStatus;
import com.book.library.domain.FineType;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Fine {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @ManyToOne
    @JoinColumn(nullable = false)
    private BookLoan bookLoan;

    private FineType type;

    @Column(nullable = false)
    private Long amount;

    private FineStatus status;

    @Column(length = 500)
    private  String reason;

    @Column(length = 1000)
    private  String note;

    @ManyToOne
    private User waiveBy;

    @Column(name = "waived_At")
    private LocalDateTime waivedAt;

    @Column(name = "waived_reason")
    private String waiveReason;

    @Column(name = "paid_at")
    private LocalDateTime paidAt;

    @ManyToOne
    @JoinColumn(name = "proccessed_by_user_id")
    private User proccessedBy;

    @Column(name = "transaction_id")
    private  String transactionId;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    @Column(nullable = false)
    @UpdateTimestamp
    private LocalDateTime updateAt;

    public  void applyPayment(Long paymentAmount){
        if (paymentAmount == null || paymentAmount <= 0){
            throw  new IllegalArgumentException("payment amount must be positive");
        }
        this.status = FineStatus.PAID;
        this.paidAt = LocalDateTime.now();
    }

    public  void waive(User admin, String reason){
        this.status = FineStatus.WAIVED;
        this.waiveBy = admin;
        this.waivedAt = LocalDateTime.now();
        this.waiveReason = reason;
    }

}
