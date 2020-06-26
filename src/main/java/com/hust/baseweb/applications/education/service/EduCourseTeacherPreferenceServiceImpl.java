package com.hust.baseweb.applications.education.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.hust.baseweb.applications.education.entity.CourseTeacherCompositeId;
import com.hust.baseweb.applications.education.entity.EduCourseTeacherPreference;
import com.hust.baseweb.applications.education.repo.EduCourseTeacherPreferenceRepo;

@Service
public class EduCourseTeacherPreferenceServiceImpl implements EduCourseTeacherPreferenceService {

	@Autowired
	EduCourseTeacherPreferenceRepo prefRepo;

	@Override
	public EduCourseTeacherPreference save(String courseId, String teacherId, String classType) {
		// TODO Auto-generated method stub
		EduCourseTeacherPreference pref = prefRepo.findByCompositeId(new CourseTeacherCompositeId(courseId, teacherId, classType));
		if (pref == null) {
			pref = new EduCourseTeacherPreference();
			CourseTeacherCompositeId id = new CourseTeacherCompositeId();
			id.setCourseId(courseId);
			id.setTeacherId(teacherId);
			id.setClassType(classType);
			pref.setCompositeId(id);
			prefRepo.save(pref);
		}
		return pref;
	}

	@Override
	public List<EduCourseTeacherPreference> findByCompositeIdCourseId(String courseId) {
		// TODO Auto-generated method stub
		return prefRepo.findByCompositeIdCourseId(courseId);
	}

	@Override
	public List<EduCourseTeacherPreference> findByCompositeIdTeacherId(String teacherId) {
		// TODO Auto-generated method stub
		return prefRepo.findByCompositeIdTeacherId(teacherId);
	}

}
