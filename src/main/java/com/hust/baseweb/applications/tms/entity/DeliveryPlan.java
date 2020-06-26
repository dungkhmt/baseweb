package com.hust.baseweb.applications.tms.entity;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class DeliveryPlan {

    @Id
    @Column(name = "delivery_plan_id")
    private String deliveryPlanId;

    @Column(name = "description")
    private String description;

    @Column(name = "created_by")
    private String createdByUserLoginId;

    @Column(name = "delivery_date")
    private Date deliveryDate;

    private Double totalWeightShipmentItems = 0.0;

    @NotNull
    public static String convertSequenceIdToDeliveryPlanId(Long id) {
        return "DP" + String.format("%010d", id);
    }
}
