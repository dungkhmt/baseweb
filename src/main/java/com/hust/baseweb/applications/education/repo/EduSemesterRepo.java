package com.hust.baseweb.applications.education.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.education.entity.EduSemester;

public interface EduSemesterRepo extends JpaRepository<EduSemester, String> {
	EduSemester save(EduSemester semester);
	EduSemester findBySemesterId(String semesterId);
	List<EduSemester> findAll();
}
