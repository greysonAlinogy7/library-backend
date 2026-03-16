package com.book.library.service;

import com.book.library.domain.PaymentGateway;
import com.book.library.domain.PaymentStatus;
import com.book.library.domain.PaymentType;
import com.book.library.entity.Subscription;
import com.book.library.entity.SubscriptionPlan;
import com.book.library.entity.User;
import com.book.library.exception.SubscriptionException;
import com.book.library.mappers.SubscriptionMapper;
import com.book.library.payload.dto.SubscriptionDTO;
import com.book.library.payload.request.PaymentInitiatedRequest;
import com.book.library.payload.response.PaymentInitiatedResponse;
import com.book.library.repository.SubscriptionPlanRepository;
import com.book.library.repository.SubscriptionRepository;
import com.book.library.service.impl.ISubscriptionService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class SubscriptionService implements ISubscriptionService {
    private  final SubscriptionRepository subscriptionRepository;
    private final SubscriptionMapper subscriptionMapper;
    private final UserService userService;
    private  final SubscriptionPlanRepository subscriptionPlanRepository;
    private  final PaymentService paymentService;

    @Override
    public PaymentInitiatedResponse subscribe(SubscriptionDTO subscriptionDTO) throws Exception {

        User user = userService.getCurrentUser();
        SubscriptionPlan plan  = subscriptionPlanRepository.findById(subscriptionDTO.getPlanId()).orElseThrow(() -> new Exception("plan not found"));
        Subscription subscription = subscriptionMapper.toEntity(subscriptionDTO, plan, user);
        subscription.initializeFromPlan();
        subscription.setIsActive(false);
        Subscription savedSubscription =subscriptionRepository.save(subscription);

        PaymentInitiatedRequest paymentInitiatedRequest = PaymentInitiatedRequest.builder()
                .userId(user.getId())
                .subscriptionId(subscription.getId())
                .status(PaymentStatus.PENDING)
                .paymentType(PaymentType.MEMBERSHIP)
                .gateway(PaymentGateway.MIX_BY_YAS)
                .amount(subscription.getPrice())
                .description("library Subscription -" + plan.getName())
                .build();

         return paymentService.initialPayment(paymentInitiatedRequest);
    }

    @Override
    public SubscriptionDTO getUserActiveSubscription(Pageable pageable) throws Exception {

        User user = userService.getCurrentUser();

        Subscription subscription = subscriptionRepository
                .findActiveSubscriptionByUserId(user.getId(), LocalDate.now())
                .orElseThrow(() -> new SubscriptionException("No active subscription"));

        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO cancelSubscription(Long subscriptionId, String reason) throws SubscriptionException {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new SubscriptionException("Subscription not found with ID" + subscriptionId));
        if (!subscription.getIsActive()){
            throw new SubscriptionException("subscription is already inActive");
        }
        subscription.setIsActive(false);
        subscription.setCancelledAt(LocalDateTime.now());
        subscription.setCancellationReason(reason != null ? reason : "Cancelled by user");
        log.info("Subscription cancelled successfully: {}", subscriptionId);
        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public SubscriptionDTO activateSubscription(Long subscriptionId, Long paymentId) throws SubscriptionException {
        Subscription subscription = subscriptionRepository.findById(subscriptionId).orElseThrow(() -> new SubscriptionException("Subscription not found by id!"));
        //verify payments
        subscription.setIsActive(true);
        subscription = subscriptionRepository.save(subscription);

        return subscriptionMapper.toDTO(subscription);
    }

    @Override
    public List<SubscriptionDTO> getAllSubscription(Pageable pageable) {
        List<Subscription> subscriptions = subscriptionRepository.findAll();
        return subscriptionMapper.toDTOList(subscriptions);
    }

    @Override
    public void deactivateExpiredSubscription() {
        List<Subscription> expiredSubscription = subscriptionRepository.findExpiredActiveSubscription(LocalDate.now());
        for (Subscription subscription : expiredSubscription){
            subscription.setIsActive(false);
            subscriptionRepository.save(subscription);
        }
    }


}
