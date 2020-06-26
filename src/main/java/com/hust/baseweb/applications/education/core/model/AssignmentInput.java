package com.hust.baseweb.applications.education.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AssignmentInput {
    private Class[] classes;
    private Teacher[] teachers;

	public AssignmentInput(Class[] classes, Teacher[] teachers) {
		super();
		this.classes = classes;
		this.teachers = teachers;
	}
}
