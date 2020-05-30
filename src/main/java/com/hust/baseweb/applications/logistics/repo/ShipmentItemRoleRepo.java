package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.ShipmentItemRole;
import com.hust.baseweb.applications.tms.entity.ShipmentItem;
import com.hust.baseweb.entity.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Collection;
import java.util.List;
import java.util.UUID;

public interface ShipmentItemRoleRepo extends JpaRepository<ShipmentItemRole, UUID> {

    List<ShipmentItemRole> findByPartyAndThruDateNull(Party party);

    Page<ShipmentItemRole> findByPartyAndThruDateNull(Party party, Pageable pageable);

    void deleteAllByShipmentItemIn(Collection<ShipmentItem> shipmentItems);

    @Query(value = "select r.*\n" +
                   "from shipment_item_role as r,\n" +
                   "     shipment_item as s\n" +
                   "where r.shipment_item_id = s.shipment_item_id\n" +
                   "  and r.thru_date is null\n" +
                   "  and r.party_id = ?1\n" +
                   "  and s.status_id = ?2\n" +
                   "  and s.shipment_item_id not in\n" +
                   "      (select d.shipment_item_id\n" +
                   "       from shipment_item_delivery_plan as d\n" +
                   "       where d.delivery_plan_id = ?3);\n",
           nativeQuery = true)
    List<ShipmentItemRole> findAllByPartyAndStatusItemAndDeliveryPlanIdNotEqual(
        Party party,
        String statusItemId,
        UUID deliveryPlanId
    );
}
