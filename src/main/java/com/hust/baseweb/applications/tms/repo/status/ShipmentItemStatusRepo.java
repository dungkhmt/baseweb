package com.hust.baseweb.applications.tms.repo.status;

import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.applications.tms.entity.status.ShipmentItemStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

/**
 * @author Hien Hoang (hienhoang2702@gmail.com)
 */
public interface ShipmentItemStatusRepo extends JpaRepository<ShipmentItemStatus, UUID> {
    List<ShipmentItemStatus> findAllByShipmentItemIn(Collection<ShipmentItem> shipmentItems);

    List<ShipmentItemStatus> findAllByShipmentItemInAndThruDateNull(Collection<ShipmentItem> shipmentItems);

    List<ShipmentItemStatus> findAllByShipmentItemAndThruDateNull(ShipmentItem shipmentItem);
}
