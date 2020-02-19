package com.hust.baseweb.applications.order.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import java.util.Date;

@Entity
@Getter
@Setter
public class SalesChannel {
    @Id
    @Column(name = "sales_channel_id")
    private String salesChannelId;

    @Column(name = "sales_channel_name")
    private String salesChannelName;

    @Column(name = "created_stamp")
    private Date createdStamp;

    @Column(name = "last_updated_stamp")
    private Date lastUpdatedStamp;
}
