package com.hust.baseweb.applications.tms.entity;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class VehicleMaintenanceHistory {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID vehicleMaintenanceHistoryId;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")
    private Vehicle vehicle;
    private Date maintenanceDate;
    private Date thruDate;
    private String description;
}
