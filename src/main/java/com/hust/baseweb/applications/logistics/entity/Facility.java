package com.hust.baseweb.applications.logistics.entity;

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
public class Facility {

    @Id
    @Column(name = "facility_id")
    private String facilityId;

    public Facility(String facilityId) {

        this.facilityId = facilityId;
    }

    @Column(name = "facility_name")
    private String facilityName;

    @JoinColumn(name = "contact_mech_id", referencedColumnName = "contact_mech_id")
    @OneToOne(fetch = FetchType.EAGER)
    private PostalAddress postalAddress;

    private Date createdStamp;
    private Date lastUpdatedStamp;

}
