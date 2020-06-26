package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduTeacher;
import com.hust.baseweb.applications.education.repo.EduTeacherRepo;

@Service
public class EduTeacherServiceImpl implements EduTeacherService {

	@Autowired
	EduTeacherRepo teacherRepo;
	
	@Override
	public EduTeacher save(String teacherId, String teacherName, String email, int maxCredit) {
		// TODO Auto-generated method stub
		EduTeacher teacher = teacherRepo.findByTeacherId(teacherId);
		if (teacher == null) {
			teacher = new EduTeacher();
			teacher.setEmail(email);
			teacher.setTeacherId(teacherId);
			teacher.setTeacherName(teacherName);
			teacher.setMaxCredit(maxCredit);
			teacherRepo.save(teacher);
		}
		return teacher;
	}

	@Override
	public List<EduTeacher> findAll() {
		// TODO Auto-generated method stub
		return teacherRepo.findAll();
	}

	@Override
	public EduTeacher findByTeacherId(String teacherId) {
		// TODO Auto-generated method stub
		return teacherRepo.findByTeacherId(teacherId);
	}

}
