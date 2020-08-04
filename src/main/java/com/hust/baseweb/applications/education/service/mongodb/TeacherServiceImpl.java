package com.hust.baseweb.applications.education.service.mongodb;

import com.hust.baseweb.applications.education.entity.mongodb.Teacher;
import com.hust.baseweb.applications.education.repo.mongodb.TeacherRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherServiceImpl implements TeacherService{
    @Autowired
    TeacherRepo teacherRepo;

    @Override
    public void uploadListOfTeachers(List<Teacher> teachers) {
        teacherRepo.deleteAll();
        teacherRepo.insert(teachers);
    }

    @Override
    public List<Teacher> getAllTeachers() {
        return teacherRepo.findAll();
    }
}
