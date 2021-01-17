package com.hust.baseweb.applications.postsys.entity;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.hust.baseweb.entity.StatusItem;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.UUID;

@Entity
@Table(name="post_ship_order")
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class PostOrder {
    @Id
    @Column(name = "post_ship_order_id")
    @GeneratedValue(strategy = GenerationType.AUTO)
    private UUID postShipOrderId;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "from_customer_id", referencedColumnName = "post_customer_id")
    private PostCustomer fromCustomer;

    @OneToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name = "to_customer_id", referencedColumnName = "post_customer_id")
    private PostCustomer toCustomer;

    @Column(name="package_name")
    private String packageName;

    @ManyToOne(cascade = CascadeType.MERGE)
    @JoinColumn(name="package_type_id", referencedColumnName = "post_package_type_id")
    private PostPackageType postPackageType;

    @Column(name="weight")
    private int weight;

    @Column(name="description")
    private String description;

    @Column(name="pickup_date")
    private Date pickupDate;

    @Column(name="expected_delivery_date")
    private Date expectedDeliveryDate;

    @ManyToOne()
    @JoinColumn(name = "status_id", referencedColumnName = "status_id", insertable=false, updatable = false)
    private StatusItem statusItem;

    @Column(name="status_id")
    private String statusId;

    @ManyToOne()
    @JoinColumn(name="from_post_office_id", referencedColumnName = "post_office_id", insertable = false, updatable = false)
    private PostOffice fromPostOffice;

    @ManyToOne()
    @JoinColumn(name="to_post_office_id", referencedColumnName = "post_office_id", insertable = false, updatable = false)
    private PostOffice toPostOffice;

    @ManyToOne()
    @JoinColumn(name="current_post_office_id", referencedColumnName = "post_office_id", insertable = false, updatable = false)
    private PostOffice currentPostOffice;

    @Column(name="to_post_office_id")
    private String toPostOfficeId;

    @Column(name="from_post_office_id")
    private String fromPostOfficeId;

    @Column(name="current_post_office_id")
    private String currentPostOfficeId;

    @Column(name="created_stamp", insertable = false, updatable = false)
    private Date createdStamp;

    public PostOrder() {
        fromCustomer = new PostCustomer();
        toCustomer = new PostCustomer();
        postPackageType = new PostPackageType();
        statusItem = new StatusItem();
    }
}
