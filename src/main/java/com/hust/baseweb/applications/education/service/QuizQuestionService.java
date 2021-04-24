package com.hust.baseweb.applications.education.service;

import com.hust.baseweb.applications.education.entity.QuizQuestion;
import com.hust.baseweb.applications.education.model.quiz.QuizChooseAnswerInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionCreateInputModel;
import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.entity.UserLogin;

import java.util.List;
import java.util.UUID;

public interface QuizQuestionService {
    public QuizQuestion save(QuizQuestionCreateInputModel input);
    public List<QuizQuestion> findAll();
    public List<QuizQuestion> findQuizOfCourse(String courseId);
    public QuizQuestionDetailModel findQuizDetail(UUID questionId);
    public QuizQuestion changeOpenCloseStatus(UUID questionId);

    public boolean checkAnswer(UserLogin userLogin, QuizChooseAnswerInputModel quizChooseAnswerInputModel);
}
