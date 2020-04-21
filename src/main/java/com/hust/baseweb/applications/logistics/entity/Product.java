package com.hust.baseweb.applications.logistics.entity;

import java.util.Date;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import com.hust.baseweb.entity.Content;

import lombok.Getter;
import lombok.Setter;

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
    private Double weight; // quantity=1, unit=kg

    @JoinColumn(name = "quantity_uom_id", referencedColumnName = "uom_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Uom uom;

    @JoinColumn(name = "product_type_id", referencedColumnName = "product_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private ProductType productType;

    @Column(name = "product_transport_category_id")
    private String productTransportCategoryId; // KHO, LANH, DONG

    private Date createdStamp;
    private Date lastUpdatedStamp;

    @Transient
    private String uomDescription;

    @Transient
    private String productTypeDescription;

    private Integer hsThu;
    private Integer hsPal;
    @OneToMany(fetch = FetchType.EAGER, cascade = CascadeType.ALL)
    @JoinTable(name = "product_content", joinColumns = @JoinColumn(name = "product_id", referencedColumnName = "product_id"), inverseJoinColumns = @JoinColumn(name = "content_id", referencedColumnName = "content_id"))
    private Set<Content> contents;
}
