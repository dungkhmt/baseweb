package com.hust.baseweb.applications.tms.repo.status;

import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import com.hust.baseweb.applications.tms.entity.status.DeliveryTripDetailStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface DeliveryTripDetailStatusRepo extends JpaRepository<DeliveryTripDetailStatus, UUID> {
    List<DeliveryTripDetailStatus> findAllByDeliveryTripDetailInAndThruDateNull(List<DeliveryTripDetail> deliveryTripDetails);

    List<DeliveryTripDetailStatus> findAllByDeliveryTripDetailAndThruDateNull(DeliveryTripDetail deliveryTripDetail);

    List<DeliveryTripDetailStatus> findAllByDeliveryTripDetail(DeliveryTripDetail deliveryTripDetail);
}
