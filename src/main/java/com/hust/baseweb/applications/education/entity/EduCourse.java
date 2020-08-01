package com.hust.baseweb.applications.education.entity;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name="edu_course")
public class EduCourse {
	@Id
	@Column(name="course_id")
	private String courseId;
	
	@Column(name="course_name")
	private String courseName;
	
	@Column(name="credit")
	private int credit;

	@Transient
	private int __rowNum__;
}
