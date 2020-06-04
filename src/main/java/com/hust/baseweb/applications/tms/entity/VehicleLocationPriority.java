package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.geo.entity.PostalAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class VehicleLocationPriority {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID vehicleLocationPriorityId;

    @JoinColumn(name = "vehicle_id", referencedColumnName = "vehicle_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Vehicle vehicle;

    @JoinColumn(name = "contact_mech_id", referencedColumnName = "contact_mech_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private PostalAddress postalAddress;

    private Integer priority;

    private Date fromDate;
    private Date thruDate;
}
