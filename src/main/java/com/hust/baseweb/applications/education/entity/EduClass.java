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
@Table(name="edu_class")
public class EduClass {
	
	@Id
	@Column(name="class_id")
	private String classId;
	
	@Column(name="class_name")
	private String className;
	
	@Column(name="course_id")
	private String courseId;
	
	@Column(name="class_type")
	private String classType;
	
	@Column(name="session_id")
	private String sessionId;
	
	@Column(name="department_id")
	private String departmentId;
	
	@Column(name="semester_id")
	private String semesterId;

}
