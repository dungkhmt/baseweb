package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionCreateInputModel;

import java.util.List;

public interface QuizQuestionService {
    public QuizQuestion save(QuizQuestionCreateInputModel input);
    public List<QuizQuestion> findAll();
}
