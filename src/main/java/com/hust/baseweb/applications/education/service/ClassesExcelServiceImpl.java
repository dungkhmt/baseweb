package com.hust.baseweb.applications.education.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.entity.EduDepartment;
import com.hust.baseweb.applications.education.entity.EduSemester;
import com.hust.baseweb.applications.education.model.ClassesInputModel;
import com.hust.baseweb.applications.education.repo.EduCourseRepo;

import lombok.AllArgsConstructor;

@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class ClassesExcelServiceImpl implements ClassesExcelService {

	EduClassService classService;
	EduCourseService courseService;
	EduCourseRepo courseRepo;
	EduDepartmentService departmentService;
	EduSemesterService semesterService;

	@Override
	public List<EduClass> save(List<ClassesInputModel> input, String semesterId) {
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

}
