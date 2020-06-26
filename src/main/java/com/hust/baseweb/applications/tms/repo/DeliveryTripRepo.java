package com.hust.baseweb.applications.tms.repo;

import com.hust.baseweb.applications.tms.entity.DeliveryPlan;
import com.hust.baseweb.applications.tms.entity.DeliveryTrip;
import com.hust.baseweb.applications.tms.entity.PartyDriver;
import com.hust.baseweb.applications.tms.entity.Vehicle;
import com.hust.baseweb.entity.StatusItem;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DeliveryTripRepo extends JpaRepository<DeliveryTrip, String> {

    Page<DeliveryTrip> findAllByDeliveryPlan(DeliveryPlan deliveryPlan, Pageable pageable);

    List<DeliveryTrip> findAllByDeliveryPlan(DeliveryPlan deliveryPlan);

    List<DeliveryTrip> findByPartyDriverAndStatusItem(PartyDriver partyDriver, StatusItem statusItem);

    List<DeliveryTrip> findAllByVehicle(Vehicle vehicle);

    List<DeliveryTrip> findAllByPartyDriver(PartyDriver partyDriver);

}
