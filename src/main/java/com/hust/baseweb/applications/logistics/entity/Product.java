package com.hust.baseweb.applications.logistics.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import java.util.Date;

@Entity
@Getter
@Setter
public class Product {
    @Id
    @Column(name = "product_id")
    private String productId;

    @Column(name="product_name")
    private String productName;
    
    @JoinColumn(name="quantity_uom_id", referencedColumnName="uom_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Uom uom;
    
    
    private Date createdStamp;
    private Date lastUpdatedStamp;

}
