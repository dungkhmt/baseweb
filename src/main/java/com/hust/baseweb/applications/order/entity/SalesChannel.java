package com.hust.baseweb.applications.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Setter;
import lombok.Getter;

@Entity
@Getter
@Setter
public class SalesChannel {
	@Id
	@Column(name="sales_channel_id")
	private String salesChannelId;
	
	@Column(name="sales_channel_name")
	private String salesChannelName;
	
	@Column(name="created_stamp")
	private Date createdStamp;
	
	@Column(name="last_updated_stamp")
	private Date lastUpdatedStamp;
}
