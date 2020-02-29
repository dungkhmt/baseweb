package com.hust.baseweb.applications.tms.entity;

import com.hust.baseweb.applications.geo.entity.GeoPoint;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.UUID;

@Entity(name = "distance_traveltime_geo_points")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class DistanceTravelTimeGeoPoint {

    @Id
    @Column(name = "distance_traveltime_geo_points_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID id;

    @JoinColumn(name = "from_geo_point_id", referencedColumnName = "geo_point_id")
    @OneToOne(fetch = FetchType.EAGER)
    private GeoPoint fromGeoPoint;

    @JoinColumn(name = "to_geo_point_id", referencedColumnName = "geo_point_id")
    @OneToOne(fetch = FetchType.EAGER)
    private GeoPoint toGeoPoint;

    @Column(name = "distance")
    private double distance;

    @Column(name = "travel_time")
    private double travelTime;
}
