package com.hust.baseweb.applications.education.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.education.entity.Semester;

public interface SemesterRepo extends JpaRepository<Semester, String> {
	Semester save(Semester semester);
	Semester findById(short semesterId);
	List<Semester> findAll();

	Semester findByActiveTrue();
}
