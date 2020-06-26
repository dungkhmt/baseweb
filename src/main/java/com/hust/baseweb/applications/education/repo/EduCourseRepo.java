package com.hust.baseweb.applications.education.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.education.entity.EduCourse;

public interface EduCourseRepo extends JpaRepository<EduCourse, String> {
	EduCourse save(EduCourse course);
	EduCourse findByCourseId(String courseId);
}
