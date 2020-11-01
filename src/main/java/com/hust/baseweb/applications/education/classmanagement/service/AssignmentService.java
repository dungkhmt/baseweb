package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.exception.ResponseSecondType;
import com.hust.baseweb.applications.education.model.CreateAssignmentIM;
import com.hust.baseweb.applications.education.model.getassignmentdetail.GetAssignmentDetailOM;
import com.hust.baseweb.applications.education.model.getassignmentdetail4teacher.GetAssignmentDetail4TeacherOM;

import java.util.List;
import java.util.UUID;

public interface AssignmentService {

    GetAssignmentDetailOM getAssignmentDetail(UUID id, String studentId);

    GetAssignmentDetail4TeacherOM getAssignmentDetail4Teacher(UUID assignmentId);

    String getSubmissionsOf(String assignmentId, List<String> studentIds);

    ResponseSecondType deleteAssignment(UUID id);

    ResponseSecondType createAssignment(CreateAssignmentIM im);
}
