package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface DeliveryPlanRepo extends PagingAndSortingRepository<DeliveryPlan, UUID> {

    DeliveryPlan findByDeliveryPlanId(UUID deliveryPlanId);

    @NotNull
    Page<DeliveryPlan> findAll(@NotNull Pageable pageable);
}
