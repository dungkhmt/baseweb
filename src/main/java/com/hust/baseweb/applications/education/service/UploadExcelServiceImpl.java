package com.hust.baseweb.applications.education.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.entity.EduCourseTeacherPreference;
import com.hust.baseweb.applications.education.entity.EduDepartment;
import com.hust.baseweb.applications.education.entity.EduSemester;
import com.hust.baseweb.applications.education.entity.EduTeacher;
import com.hust.baseweb.applications.education.model.ClassesInputModel;
import com.hust.baseweb.applications.education.model.Course4teacherInputModel;
import com.hust.baseweb.applications.education.repo.EduCourseRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class UploadExcelServiceImpl implements UploadExcelService {
	
	EduClassService classService;
	EduCourseService courseService;
	EduCourseRepo courseRepo;
	EduDepartmentService departmentService;
	EduSemesterService semesterService;
	EduCourseTeacherPreferenceService preferenceService;
	EduTeacherService teacherService;

	@Override
	public List<EduClass> saveClasses(List<ClassesInputModel> input, String semesterId) {
		// TODO Auto-generated method stub
		List<EduClass> result = new ArrayList<EduClass>();
		EduSemester semester = semesterService.save(semesterId, semesterId);
		for (ClassesInputModel item : input) {
			EduCourse course = courseRepo.findByCourseId(item.getCourseId());
			if (course==null) {
				course = new EduCourse();
				course.setCourseId(item.getCourseId());
				course.setCourseName(item.getClassName());
			}
			course.setCredit(item.getCredit());
			courseRepo.save(course);
			
			EduDepartment department = departmentService.save(item.getDepartment(), item.getDepartment());

			EduClass eduClass = classService.save(item.getClassId(), item.getClassName(), item.getClassType(),
					course.getCourseId(), item.getSession(), department.getDepartmentId(), semester.getSemesterId());
			result.add(eduClass);
		}
		return result;
	}

	@Override
	public List<EduCourseTeacherPreference> saveCourseTeacherPreference(List<Course4teacherInputModel> input) {
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
