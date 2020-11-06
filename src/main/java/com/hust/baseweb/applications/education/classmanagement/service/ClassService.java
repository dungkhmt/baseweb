package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.classmanagement.enumeration.RegistStatus;
import com.hust.baseweb.applications.education.entity.EduClass;
import com.hust.baseweb.applications.education.exception.SimpleResponse;
import com.hust.baseweb.applications.education.model.*;
import com.hust.baseweb.applications.education.model.getclasslist.GetClassListOM;
import com.hust.baseweb.entity.UserLogin;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ClassService {

    EduClass save(UserLogin userLogin, AddClassModel addClassModel);

    GetClassListOM getClassesOfCurrentSemester(String studentId, GetClassesIM filterParams, Pageable pageable);

    SimpleResponse register(UUID classId, String studentId);

    Map<String, SimpleResponse> updateRegistStatus(UUID classId, Set<String> studentIds, RegistStatus status);

    List<GetClassesOfTeacherOM> getClassesOfTeacher(String teacherId);

    List<GetClassesOfStudentOM> getClassesOfStudent(String studentId);

    GetClassDetailOM getClassDetail(UUID id);

    List<GetAssignmentsOM> getAssignments(UUID classId);

    List<GetStudentsOfClassOM> getStudentsOfClass(UUID id);

    List<GetStudentsOfClassOM> getRegistStudentsOfClass(UUID id);
}
