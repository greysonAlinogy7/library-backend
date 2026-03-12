package com.book.library.service.impl;

import com.book.library.exception.BookException;
import com.book.library.payload.dto.BookDTO;
import com.book.library.payload.request.BookSearchRequest;
import com.book.library.payload.response.PageResponse;
import java.util.List;

public interface IBookService {
    BookDTO createBook(BookDTO bookDTO) throws BookException;
    List<BookDTO> createBookBulk(List<BookDTO> bookDTOS) throws BookException;
    BookDTO getBookById(Long bookId) throws BookException;
    BookDTO getBookByISBN(String isbn) throws BookException;
    BookDTO updateBook(Long bookId, BookDTO bookDTO) throws BookException;
    void deleteBook(Long bookId) throws BookException;
    void hardDeleteBook(Long bookId) throws BookException;
    PageResponse<BookDTO> searchBookWithFilter(BookSearchRequest searchRequest);
    long getTotalActiveBooks();
    long getTotalAvailableBooks();

}
