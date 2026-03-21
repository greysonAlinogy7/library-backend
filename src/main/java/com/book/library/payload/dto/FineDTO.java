package com.book.library.payload.dto;
import com.book.library.domain.FineStatus;
import com.book.library.domain.FineType;
import jakarta.persistence.Column;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class FineDTO {
    private Long id;


    @NotNull(message = "BookLoan Id is mandatory")
    private Long bookLoanId;
    private  String bookTitle;
    private  String bookIsbn;

    @NotNull(message = "User ID is mandatory")
    private  Long userId;
    private  String userName;
    private  String userEmail;

    @NotNull(message = "fine type is mandatory")
    private FineType type;


    @NotNull(message = "Fine amount is mandatory")
    @PositiveOrZero(message = "Amount paid can not be negative")
    private Long amount;
    @PositiveOrZero(message = "amount paid can not be negative")
    private  Long amountPaid;
    private  Long amountOutstanding;

    @NotNull(message = "Fine status is mandatory")
    private FineStatus status;

    @Column(length = 500)
    private  String reason;

    @Column(length = 1000)
    private  String note;


    private Long waiveByUserId;
    private  String waivedByUserName;



    private LocalDateTime waivedAt;


    private String waiveReason;


    private LocalDateTime paidAt;


    private Long processedByUserId;
    private  String processedByUserName;


    private  String transactionId;


    private LocalDateTime createdAt;


    private LocalDateTime updateAt;
}
