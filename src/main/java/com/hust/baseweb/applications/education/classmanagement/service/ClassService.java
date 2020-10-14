package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.classmanagement.enumeration.RegistStatus;
import com.hust.baseweb.applications.education.exception.ResponseSecondType;
import com.hust.baseweb.applications.education.model.*;
import com.hust.baseweb.applications.education.model.getclasslist.GetClassListOM;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.UUID;

public interface ClassService {

    GetClassListOM getClassesOfCurrentSemester(String studentId, GetClassesIM filterParams, Pageable pageable);

    ResponseSecondType register(UUID classId, String studentId);

    Map<String, ResponseSecondType> updateRegistStatus(UUID classId, Set<String> studentIds, RegistStatus status);

    List<GetClassesOfTeacherOM> getClassesOfTeacher(String teacherId);

    List<GetClassesOfStudentOM> getClassesOfStudent(String studentId);

    GetClassDetailOM getClassDetail(UUID id);

    List<GetAssignmentsOM> getAssignments(UUID classId);

    List<GetStudentsOfClassOM> getStudentsOfClass(UUID id);

    List<GetStudentsOfClassOM> getRegistStudentsOfClass(UUID id);
}
