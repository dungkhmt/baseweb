package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.entity.StatusItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface DeliveryTripDetailRepo extends
    JpaRepository<DeliveryTripDetail, UUID> {

    Page<DeliveryTripDetail> findAllByDeliveryTrip(DeliveryTrip deliveryTrip, Pageable pageable);

    List<DeliveryTripDetail> findAllByDeliveryTrip(DeliveryTrip deliveryTrip);

    List<DeliveryTripDetail> findAllByDeliveryTripIn(List<DeliveryTrip> deliveryTrips);

    List<DeliveryTripDetail> findAllByDeliveryTripInAndStatusItem(
        List<DeliveryTrip> deliveryTrips,
        StatusItem statusItem
    );

    DeliveryTripDetail findByDeliveryTripDetailId(UUID deliveryTripDetailId);

    List<DeliveryTripDetail> findAllByDeliveryTripDetailIdIn(List<UUID> deliveryTripDetailIds);

    List<DeliveryTripDetail> findAllByShipmentItem(ShipmentItem shipmentItem);

    List<DeliveryTripDetail> findAllByShipmentItemIn(Collection<ShipmentItem> shipmentItems);
}
