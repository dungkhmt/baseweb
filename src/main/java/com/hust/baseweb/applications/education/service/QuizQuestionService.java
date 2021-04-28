package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionCreateInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;

import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface QuizQuestionService {
    public QuizQuestion save(QuizQuestionCreateInputModel input);
    public QuizQuestion save(QuizQuestionCreateInputModel input, MultipartFile[] files);

    public List<QuizQuestion> findAll();
    public List<QuizQuestion> findQuizOfCourse(String courseId);
    public QuizQuestionDetailModel findQuizDetail(UUID questionId);
}
