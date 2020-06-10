package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.CompositeVehicleDeliveryPlanId;
import com.hust.baseweb.applications.tms.entity.VehicleDeliveryPlan;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface VehicleDeliveryPlanRepo
    extends PagingAndSortingRepository<VehicleDeliveryPlan, CompositeVehicleDeliveryPlanId> {

    List<VehicleDeliveryPlan> findAllByDeliveryPlanId(String deliveryPlanId);

    Page<VehicleDeliveryPlan> findAllByDeliveryPlanId(String deliveryPlanId, Pageable pageable);

    VehicleDeliveryPlan findByDeliveryPlanIdAndVehicleId(String deliveryPlanId, String vehicleId);

}
