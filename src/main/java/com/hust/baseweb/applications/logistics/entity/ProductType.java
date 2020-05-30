package com.hust.baseweb.applications.logistics.entity;


import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class ProductType {

    @Id
    @Column(name = "product_type_id")
    private String productTypeId;

    @Column(name = "description")
    private String description;

    @Column(name = "created_stamp")
    private Date createdStamp;


}
