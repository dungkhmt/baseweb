package com.hust.baseweb.applications.order.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
@Getter
@Setter
public class OrderItemType {
    @Id
    @Column(name = "order_item_type_id")
    private String orderItemTypeId;

}
