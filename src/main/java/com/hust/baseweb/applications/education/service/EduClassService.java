package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduClass;

@Service
public interface EduClassService {
	EduClass save(
			String classId,
			String className,
			String classType,
			String courseId,
			String sessionId,
			String departmentId,
			String semesterId);
	List<EduClass> findAll();
	List<EduClass> findBySemesterId(String semesterId);
}
