package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripDetailInputModel;
import com.hust.baseweb.applications.tms.model.createdeliverytrip.CreateDeliveryTripInputModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.DeliveryTripInfoModel;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DeliveryTripService {
    public DeliveryTrip save(CreateDeliveryTripInputModel input);

    Page<DeliveryTrip> findAllByDeliveryPlanId(String deliveryPlanId, Pageable pageable);

    List<DeliveryTrip> findAllByDeliveryPlanId(String deliveryPlanId);

    DeliveryTrip findById(UUID deliveryTripId);

    DeliveryTripInfoModel getDeliveryTripInfo(String deliveryTripId, List<CreateDeliveryTripDetailInputModel> shipmentItemModels);
}
