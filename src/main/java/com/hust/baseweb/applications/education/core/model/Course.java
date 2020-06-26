package com.hust.baseweb.applications.education.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Course {
	private String code;
	private String name;
	private String type;

	public Course() {
	}

	public Course(String code, String name, String type) {
		super();
		this.code = code;
		this.name = name;
		this.type = type;
	}
}
