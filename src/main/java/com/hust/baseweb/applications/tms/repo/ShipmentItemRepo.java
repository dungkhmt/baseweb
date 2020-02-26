package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.UUID;

public interface ShipmentItemRepo extends PagingAndSortingRepository<ShipmentItem, UUID> {
    //public ShipmentItem findByShipmentIdAndShipmentItemSeqId(UUID shipmentId, String shipmentItemSeqId);
	public ShipmentItem findByShipmentItemId(UUID shipmentItemId);
}
