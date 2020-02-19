package com.hust.baseweb.applications.order.entity;

import com.hust.baseweb.applications.logistics.entity.Facility;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;
import java.util.List;

@Entity
@Getter
@Setter
public class OrderHeader {
    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "order_id")
    private String orderId;

    @JoinColumn(name = "order_type_id", referencedColumnName = "order_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private OrderType orderType;

    @JoinColumn(name = "sales_channel_id", referencedColumnName = "sales_channel_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private SalesChannel salesChannel;

    @JoinColumn(name = "original_facility_id", referencedColumnName = "facility_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Facility facility;

    @Column(name = "order_date")
    private Date orderDate;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Column(name = "last_updated_stamp")
    private Date lastUpdatedStamp;


    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @OneToMany(fetch = FetchType.LAZY)
    private List<OrderItem> orderItems;

    //@JoinTable(name="OrderRole", inverseJoinColumns=@JoinColumn(name="party_id", referencedColumnName="party_id"),
    //			joinColumns=@JoinColumn(name="order_id", referencedColumnName="order_id"))
    //private List<Party> parties;
}
