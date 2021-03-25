package com.hust.baseweb.applications.education.suggesttimetable.repo;


import com.hust.baseweb.applications.education.suggesttimetable.entity.EduClass;
import com.hust.baseweb.applications.education.suggesttimetable.model.GroupClassesOM;

import java.util.List;
import java.util.Set;

public interface IClassRepo {

    List<EduClass> saveAll(List<EduClass> eduClass);

    void insertClassesInBatch(List<EduClass> classes);

    List<GroupClassesOM> getAllClassesOfCourses(Set<String> courseIds);
}
