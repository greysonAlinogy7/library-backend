package com.book.library.service.impl;

import com.book.library.payload.dto.BookReviewDTO;
import com.book.library.payload.request.CreateReviewRequest;
import com.book.library.payload.request.UpdateReviewRequest;
import com.book.library.payload.response.PageResponse;

public interface IReviewService {

    BookReviewDTO createReview(CreateReviewRequest createReviewRequest) throws Exception;
    BookReviewDTO updateReview(Long reviewId, UpdateReviewRequest updateReviewRequest);
    void deleteReview(Long reviewId);
    PageResponse<BookReviewDTO> getReviewByBookId(Long id, int page, int size);

}
