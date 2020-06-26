package com.hust.baseweb.applications.education.test;

import com.poiji.annotation.ExcelCellName;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
public class ClassModel {
	
	@ExcelCellName("course_id")
	private String courseId;
	
	@ExcelCellName("course_name")
	private String courseName;
	
	@ExcelCellName("class_type")
	private String classType;
	
	@ExcelCellName("teacher")
	private String teacher;
	
	@ExcelCellName("email")
	private String email;
	
	@Getter
    @Setter
    @AllArgsConstructor
    @NoArgsConstructor
	public static class teacher {
		private String teacherId;
		private String teacherName;
		private String email;
	}
}
