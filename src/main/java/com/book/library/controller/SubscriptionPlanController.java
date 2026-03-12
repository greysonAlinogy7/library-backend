package com.book.library.controller;


import com.book.library.payload.dto.SubscriptionPlanDTO;
import com.book.library.payload.response.ApiResponse;
import com.book.library.service.SubscriptionPlanService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/subscription-plan")
@RequiredArgsConstructor
public class SubscriptionPlanController {
    private  final SubscriptionPlanService subscriptionPlanService;

    @GetMapping
    public ResponseEntity<?> getAllSubscriptionPlans(){
        List<SubscriptionPlanDTO> plans = subscriptionPlanService.getAllSubscriptionPlan();
        return ResponseEntity.ok(plans);
    }

    @PostMapping("/admin/create")
    public ResponseEntity<?> createSubscriptionPlan(@Valid  @RequestBody SubscriptionPlanDTO subscriptionPlanDTO) throws Exception {
        SubscriptionPlanDTO plans = subscriptionPlanService.createSubscription(subscriptionPlanDTO);
        return ResponseEntity.ok(plans);
    }

    @PutMapping("/admin/{id}")
    public ResponseEntity<?> updateSubscriptionPlan(@Valid  @RequestBody SubscriptionPlanDTO subscriptionPlanDTO, @PathVariable Long id) throws Exception {
        SubscriptionPlanDTO plans = subscriptionPlanService.updateSubscription(id, subscriptionPlanDTO);
        return ResponseEntity.ok(plans);
    }

    @DeleteMapping("/admin/{id}")
    public ResponseEntity<?> deleteSubscriptionPlan( @PathVariable Long id) throws Exception {
         subscriptionPlanService.deleteSubscriptionPlan(id);
        ApiResponse response = new ApiResponse("plan deleted successfully",true);
        return ResponseEntity.ok(response);
    }

}
