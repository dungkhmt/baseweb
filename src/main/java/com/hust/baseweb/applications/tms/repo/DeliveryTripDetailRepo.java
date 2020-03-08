package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DeliveryTripDetail;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryTripDetailRepo extends
        PagingAndSortingRepository<DeliveryTripDetail, UUID> {
    Page<DeliveryTripDetail> findAllByDeliveryTripId(UUID deliveryTripId, Pageable pageable);

    List<DeliveryTripDetail> findAllByDeliveryTripId(UUID deliveryTripId);

    List<DeliveryTripDetail> findAllByDeliveryTripIdIn(List<UUID> deliveryTripIds);
    
    DeliveryTripDetail findByDeliveryTripDetailId(UUID deliveryTripDetailId);
}
