package com.hust.baseweb.applications.education.repo;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.education.entity.EduTeacher;

public interface EduTeacherRepo extends JpaRepository<EduTeacher, String> {
	EduTeacher save(EduTeacher teacher);
	EduTeacher findByTeacherId(String teacherId);
}
