package com.hust.baseweb.applications.education.entity;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@AllArgsConstructor
@NoArgsConstructor
@Table(name="edu_teacher")
public class EduTeacher {
	@Id
	@Column(name="teacher_id")
	private String teacherId;
	
	@Column(name="teacher_name")
	private String teacherName;
	
	@Column(name="email")
	private String email;
	
	@Column(name="max_credit")
	private int maxCredit;
}
