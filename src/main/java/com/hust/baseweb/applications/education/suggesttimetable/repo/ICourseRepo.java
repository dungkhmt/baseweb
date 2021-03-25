package com.hust.baseweb.applications.education.suggesttimetable.repo;

import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;

import java.util.List;
import java.util.Set;

public interface ICourseRepo {

    void saveAll(List<EduCourse> eduCourses);

    List<EduCourse> findAllById(Set<String> courseIds);

    void insertCoursesInBatch(List<EduCourse> courses);
}
