package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryTripDetailRepo extends
        JpaRepository<DeliveryTripDetail, UUID> {
    Page<DeliveryTripDetail> findAllByDeliveryTrip(DeliveryTrip deliveryTrip, Pageable pageable);

    List<DeliveryTripDetail> findAllByDeliveryTrip(DeliveryTrip deliveryTrip);

    List<DeliveryTripDetail> findAllByDeliveryTripIn(List<DeliveryTrip> deliveryTrips);

    DeliveryTripDetail findByDeliveryTripDetailId(UUID deliveryTripDetailId);

    List<DeliveryTripDetail> findAllByShipmentItem(ShipmentItem shipmentItem);
}
