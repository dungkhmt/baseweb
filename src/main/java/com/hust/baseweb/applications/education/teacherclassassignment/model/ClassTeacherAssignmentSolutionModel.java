package com.hust.baseweb.applications.education.teacherclassassignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.UUID;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ClassTeacherAssignmentSolutionModel {

    private UUID solutionItemId;

    private String classCode;

    private String courseId;

    private String courseName;

    private String teacherId;

    private String teacherName;

    private String timetable;

    private double hourLoad;

    // data structures for viewing under grid
    private int startSlot;
    private int endSlot;
    private int startIndexFromPrevious; // so tiet trong ke tu tiet cuoi cung cua lop truoc
    private int duration; // so tiet
}
