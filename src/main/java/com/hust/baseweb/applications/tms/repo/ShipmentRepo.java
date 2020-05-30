package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.UUID;

public interface ShipmentRepo extends JpaRepository<Shipment, UUID> {

    void deleteAllByShipmentIdIn(Collection<UUID> shipmentIds);

}
