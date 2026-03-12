package com.book.library.service.impl;

import com.book.library.payload.dto.PaymentDTO;
import com.book.library.payload.request.PaymentInitiatedRequest;
import com.book.library.payload.request.PaymentVerifyRequest;
import com.book.library.payload.response.PaymentInitiatedResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface IPaymentService {
    PaymentInitiatedResponse initialPayment(PaymentInitiatedRequest paymentRequest) throws Exception;
    PaymentDTO verifyPayment(PaymentVerifyRequest request) throws Exception;
    Page<PaymentDTO> getAllPayments(Pageable pageable);

}
