package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.entity.QuizCourseTopic;

import java.util.List;

public interface QuizCourseTopicService {
    public List<QuizCourseTopic> findAll();
    public List<QuizCourseTopic> findAllByEduCourse(String courseId);
}
