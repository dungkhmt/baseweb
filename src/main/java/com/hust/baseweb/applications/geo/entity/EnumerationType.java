package com.hust.baseweb.applications.geo.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;


@Entity(name = "enumeration_type")
@Setter
@Getter
public class EnumerationType {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "enumeration_type_id")
    private String enumerationTypeId;
}
