package com.book.library.controller;
import com.book.library.domain.BookLoanStatus;
import com.book.library.payload.dto.BookLoanDTO;
import com.book.library.payload.request.BookLoanSearchRequest;
import com.book.library.payload.request.CheckinRequest;
import com.book.library.payload.request.CheckoutRequest;
import com.book.library.payload.request.RenewalRequest;
import com.book.library.payload.response.ApiResponse;
import com.book.library.payload.response.PageResponse;
import com.book.library.service.BookLoanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book-loan")
public class BookLoanController {

    private final BookLoanService bookLoanService;

    @PostMapping("/checkout")
    public ResponseEntity<?> checkoutBook(@Valid @RequestBody CheckoutRequest checkoutRequest) throws Exception {
        BookLoanDTO bookLoan = bookLoanService.checkoutBook(checkoutRequest);
        return new ResponseEntity<>(bookLoan, HttpStatus.CREATED);
    }


    @PostMapping("/checkout/user/{userId}")
    public ResponseEntity<?> checkoutBookForUser(@PathVariable Long userId, @Valid @RequestBody CheckoutRequest checkoutRequest) throws Exception {
        BookLoanDTO bookLoan = bookLoanService.checkoutBookForUser(userId, checkoutRequest);
        return  new ResponseEntity<>(bookLoan, HttpStatus.CREATED);
    }

    @PostMapping("/checkin")
    public ResponseEntity<?> checkInBook( @Valid @RequestBody CheckinRequest checkinRequest) throws Exception {
        BookLoanDTO bookLoan = bookLoanService.checkinBook(checkinRequest);
        return  new ResponseEntity<>(bookLoan, HttpStatus.CREATED);
    }
    @PostMapping("/renew")
    public ResponseEntity<?> renew(@RequestBody RenewalRequest renewalRequest) throws Exception {
        BookLoanDTO bookLoan = bookLoanService.renewCheckout(renewalRequest);
        return  new ResponseEntity<>(bookLoan, HttpStatus.OK);
    }

    @GetMapping("/my")
    public ResponseEntity<?> getMyBookLoan(@RequestParam(required = false)BookLoanStatus status, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size) throws Exception {
        PageResponse<BookLoanDTO> bookLoan = bookLoanService.getMyBookLoans(status, page, size);
        return ResponseEntity.ok(bookLoan);
    }
    @PostMapping("/search")
    public ResponseEntity<?> getAllBookLoana(@RequestBody BookLoanSearchRequest searchRequest){
        PageResponse<BookLoanDTO> bookLoans = bookLoanService.getBookLaons(searchRequest);
        return ResponseEntity.ok(bookLoans);
    }

    @PostMapping("/admin/update-overdue")
    public ResponseEntity<?> updateOverdueBookLoan(){
        int updateCount = bookLoanService.updateOverDueBookLoan();
        return ResponseEntity.ok(new ApiResponse("overdue book loan is updated", true));
    }

}
