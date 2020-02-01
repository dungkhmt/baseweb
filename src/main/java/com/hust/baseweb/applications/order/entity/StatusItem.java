package com.hust.baseweb.applications.order.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;

import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class StatusItem {
	@Id
	@Column(name="status_id")
	private String statusId;
	
	@Column(name="status_type_id")
	private String statusTypeId;
	
	@Column(name="status_code")
	private String statusCode;
	
}
