package com.hust.baseweb.applications.humanresource.entity;

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

public class Department {
	@Id
	//@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name="department_id")
	private String departmentId;
	
	@Column(name="department_name")
	private String departmentName;
	
}
