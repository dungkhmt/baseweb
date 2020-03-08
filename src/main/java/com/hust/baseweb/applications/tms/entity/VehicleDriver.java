package com.hust.baseweb.applications.tms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class VehicleDriver {
    @Id
    @Column(name = "vehicle_driver_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID vehicleDriverId;

    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Vehicle vehicle;

    @JoinColumn(name = "party_driver_id", referencedColumnName = "party_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PartyDriver partyDriver;

    @Column(name = "from_date")
    private Date fromDate;

    @Column(name = "thru_date")
    private Date thruDate;

}
