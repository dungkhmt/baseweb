package com.hust.baseweb.applications.education.teacherclassassignment.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class TeacherMaxHourLoad {
    private String teacherId;
    private int maxHourLoad;
}
