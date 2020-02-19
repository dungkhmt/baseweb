package com.hust.baseweb.applications.geo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Setter
@Getter

public class PostalAddress {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contact_mech_id")
    private UUID contactMechId;

    @Column(name = "address")
    private String address;

    @JoinColumn(name = "geo_point_id", referencedColumnName = "geo_point_id")
    @ManyToOne
    private GeoPoint geoPoint;


}
