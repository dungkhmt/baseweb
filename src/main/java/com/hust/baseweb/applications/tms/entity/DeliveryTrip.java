package com.hust.baseweb.applications.tms.entity;

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

import com.hust.baseweb.entity.Party;
import com.hust.baseweb.entity.UserLogin;

import lombok.Setter;
import lombok.Getter;

@Entity
@Getter
@Setter
public class DeliveryTrip {
	@Id
	@GeneratedValue(strategy=GenerationType.AUTO)
	@Column(name="delivery_trip_id")
	private UUID delvieryTripId;
	
	@JoinColumn(name="delivery_plan_id", referencedColumnName="delivery_plan_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private DeliveryPlan deliveryPlan;
	
	@Column(name="delivery_plan_solution_seq_id")
	private String deliveryPlanSoutionSeqId;
	
	@JoinColumn(name="vehicle_id", referencedColumnName="vehicle_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private Vehicle vehicle;
	
	@JoinColumn(name="driver_id", referencedColumnName="party_id")
	@ManyToOne(fetch=FetchType.EAGER)
	private Party party;
	
	@Column(name="execute_date")
	private Date executeDate; 
	
	@JoinColumn(name="execute_external_vehicle_type_id", referencedColumnName="vehicle_type_id")
	@ManyToOne(fetch=FetchType.LAZY)
	private VehicleType externalVehicleType;
	
}
