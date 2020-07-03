package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.EduCourseTeacherPreference;
import com.hust.baseweb.applications.education.model.ClassesInputModel;
import com.hust.baseweb.applications.education.model.Course4teacherInputModel;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UploadExcelService {
	List<EduClass> saveClasses(List<ClassesInputModel> input, String semesterId);
	List<EduCourseTeacherPreference> saveCourseTeacherPreference(List<Course4teacherInputModel> input);
}
