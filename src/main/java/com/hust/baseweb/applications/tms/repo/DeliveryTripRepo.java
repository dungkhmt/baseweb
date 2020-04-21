package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.PartyDriver;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DeliveryTripRepo extends JpaRepository<DeliveryTrip, UUID> {
    Page<DeliveryTrip> findAllByDeliveryPlan(DeliveryPlan deliveryPlan, Pageable pageable);

    List<DeliveryTrip> findAllByDeliveryPlan(DeliveryPlan deliveryPlan);

    List<DeliveryTrip> findByPartyDriver(PartyDriver partyDriver);
    
    List<DeliveryTrip> findAll();
}
