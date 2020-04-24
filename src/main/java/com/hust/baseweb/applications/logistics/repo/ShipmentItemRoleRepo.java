package com.hust.baseweb.applications.logistics.repo;

import com.hust.baseweb.applications.logistics.entity.ShipmentItemRole;
import com.hust.baseweb.entity.Party;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface ShipmentItemRoleRepo extends JpaRepository<ShipmentItemRole, UUID> {
    List<ShipmentItemRole> findByPartyAndThruDateNull(Party party);

    Page<ShipmentItemRole> findByPartyAndThruDateNull(Party party, Pageable pageable);
}
