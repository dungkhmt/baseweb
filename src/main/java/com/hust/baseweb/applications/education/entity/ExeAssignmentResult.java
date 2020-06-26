package com.hust.baseweb.applications.education.entity;

import java.util.List;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ExeAssignmentResult {
	private int classesNo;
	private int sucessNo;
	private double sucessRate;
	private List<EduClass> exception;
}
