package com.book.library.payload.request;

import com.book.library.domain.BookLoanStatus;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class BookLoanSearchRequest {
    private Long userId;
    private  Long bookId;
    private BookLoanStatus status;
    private  Boolean overdueOnly;
    private Boolean unpaidFineOnly;
    private LocalDate startDate;
    private LocalDate endDate;
    private  Integer page = 0;
    private Integer size= 20;
    private  String sortBy = "createdAt";
    private  String sortDirection= "DESC";
}
