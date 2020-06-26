package com.hust.baseweb.applications.education.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hust.baseweb.applications.education.entity.ClassTeacherCompositeId;
import com.hust.baseweb.applications.education.entity.EduClassTeacherAssignment;

public interface EduAssignmentRepo extends JpaRepository<EduClassTeacherAssignment, ClassTeacherCompositeId> {
	List<EduClassTeacherAssignment> findAll();

	EduClassTeacherAssignment save(EduClassTeacherAssignment assignment);

	List<EduClassTeacherAssignment> findByClassTeacherCompositeIdTeacherId(String teacherId);

	EduClassTeacherAssignment findByClassTeacherCompositeIdClassId(String classId);

	@Query(value = "select ec.class_id classId, ec.course_id courseId, ec.class_name courseName, ec2.credit, ec.class_type classType, ec.session_id sessionId, et.teacher_name teacherName, et.email \n"
			+ "from edu_class_teacher_asignment ecta, edu_class ec , edu_course ec2, edu_teacher et \n"
			+ "where ecta.class_id = ec.class_id and ec.course_id = ec2.course_id and et.teacher_id = ecta.teacher_id and ec.semester_id = ?1", nativeQuery = true)
	List<EduClassTeacherAssignmentOutputModel> getClassTeacherAssignmentBySemesterId(String semesterId);
	
	@Query(value = "select ec.class_id classId, ec.course_id courseId, ec.class_name courseName, ec2.credit, ec.class_type classType, ec.session_id sessionId, et.teacher_name teacherName, et.email \n"
			+ "from edu_class_teacher_asignment ecta, edu_class ec , edu_course ec2, edu_teacher et \n"
			+ "where ecta.class_id = ec.class_id and ec.course_id = ec2.course_id and et.teacher_id = ecta.teacher_id and ec.semester_id = ?1 and et.teacher_id = ?2", nativeQuery = true)
	List<EduClassTeacherAssignmentOutputModel> getClassTeacherAssignmentBySemesterIdTeacherId(String semesterId,
			String teacherId);

	interface EduClassTeacherAssignmentOutputModel {
		String getClassId();

		String getCourseId();

		String getCourseName();

		String getClassType();

		int getCredit();

		String getSessionId();

		String getTeacherName();

		String getEmail();
	}
}
