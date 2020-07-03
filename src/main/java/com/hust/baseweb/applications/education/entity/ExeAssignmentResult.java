package com.hust.baseweb.applications.education.entity;

import com.hust.baseweb.applications.education.repo.EduAssignmentRepo.EduClassTeacherAssignmentOutputModel;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExeAssignmentResult {
	private int classesNo;
	private int sucessNo;
	private double sucessRate;
	private List<EduClass> exception;
	private List<EduClassTeacherAssignmentOutputModel> assignmentResult;
}
