package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel;
import com.hust.baseweb.applications.tms.model.DeliveryTripModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.GetDeliveryTripAssignedToDriverOutputModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DeliveryTripService {
    DeliveryTrip save(com.hust.baseweb.applications.tms.model.DeliveryTripModel.Create input,
                      double totalDistance,
                      double totalWeight,
                      double totalPallet);

    Page<DeliveryTripModel> findAllByDeliveryPlanId(String deliveryPlanId, Pageable pageable);

    List<DeliveryTripModel> findAllByDeliveryPlanId(String deliveryPlanId);

    DeliveryTrip findById(UUID deliveryTripId);

    DeliveryTripModel.Tour getDeliveryTripInfo(String deliveryTripId,
                                               List<DeliveryTripDetailModel.Create> shipmentItemModels);

    GetDeliveryTripAssignedToDriverOutputModel getDeliveryTripAssignedToDriver(String driverUserLoginId);

    
}
