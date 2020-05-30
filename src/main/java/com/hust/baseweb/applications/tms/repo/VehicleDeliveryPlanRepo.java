package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.VehicleDeliveryPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface VehicleDeliveryPlanRepo extends PagingAndSortingRepository<VehicleDeliveryPlan, String> {

    List<VehicleDeliveryPlan> findAllByDeliveryPlanId(UUID deliveryPlanId);

    Page<VehicleDeliveryPlan> findAllByDeliveryPlanId(UUID deliveryPlanId, Pageable pageable);

    VehicleDeliveryPlan findByDeliveryPlanIdAndVehicleId(UUID deliveryPlanId, String vehicleId);

}
