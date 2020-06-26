package com.hust.baseweb.applications.tms.service;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.model.DeliveryTripDetailModel;
import com.hust.baseweb.applications.tms.model.DeliveryTripModel;
import com.hust.baseweb.applications.tms.model.deliverytrip.GetDeliveryTripAssignedToDriverOutputModel;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public interface DeliveryTripService {

    DeliveryTrip save(
        DeliveryTripModel.Create input,
        double totalDistance,
        double totalWeight, // kg
        double totalPallet,
        double totalExecutionTime,
        int totalLocation,
        int completedDeliveryTripDetailCount,
        int deliveryTripDetailCount
    );

    DeliveryTrip save(DeliveryTrip deliveryTrip);

    Page<DeliveryTripModel> findAllByDeliveryPlanId(String deliveryPlanId, Pageable pageable);

    List<DeliveryTripModel> findAllByDeliveryPlanId(String deliveryPlanId);

    DeliveryTripModel findById(String deliveryTripId);

    DeliveryTripModel.Tour getDeliveryTripInfo(
        String deliveryTripId,
        List<DeliveryTripDetailModel.Create> shipmentItemModels,
        UserLogin userLogin
    );

    GetDeliveryTripAssignedToDriverOutputModel getDeliveryTripAssignedToDriver(String driverUserLoginId);

    boolean approveDeliveryTrip(String deliveryTripId);

    boolean startExecuteDeliveryTrip(String deliveryTripId);

    boolean deleteAll();

    List<DeliveryTripModel> findAllByVehicleId(String vehicleId);

    List<DeliveryTripModel> findAllByDriverId(UUID driverId);
}
