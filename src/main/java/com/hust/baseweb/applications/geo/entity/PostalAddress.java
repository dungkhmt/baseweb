package com.hust.baseweb.applications.geo.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class PostalAddress {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "contact_mech_id")
    private UUID contactMechId;

    @Column(name = "location_code")
    private String locationCode;

    @Column(name = "address")
    private String address;

    @JoinColumn(name = "geo_point_id", referencedColumnName = "geo_point_id")
    @ManyToOne
    private GeoPoint geoPoint;

    private Double maxLoadWeight;

    @Transient
    private String coordinates;

    @Transient
    private Double lat;

    @Transient
    private Double lng;


    public PostalAddress(
        UUID contactMechId,
        String locationCode,
        String address,
        GeoPoint geoPoint,
        Double maxLoadWeight
    ) {
        this.contactMechId = contactMechId;
        this.locationCode = locationCode;
        this.address = address;
        this.geoPoint = geoPoint;
        this.maxLoadWeight = maxLoadWeight;
    }
}
