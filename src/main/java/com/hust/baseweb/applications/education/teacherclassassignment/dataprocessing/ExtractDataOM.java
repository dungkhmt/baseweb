package com.hust.baseweb.applications.education.teacherclassassignment.dataprocessing;

import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoClassIM;
import com.hust.baseweb.applications.education.teacherclassassignment.model.AlgoTeacherIM;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ExtractDataOM {

    private List<AlgoTeacherIM> teachers;

    private List<AlgoClassIM> classes;
}
