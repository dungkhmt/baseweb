package com.hust.baseweb.applications.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Getter
@Setter
@Table(name = "product_promo_product")
public class ProductPromoProduct {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "product_promo_rule_id")
    private UUID productPromoRuleId;

    @Column(name = "product_id")
    private String productId;

    private Date createdStamp;
    private Date lastUpdatedStamp;
}
