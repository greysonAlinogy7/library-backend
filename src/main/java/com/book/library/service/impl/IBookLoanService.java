package com.book.library.service.impl;

import com.book.library.domain.BookLoanStatus;
import com.book.library.payload.dto.BookLoanDTO;
import com.book.library.payload.request.BookLoanSearchRequest;
import com.book.library.payload.request.CheckinRequest;
import com.book.library.payload.request.CheckoutRequest;
import com.book.library.payload.request.RenewalRequest;
import com.book.library.payload.response.PageResponse;

public interface IBookLoanService {
    BookLoanDTO checkoutBook(CheckoutRequest checkoutRequest) throws Exception;
    BookLoanDTO checkoutBookForUser(Long userId, CheckoutRequest checkoutRequest) throws Exception;
    BookLoanDTO checkinBook(CheckinRequest checkinRequest) throws Exception;
    BookLoanDTO renewCheckout(RenewalRequest renewalRequest) throws Exception;
    PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int size) throws Exception;
    PageResponse<BookLoanDTO> getBookLaons(BookLoanSearchRequest request);
    int updateOverDueBookLoan();

}
