package com.hust.baseweb.applications.geo.entity;

import com.hust.baseweb.applications.geo.embeddable.DistanceTravelTimePostalAddressEmbeddableId;
import lombok.*;

import javax.persistence.*;
import java.util.UUID;


@Entity(name = "distance_traveltime_postal_address")
@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class DistanceTravelTimePostalAddress {

    @EmbeddedId
    private DistanceTravelTimePostalAddressEmbeddableId distanceTravelTimePostalAddressEmbeddableId;

    @Column(name = "distance")
    private Integer distance;   // meter

    @Column(name = "travel_time")
    private Integer travelTime; // second

    @Column(name = "travel_time_truck")
    private Integer travelTimeTruck; // second

    @Column(name = "travel_time_motorbike")
    private Integer travelTimeMotorbike; // second

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



