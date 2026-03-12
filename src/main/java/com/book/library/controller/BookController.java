package com.book.library.controller;

import com.book.library.exception.BookException;
import com.book.library.payload.dto.BookDTO;
import com.book.library.payload.request.BookSearchRequest;
import com.book.library.payload.response.ApiResponse;
import com.book.library.payload.response.PageResponse;
import com.book.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/books")
public class BookController {
    private  final BookService bookService;
    

    @PostMapping("/bulk")
    public ResponseEntity<?> createBookBulk(@Valid @RequestBody List<BookDTO> bookDTO) throws BookException {
        List<BookDTO> createBooks = bookService.createBookBulk(bookDTO);
        return  ResponseEntity.ok(createBooks);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BookDTO> getBookById(@PathVariable Long id) throws BookException {
        BookDTO book = bookService.getBookById(id);
        return ResponseEntity.ok(book);
    }

    @GetMapping("/isbn/{isbn}")
    public ResponseEntity<BookDTO> getBookByIsbn(@PathVariable String isbn) throws BookException {
        BookDTO book = bookService.getBookByISBN(isbn);
        return ResponseEntity.ok(book);
    }

    @GetMapping
    public ResponseEntity<PageResponse<BookDTO>> searchBooks(@RequestParam(required = false) Long genreId,
                                                             @RequestParam(required = false, defaultValue = "false") Boolean availableOnly, @RequestParam(defaultValue = "true") boolean activeOnly, @RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "20") int size, @RequestParam(defaultValue = "createdAt") String sortBy, @RequestParam(defaultValue = "DESC") String sortDirection){
        BookSearchRequest searchRequest = new BookSearchRequest();
        searchRequest.setGenreId(genreId);
        searchRequest.setAvailableOnly(availableOnly);
        searchRequest.setPage(page);
        searchRequest.setSize(size);
        searchRequest.setSortBy(sortBy);
        searchRequest.setSortDirection(sortDirection);
        PageResponse<BookDTO> books = bookService.searchBookWithFilter(searchRequest);
        return ResponseEntity.ok(books);
    }

    @PutMapping("/{id}")
    public ResponseEntity<BookDTO> updateBook(@PathVariable Long id, @RequestBody BookDTO bookDTO){
        try{
            BookDTO updatedBook = bookService.updateBook(id, bookDTO);
            return ResponseEntity.ok(updatedBook);
        } catch (BookException e){
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse> deleteBook(@PathVariable Long id) throws BookException {
        bookService.deleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book deleted successfully", true));
    }

    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<ApiResponse> hardDeleteBook(@PathVariable Long id) throws BookException {
        bookService.hardDeleteBook(id);
        return ResponseEntity.ok(new ApiResponse("Book permanent deleted", true));
    }

    @PostMapping("/search")
    public ResponseEntity<PageResponse<BookDTO>> advancedSearch(@RequestBody BookSearchRequest searchRequest){
        PageResponse<BookDTO> books = bookService.searchBookWithFilter(searchRequest);
        return ResponseEntity.ok(books);
    }

    @GetMapping("/stats")
    public ResponseEntity<BookStatsResponse> getBookStats(){
        long totalActive = bookService.getTotalActiveBooks();
        long totalAvailable = bookService.getTotalAvailableBooks();
        BookStatsResponse stats = new BookStatsResponse(totalActive, totalAvailable);
        return ResponseEntity.ok(stats);

    }

    public static class BookStatsResponse{
        public long totalActiveBooks;
        public long totalAvailableBooks;

        public BookStatsResponse(long totalAvailableBooks, long totalActiveBooks){
            this.totalAvailableBooks = totalAvailableBooks;
            this.totalActiveBooks = totalActiveBooks;
        }
    }

}
