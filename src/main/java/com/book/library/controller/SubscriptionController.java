package com.book.library.controller;

import com.book.library.exception.SubscriptionException;
import com.book.library.payload.dto.SubscriptionDTO;
import com.book.library.payload.response.ApiResponse;
import com.book.library.payload.response.PaymentInitiatedResponse;
import com.book.library.service.SubscriptionService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/subscriptions")
public class SubscriptionController {
    private  final SubscriptionService subscriptionService;

    @PostMapping("/subscrible")
    public ResponseEntity<?> subscrible( @RequestBody SubscriptionDTO subscription) throws Exception {
        PaymentInitiatedResponse dto = subscriptionService.subscribe(subscription);
        return ResponseEntity.ok(dto);

    }

    @GetMapping("/user/active")
    public ResponseEntity<?> getUserActiveSubscriptions(Pageable pageable) throws Exception {
        int page = 0;
        int size = 10;
        Pageable pageable1 = PageRequest.of(page, size);
        SubscriptionDTO dto= subscriptionService.getUserActiveSubscription(pageable1);
        return ResponseEntity.ok(dto);
    }

    @GetMapping("/admin")
    public ResponseEntity<?> getAllSubscriptions(){
        int page = 0;
        int size = 10;
        Pageable pageable = PageRequest.of(page,size);
        List<SubscriptionDTO> dtoList = subscriptionService.getAllSubscription(pageable);
        return ResponseEntity.ok(dtoList);
    }

    @GetMapping("/admin/deactivate-expired")
    public ResponseEntity<?> deactivateExpiredSubscriptions(){
        subscriptionService.deactivateExpiredSubscription();
        ApiResponse response = new ApiResponse("task done", true);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/cancel/{subscriptionId}")
    public ResponseEntity<?> cancelSubscription(@PathVariable Long subscriptionId, @RequestParam(required = false) String reason) throws SubscriptionException {
        SubscriptionDTO subscription = subscriptionService.cancelSubscription(subscriptionId, reason);
        return ResponseEntity.ok(subscription);
    }

    @PostMapping("/activate")
    public ResponseEntity<?> activateSubscription(@RequestParam Long subscriptionId, @RequestParam Long paymentId) throws SubscriptionException {
        SubscriptionDTO subscription = subscriptionService.activateSubscription(subscriptionId, paymentId);
        return ResponseEntity.ok(subscription);
    }

}
