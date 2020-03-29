package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.UUID;

public interface ShipmentItemRepo extends PagingAndSortingRepository<ShipmentItem, UUID> {
    //public ShipmentItem findByShipmentIdAndShipmentItemSeqId(UUID shipmentId, String shipmentItemSeqId);
    ShipmentItem findByShipmentItemId(UUID shipmentItemId);

    List<ShipmentItem> findAllByShipmentItemIdIn(List<UUID> shipmentItemIds);

    List<ShipmentItem> findAllByFacility(Facility facility);
}
