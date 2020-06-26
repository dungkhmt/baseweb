package com.hust.baseweb.applications.education.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.hust.baseweb.applications.education.entity.CourseTeacherCompositeId;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.entity.EduCourseTeacherPreference;
import com.hust.baseweb.applications.education.entity.EduTeacher;

public interface EduCourseTeacherPreferenceRepo extends JpaRepository<EduCourseTeacherPreference, CourseTeacherCompositeId> {
	EduCourseTeacherPreference save(EduCourseTeacherPreference pref);
	List<EduCourseTeacherPreference> findByCompositeIdCourseId(String courseId);
	List<EduCourseTeacherPreference> findByCompositeIdTeacherId(String teacherId);
	EduCourseTeacherPreference findByCompositeId(CourseTeacherCompositeId id);
}
