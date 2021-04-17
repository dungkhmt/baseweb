package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.QuizChoiceAnswer;
import com.hust.baseweb.applications.education.model.quiz.QuizChoiceAnswerCreateInputModel;

import java.util.List;
import java.util.UUID;

public interface QuizChoiceAnswerService {
    public List<QuizChoiceAnswer> findAll();
    public QuizChoiceAnswer save(QuizChoiceAnswerCreateInputModel input);
    public List<QuizChoiceAnswer> findAllByQuizQuestionId(UUID quizQuestionId);
}
