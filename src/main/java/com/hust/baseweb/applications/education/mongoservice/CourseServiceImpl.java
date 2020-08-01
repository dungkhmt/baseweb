package com.hust.baseweb.applications.education.mongoservice;

import com.hust.baseweb.applications.education.entity.mongodb.Course;
import com.hust.baseweb.applications.education.repo.mongodb.CourseRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CourseServiceImpl implements CourseService {
    @Autowired
    CourseRepo courseRepo;

    @Override
    public void uploadListOfCourses(List<Course> courseList) {
        courseRepo.deleteAll();
        courseRepo.insert(courseList);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }
}
