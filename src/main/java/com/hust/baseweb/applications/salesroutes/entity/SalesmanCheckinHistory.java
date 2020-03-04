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

import com.hust.baseweb.applications.customer.entity.PartyCustomer;
import com.hust.baseweb.entity.UserLogin;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class SalesmanCheckinHistory {
	@Id
	@Column(name="salesman_checkin_history_id")
	@GeneratedValue(strategy=GenerationType.AUTO)
	private UUID salesmanCheckinHistoryId;
	
	//@JoinColumn(name="user_login_id", referencedColumnName="user_login_id")
	//@ManyToOne(fetch = FetchType.EAGER)
	//private UserLogin userLogin;
	
	@Column(name="user_login_id")
	private String userLoginId;
	
	@JoinColumn(name="party_customer_id", referencedColumnName="party_id")
	@ManyToOne(fetch = FetchType.EAGER)
	private PartyCustomer  partyCustomer;
	
	@Column(name="check_in_action")
	private String checkinAction;// Y (check-in) or N (check-out)
	
	@Column(name="location")
	private String location;
	
	@Column(name="time_point")
	 private Date timePoint;
	
}
