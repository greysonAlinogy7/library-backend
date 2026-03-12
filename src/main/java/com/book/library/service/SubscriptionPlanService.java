package com.book.library.service;

import com.book.library.entity.SubscriptionPlan;
import com.book.library.entity.User;
import com.book.library.mappers.SubscriptionPlanMapper;
import com.book.library.payload.dto.SubscriptionPlanDTO;
import com.book.library.repository.SubscriptionPlanRepository;
import com.book.library.service.impl.ISubscriptionPlanService;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class SubscriptionPlanService implements ISubscriptionPlanService {
    private  final SubscriptionPlanRepository planRepository;
    private final SubscriptionPlanMapper planMapper;
    private  final UserService userService;

    @Override
    public SubscriptionPlanDTO createSubscription(SubscriptionPlanDTO planDTO) throws Exception {
        if (planRepository.existsByPlanCode(planDTO.getPlanCode())){
            throw  new Exception("plan code is already exist");
        }
        SubscriptionPlan plan = planMapper.toEntity(planDTO);
        User currentUser = userService.getCurrentUser();
        plan.setUpdatedBy(currentUser.getFullName());
        plan.setCreatedBy(currentUser.getFullName());
       SubscriptionPlan savedPlan =  planRepository.save(plan);
       return planMapper.toDTO(savedPlan);
    }

    @Override
    public SubscriptionPlanDTO updateSubscription(Long planId, SubscriptionPlanDTO dto) throws Exception {
        SubscriptionPlan existingPlan = planRepository.findById(planId).orElseThrow(() -> new Exception("plan not found"));
        planMapper.updateEntity(existingPlan, dto);
        SubscriptionPlan updatedPlan = planRepository.save(existingPlan);
        return planMapper.toDTO(updatedPlan);
    }

    @Override
    public void deleteSubscriptionPlan(Long planId) throws Exception {
        SubscriptionPlan existingPlan = planRepository.findById(planId).orElseThrow(() -> new Exception("plan not found"));
        planRepository.delete(existingPlan);



    }

    @Override
    public List<SubscriptionPlanDTO> getAllSubscriptionPlan() {
        List<SubscriptionPlan> planList = planRepository.findAll();
        return planList.stream().map(planMapper::toDTO).collect(Collectors.toList());
    }

    @Override
    public SubscriptionPlan getBySubscriptionPlanCode(String subscriptionPlanCode) throws Exception {
        SubscriptionPlan plan = planRepository.findByPlanCode(subscriptionPlanCode);
        if (plan == null){
            throw new Exception("plan not found");
        }
        return plan;
    }
}
