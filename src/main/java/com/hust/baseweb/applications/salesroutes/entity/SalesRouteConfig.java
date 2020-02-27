package com.hust.baseweb.applications.salesroutes.entity;

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
public class SalesRouteConfig {
	
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="sales_route_config_id")
	private UUID salesRouteConfigId;
	
	@Column(name="days")
	private String days;
	
	@Column(name="repeat_week")
	private int repeatWeek;
	
	@Column(name="status_id")
	private String statusId;
	
	@Column(name="description")
	private String description;
	
}
