package com.hust.baseweb.applications.education.quiztest.service;

import com.hust.baseweb.applications.education.model.quiz.QuizQuestionDetailModel;
import com.hust.baseweb.applications.education.quiztest.entity.EduQuizTestQuizQuestion;
import com.hust.baseweb.applications.education.quiztest.model.quiztestquestion.CreateQuizTestQuestionInputModel;
import com.hust.baseweb.entity.UserLogin;

import java.util.List;

public interface EduQuizTestQuizQuestionService {
    public EduQuizTestQuizQuestion createQuizTestQuestion(UserLogin u, CreateQuizTestQuestionInputModel input);
    public EduQuizTestQuizQuestion removeQuizTestQuestion(UserLogin u, CreateQuizTestQuestionInputModel input);
    public List<QuizQuestionDetailModel> findAllByTestId(String testId);

}
