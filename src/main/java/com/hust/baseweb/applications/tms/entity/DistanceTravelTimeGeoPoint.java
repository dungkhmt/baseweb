package com.hust.baseweb.applications.tms.entity;

import lombok.*;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.io.Serializable;
import java.util.UUID;

@Entity(name = "distance_traveltime_geo_points")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@IdClass(CompositeDistanceTravelTimeGeoPointId.class)
public class DistanceTravelTimeGeoPoint {

    @Id
    private UUID fromGeoPointId;
    @Id
    private UUID toGeoPointId;

    @Column(name = "distance")
    private double distance;

    @Column(name = "travel_time")
    private double travelTime;
}

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@EqualsAndHashCode
class CompositeDistanceTravelTimeGeoPointId implements Serializable {
    private UUID fromGeoPointId;
    private UUID toGeoPointId;
}