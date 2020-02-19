package com.hust.baseweb.applications.geo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;


@Entity
@Setter
@Getter
public class GeoPoint {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "geo_point_id")
    private UUID geoPointId;

    @Column(name = "latitude")
    private String latitude;

    @Column(name = "longitude")
    private String longitude;

}
