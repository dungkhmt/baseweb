package com.hust.baseweb.applications.education.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity
public class EduDepartment {

    @Id
	private String id;
	
	@Column(name="department_name")
	private String name;
}
