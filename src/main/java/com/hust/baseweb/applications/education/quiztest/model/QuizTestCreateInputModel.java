package com.hust.baseweb.applications.education.quiztest.model;


import java.util.Date;
import java.util.UUID;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class QuizTestCreateInputModel {
    private String testId;
    private String testName;
    private Date scheduleDatetime;
    private Integer duration;
    private String courseId;
    private UUID classId;

    @Override
    public String toString() {
        return "[TestId: " + testId + 
        ", TestName: " + testName + 
        ", ScheduleDatetime: " + scheduleDatetime + 
        ", Duration: " + duration +
        ", CourseId: " + courseId + 
        ", ClassId: " + classId + "]";
    }
}
