package com.book.library.service;

import com.book.library.domain.BookLoanStatus;
import com.book.library.domain.BookLoanType;
import com.book.library.entity.Book;
import com.book.library.entity.BookLoan;
import com.book.library.entity.User;
import com.book.library.exception.BookException;
import com.book.library.mappers.BookLoanMapper;
import com.book.library.payload.dto.BookLoanDTO;
import com.book.library.payload.dto.SubscriptionDTO;
import com.book.library.payload.request.BookLoanSearchRequest;
import com.book.library.payload.request.CheckinRequest;
import com.book.library.payload.request.CheckoutRequest;
import com.book.library.payload.request.RenewalRequest;

import com.book.library.payload.response.PageResponse;
import com.book.library.repository.BookLoanRepository;
import com.book.library.repository.BookRepository;
import com.book.library.service.impl.IBookLoanService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookLoanService implements IBookLoanService {

    private  final BookLoanRepository bookLoanRepository;
    private  final UserService userService;
    private  final  SubscriptionService subscriptionService;
    private  final BookRepository bookRepository;
    private  final BookLoanMapper bookLoanMapper;

    @Override
    public BookLoanDTO checkoutBook(CheckoutRequest checkoutRequest) throws Exception {
        User user = userService.getCurrentUser();
        return checkoutBookForUser(user.getId(), checkoutRequest);
    }

    @Override
    public BookLoanDTO checkoutBookForUser(Long userId, CheckoutRequest checkoutRequest) throws Exception {
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page, size);
        // 1. validate user existence
        User user = userService.findById(userId);
        //2. validate user has exception or not
        SubscriptionDTO subscription = subscriptionService.getUserActiveSubscription(pageable);
        // 3. validate book exists and is available
        Book book = bookRepository.findById(checkoutRequest.getBookId()).orElseThrow(() -> new BookException("book not found with iq" + checkoutRequest.getBookId()));
        if (!book.getActive()){
            throw new BookException("book is not active");
        }
        if (book.getAvailableCopies()<=0){
            throw  new BookException("book is not available");
        }

        //check if user already this check out
        if (bookLoanRepository.hasActiveCheckout(userId, book.getId())){
            throw  new BookException("book has already active checkout");
        }
        // 5.check user active checkout limit
        long activeCheckout = bookLoanRepository.countActiveBookLoansByUser(userId);

        int maxBooksAllowed = subscription.getMaxBooksAllowed();
        if (activeCheckout>maxBooksAllowed){
            throw  new Exception("you have reached maximum number of allowed");
        }

        //6. check for overdue books
        long overdueCount = bookLoanRepository.countOverdueBookLoanByUser(userId);
        if (overdueCount>0){
            throw  new Exception("first return old overdue book!");
        }
        //7. create book loan
        BookLoan bookLoan = BookLoan.builder()
                .user(user)
                .book(book)
                .type(BookLoanType.CHECKOUT)
                .status(BookLoanStatus.CHECKED_OUT)
                .checkoutDate(LocalDate.now())
                .dueDate(LocalDate.now().plusDays(checkoutRequest.getCheckoutDays()))
                .renewalCount(0)
                .maxRenewals(2)
                .notes(checkoutRequest.getNotes())
                .isOverdue(false)
                .overdueDays(0)
                .build();

// update book available copies
        book.setAvailableCopies(book.getAvailableCopies() - 1);
        bookRepository.save(book);
        BookLoan saveBookLoan = bookLoanRepository.save(bookLoan);

        return bookLoanMapper.toDTO(saveBookLoan);
    }

    @Override
    public BookLoanDTO checkinBook(CheckinRequest checkinRequest) throws Exception {
        //1. validate book laon exist
        BookLoan bookLoan = bookLoanRepository.findById(checkinRequest.getBookLoanId()).orElseThrow(() -> new Exception("book loan does not exist"));
        //2. check if already returned
        if (!bookLoan.isActive()){
            throw  new BookException("book loan is already returned");
        }
        //3. set return date
        bookLoan.setReturnDate(LocalDate.now());
        BookLoanStatus condition=checkinRequest.getCondition();
        if (condition==null){
            condition=BookLoanStatus.RETURNED;
        }
        bookLoan.setStatus(condition);

        bookLoan.setOverdueDays(0);
        bookLoan.setIsOverdue(false);
        bookLoan.setNotes("book returned by user");
        //7. update book availability
        if (condition != BookLoanStatus.LOST){
            Book book = bookLoan.getBook();
            book.setAvailableCopies(book.getAvailableCopies() + 1);
            bookRepository.save(book);

            //procces next reservation
        }
        BookLoan savedBook = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(savedBook);
    }

    @Override
    public BookLoanDTO renewCheckout(RenewalRequest renewalRequest) throws Exception {
        //1. validate book laon exist
        BookLoan bookLoan = bookLoanRepository.findById(renewalRequest.getBookLoanId()).orElseThrow(() -> new Exception("book loan does not exist"));
      //2. check if can be renewed
        if (!bookLoan.canRenew()){
            throw  new BookException("book can not be renewed");
        }
        //3. update the book
        bookLoan.setDueDate(bookLoan.getDueDate().plusDays(renewalRequest.getExtensionDays()));
        bookLoan.setRenewalCount(bookLoan.getRenewalCount() + 1);
        bookLoan.setNotes("book renewed by the user");
        BookLoan bookLoan1 = bookLoanRepository.save(bookLoan);
        return bookLoanMapper.toDTO(bookLoan1);
    }

    @Override
    public PageResponse<BookLoanDTO> getMyBookLoans(BookLoanStatus status, int page, int size) throws Exception {
        User currentUser = userService.getCurrentUser();
        Page<BookLoan> bookLoanPage;
        if (status != null){
            Pageable pageable = PageRequest.of(page, size, Sort.by("dueDate").ascending());
            bookLoanPage = bookLoanRepository.findByStatusAndUser(status, currentUser, pageable);
        } else {
            Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
            bookLoanPage = bookLoanRepository.findByUserId(currentUser.getId(), pageable);
        }
        return convertToPageResponse(bookLoanPage);
    }

    private PageResponse<BookLoanDTO> convertToPageResponse(Page<BookLoan> bookLoanPage) {
        List<BookLoanDTO> bookLoanDTOs = bookLoanPage.getContent()
                .stream().map(bookLoanMapper::toDTO)
                .collect(Collectors.toList());

        return new PageResponse<>(
                bookLoanDTOs,
                bookLoanPage.getNumber(),
                bookLoanPage.getSize(),
                bookLoanPage.getTotalPages(),
                bookLoanPage.getNumberOfElements(),
                bookLoanPage.isFirst(),
                bookLoanPage.isLast(),
                bookLoanPage.isEmpty()
        );
    }

    @Override
    public PageResponse<BookLoanDTO> getBookLaons(BookLoanSearchRequest searchRequest) {
        Pageable pageable = createPage(searchRequest.getPage(), searchRequest.getSize(), searchRequest.getSortBy(), searchRequest.getSortDirection());
        Page<BookLoan> bookLoanPage;
        if (Boolean.TRUE.equals(searchRequest.getOverdueOnly())){
            bookLoanPage = bookLoanRepository.findOverdueBookLoans(LocalDate.now(), pageable);
        } else if (searchRequest.getUserId() != null) {
            bookLoanPage = bookLoanRepository.findByUserId(searchRequest.getUserId(), pageable);

        } else if (searchRequest.getBookId() != null) {
            bookLoanPage = bookLoanRepository.findByBookId(searchRequest.getBookId(), pageable);

        } else if (searchRequest.getStatus() != null) {
            bookLoanPage = bookLoanRepository.findByStatus(searchRequest.getStatus(), pageable);

        } else if (searchRequest.getStartDate() != null && searchRequest.getEndDate() != null) {
            bookLoanPage = bookLoanRepository.findBookLoansByDateRange(searchRequest.getStartDate(), searchRequest.getEndDate(), pageable);

        } else {
            bookLoanPage = bookLoanRepository.findAll(pageable);
        }

        return convertToPageResponse(bookLoanPage);
    }

    @Override
    public int updateOverDueBookLoan() {
        Pageable pageable = PageRequest.of(0, 1000);
        Page<BookLoan> overduePage = bookLoanRepository.findOverdueBookLoans(LocalDate.now(), pageable);
        int updateCount = 0;
        for (BookLoan bookLoan : overduePage.getContent()){
            if (bookLoan.getStatus() == BookLoanStatus.CHECKED_OUT){
                bookLoan.setStatus(BookLoanStatus.OVERDUE);
                bookLoan.setIsOverdue(true);

                int overdueDays = calculateOverdueDate(bookLoan.getDueDate(), LocalDate.now());
                bookLoan.setOverdueDays(overdueDays);

                //calculate fine
                bookLoanRepository.save(bookLoan);
                updateCount++;
            }
        }
        return updateCount;
    }

    private Pageable createPage(int page, int size, String sortBy, String sortDirection){
        size = Math.min(size, 100);
        size = Math.max(size, 1);

        Sort sort = sortDirection.equalsIgnoreCase("ASC") ? Sort.by(sortBy).ascending() : Sort.by(sortBy).descending();
        return PageRequest.of(page, size, sort);
    }

    public int calculateOverdueDate(LocalDate dueDate, LocalDate today){
        if (today.isBefore(dueDate) || today.isEqual(dueDate)){
            return 0;
        }
        return (int) ChronoUnit.DAYS.between(dueDate,today);
    }
}
