package com.hust.baseweb.applications.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import java.math.BigDecimal;

@Entity
@Getter
@Setter
@IdClass(CompositeProductFacilityId.class)
public class ProductFacility {
    @Id
    @Column(name = "product_id")
    private String productId;

    @Id
    @Column(name = "facility_id")
    private String facilityId;

    @Column(name = "last_inventory_count")
    private BigDecimal lastInventoryCount;

    @Column(name = "atp_inventory_count")
    private BigDecimal atpInventoryCount;

    public static void main(String[] args) {

    }
}
