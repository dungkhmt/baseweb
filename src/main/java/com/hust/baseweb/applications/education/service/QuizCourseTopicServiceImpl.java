package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.EduCourse;
import com.hust.baseweb.applications.education.entity.QuizCourseTopic;
import com.hust.baseweb.applications.education.model.quiz.QuizCourseTopicCreateInputModel;
import com.hust.baseweb.applications.education.repo.EduCourseRepo;
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
    private EduCourseRepo eduCourseRepo;
    @Override
    public List<QuizCourseTopic> findAll() {
        return quizCourseTopicRepo.findAll();
    }

    public List<QuizCourseTopic> findByEduCourse_Id(String courseId) {
        return quizCourseTopicRepo.findByEduCourse_Id(courseId);
    }
    @Override
    public QuizCourseTopic save(QuizCourseTopicCreateInputModel input) {
        QuizCourseTopic quizCourseTopic = new QuizCourseTopic();
        quizCourseTopic.setQuizCourseTopicId(input.getQuizCourseTopicId());
        quizCourseTopic.setQuizCourseTopicName(input.getQuizCourseTopicName());
        EduCourse eduCourse = eduCourseRepo.findById(input.getCourseId()).orElse(null);
        quizCourseTopic.setEduCourse(eduCourse);
        QuizCourseTopic quizCourseTopicDuplicate = quizCourseTopicRepo
            .findById(quizCourseTopic.getQuizCourseTopicId())
            .orElse(null);
        if (quizCourseTopicDuplicate == null) {
            quizCourseTopic = quizCourseTopicRepo.save(quizCourseTopic);
        } else {
            quizCourseTopic.setMessage("duplicate");
        }

        return quizCourseTopic;
    }
    
    @Override
    public List<QuizCourseTopic> findAllByEduCourse(String courseId) {
        EduCourse eduCourse = eduCourseRepo.findById(courseId).orElse(null);
        return quizCourseTopicRepo.findAllByEduCourse(eduCourse);
    }
}
