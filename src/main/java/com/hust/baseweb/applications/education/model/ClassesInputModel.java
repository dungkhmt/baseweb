package com.hust.baseweb.applications.education.model;

import com.poiji.annotation.ExcelCellName;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ClassesInputModel {
	
	@ExcelCellName("class_id")
	private String classId;
	
	@ExcelCellName("course_id")
	private String courseId;
	
	@ExcelCellName("class_name")
	private String className;
	
	@ExcelCellName("class_type")
	private String classType;
	
	@ExcelCellName("session")
	private String session;
	
	@ExcelCellName("credit_num")
	private int credit;
	
	@ExcelCellName("department")
	private String department;

}
