package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduSemester;
import com.hust.baseweb.applications.education.repo.EduSemesterRepo;

@Service
public class EduSemesterServiceImpl implements EduSemesterService {
	
	@Autowired
	EduSemesterRepo semesterRepo;

	@Override
	public EduSemester save(String semesterId, String semesterName) {
		// TODO Auto-generated method stub
		EduSemester semester = semesterRepo.findBySemesterId(semesterId);
		if (semester == null) {
			semester = new EduSemester();
			semester.setSemesterId(semesterId);
			semester.setSemesterName(semesterName);
			semesterRepo.save(semester);
		}
		return semester;
	}

	@Override
	public List<EduSemester> findAll() {
		// TODO Auto-generated method stub
		return semesterRepo.findAll();
	}

}
