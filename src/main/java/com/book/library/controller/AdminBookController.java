package com.book.library.controller;

import com.book.library.exception.BookException;
import com.book.library.payload.dto.BookDTO;
import com.book.library.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/admin/book")
@RequiredArgsConstructor
public class AdminBookController {
    private  final BookService bookService;

    @PostMapping()
    public ResponseEntity<BookDTO> createBook(@Valid @RequestBody BookDTO bookDTO) throws BookException {
        BookDTO createBook = bookService.createBook(bookDTO);
        return  ResponseEntity.ok(createBook);
    }
}
