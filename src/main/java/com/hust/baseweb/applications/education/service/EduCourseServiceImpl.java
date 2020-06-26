package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.repo.EduCourseRepo;

@Service
public class EduCourseServiceImpl implements EduCourseService {
	
	@Autowired
	EduCourseRepo courseRepo;

	@Override
	public EduCourse save(String courseId, String courseName, int credit) {
		// TODO Auto-generated method stub
		EduCourse course = courseRepo.findByCourseId(courseId);
		if (course==null) {
			course = new EduCourse();
			course.setCourseId(courseId);
			course.setCourseName(courseName);
			course.setCredit(credit);
			courseRepo.save(course);
		}
		return course;
	}

	@Override
	public List<EduCourse> findAll() {
		// TODO Auto-generated method stub
		return courseRepo.findAll();
	}

	@Override
	public EduCourse findByCourseId(String courseId) {
		// TODO Auto-generated method stub
		return courseRepo.findByCourseId(courseId);
	}

}
