package com.hust.baseweb.applications.education.repo;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.hust.baseweb.applications.education.entity.EduClass;

public interface EduClassRepo extends JpaRepository<EduClass, String> {
	EduClass save(EduClass eduClass);

	EduClass findByClassId(String classId);

	List<EduClass> findBySemesterId(String semesterId);

	@Query(value = "select * from edu_class ec where ec.semester_id=?1 "
			+ "and not exists (select class_id from edu_class_teacher_asignment ecta where ec.class_id = ecta.class_id) " + 
			"	or exists (select * from edu_class_teacher_asignment ecta where ec.class_id = ecta.class_id and ecta.teacher_id = 'NULL')", nativeQuery = true)
	List<EduClass> findNotAssignedBySemesterId(String semesterId);
}
