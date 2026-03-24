package com.book.library.service;

import com.book.library.domain.BookLoanStatus;
import com.book.library.entity.Book;
import com.book.library.entity.BookLoan;
import com.book.library.entity.BookReview;
import com.book.library.entity.User;
import com.book.library.mappers.BookReViewMapper;
import com.book.library.payload.dto.BookReviewDTO;
import com.book.library.payload.request.CreateReviewRequest;
import com.book.library.payload.request.UpdateReviewRequest;
import com.book.library.payload.response.PageResponse;
import com.book.library.repository.BookLoanRepository;
import com.book.library.repository.BookRepository;
import com.book.library.repository.BookReviewRepository;
import com.book.library.service.impl.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookReviewService implements IReviewService {

    private  final BookReviewRepository bookReviewRepository;
    private  final UserService userService;
    private  final BookRepository bookRepository;

    private final BookReViewMapper bookReviewMapper;
    private  final BookLoanRepository bookLoanRepository;

    @Override
    public BookReviewDTO createReview(CreateReviewRequest createReviewRequest) throws Exception {
        //1. fetch the authenticate user or login user
        User user = userService.getCurrentUser();
        //2. validate book
        Book book = bookRepository.findById(createReviewRequest.getBookId()).orElseThrow(() -> new Exception("book not found"));

        //3. check if user has already reviewed the book
        if (!bookReviewRepository.existsByUserIdAndBookId(user.getId(), book.getId())){
            throw  new Exception("you have already reviewed this book!");
        }

        //4.check if user has read the book
        boolean hasReadBook = hasUserReadBook(user.getId(), book.getId());
        if (!hasReadBook){
            throw  new Exception("you have not read this book");
        }
        //5.create review
        BookReview bookReview = BookReview.builder()
                .user(user)
                .book(book)
                .rating(createReviewRequest.getRating())
                .reviewText(createReviewRequest.getReviewText())
                .title(createReviewRequest.getTitle())
                .build();
        BookReview savedReview = bookReviewRepository.save(bookReview);
        return bookReviewMapper.toDTO(savedReview);
    }



    @Override
    public BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest updateReviewRequest) throws Exception {
        User user = userService.getCurrentUser();
        //find review
        BookReview bookReview =bookReviewRepository.findById(reviewId).orElseThrow(() -> new Exception("review not found"));
        if (!bookReview.getUser().getId().equals(user.getId())){
            throw  new Exception("you have not reviewed this book");
        }
       bookReview.setReviewText(updateReviewRequest.getReviewText());
        bookReview.setTitle(updateReviewRequest.getTitle());
        bookReview.setRating(updateReviewRequest.getRating());

        BookReview savedBookReview = bookReviewRepository.save(bookReview);

        return bookReviewMapper.toDTO(savedBookReview);
    }

    @Override
    public void deleteReview(Long reviewId) throws Exception {
        User user = userService.getCurrentUser();
        BookReview bookReview = bookReviewRepository.findById(reviewId).orElseThrow(() -> new Exception("review not found with id" + reviewId));

        if (!bookReview.getUser().getId().equals(user.getId())){
            throw  new Exception("You can only delete your own reviews");
        }
        bookReviewRepository.delete(bookReview);

    }

    @Override
    public PageResponse<BookReviewDTO> getReviewByBookId(Long id, int page, int size) throws Exception {
        Pageable pageable = PageRequest.of(page,size, Sort.by("createdAt").descending());
        Book book = bookRepository.findById(id).orElseThrow(() -> new Exception("book not found"));
        Page<BookReview> reviewPage = bookReviewRepository.findByBook(book, pageable);
        return convertToPageResponse(reviewPage);
    }

    private PageResponse<BookReviewDTO> convertToPageResponse(Page<BookReview> reviewPage) {
        List<BookReviewDTO> reviewDTOs = reviewPage.getContent().stream().map(bookReviewMapper::toDTO).collect(Collectors.toList());
        return  new PageResponse<>(
                reviewDTOs,
                reviewPage.getNumber(),
                reviewPage.getSize(),
                reviewPage.getTotalElements(),
                reviewPage.getTotalPages(),
                reviewPage.isLast(),
                reviewPage.isFirst(),
                reviewPage.isEmpty()
        );
    }

    private boolean hasUserReadBook(Long userId, Long bookId) {
        List<BookLoan> bookLoans = bookLoanRepository.findByBookId(bookId);
        return bookLoans.stream().anyMatch(loan -> loan.getUser().getId().equals(userId) &&
                loan.getStatus() == BookLoanStatus.RETURNED);
    }
}
