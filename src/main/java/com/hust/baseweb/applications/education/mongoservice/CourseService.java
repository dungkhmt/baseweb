package com.hust.baseweb.applications.education.mongoservice;

import com.hust.baseweb.applications.education.entity.mongodb.Course;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface CourseService {
    void uploadListOfCourses(List<Course> courseList);

    List<Course> getAllCourses();
}
