package com.hust.baseweb.applications.tmscontainer.entity;


import com.hust.baseweb.applications.geo.entity.PostalAddress;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ContDepotTrailer {

    @Id
    @Column(name = "depot_trailer_id")
    private String depotTrailerId;

    @Column(name = "depot_trailer_name")
    private String depotTrailerName;

    @JoinColumn(name = "contact_mech_id", referencedColumnName = "contact_mech_id")
    @ManyToOne
    private PostalAddress postalAddress;

    @Column(name = "last_updated_stamp")
    private Date lastUpdatedStamp;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Transient
    private String address;

    @Transient
    private Double lat;

    @Transient
    private Double lng;
}
