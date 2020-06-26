package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.EduClassTeacherAssignment;
import com.hust.baseweb.applications.education.entity.ExeAssignmentResult;
import com.hust.baseweb.applications.education.repo.EduAssignmentRepo;

@Service
public interface EduAssignmentService {
	List<EduClassTeacherAssignment> findAll();

	List<EduClassTeacherAssignment> findByTeacherId(String teacherId);

	EduClassTeacherAssignment findByClassId(String classId);

	ExeAssignmentResult executeAssignment(String semesterid);

	List<EduAssignmentRepo.EduClassTeacherAssignmentOutputModel> getEduClassTeacherAssignmentBySemesterId(
			String semesterId);

	List<EduAssignmentRepo.EduClassTeacherAssignmentOutputModel> getEduClassTeacherAssignmentBySemesterIdTeacherId(
			String semesterId, String teacherId);
}
