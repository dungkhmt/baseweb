package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduSemester;

@Service
public interface EduSemesterService {
	EduSemester save(String semesterId, String semesterName);
	List<EduSemester> findAll();
}
