package com.hust.baseweb.applications.order.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Getter
@Setter
public class OrderStatus {

    @Id
    @Column(name = "order_status_id")
    private String orderStatusId;

    @JoinColumn(name = "order_id", referencedColumnName = "order_id")
    @ManyToOne(fetch = FetchType.LAZY)
    private OrderHeader order;

    //@JoinColumn(name="status_id", referencedColumnName="status_id")
    //@ManyToOne(fetch = FetchType.LAZY)
    //private StatusItem status;

    @Column(name = "status_id")
    private String statusId;

}
