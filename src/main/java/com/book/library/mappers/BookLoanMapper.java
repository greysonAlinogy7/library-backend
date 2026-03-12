package com.book.library.mappers;

import com.book.library.entity.Book;
import com.book.library.entity.BookLoan;
import com.book.library.entity.User;
import com.book.library.payload.dto.BookLoanDTO;
import org.springframework.stereotype.Component;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;

@Component
public class BookLoanMapper {
    public BookLoanDTO toDTO(BookLoan bookLoan){
        if (bookLoan == null){
            return null;
        }
        BookLoanDTO dto = new BookLoanDTO();
        dto.setId(bookLoan.getId());

        if (bookLoan.getUser() != null){
            dto.setUserId(bookLoan.getUser().getId());
            dto.setUserName(bookLoan.getUser().getFullName());
            dto.setUserEmail(bookLoan.getUser().getEmail());

        }

        if (bookLoan.getBook() != null){
            dto.setBookId(bookLoan.getBook().getId());
            dto.setBookTitle(bookLoan.getBook().getTitle());
            dto.setBookIsbn(bookLoan.getBook().getIsbn());
            dto.setBookAuthor(bookLoan.getBook().getAuthor());
            dto.setBookCoverImage(bookLoan.getBook().getCoverImageUrl());
        }

        dto.setType(bookLoan.getType());
        dto.setStatus(bookLoan.getStatus());
        dto.setDueDate(bookLoan.getDueDate());
        dto.setCheckoutDate(bookLoan.getCheckoutDate());
        dto.setRemainingDays(ChronoUnit.DAYS.between(LocalDate.now(), bookLoan.getDueDate()));
        dto.setReturnDate(bookLoan.getReturnDate());
        dto.setRenewalCount(bookLoan.getRenewalCount());
        dto.setMaxRenewals(bookLoan.getMaxRenewals());
        dto.setNotes(bookLoan.getNotes());
        dto.setIsOverdue(bookLoan.getIsOverdue());
        dto.setOverdueDays(bookLoan.getOverdueDays());
        dto.setCreatedAt(bookLoan.getCreatedAt());
        dto.setUpdatedAt(bookLoan.getUpdatedAt());
        return dto;

    }

    public BookLoan  toEntity(BookLoanDTO bookLoanDTO, User user, Book book){
        if (bookLoanDTO == null){
            return null;
        }
        BookLoan bookLoan = new BookLoan();
        bookLoan.setId(bookLoanDTO.getId());
        bookLoan.setUser(user);
        bookLoan.setBook(book);
        bookLoan.setType(bookLoanDTO.getType());
        bookLoan.setStatus(bookLoanDTO.getStatus());
        bookLoan.setCheckoutDate(bookLoanDTO.getCheckoutDate());
        bookLoan.setDueDate(bookLoanDTO.getDueDate());
        bookLoan.setReturnDate(bookLoanDTO.getReturnDate());
        bookLoan.setRenewalCount(bookLoanDTO.getRenewalCount());
        bookLoan.setMaxRenewals(bookLoanDTO.getMaxRenewals());
        bookLoan.setNotes(bookLoanDTO.getNotes());
        bookLoan.setIsOverdue(bookLoanDTO.getIsOverdue());
        bookLoan.setOverdueDays(bookLoanDTO.getOverdueDays());
        bookLoan.setCreatedAt(bookLoanDTO.getCreatedAt());
        bookLoan.setUpdatedAt(bookLoanDTO.getUpdatedAt());
        return bookLoan;
    }

}
