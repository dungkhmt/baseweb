package com.hust.baseweb.applications.education.service.mongodb;

import com.hust.baseweb.applications.education.entity.mongodb.Teacher;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface TeacherService {
    void uploadListOfTeachers(List<Teacher>  teachers);

    List<Teacher> getAllTeachers();
}
