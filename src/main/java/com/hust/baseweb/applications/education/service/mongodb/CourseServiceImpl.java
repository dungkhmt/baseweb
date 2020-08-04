package com.hust.baseweb.applications.education.service.mongodb;

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
    public void uploadListOfCourses(List<Course> courses) {
        courseRepo.deleteAll();
        courseRepo.insert(courses);
    }

    @Override
    public List<Course> getAllCourses() {
        return courseRepo.findAll();
    }
}
