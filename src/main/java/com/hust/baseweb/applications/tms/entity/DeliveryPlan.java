package com.hust.baseweb.applications.tms.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
public class DeliveryPlan {

    @Id
    @Column(name = "delivery_plan_id")
    private UUID deliveryPlanId;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private String createdByUserLoginId;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    private Double totalWeightShipmentItems = 0.0;
}
