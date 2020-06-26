package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduCourse;

@Service
public interface EduCourseService {
	EduCourse save(String courseId, String courseName, int credit);
	List<EduCourse> findAll();
	EduCourse findByCourseId(String courseId);
}
