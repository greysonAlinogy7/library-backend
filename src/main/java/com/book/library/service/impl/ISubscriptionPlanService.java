package com.book.library.service.impl;


import com.book.library.entity.SubscriptionPlan;
import com.book.library.payload.dto.SubscriptionPlanDTO;

import java.util.List;

public interface ISubscriptionPlanService {
    SubscriptionPlanDTO createSubscription(SubscriptionPlanDTO planDTO) throws Exception;
    SubscriptionPlanDTO updateSubscription(Long planId, SubscriptionPlanDTO dto) throws Exception;
    void deleteSubscriptionPlan(Long planId) throws Exception;
    List<SubscriptionPlanDTO> getAllSubscriptionPlan();
    SubscriptionPlan getBySubscriptionPlanCode(String subscriptionPlanCode) throws Exception;

}
