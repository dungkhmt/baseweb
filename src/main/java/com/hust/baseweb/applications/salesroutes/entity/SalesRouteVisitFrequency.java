package com.hust.baseweb.applications.salesroutes.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

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
