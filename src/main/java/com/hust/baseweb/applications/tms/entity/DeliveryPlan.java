package com.hust.baseweb.applications.tms.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class DeliveryPlan {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="delivery_plan_id")
	private UUID deliveryPlanId;
	
	@Column(name="description")
	private String description;
	
	@Column(name="created_by")
	private String createdByUserLoginId;
	
	@Column(name="delivery_date")
	private Date deliveryDate;
	
	
}
