package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.tms.model.DeliveryTripModel;
import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.entity.UserLogin;
import com.hust.baseweb.utils.Constant;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.Optional;
import java.util.UUID;

@Entity
@Getter
@Setter
public class DeliveryTrip {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "delivery_trip_id")
    private UUID deliveryTripId;

    @JoinColumn(name = "delivery_plan_id", referencedColumnName = "delivery_plan_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private DeliveryPlan deliveryPlan;

    @Column(name = "delivery_plan_solution_seq_id")
    private String deliveryPlanSolutionSeqId;

    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Vehicle vehicle;

    @JoinColumn(name = "driver_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartyDriver partyDriver;

    @Column(name = "execute_date")
    private Date executeDate;

    private Double distance;    // meter

    private Double totalWeight; // kg

    private Double totalPallet;

    private Double totalExecutionTime; // second

    private Integer totalLocation;

    @JoinColumn(name = "execute_external_vehicle_type_id", referencedColumnName = "vehicle_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private VehicleType externalVehicleType;

    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private StatusItem statusItem;

    private Integer completedDeliveryTripDetailCount = 0;
    private Integer deliveryTripDetailCount = 0;

    public DeliveryTripModel toDeliveryTripModel() {

        return new DeliveryTripModel(
            deliveryPlan.getDeliveryPlanId().toString(),
            deliveryPlanSolutionSeqId,
            deliveryTripId.toString(),
            Constant.DATE_FORMAT.format(executeDate),
            distance,
            totalWeight,
            totalPallet,
            totalExecutionTime,
            totalLocation,
            Optional.ofNullable(vehicle).map(Vehicle::getVehicleId).orElse(null),
            Optional.ofNullable(externalVehicleType).map(VehicleType::getVehicleTypeId).orElse(null),
            Optional.ofNullable(vehicle).map(Vehicle::getProductTransportCategoryId).orElse(null),
            Optional.ofNullable(vehicle).map(Vehicle::getCapacity).orElse(null),
            Optional
                .ofNullable(partyDriver)
                .map(PartyDriver::getParty)
                .map(Party::getUserLogin)
                .map(UserLogin::getUserLoginId)
                .orElse(null),
            Optional.ofNullable(partyDriver).map(p -> p.getPartyId().toString()).orElse(null),
            distance,
            (statusItem != null ? statusItem.getStatusId() : null)
        );
    }
}
