package com.hust.baseweb.applications.education.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.controller.EduControllerAPI;
import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.entity.EduCourseTeacherPreference;
import com.hust.baseweb.applications.education.entity.EduTeacher;
import com.hust.baseweb.applications.education.model.Course4teacherInputModel;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class Course4teacherServiceImpl implements Course4teacherService {
	
	EduCourseTeacherPreferenceService preferenceService;
	EduCourseService courseService;
	EduTeacherService teacherService;
	
	@Override
	public List<EduCourseTeacherPreference> save(List<Course4teacherInputModel> input) {
		// TODO Auto-generated method stub
		List<EduCourseTeacherPreference> result = new ArrayList<EduCourseTeacherPreference>();
		for (Course4teacherInputModel item: input) {
			EduCourse course = courseService.save(item.getCourseId(), item.getCourseName(), 0);
			EduTeacher teacher = teacherService.save(item.getEmail(), item.getTeacherName(), item.getEmail(), item.getMaxCredit());
			EduCourseTeacherPreference pref = preferenceService.save(course.getCourseId(), teacher.getTeacherId(), item.getClassType());
			result.add(pref);
		}
		return result;
	}
}
