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
@Table(name="edu_department")
public class EduDepartment {
	@Id
	@Column(name="department_id")
	private String departmentId;
	
	@Column(name="department_name")
	private String departmentName;
}
