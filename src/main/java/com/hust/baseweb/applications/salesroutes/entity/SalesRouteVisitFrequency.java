package com.hust.baseweb.applications.salesroutes.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Getter
@Setter
@Entity
public class SalesRouteVisitFrequency {

    @Id
    @Column(name = "visit_frequency_id")
    private String visitFrequencyId;

    @Column(name = "description")
    private String description;


}
