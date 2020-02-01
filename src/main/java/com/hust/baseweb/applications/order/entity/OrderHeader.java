package com.hust.baseweb.applications.order.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;

import com.hust.baseweb.applications.logistics.entity.Facility;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class OrderHeader {
	@Id
	//@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name="order_id")
	private String orderId;
	
	@JoinColumn(name = "order_type_id", referencedColumnName = "order_type_id")
    @ManyToOne(fetch = FetchType.EAGER)
	private OrderType orderType;
	
	@JoinColumn(name = "sales_channel_id", referencedColumnName = "sales_channel_id")
    @ManyToOne(fetch = FetchType.EAGER)
	private SalesChannel salesChannel;

	@JoinColumn(name = "original_facility_id", referencedColumnName = "facility_id")
    @ManyToOne(fetch = FetchType.EAGER)
	private Facility facility;

	
	@Column(name="created_stamp")
	private Date createdStamp;
	
	@Column(name="last_updated_stamp")
	private Date lastUpdatedStamp;

	//private OrderItem[] orderItems;
	
}
