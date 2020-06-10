package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.logistics.entity.Facility;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ShipmentItemRepo extends JpaRepository<ShipmentItem, UUID> {

    //public ShipmentItem findByShipmentIdAndShipmentItemSeqId(UUID shipmentId, String shipmentItemSeqId);
    ShipmentItem findByShipmentItemId(UUID shipmentItemId);

//    List<ShipmentItem> findAllByShipmentItemIdIn(List<UUID> shipmentItemIds);

    List<ShipmentItem> findAllByShipmentItemIdInAndUserLogin(Collection<UUID> shipmentItemIds, UserLogin userLogin);

    List<ShipmentItem> findAllByFacilityAndUserLogin(Facility facility, UserLogin userLogin);

//    List<ShipmentItem> findAllByOrderItemIn(Collection<OrderItem> orderItems);

    List<ShipmentItem> findAllByOrderIdIn(Collection<String> orderIds);

    List<ShipmentItem> findAllByOrderIdAndUserLogin(String orderId, UserLogin userLogin);

    List<ShipmentItem> findAllByUserLogin(UserLogin userLogin);

    Page<ShipmentItem> findAllByUserLogin(UserLogin userLogin, Pageable pageable);


}
