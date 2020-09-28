package com.hust.baseweb.applications.education.classmanagement.service;

import com.hust.baseweb.applications.education.model.getassignmentdetail.GetAssignmentDetailOM;
import com.hust.baseweb.applications.education.model.getassignmentdetail4teacher.GetAssignmentDetail4TeacherOM;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.InputStreamResource;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Path;
import java.util.List;
import java.util.UUID;

public interface AssignmentService {

    GetAssignmentDetailOM getAssignmentDetail(UUID id, String studentId);

    GetAssignmentDetail4TeacherOM getAssignmentDetail4Teacher(UUID assignmentId);

    InputStream getFiles(String assignmentId, List<String> studentIds);
}
