package com.hust.baseweb.applications.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter

public class Facility {
    @Id
    @Column(name = "facility_id")
    private String facilityId;

    @Column(name = "facility_name")
    private String facilityName;

    private Date createdStamp;
    private Date lastUpdatedStamp;

}
