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
@Table(name="edu_semester")
public class EduSemester {
	@Id
	@Column(name="semester_id")
	private String semesterId;
	
	@Column(name="semester_name")
	private String semesterName;
}
