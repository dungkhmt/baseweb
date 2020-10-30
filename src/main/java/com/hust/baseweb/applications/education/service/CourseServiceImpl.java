package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.Course;

import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))
public class CourseServiceImpl implements  CourseService{
    //private CourseRepo courseRepo;
    @Override
    public List<Course> findAll() {
        return null;// TODO
        //return courseRepo.findAll();
    }
}
