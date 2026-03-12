package com.book.library.repository;

import com.book.library.entity.SubscriptionPlan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SubscriptionPlanRepository extends JpaRepository<SubscriptionPlan, Long> {
    Boolean existsByPlanCode(String planCode);
    SubscriptionPlan findByPlanCode(String planCode);
}
