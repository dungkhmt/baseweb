package com.hust.baseweb.applications.education.suggesttimetable.repo;

import com.hust.baseweb.applications.education.suggesttimetable.entity.EduCourse;

import java.util.List;

public interface ICourseRepo {
    void saveAll(List<EduCourse> eduCourses);

}
