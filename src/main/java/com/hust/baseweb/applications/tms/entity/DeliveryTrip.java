package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.entity.Party;
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
    private Party party;

    @Column(name = "execute_date")
    private Date executeDate;

    @JoinColumn(name = "execute_external_vehicle_type_id", referencedColumnName = "vehicle_type_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private VehicleType externalVehicleType;

}
