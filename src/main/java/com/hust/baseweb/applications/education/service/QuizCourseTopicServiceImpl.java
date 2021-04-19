package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.QuizCourseTopic;
import com.hust.baseweb.applications.education.repo.QuizCourseTopicRepo;
import lombok.AllArgsConstructor;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
@Log4j2
@Service
@AllArgsConstructor(onConstructor = @__(@Autowired))

public class QuizCourseTopicServiceImpl implements QuizCourseTopicService {
    private QuizCourseTopicRepo quizCourseTopicRepo;
    @Override
    public List<QuizCourseTopic> findAll() {
        return quizCourseTopicRepo.findAll();
    }
}
