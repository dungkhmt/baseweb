package com.hust.baseweb.applications.education.model;

import com.poiji.annotation.ExcelCellName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Course4teacherInputModel {
	
	@ExcelCellName("course_id")
	private String courseId;
	
	@ExcelCellName("course_name")
	private String courseName;
	
	@ExcelCellName("class_type")
	private String classType;
	
	@ExcelCellName("teacher")
	private String teacherName;
	
	@ExcelCellName("email")
	private String email;
	
	@ExcelCellName("max_credit")
	private int maxCredit;
	
	public String toString() {
		return "Course4teacherInputModel, courseId=" + courseId
				+ " courseName=" + courseName
				+ " classType=" + classType 
				+ " teacher=" + teacherName
				+ " email=" + email;
	}

}
