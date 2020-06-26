package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduCourseTeacherPreference;
import com.hust.baseweb.applications.education.model.Course4teacherInputModel;

@Service
public interface Course4teacherService {
	List<EduCourseTeacherPreference> save(List<Course4teacherInputModel> input);
}
