package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.tms.model.DeliveryTripModel;
import com.hust.baseweb.entity.StatusItem;
import com.hust.baseweb.utils.Constant;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
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

    private Double distance;

    private Double totalWeight;

    private Double totalPallet;

    private Double totalExecutionTime;

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
                vehicle == null ? null : vehicle.getVehicleId(),
                externalVehicleType == null ? null : externalVehicleType.getVehicleTypeId(),
                vehicle == null ? null : vehicle.getCapacity(),
                partyDriver == null ? null : partyDriver.getParty().getUserLogin().getUserLoginId(),
                distance,
                statusItem.getStatusId()
        );
    }
}
