package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.ShipmentItemRole;
import com.hust.baseweb.entity.Party;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;
import java.util.UUID;

public interface ShipmentItemRoleRepo extends JpaRepository<ShipmentItemRole, UUID> {
    public ShipmentItemRole save(ShipmentItemRole shipmentItemRole);
    public List<ShipmentItemRole> findByPartyAndThruDate(Party party, Date thruDate);
}
