package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduTeacher;

@Service
public interface EduTeacherService {
	EduTeacher save(String teacherId, String teacherName, String email, int maxCredit);
	List<EduTeacher> findAll();
	EduTeacher findByTeacherId(String teacherId);
}
