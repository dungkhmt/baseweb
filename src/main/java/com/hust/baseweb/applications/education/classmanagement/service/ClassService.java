package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.classmanagement.enumeration.RegistStatus;
import com.hust.baseweb.applications.education.exception.ResponseSecondType;
import com.hust.baseweb.applications.education.model.*;
import com.hust.baseweb.applications.education.model.getclasslist.GetClassListOM;

import java.util.List;
import java.util.UUID;

public interface ClassService {

    GetClassListOM getClassListOfCurrentSemester(String studentId, int page, int size);

    ResponseSecondType register(UUID classId, String studentId);

    ResponseSecondType updateRegistStatus(UUID classId, String studentId, RegistStatus status);

    List<GetClassesOfTeacherOM> getClassesOfTeacher(String teacherId);

    List<GetClassesOfStudentOM> getClassesOfStudent(String studentId);

    GetClassDetailOM getClassDetail(UUID id);

    List<GetAssignmentsOM> getAssignments(UUID classId);

    List<GetStudentsOfClassOM> getStudentsOfClass(UUID id);

    List<GetStudentsOfClassOM> getRegistStudentsOfClass(UUID id);
}
