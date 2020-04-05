package com.hust.baseweb.applications.geo.entity;

import com.hust.baseweb.applications.geo.embeddable.DistanceTraveltimePostalAddressEmbeddableId;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;


@Entity(name = "distance_traveltime_postal_address")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DistanceTraveltimePostalAddress {

    @EmbeddedId
    private DistanceTraveltimePostalAddressEmbeddableId distanceTraveltimePostalAddressEmbeddableId;

    @Column(name = "distance")
    private double distance;

    @Column(name = "travel_time")
    private double travelTime;

    @Column(name = "travel_time_truck")
    private double travelTimeTruck;

    @Column(name = "travel_time_motobike")
    private double travelTimeMotobike;

    @JoinColumn(name = "source_enum_id", referencedColumnName = "enum_id")
    @ManyToOne
    private Enumeration enumeration;

    @Transient
    private String addressStart;

    @Transient
    private String addressEnd;

    @Transient
    private UUID idStart;

    @Transient
    private UUID idEnd;

    @Transient
    private String enumID;




}



