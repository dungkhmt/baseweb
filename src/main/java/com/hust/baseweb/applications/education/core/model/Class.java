package com.hust.baseweb.applications.education.core.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Class {
    private String code;
    private int credit;
    private Session[] timeTable;
    private Course course;

	public Class(String code, int credit, Session[] timeTable, Course course) {
		super();
		this.code = code;
		this.credit = credit;
		this.timeTable = timeTable;
		this.course = course;
	}

	public Class() {
	
	}
}
