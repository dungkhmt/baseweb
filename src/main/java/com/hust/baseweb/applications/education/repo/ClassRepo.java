package com.hust.baseweb.applications.education.repo;

import com.hust.baseweb.applications.education.entity.Class;
import com.hust.baseweb.applications.education.model.getclasslist.ClassOM;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ClassRepo extends JpaRepository<Class, String> {

    /*Class save(Class aClass);

    Class findByClassId(String classId);

    List<Class> findBySemesterId(String semesterId);

    @Query(value = "from edu_class ec where ec.semester_id=?1 \n" +
                   "and not exists (select class_id \n" +
                   "\t\t\t\tfrom edu_class_teacher_asignment ecta \n" +
                   "\t\t\t\twhere ec.class_id = ecta.class_id) \n" +
                   "\tor exists (select * \n" +
                   "\t\t\t\tfrom edu_class_teacher_asignment ecta \n" +
                   "\t\t\t\twhere ec.class_id = ecta.class_id and ecta.teacher_id = 'NULL')",
           nativeQuery = true)
    List<Class> findNotAssignedBySemesterId(String semesterId);*/

    // Class Management
    @Query(value = "select cast(cl.id as varchar) id,\n" +
                   "\tcode, \n" +
                   "\tco.id courseId,\n" +
                   "\tco.course_name courseName,\n" +
                   "\tcl.class_type classType,\n" +
                   "\td.id departmentId\n" +
                   "from edu_class as cl \n" +
                   "\tinner join edu_course as co on cl.course_id = co.id \n" +
                   "\tinner join edu_department as d ON cl.department_id = d.id \n" +
                   "where cl.semester_id = ?1",
           nativeQuery = true)
    Page<ClassOM> findBySemesterId(short semesterId, Pageable pageable);

    @Query(value = "select count(student_id)\n" +
                   "from edu_class_registration ecr \n" +
                   "where class_id = ?1 and status = 'APPROVED'",
           nativeQuery = true)
    int getNoStudentsOf(UUID classId);
}
