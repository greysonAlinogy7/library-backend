package com.book.library.payload.dto;

import com.book.library.domain.PaymentGateway;
import com.book.library.domain.PaymentStatus;
import com.book.library.domain.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaymentDTO {
    private Long id;
    @NotNull(message = "User Id is mandatory")
    private Long userId;
    private  String userName;
    private  String userEmail;
    private Long bookLoanId;
    private  String bookLoan;
    private Long subscriptionId;
    @NotNull(message = "Payment type is mandatory")
    private PaymentType paymentType;
    @NotNull(message = "Payment gateway is mandatory")
    private PaymentGateway gateway;
    private PaymentStatus status;

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private Long amount;

    private String transactionId;
    private  String gatewayPaymentId;
    private String gatewayOrderId;
    private  String gatewaySignature;
    private  String paymentMethod;
    private String description;
    private  String failureReason;
    private  Integer retryCount;
    private LocalDateTime initiatedAt;
    private LocalDateTime completedAt;
    private Boolean notificationSent;
    private Boolean active;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
