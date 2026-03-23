package com.book.library.service;

import com.book.library.entity.Book;
import com.book.library.entity.BookReview;
import com.book.library.entity.User;
import com.book.library.payload.dto.BookReviewDTO;
import com.book.library.payload.request.CreateReviewRequest;
import com.book.library.payload.request.UpdateReviewRequest;
import com.book.library.payload.response.PageResponse;
import com.book.library.repository.BookRepository;
import com.book.library.repository.BookReviewRepository;
import com.book.library.service.impl.IReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookReviewService implements IReviewService {

    private  final BookReviewRepository bookReviewRepository;
    private  final UserService userService;
    private  final BookRepository bookRepository;

    @Override
    public BookReviewDTO createReview(CreateReviewRequest createReviewRequest) throws Exception {
        //1. fetch the authenticate user or login user
        User user = userService.getCurrentUser();
        //2. validate book
        Book book = bookRepository.findById(createReviewRequest.getBookId()).orElseThrow(() -> new Exception("book not found"));

        //3. check if user has already reviewed the book
        if (bookReviewRepository.existsByUserIdAndBookId(user.getId(), book.getId())){
            throw  new Exception("you have already reviewed this book!");
        }

        //4.check if user has read the book
        boolean hasReadBook = hasUserReadBook(user.getId(), book.getId());
        if (!hasReadBook){
            throw  new Exception("you have not read this book");
        }
        //5.create review
        BookReview bookReview = new BookReview();
        bookReview.setUser(user);
        bookReview.setBook(book);
        bookReview.setRating(createReviewRequest.getRating());
        bookReview.setReviewText(createReviewRequest.getReviewText());
        bookReview.setTitle(createReviewRequest.getTitle());
        BookReview savedReview = bookReviewRepository.save(bookReview);
        return null;
    }



    @Override
    public BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest updateReviewRequest) {
        return null;
    }

    @Override
    public void deleteReview(Long reviewId) {

    }

    @Override
    public PageResponse<BookReviewDTO> getReviewByBookId(Long id, int page, int size) {
        return null;
    }

    private boolean hasUserReadBook(Long id, Long id1) {
        return false;
    }
}
