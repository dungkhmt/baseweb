package com.hust.baseweb.applications.education.teacherclassassignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassesAssignedToATeacherModel {

    private String teacherId;
    private String teacherName;
    private double hourLoad;
    private int numberOfClass;
    private List<ClassTeacherAssignmentSolutionModel> classList;

}
