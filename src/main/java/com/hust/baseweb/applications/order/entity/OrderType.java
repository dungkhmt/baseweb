package com.hust.baseweb.applications.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class OrderType {
	@Id
	@Column(name="order_type_id")
	private String orderTypeId;
	
	@Column(name="description")
	private String description;
	
	@Column(name="created_stamp")
	private Date createdStamp;
	
	@Column(name="last_updated_stamp")
	private Date lastUpdatedStamp;
	
	
	
}
