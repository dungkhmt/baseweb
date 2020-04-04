package com.hust.baseweb.applications.geo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity(name = "enumeration")
@Setter
@Getter
public class Enumeration {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "enum_id")
    private String enumId;

    @Column(name = "enum_code")
    private String enumCode;

    @Column(name = "sequence_id")
    private String sequenceId;

    @Column(name = "description")
    private String description;

    @Column(name = "last_updated_stamp")
    private Date lastUpdateStamp;

    @Column(name = "created_stamp")
    private Date createdStamp;
}
