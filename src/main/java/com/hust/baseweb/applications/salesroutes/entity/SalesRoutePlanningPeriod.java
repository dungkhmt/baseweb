package com.hust.baseweb.applications.salesroutes.entity;

import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

import com.hust.baseweb.entity.UserLogin;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter

public class SalesRoutePlanningPeriod {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="sales_route_planning_period_id")
	private UUID salesRoutePlanningPeriodId;
	
	@Column(name="from_date")
	private Date fromDate;
	
	@Column(name="to_date")
	private Date toDate;
	
	@Column(name="description")
	private String description;
	
	@JoinColumn(name="created_by", referencedColumnName="user_login_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private UserLogin createdByUserLogin;
	
}
