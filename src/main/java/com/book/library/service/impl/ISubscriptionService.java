package com.book.library.service.impl;

import com.book.library.exception.SubscriptionException;
import com.book.library.payload.dto.SubscriptionDTO;
import com.book.library.payload.response.PaymentInitiatedResponse;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ISubscriptionService {
    PaymentInitiatedResponse subscribe(SubscriptionDTO subscriptionDTO) throws Exception;
    SubscriptionDTO getUserActiveSubscription(Pageable pageable) throws Exception;
    SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException;
    SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException;
    List<SubscriptionDTO> getAllSubscription(Pageable pageable);
    void deactivateExpiredSubscription();



}
