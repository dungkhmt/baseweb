package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoTeacherAssignmentIM;
import com.hust.baseweb.applications.education.teacherclassassignment.model.TeacherClassAssignmentOM;
import org.springframework.stereotype.Service;


public interface TeacherClassAssignmentAlgoService {

    public TeacherClassAssignmentOM computeTeacherClassAssignment(AlgoTeacherAssignmentIM input);
}
