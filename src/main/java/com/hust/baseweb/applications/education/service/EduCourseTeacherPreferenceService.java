package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduCourseTeacherPreference;

@Service
public interface EduCourseTeacherPreferenceService {
	EduCourseTeacherPreference save(String courseId, String teacherId, String classType);
	List<EduCourseTeacherPreference> findByCompositeIdCourseId(String courseId);
	List<EduCourseTeacherPreference> findByCompositeIdTeacherId(String teacherId);
}
