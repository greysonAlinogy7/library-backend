package com.book.library.service;

import com.book.library.domain.PaymentGateway;
import com.book.library.domain.PaymentStatus;
import com.book.library.entity.Payment;
import com.book.library.entity.Subscription;
import com.book.library.entity.User;
import com.book.library.event.publisher.PaymentEventPublisher;
import com.book.library.gateway.NmbPaymentService;
import com.book.library.mappers.PaymentMapper;
import com.book.library.payload.dto.PaymentDTO;
import com.book.library.payload.request.PaymentInitiatedRequest;
import com.book.library.payload.request.PaymentVerifyRequest;
import com.book.library.payload.response.PaymentInitiatedResponse;
import com.book.library.payload.response.PaymentLinkResponse;
import com.book.library.repository.PaymentRepository;
import com.book.library.repository.SubscriptionRepository;
import com.book.library.repository.UserRepository;
import com.book.library.service.impl.IPaymentService;
import lombok.RequiredArgsConstructor;
import org.json.JSONObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class PaymentService implements IPaymentService {
    private  final PaymentEventPublisher paymentEventPublisher;

    private  final PaymentRepository paymentRepository;
    private  final UserRepository userRepository;
    private  final SubscriptionRepository subscriptionRepository;
    private  final NmbPaymentService nmbService;
    private  final PaymentMapper paymentMapper;

    @Override
    public PaymentInitiatedResponse initialPayment(PaymentInitiatedRequest paymentRequest) throws Exception {
        User user = userRepository.findById(paymentRequest.getUserId())
                .orElseThrow(() -> new Exception("user not found"));

        Payment payment = new Payment();
        payment.setUser(user);
        payment.setPaymentType(paymentRequest.getPaymentType());
        payment.setGateway(paymentRequest.getGateway());
       payment.setPaymentMethod(paymentRequest.getPaymentMethod());
        payment.setAmount(paymentRequest.getAmount());
        payment.setDescription(paymentRequest.getDescription());
        payment.setPaymentStatus(PaymentStatus.PENDING);
        payment.setTransactionId("TXN" + UUID.randomUUID());
        payment.setInitiatedAt(LocalDateTime.now());

        if (paymentRequest.getSubscriptionId() != null) {
            Subscription sub = subscriptionRepository.findById(paymentRequest.getSubscriptionId())
                    .orElseThrow(() -> new Exception("Subscription not found"));
            payment.setSubscription(sub);
        }

        payment = paymentRepository.save(payment);

        PaymentInitiatedResponse response ;

        if (paymentRequest.getGateway() == PaymentGateway.MIX_BY_YAS) {
            // **Use mock payment link**
            PaymentLinkResponse paymentLinkResponse = nmbService.createPaymentLink(user, payment);

            response = PaymentInitiatedResponse.builder()
                    .paymentId(payment.getId())
                    .gateway(payment.getGateway())
                    .checkoutUrl(paymentLinkResponse.getPayment_link_url())
                    .transactionId(paymentLinkResponse.getPayment_link_id())
                    .amount(payment.getAmount())
                    .description(payment.getDescription())
                    .success(true)
                    .message("payment initiated successfully (mocked)")
                    .build();

            payment.setGatewayOrderId(paymentLinkResponse.getPayment_link_id());
            payment.setPaymentStatus(PaymentStatus.PROCESSING);
            paymentRepository.save(payment);
        } else {
            response = PaymentInitiatedResponse.builder()
                    .success(false)
                    .message("Unsupported payment gateway")
                    .build();
        }

        return response;
    }

    @Override
    public PaymentDTO verifyPayment(PaymentVerifyRequest request) throws Exception {
        // Always valid for mock
        Payment payment = paymentRepository.findById(1L).orElseThrow(() -> new Exception("Payment not found"));
        payment.setPaymentStatus(PaymentStatus.SUCCESS);
        payment.setCompletedAt(LocalDateTime.now());
        payment = paymentRepository.save(payment);
        paymentEventPublisher.publishPaymentSuccessEvent(payment);
        return paymentMapper.toDTO(payment);
    }

    @Override
    public Page<PaymentDTO> getAllPayments(Pageable pageable) {
        return paymentRepository.findAll(pageable).map(paymentMapper::toDTO);
    }
}
