package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.classmanagement.enumeration.RegistStatus;
import com.hust.baseweb.applications.education.exception.ResponseSecondType;
import com.hust.baseweb.applications.education.model.GetListStudentsOfClassOM;
import org.springframework.http.ResponseEntity;

import java.util.UUID;

public interface ClassService {

    ResponseEntity<?> getClassListOfCurrentSemester(int page, int size);

    ResponseSecondType register(UUID classId, String studentId);

    GetListStudentsOfClassOM getListStudentsOfClass(UUID id);

    ResponseSecondType updateRegistStatus(UUID classId, String studentId, RegistStatus status);
}
