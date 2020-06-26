package com.hust.baseweb.applications.education.core.model;

import java.util.ArrayList;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Teacher {
    private String code;
    private String name;
    private int maxCredit;
    private ArrayList<Course> courses;

    
    public Teacher(String code, String name, int maxCredit,
			ArrayList<Course> courses) {
		super();
		this.code = code;
		this.name = name;
		this.maxCredit = maxCredit;
		this.courses = courses;
	}

	public Teacher() {
	}
}
