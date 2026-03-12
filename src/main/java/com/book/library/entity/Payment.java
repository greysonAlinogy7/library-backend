package com.book.library.entity;

import com.book.library.domain.PaymentGateway;
import com.book.library.domain.PaymentMethod;
import com.book.library.domain.PaymentStatus;
import com.book.library.domain.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @ManyToOne
    private User user;
    @ManyToOne
    private Subscription subscription;
    @Enumerated(EnumType.STRING)
    private PaymentType paymentType;
    @Enumerated(EnumType.STRING)
    private PaymentStatus paymentStatus;
    @Enumerated(EnumType.STRING)
   private PaymentMethod paymentMethod;
    @Enumerated(EnumType.STRING)
    private PaymentGateway gateway;
    private Long amount;
    private  String transactionId;
    private  String gatewayPaymentId;
    private  String gatewayOrderId;
    private  String gatewaySignature;

    private String description;
    private String failureReason;
    @CreationTimestamp
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;

    @CreationTimestamp
    private  LocalDateTime createdAt;
    @UpdateTimestamp
    private  LocalDateTime updatedAt;

}
