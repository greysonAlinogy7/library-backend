package com.book.library.payload.request;

import com.book.library.domain.PaymentGateway;
import com.book.library.domain.PaymentMethod;
import com.book.library.domain.PaymentStatus;
import com.book.library.domain.PaymentType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class PaymentInitiatedRequest {

    @NotNull(message = "user ID is mandatory")
    private Long userId;
    private Long bookLoanId;

    @NotNull(message = "payment type is mandatory")
    private PaymentType paymentType;

    @NotNull(message = "payment gateway is mandatory")
    private PaymentGateway gateway;
    private PaymentStatus status;
    private PaymentMethod paymentMethod;

    @NotNull(message = "Amount is mandatory")
    @Positive(message = "Amount must be positive")
    private Long amount;

    @Size(max = 500, message = "Description Url must not exceed 500 character")
    private String description;
    private  Long fineId;
    private Long subscriptionId;

    @Size(max = 500, message = "Success URL must not exceed 500 characters")
    private String successUrl;

    @Size(message = "Cancel URL must not exceed 500 character")
    private  String cancelUrl;


}
