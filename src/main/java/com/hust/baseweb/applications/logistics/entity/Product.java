package com.hust.baseweb.applications.logistics.entity;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Product {
	@Id
	@Column(name="product_id")
	private String productId;
	
	
	private Date createdStamp;
	private Date lastUpdatedStamp;
	    
}
