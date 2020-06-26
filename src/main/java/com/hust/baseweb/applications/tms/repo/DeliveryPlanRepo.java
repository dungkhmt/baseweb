package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface DeliveryPlanRepo extends JpaRepository<DeliveryPlan, String> {

    @NotNull
    Page<DeliveryPlan> findAll(@NotNull Pageable pageable);
}
