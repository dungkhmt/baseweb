package com.hust.baseweb.applications.education.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTeacherInputModel {
	private String teacherName;
	private String email;
	private int maxCredit;

}
