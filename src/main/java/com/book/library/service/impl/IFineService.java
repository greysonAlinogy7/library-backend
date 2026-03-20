package com.book.library.service.impl;

import com.book.library.domain.FineStatus;
import com.book.library.domain.FineType;
import com.book.library.payload.dto.FineDTO;
import com.book.library.payload.request.CreateFineRequest;
import com.book.library.payload.request.WaiveFineRequest;
import com.book.library.payload.response.PageResponse;
import com.book.library.payload.response.PaymentInitiatedResponse;

import java.util.List;

public interface IFineService {
    FineDTO createFine(CreateFineRequest fineRequest);
    PaymentInitiatedResponse payFine(Long fineId, String transactionId) throws Exception;
    void markFineAsPaid(Long fineId, Long amount, String transactionId) throws Exception;
    FineDTO waiveFine(WaiveFineRequest waiveFineRequest) throws Exception;
    List<FineDTO> getMyFines(FineStatus status, FineType type) throws Exception;
    PageResponse<FineDTO> getAllFines(
            FineStatus status,
            FineType type,
            Long userId,
            int page,
            int size
    );

}
