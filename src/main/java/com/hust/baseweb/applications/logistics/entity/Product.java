package com.hust.baseweb.applications.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name = "product_name")
    private String productName;

    @Column(name = "weight")
    private Double weight; // quantity=1

    @JoinColumn(name = "quantity_uom_id", referencedColumnName = "uom_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Uom uom;

    @JoinColumn(name = "product_type_id", referencedColumnName = "product_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductType productType;

    @Column(name = "product_transport_category_id")
    private String productTransportCategoryId;  // KHO, LANH, DONG

    private Date createdStamp;
    private Date lastUpdatedStamp;

    @Transient
    private String uomDescription;

    @Transient
    private String productTypeDescription;

    private Integer hsThu;
    private Integer hsPal;
}
