package com.hust.baseweb.applications.education.teacherclassassignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassTeacherAssignmentSolutionModel {
    private String classCode;
    private String courseId;
    private String courseName;
    private String teacherId;
    private String teacherName;
    private String timetable;
}
