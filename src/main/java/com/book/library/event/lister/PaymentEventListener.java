package com.book.library.event.lister;


import com.book.library.entity.Payment;
import com.book.library.exception.SubscriptionException;
import com.book.library.service.SubscriptionService;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentEventListener {
    private  final SubscriptionService subscriptionService;

    @Async
    @EventListener
    @Transactional
    public  void handlePaymentSuccess(Payment payment) throws SubscriptionException {
        switch (payment.getPaymentType()){
            case FINE:
            case LOST_BOOK_PENALTY:
            case DAMAGED_BOOK_PENALTY:
                break;
            case MEMBERSHIP:
                subscriptionService.activateSubscription(payment.getSubscription().getId(), payment.getId());
        }
    }
}
