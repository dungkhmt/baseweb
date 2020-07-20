package com.hust.baseweb.applications.education.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateClassInputModel {
private String classId;
	private String className;
	private String classType;
	private String courseId;
	private String sessionId;
	private String departmentId;
	private String semesterId;
}
