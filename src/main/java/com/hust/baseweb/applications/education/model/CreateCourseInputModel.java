package com.hust.baseweb.applications.education.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCourseInputModel {
	private String courseId;
	private String courseName;
	private int credit;
}
