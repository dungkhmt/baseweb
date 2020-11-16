package com.hust.baseweb.applications.education.teacherclassassignment.model;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AlgoTeacherIM {

    private String id;

    private String name;

    private List<Course4Teacher> courses;// danh sach ma cac mon hoc ma giao vien co the day (file excel course4teacher)

    private double prespecifiedHourLoad; // so gio da duoc phan cong boi nhiem vu giang day khac
}
