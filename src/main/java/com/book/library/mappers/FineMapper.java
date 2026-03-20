package com.book.library.mappers;

import com.book.library.entity.Fine;
import com.book.library.payload.dto.FineDTO;
import org.springframework.stereotype.Component;

@Component
public class FineMapper {
    public FineDTO toDTO(Fine fine){
        if (fine == null){
            return null;
        }
        FineDTO dto = new FineDTO();
        if (fine.getBookLoan() != null){
            dto.setBookLoanId(fine.getBookLoan().getId());
            if (fine.getBookLoan().getBook() != null){
                dto.setBookTitle(fine.getBookLoan().getBook().getTitle());
                dto.setBookIsbn(fine.getBookLoan().getBook().getIsbn());
            }
        }

        if (fine.getUser() != null){
            dto.setUserId(fine.getUser().getId());
            dto.setUserName(fine.getUser().getFullName());
            dto.setUserEmail(fine.getUser().getEmail());
        }

        dto.setType(fine.getType());
        dto.setAmount(fine.getAmount());
        dto.setAmountPaid(dto.getAmountPaid());
        dto.setStatus(fine.getStatus());
        dto.setReason(fine.getReason());
        dto.setNote(fine.getNote());

        if (fine.getWaiveBy() != null){
            dto.setWaiveByUserId(fine.getWaiveBy().getId());
            dto.setWaivedByUserName(fine.getWaiveBy().getFullName());

        }
        dto.setWaivedAt(fine.getWaivedAt());
        dto.setWaiveReason(fine.getWaiveReason());

        dto.setPaidAt(fine.getPaidAt());

        if (fine.getProccessedBy() != null){
            dto.setProcessedByUserId(fine.getProccessedBy().getId());
            dto.setProcessedByUserName(fine.getProccessedBy().getFullName());
        }
        dto.setTransactionId(fine.getTransactionId());
        dto.setCreatedAt(fine.getCreatedAt());
        dto.setUpdateAt(fine.getUpdateAt());
        return dto;
    }
}
