package com.hust.baseweb.applications.education.teacherclassassignment.service;

import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoInputTeacherAssignmentModel;
import com.hust.baseweb.applications.education.teacherclassassignment.model.OutputTeacherClassAssignmentModel;

public interface TeacherClassAssignmentAlgoService {
    public OutputTeacherClassAssignmentModel computeTeacherClassAssignment(AlgoInputTeacherAssignmentModel input);
}
